package com.hp.android.haoxin.command;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.hp.android.haoxin.LoadingActivity;
import com.hp.android.haoxin.MainActivity;
import com.hp.android.haoxin.R;
import com.hp.android.haoxin.beans.CheckReagentStateBean;
import com.hp.android.haoxin.callback.OnConnectedCallBack;
import com.hp.android.haoxin.callback.OnConnectedCallBackImpl;
import com.hp.android.haoxin.callback.OnReadDeviceDataCallBack;
import com.hp.android.haoxin.callback.OnReadDeviceDataCallBackImpl;
import com.hp.android.haoxin.global.Global;
import com.hp.android.haoxin.global.GlobalState;
import com.hp.android.haoxin.global.Instruction;
import com.hp.android.haoxin.global.Response;
import com.hp.android.haoxin.test.TestCmd;
import com.hp.android.haoxin.utils.FomatTool;
import com.hp.android.haoxin.widgets.CustomDialog;
import com.hp.android.haoxin.workview.RBListener;
import com.hp.android.haoxin.workview.ViewController;
import com.hx.domain.ModeDetectBean;
import com.hx.domain.ParameterBean;
import com.hx.protocol.IComDevice;
import com.hx.protocol.ProtocolType;
import com.hx.service.DeviceService;
import com.hx.service.IOperatorService;
import com.hx.service.OperatorServiceImpl;
import com.hx.service.ReadDeviceData;

import java.io.IOException;
import java.util.Random;


/**
 * 真实的请求操作，与底层交互
 * Created by AZZ on 15/8/3.
 */
public class RealCommand extends TestCommand {
    /**
     * 打印标识
     */
    public static final String TAG = "RealCommand";

    /**
     * 1 染色；2 清洗；3 填充；4 离心
     */
    public static final int TAG_DYE = 1;
    public static final int TAG_CLEAN = 2;
    public static final int TAG_FILL = 3;
    public static final int TAG_CENTRIFUGAL = 4;
    /**
     * 发送命令模块
     */
    private IOperatorService operatorService;
    /**
     * 测试模块-用来模拟一些命令发送与返回
     */
    private TestCmd Test = TestCmd.getInstance();

    /***********************************************\
     *                                             *
     *              取消时要做的重置操作              *
     *                                             *
     ***********************************************/
    public void resetState() {
        resetSystemState();
        resetOnReadDeviceOptionCallBackLastProgressResponse();
    }

    /**
     * 重置系统状态（HOME）
     */
    public void resetSystemState() {
        Global.resetState();
    }

    /**
     * 重置OnReadDeviceOptionCallBackImpl里面的lastProgressResponse（上一次进度命令）
     * 该命令的作用是为了防止连续参数的回调，导致动画卡顿（如果发现回调是一样的参数，则不改变原动画）
     *
     * 这里需要重置的原因是：比如清洗，当取消时，lastProgressResponse还为清洗的命令，如果下次又进入清洗的话，
     * 那么就不会执行动画
     *
     * 11/28更新：同样，当有异常抛出时，比如清洗的时候出现上盖打开异常，然后下次又进入清洗的话，不会执行清洗动画
     */
    public void resetOnReadDeviceOptionCallBackLastProgressResponse() {
        getReadDeviceCallBack().resetOptionCallBackLastProgressResponse();
    }
    /***********************************************\
     *                                             *
     *                获取全局变量                   *
     *                                             *
     ***********************************************/
    /**
     * 获取设备
     * @return IComDevice
     */
    public IComDevice getSeriaPort() {
        try {
            return DeviceService.getDevice();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public IOperatorService getOperatorService() {
        if (operatorService == null) {
            operatorService = new OperatorServiceImpl(getSeriaPort());
        }
        return operatorService;
    }
    public OnReadDeviceDataCallBackImpl getReadDeviceCallBack() {
        return OnReadDeviceDataCallBackImpl.getInstance();
    }

    public ViewController getViewController() {
        return ViewController.getInstance();
    }
    /***********************************************\
     *                                             *
     *                  Dialog                     *
     *                                             *
     ***********************************************/
    public ProgressDialog progressDialog = null;

    protected void showProgressDialog(int resId, Context context) {
        if (context == null) {
            Log.e("RealCommand", "showProgressDialog error: context is null");
        }
        dismissProgressDialog();//先把之前的dismiss掉
        String text = null;
        if (resId == 0) {
            text = mContext.getResources().getString(R.string.waiting);
        } else {
            text = mContext.getResources().getString(resId);
        }
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(text);
        progressDialog.show();
    }


    protected void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            Log.v("dismiss", "dismiss后的progressDialog = " + progressDialog);
            progressDialog = null;
        }
    }

