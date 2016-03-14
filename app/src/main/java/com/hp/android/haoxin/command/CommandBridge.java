package com.hp.android.haoxin.command;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hp.android.haoxin.LoadingActivity.LoadingListener;
import com.hp.android.haoxin.global.Global;
import com.hp.android.haoxin.global.GlobalState;
import com.hp.android.haoxin.widgets.CustomDialog;
import com.hp.android.haoxin.workview.ViewController;
import com.hp.android.haoxin.workview.WorkBaseView;
import com.hp.android.haoxin.workview.WorkCleanView;
import com.hp.android.haoxin.workview.WorkDyeView;
import com.hp.android.haoxin.workview.WorkLiuView;

public class CommandBridge {

	private static CommandBridge mInstance;
	
	/**
	 * 程序发送数据到硬件的接口，需要用户自己实现
	 */
	private CommandInterface mInterface;
	
	/**
	 * 硬件发送指令到程序的回调，不同的指令需要用户调用里面不同的方法
	 */
	public Call call;

	
	private LoadingListener mLoadingListener;

	private CommandBridge() {}

	/**
	 * 设置接口，接口需要自己实现
	 * 默认自己实现接口
	 */
	public void setInterface() {
		mInterface = new RealCommand();
	}

	public static CommandBridge getInstance() {
		if (mInstance == null) {
			mInstance = new CommandBridge();
			mInstance.call = mInstance.new Call();
			mInstance.setInterface();
		}
		return mInstance;
	}

	public void showToast(String text) {
		if (mInterface != null) {
			mInterface.showToast(text);
		}
	}
	public void showDialogWithType(int resId, Context context, CustomDialog.CustomDialogType type) {
		if (mInterface != null) {
			mInterface.showDialogWithType(resId, context, type);
		}
	}

	/**
	 * 重置系统状态为{@link com.hp.android.haoxin.global.GlobalState}中的HOME
	 */
	public void resetState() {
		if (mInterface != null) {
			mInterface.resetState();
		}
	}
	public void appSetLoadingLinstener(LoadingListener listener) {
		mLoadingListener = listener;
	}

	/**
	 * 程序启动，通知硬件开始加载
	 * @param context Activity的上下文
	 */
	public void linkLoadingStart(Context context) {
		if (mInterface != null)
			mInterface.loadingStart(context);
	}

	/**
	 * 参数设置界面完成后，通过该方法将数据传到硬件 
	 * 染色：1~9 结晶：
	 * 0~2(关闭、普通、高效) 
	 * 碘酒：0~2(高、中、低)
	 * 固定：0~2(关闭、普通、高效)
	 * 称重：0~1(关闭、启动)
	 */
	public void linkSiteDates(int ranse, int guding, int jiejing, int dianjiu,int chengzhong) {
		if (mInterface != null)
		mInterface.siteDates(ranse + 1, guding, jiejing, dianjiu, chengzhong);
	}

	/**
	 * 参数设置界面完成后，通过该方法将数据传到硬件
	 * @param glassCount 玻片数选择（1~24）
	 * @param dyeingThickness 染色厚度选择(0x00~0x08)
	 * @param alcoholFix 酒精固定选择（00：关闭 01：普通 02：高效）
	 * @param crystalViolet 结晶紫选择（00：高 01：中 02：低）
	 * @param iodine 碘酒选择（00：关闭 01：普通 02：高效）
	 * @param weigh 称重使能选择（00：关闭 01：启动）
	 */
	public void linkSiteDates(int glassCount, int dyeingThickness, int alcoholFix, int crystalViolet, int iodine, int weigh) {
		if (mInterface != null)
		mInterface.siteChanged(glassCount, dyeingThickness, alcoholFix, crystalViolet, iodine, weigh);
	}

	/**
	 * 工程师菜单
	 * @param aPump A泵占空比（1-100）
	 * @param bPump B泵占空比（1-100）
	 * @param cPump C泵占空比（1-100）
	 * @param dPump D泵占空比（1-100）
	 * @param ePump E泵占空比（1-100）
	 * @param cellCentrifugalSpeed  细胞离心速率（1500/2000）
	 */
	public void linkEngineerDatas(int aPump, int bPump, int cPump, int dPump, int ePump, int cellCentrifugalSpeed) {
		if (mInterface != null) {
			mInterface.engineerChanged(aPump, bPump, cPump, dPump, ePump, cellCentrifugalSpeed);
		}
	}

	/**
	 * 系统维护，流量、模式检测选项值改变
	 * @param type 检测类型：1表示流量检测，0表示模式检测
	 * @param key 模式检测的方案范围0~4(对应A~E)
	 * @param keyMode 0按下，1弹起
	 * @param context 用于显示进度条dialog
	 */
	public void linkJianCeChange(byte type, byte key, byte keyMode, Context context) {
		if (mInterface != null)
		mInterface.systemJianCeChange(type, key, keyMode, context);
	}