    /**
     * 倒计时显示，如果时间为0，则progressDialog自动消失
     * @param countDownTime 倒计时间
     * @return true表示正常执行，false表示有错误情况，比如progressDialog为null，或者倒计时为0等
     */
    protected boolean countDownDialog(int countDownTime) {
        if (null == progressDialog) {
            Log.v(TAG, "countDownDialog: progressDialog is null!");
            return false;
        }
        if (0 == countDownTime) {
            dismissProgressDialog();
            return false;
        }
        String text = mContext.getResources().getString(R.string.waiting);
        final String show = String.format(text + "(%ss)", String.valueOf(countDownTime));

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null) {
                    progressDialog.setMessage(show);
                }
            }
        });
        return true;
    }
    /***********************************************\
     *                                             *
     *                 参数设置                     *
     *                                             *
     ***********************************************/
    @Override
    public void siteChanged(int glassCount, int dyeingThickness, int alcoholFix, int crystalViolet, int iodine, int weigh) {
        //打印数据到屏幕
        super.siteChanged(glassCount, dyeingThickness, alcoholFix, crystalViolet, iodine, weigh);
        Log.d("RealCommand", "虚拟硬件接收到：玻片数("+glassCount+") 染色("+dyeingThickness+") 固定("+alcoholFix+") 结晶("+crystalViolet+") 碘酒("+iodine+") 称重("+weigh+")");
        //封装数据
        ParameterBean parameterBean = new ParameterBean();
        parameterBean.setGalssCount(FomatTool.int2byte(glassCount));
        parameterBean.setDyeingThickness(FomatTool.int2byte(dyeingThickness));
        parameterBean.setAlcoholFix(FomatTool.int2byte(alcoholFix));
        parameterBean.setCrystalViolet(FomatTool.int2byte(crystalViolet));
        parameterBean.setIodine(FomatTool.int2byte(iodine));
        parameterBean.setWeigh(FomatTool.int2byte(weigh));
        //连接设备
        IComDevice serialPort = getSeriaPort();
        //实现业务
        OperatorServiceImpl operatorService = new OperatorServiceImpl(serialPort);
        try {
            operatorService.sendParameterPackets(parameterBean);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***********************************************\
     *                                             *
     *                    启动                     *
     *                                             *
     ***********************************************/
    @Override
    public void loadingStart(Context context) {
        Global.setState(GlobalState.LOADING);
        connectStart(context);
    }

    /**
     * 开始和下机位建立连接
     * @param context Activity上下文
     */
    public void connectStart(final Context context) {
        Log.d(TAG, "connectStart 开始和下机位建立连接");
        final OnConnectedCallBackImpl connectedCallBack = OnConnectedCallBackImpl.getInstance();
        connectedCallBack.setFirstCallBack(new OnConnectedCallBack.OnFirstConnectedCallBack() {

            @Override
            public void onFirstConnected(boolean isConnected) {
                if (isConnected) {
                    loadingStartAfterConnected();    //获取下位机信息
                } else {
                    connectFailed(context);
                }
            }
        });
        //建立接收消息服务,自带建立连接
        ReadDeviceData readDeviceData = new ReadDeviceData(getSeriaPort());
        readDeviceData.setConnListener(connectedCallBack);
        readDeviceData.setCallBack(getReadDeviceCallBack());
        readDeviceData.start();

        // FIXME: 15/9/5 测试软硬件版本
        /*getReadDeviceCallBack().onReadDeviceData(FomatTool.int2bytes(1), ProtocolType.HW_VERSION);
        getReadDeviceCallBack().onReadDeviceData(FomatTool.int2bytes(0), ProtocolType.SW_VERSION);*/
    }
    CustomDialog connectFailedDialog;
    /**
     * 启动过程中建立连接失败
     */
    private void connectFailed(final Context context) {
        if (connectFailedDialog != null) {
            showToast("连接失败，异常连接窗口已打开...");
            return;
        }
        showToast("连接失败，开启异常连接窗口...");
        Log.e(TAG, context + " = context");
        try {
            connectFailedDialog = CustomDialog.createExceptionDialog(context, R.string.exception_init_not_connected);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0); //在未显示出dialog之前按back键，如果不用这句执行退出程序，线程还是会继续运行
        }

        connectFailedDialog.setCancelBtnVisibilty(View.VISIBLE);
        //按返回键时
        connectFailedDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                connectFailedDialog.dismiss();
                connectFailedDialog = null;
                //FIXME: 8/30 test connect ok --- 后门进入主页面 --- 可屏蔽也可保留
                OnConnectedCallBackImpl.getInstance().onConnected(true);
            }
        });
        connectFailedDialog.setPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectFailedDialog.dismiss();
                connectFailedDialog = null;
            }
        });
        connectFailedDialog.setNegativeListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(0); //按取消时退出程序
            }
        });
    }

    /**
     * 加载模块，在建立连接后触发
     */
    public void loadingStartAfterConnected() {
        showToast("建立通信连接成功，开始加载...");
        Log.d(TAG, "loadingStartAfterConnected 连接成功！开始接收下机位数据");
        try {
            getOperatorService().sendHWVerPackets();
            getOperatorService().sendSWVerPackets();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Global.initDatas(mContext);       //初始化系统参数
        //TODO: 8/27 发送参数设置指令
        TestLoadingStartAfterConnected();
    }
    //@Test
    private void TestLoadingStartAfterConnected() {
        final Handler updateUpdateHandler = new Handler();
        new Handler().post(new Runnable() {
            int t = -1;
            Random random = new Random();

            public void run() {
                t++;
                if (t < 5) {
                    mCommand.call.loadingSetStep(t);//加载开始一步，调用一次该方法，t为步数
                    updateUpdateHandler.postDelayed(this, 1800 + random.nextInt(5) * 100);
                }
            }
        });
    }
    /***********************************************\
     *                                             *
     *                    染色                     *
     *                                             *
     ***********************************************/
    @Override
    public void dyeStart(Context context) {
        //FIXME: 纠错验证
        if (!checkWork(context, TAG_DYE)) {
            return;
        }
		mCommand.call.workStartRotate(false);
		mCommand.call.workStartDiskColorAnimation(0xffffffff, 5000, 0, 180);
		Global.setState(GlobalState.DYE);
        if (Global.getSystemStateBean().isStatDevFilled()) { //流路已经被填充
            try {
                Global.sendDataToDevice();
                showToast("开始染色");
                getOperatorService().sendDyeingPackets(Instruction.Dye.START_DYE);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // FIXME: 15/8/30 测试染色报文
            Test.testClean();
        } else { //询问是否进行流路填充
            mCommand.call.requestDevFill();
        }
    }
	@Override
	public void dyeCancel() {
		//isDyeCancel = true;
		showToast("染色取消了");
		try {
            getOperatorService().sendDyeingPackets(Instruction.Dye.CANCEL_DYE);

            // FIXME: 15/11/4 测试取消染色
            Test.testCancel();
        } catch (IOException e) {
            e.printStackTrace();
        }
		mCommand.call.workFinish(false);
    }

    /***********************************************\
     *                                             *
     *                    清洗                     *
     *                                             *
     ***********************************************/
    /**
     * 清洗操作开始.
     */
	@Override
	public void cleanStart(final Context context) {
        //FIXME: 清洗模块 纠错验证
        if (!checkWork(context, TAG_CLEAN)) {
            return;
        }
        Global.setState(GlobalState.CLEAN);
        showToast("开始清洗");

		try {
			getOperatorService().sendClearPackets((byte) 0);
		} catch (IOException e) {
			e.printStackTrace();
		}
        // FIXME: 15/8/30 测试清洗报文
//        Test.testClean();
//        Test.testDisposeException();
        Test.testFinish();
	}


    @Override
    public void cleanCancel() {
//        isCleanCancel = true;
        showToast("清洗取消了");
        try {
            getOperatorService().sendClearPackets((byte) 1);

            // FIXME: 15/11/4 测试取消清洗
            Test.testCancel();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCommand.call.workFinish(false);
    }

    /***********************************************\
     *                                             *
     *                    填充                     *
     *                                             *
     ***********************************************/
    @Override
    public void fillStart(Context context) {
        //FIXME: 纠错验证
        Log.d(TAG, "开始填充前-当前状态：" + Global.getState());
        if (GlobalState.CLEAN != Global.getState() //从清洗跳转过来不用再进行判断
                && GlobalState.DYE != Global.getState()) { //从染色跳转过来不用再进行判断
            if (!checkWork(context, TAG_FILL)) {
                return;
            }
        }

        showToast("开始填充");
        try {
                //设置应用当前状态
                if (GlobalState.DYE == Global.getState()) {
                    Global.setState(GlobalState.FILL_FROM_DYE);
                    getOperatorService().sendDyeingPackets((byte) 2);

                    // FIXME: 15/11/4 测试从染色界面跳转过来的填充完成
                    Test.testFinish();
                } else  if (GlobalState.CLEAN == Global.getState()) {
                    Global.setState(GlobalState.FILL_FROM_CLEAN);
                    getOperatorService().sendClearPackets((byte) 2);
                } else {
                    Global.setState(GlobalState.FILL);
                    getOperatorService().sendFillPackets((byte) 0);

                    // FIXME: 15/11/4 测试从系统界面跳转过来的填充开始
                    Test.testClean();
//                    Test.testDisposeException();
                }
            } catch (IOException e) {
            e.printStackTrace();
        }
        //FIXME: 15/09/26 测试甩干和等待 圆盘转动
//        Test.testDryAndWaitRotate();

    }

    @Override
    public void fillCancel() {
        showToast("填充取消了");
        try {
            //设置应用当前状态
            Log.d(TAG, "填充取消前-当前状态：" + Global.getState());

            if (GlobalState.FILL_FROM_DYE == Global.getState()) {
                getOperatorService().sendDyeingPackets((byte) 3);
                ViewController.getInstance().setIdBack2Home(true); //取消后返回主页

            } else if (GlobalState.FILL_FROM_CLEAN == Global.getState() //清洗跳转到填充，开始填充后点取消
                    || GlobalState.CLEAN == Global.getState()) {        //清洗跳转到填充前弹出窗口，点取消填充时
                getOperatorService().sendClearPackets((byte) 3); //取消后返回主页
                ViewController.getInstance().setIdBack2Home(true);

            } else {
                getOperatorService().sendFillPackets((byte) 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCommand.call.workFinish(false);
    }

    /***********************************************\
     *                                             *
     *                    离心                     *
     *                                             *
     ***********************************************/
    @Override
    public void centrifugalStart(Context context) {
        if (!checkWork(context, TAG_CENTRIFUGAL)) {
            return;
        }
        showToast("离心开始");
        Global.setState(GlobalState.CENTRIFUGAL);
        try {
            getOperatorService().sendCytocentrifugationPackets((byte) 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // FIXME: 15/9/9 测试离心接收报文
//        Test.testClean();
    }

    @Override
    public void centrifugalCancel() {
        super.centrifugalCancel();
        try {
            getOperatorService().sendCytocentrifugationPackets((byte) 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCommand.call.workFinish(false);
    }

    /***********************************************\
     *                                             *
     *                  流路检测                   *
     *                                             *
     ***********************************************/
    @Override
    public void liuluNext(int step) {
        super.liuluNext(step);
        Global.setState(GlobalState.CHECK_B_PASS);
        try {
            getOperatorService().sendBPassPackets((byte) step);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // FIXME: 15/10/14 测试流路检测
        if (step > 0) {
            Test.testLiuLuProgress();
        }
    }
    @Override
    public void weighNext(int step) {
        super.weighNext(step);
        Global.setState(GlobalState.WEIGH);
        try {
            getOperatorService().sendWeighPackets((byte) step);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // FIXME: 15/10/14 测试称重校验
//        Test.testLiuLuProgress();
    }

    private Runnable countDownThread = new Runnable() {
        @Override
        public void run() {
            int countDownTime = 10;

            while (countDownTime >= 0) {
                boolean flag = countDownDialog(countDownTime);

                if (!flag) { //如果执行倒计时的函数返回false，则跳出循环不需要继续执行倒计时了
                    return;
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                countDownTime--;

            }

        }
    };
	@Override
	/**
	 * 系统维护模块，模式、流量检测值发生改变时触发
	 * @param type  哪种检测模式： 0表示模式检测，1表示流量检测
	 * @param index 新的模式检测的值0~4（对应A-E）
	 * @param keyMode 0按下，1弹起
	 */
	public void systemJianCeChange(byte type, byte key, byte keyMode, Context context) {
        // FIXME:检测连接(如果被关闭要打开）
//        if (!getViewController().checkConnect(false)) {
//            return;
//        }
        if (type == RBListener.TYPE_LIU_LIANG) { //流量检测显示进度圈圈（模式检测无需显示进度圈圈）
            showProgressDialog(0, context);

            new Thread(countDownThread).start();
        }
		super.systemJianCeChange(type, key, keyMode, context);
        Global.setState(GlobalState.CHECK_ABCDE); //FIXME:并没有区分式模式检测还是流量检测，后面也一样
		try {
			if (type == 0) {
				ModeDetectBean mpb = new ModeDetectBean(key, keyMode);
				getOperatorService().sendModeDetectPakets(mpb);
			} else if (type == 1) { //弹起时发送
				getOperatorService().sendFlowPackets(key);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
//        Test.testJianCe();
	}

    /**
     * 系统维护，模式检测/流量检测完成
     */
    @Override
    public void systemJianCeFinish() {
        showToast("检测完成！");
        dismissProgressDialog();
    }

    /***********************************************\
     *                                             *
     *                    异常                     *
     *                                             *
     ***********************************************/
    /**
     * 已经检查过试剂是否充足（当弹出试剂是否充足的选项框时,按继续就不需要再检查了）
     */
    private CheckReagentStateBean checkReagentStateBean = new CheckReagentStateBean();
    /**
     * 检测清洗/染色/填充工作是否正常
     * @param which 1 染色；2 清洗；3 填充；4 离心
     * return pass正常，false不正常
     */
    private boolean checkWork(Context context, int which) {
        // FIXME: 15/9/26 门打开异常-测试-若被注释需要打开
//    	if (!Global.getSystemStateBean().isDoorClosed()) {
//            Log.e(TAG, "门打开，异常。");
//            workFailed(context, R.string.exception_top_cover_opened, which);
//            return false;
//        }
        Log.d(TAG, "门关闭，正常。");
        if (which == TAG_CENTRIFUGAL) { //离心不需要判断试剂
            return true;
        }
        //检查试剂
        if (!checkReagent(context, which)) {
            return false;
        }

        return true;
    }

    /**
     * 检测试剂状态是否正常
     */
    private boolean checkReagent(Context context, int which) {
        //如果全部冲剂都充足，则直接返回true
        if (Global.getSystemStateBean().isReagentEnough()) {
            Log.d(TAG, "全部试剂补给，正常。");
            return true;
        }
        if (!checkReagentStateBean.checkCurrentReagent()) {
            Log.e(TAG, "试剂补给，异常。");
            String msg = mContext.getResources().getString(R.string.exception_reagent_not_enough, checkReagentStateBean.getErrorReagentName());
            workFailed(context, msg, which);
            return false;
        }
        Log.d(TAG, "试剂补给，验证通过。");
        checkReagentStateBean.resetAllCheckedReagent(); //检查试剂状态 恢复初始化
        return true;
    }

    /**
     * 清洗，染色，填充，离心条件不满足,显示提示信息给用户
     */
    private void workFailed(final Context context, final int which, final CustomDialog hintDialog) {
        //按返回键时
        hintDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                //重置检查试剂
                checkReagentStateBean.resetAllCheckedReagent();
                hintDialog.dismiss();
                mCommand.call.workFinish(false);
            }
        });
        hintDialog.setPositiveButton("继续", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hintDialog.dismiss();
                switch (which) {
                    case TAG_DYE:
                        dyeStart(context);
                        break;
                    case TAG_CLEAN:
                        cleanStart(context);
                        break;
                    case TAG_FILL:
                        fillStart(context);
                        break;
                    case TAG_CENTRIFUGAL:
                        centrifugalStart(context);
                        break;
                }
            }
        });
        //取消
        hintDialog.setNegativeListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //重置检查试剂
                checkReagentStateBean.resetAllCheckedReagent();
                hintDialog.dismiss();
                mCommand.call.workFinish(false);
            }
        });
    }
    private void workFailed(final Context context, int resId, final int which) {
        final CustomDialog hintDialog = CustomDialog.createHintDialog(context, resId);
        workFailed(context, which, hintDialog);
    }
    //重载
    private void workFailed(final Context context, String msg, final int which) {
        final CustomDialog hintDialog = CustomDialog.createHintDialog(context, msg);
        workFailed(context, which, hintDialog);
    }

}