	/**
	 * 系统维护，模式检测/流量检测完成时
	 */
	public void linkJianCeFinish() {
		if (mInterface != null) {
			mInterface.systemJianCeFinish();
		}
	}
	/**
	 * 染色开始前，传递设置的载玻片数到硬件
	 * 
	 * @param number 载玻片数目
	 */
	public void linkSetZaiBoPian(int number) {
		if (mInterface != null)
		mInterface.dyeSetZaiBoPian(number);
	}

	/**
	 * 流路检测到某一步
	 * @param step 当前步数
	 */
	public void linkLiuluCheck(int step) {
		if (mInterface != null) {
			mInterface.liuluNext(step);
		}
	}

	/**
	 * 取消流路检测的某一步时
	 * @param step 取消的步凑数
	 */
	public void linkLiuluCancel(int step){
		if (mInterface != null)
			mInterface.liuluCancel(step);
	}

	/**
	 * 称重检测到某一步
	 * @param step 当前步数
	 */
	public void linkChengzhongCheck(int step) {
		if (mInterface != null) {
			mInterface.weighNext(step);
		}
	}
	/**
	 * 取消称重校验的某一步时
	 * @param step 取消的步凑数
	 */
	public void linkChengzhongCancel(int step){
		if (mInterface != null)
			mInterface.chengzhongCancel(step);
	}
	
	
	/**
	 * 开始染色，通知硬件染色开始
	 */
	public void linkRanseStart(Context context){
		if (mInterface != null)
			mInterface.dyeStart(context);
	}
	
	
	/**
	 * 开始清洗，通知硬件清洗开始
	 */
	public void linkCleanStart(Context context){
		if (mInterface != null)
			mInterface.cleanStart(context);
	}
	
	
	/**
	 * 开始填充，通知硬件填充开始
	 */
	public void linkFillStart(Context context){
		if (mInterface != null)
			mInterface.fillStart(context);
	}
	
	public void linkDyeCancel(){
		if (mInterface != null)
			mInterface.dyeCancel();
	}
	
	public void linkCleanCancel(){
		if (mInterface != null)
			mInterface.cleanCancel();
	}
	
	public void linkFillCancel(){
		if (mInterface != null)
			mInterface.fillCancel();
	}

	/**
	 * 离心
	 * @param context 用于显示dialog
	 */
	public void linkCentrifugalStart(Context context){
		if (mInterface != null)
			mInterface.centrifugalStart(context);
	}

	public void linkCentrifugalCancel(){
		if (mInterface != null)
			mInterface.centrifugalCancel();
	}

	
	
	
	//=========================================================================================
	/**
	 *里面含有硬件往程序发送指令时需要调用到的方法
	 */
	public class Call{
		/**
		 * 硬件启动加载每完成一步时调用，用于更新UI
		 * @param index 加载的步骤，范围0~4,依次递增
		 */
		public void loadingSetStep(int index) {
			if (mLoadingListener == null)return;
			mLoadingListener.updateLoadingStep(index);
		}
		
		/**
		 * 初始化参数，将硬件的参数传入到程序，使得硬件软件数据一致
		 * @param zaibopian  载玻片数量：1-12 (分别对应不同载玻片数量)
		 * @param ranse      染色厚度：1-9 (分别对应不同染色厚度)
		 * @param guding     固定启动终止：0,1(0为关闭，1为开启)
		 * @param jiejing    结晶紫调整 ：0-2 (分别对应，关闭、普通、高效)
		 * @param dianjiu    碘酒调整：0-2(分别对应，关闭、普通、高效)
		 * @param chengzhong 称重启动终止：0,1(0为关闭，1为开启)
		 */
		public void loadingInitDatas(int zaibopian,int ranse,int guding,
				int jiejing,int dianjiu,int chengzhong){
			if (mLoadingListener == null)return;
			mLoadingListener.initDatas(zaibopian, ranse - 1, guding, jiejing, dianjiu, chengzhong);
		}

		/**
		 * 询问是否进行流路填充
		 */
		public void requestDevFill() {
			if(getCurrentCleanView() != null){
				getCurrentDyeView().onRequest();
			}
		}
		
		/**
		 * 圆盘（染色、清洗、填充）模块，设置盘子转动
		 * @param isOffCenter 是否为离心转动。true为空转，false为离心
		 */
		public void workStartRotate(boolean isOffCenter) {
			if(getCurrentCleanView() != null){
				getCurrentCleanView().getListener().startRotate(isOffCenter);
			}
		}
		
		/**
		 * 圆盘（染色、清洗、填充）模块，停止盘子转动
		 */
		public void workStopRotate() {
			if(getCurrentCleanView() != null){
				getCurrentCleanView().getListener().stopRotate();
			}
		}
		
		
		/**
		 * 圆盘（染色、清洗、填充）模块，设置进度条值
		 * @param progress 当前进度值
		 */
		public void workSetProgress(int progress) {
			if(getCurrentCleanView() != null){
				getCurrentCleanView().getListener().setProgress(progress);
			}
		}
		
		/**
		 * 圆盘（染色、清洗、填充）模块，设置当前进度信息
		 * @param title 当前操作标题
		 * @param des 当前操作内容
		 */
		public void workSetProgressText(String title,String des) {
			if(getCurrentCleanView() != null){
				getCurrentCleanView().getListener().setProgressText(title, des);
			}
		}
		public void workSetProgressText(int title,int des) {
			if(getCurrentCleanView() != null){
				getCurrentCleanView().getListener().setProgressText(title, des);
			}
		}
		
		/**
		 * 设置圆盘（染色、清洗、填充）模块，碰头喷射
		 * @param index 喷头的编号，从下往上依次为0-5
		 * @param isClean 是否为清洗阶段，因为A、B、C三个喷头在清洗和填充阶段喷射效果不一样。·
		 *                true为喷出白色效果，false为其他颜色
		 */
		public void workStartSpout(int index, boolean isClean){//int color,
			if(getCurrentCleanView() != null){
				getCurrentCleanView().getListener().startSpout(index, isClean);
			}
		}
		
		
		/**
		 * 设置圆盘（染色、清洗、填充）模块中盘子上的色块颜色
		 * @param color 盘子上的颜色，16进制，格式为argb（例如：0xff00bb99)
		 */
		public void workSetDiskColor(int color){
			if(getCurrentCleanView() != null){
				getCurrentCleanView().getListener().setDiskMarkColor(color);
			}
		}
		
		/**
		 * 设置圆盘（染色、清洗、填充）模块中盘子上的色块颜色
		 * @param color 盘子上的颜色，16进制，格式为argb（例如：0xff00bb99）
		 * @param duration 颜色透明度渐变持续时间，单位毫秒
		 * @param fromAlpha 颜色透明度起始值 0-255
		 *
		 */
		public void workStartDiskColorAnimation(int color,int duration,float fromAlpha,float toAlpha){
			if(getCurrentCleanView() != null){
				getCurrentCleanView().getListener().setDiskMarkColor(color, duration, fromAlpha, toAlpha);
			}
		}
		
		/**
		 * 设置圆盘（染色、清洗、填充）模块，喷头停止喷射
		 * @param index 要停止喷射喷头的编号，从下往上依次为0-5
		 */
		public void workStopSpout(int index){
			if(getCurrentCleanView() != null){
				getCurrentCleanView().getListener().stopSpout(index);
			}
		}

		/**
		 * 结束所有的喷射
		 */
		public void workStopAllSpout() {
			if(getCurrentCleanView() != null){
				getCurrentCleanView().getListener().stopAll();
			}
		}

		/**
		 * 设置圆盘（染色、清洗、填充）模块，完成工作
		 */
		public void workFinish(boolean isFinished){
			if(getCurrentCleanView() != null){
				getCurrentCleanView().getListener().finish(isFinished);
				mInterface.resetState();
			}
		}
		public void maintainFinish() {
			if (getCurrentView() instanceof WorkLiuView) {
				((WorkLiuView) getCurrentView()).finish();
			}
		}
		private WorkBaseView getCurrentView() {
			return ViewController.getInstance().getCurrentView();
		}
		private WorkCleanView getCurrentCleanView(){
			if (ViewController.getInstance().getCurrentView() instanceof WorkCleanView) {
				return (WorkCleanView)ViewController.getInstance().getCurrentView();
			} else {
				//UI is switched, not need updating already
				Log.w("", "Current View is not WorkCleanView!!!");
				return null;
			}
		}
		private WorkDyeView getCurrentDyeView(){
			if (ViewController.getInstance().getCurrentView() instanceof WorkDyeView) {
				return (WorkDyeView)ViewController.getInstance().getCurrentView();
			} else {
				//UI is switched, not need updating already
				Log.w("", "Current View is not WorkDyeView!!!");
				return null;
			}
		}
		/**
		 * 系统维护（流路检测，称重校验），设置进度条值
		 * @param progress 当前进度值
		 */
		public void workSetMainTainProgress(int progress) {
			WorkBaseView view = ViewController.getInstance().getCurrentView();
			if (view instanceof WorkLiuView) {
				((WorkLiuView) view).updateProgress(progress);
			}
		}
	}

	//----------------------------------------------------------------------------------调试用
	/**
	 * 开启悬浮返回键
	 * @param context 用来发广播
	 */
	public void openBackButton(Context context) {
		Intent intent = new Intent("com.system.action.back");
		intent.putExtra("OPERATION", 1); //1 open; 0 close
		context.sendBroadcast(intent);
	}

	/**
	 * 关闭悬浮返回键
	 * @param context 用来发广播
	 */
	public void closeBackButton(Context context) {
		Intent intent = new Intent("com.system.action.back");
		intent.putExtra("OPERATION", 0); //1 open; 0 close
		context.sendBroadcast(intent);
	}
}
