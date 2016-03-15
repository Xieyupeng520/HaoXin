package com.hp.android.haoxin.callback;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.hp.android.haoxin.R;
import com.hp.android.haoxin.command.CommandBridge;
import com.hp.android.haoxin.command.CommandInterface;
import com.hp.android.haoxin.command.RealCommand;
import com.hp.android.haoxin.global.Global;
import com.hp.android.haoxin.global.Response;
import com.hp.android.haoxin.widgets.SpoutView;
import com.hp.android.haoxin.workview.WorkCleanView;
import com.hx.protocol.ProtocolType;


/**
 * Created by AZZ on 15/8/29.
 */
public class OnReadDeviceOptionCallBackImpl implements OnReadDeviceDataCallBack {
    private CommandBridge.Call call;
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    public void onReadDeviceData(final byte[] data, final ProtocolType type) {
        call = CommandBridge.getInstance().call;
        Log.d("OnReadDeviceOption", "当前状态：" + Global.getState());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                switch (Global.getState()) {
                    case DYE:
                    case CLEAN:
                    case FILL_FROM_DYE:
                    case FILL_FROM_CLEAN:
                    case FILL:
                        work(data, type);
                        break;
                    case CHECK_B_PASS:
                    case WEIGH:
                        maintain(data, type);
                        break;
                    case CHECK_ABCDE:
                        check(data, type);
                        break;
                    case CENTRIFUGAL:
                        centrifugal(data, type);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 工作(染色，清洗，填充）
     */
    private void work(byte[] data, ProtocolType type) {

        switch (type) {
        case RPT_OP_RESPONSE:
            if (data[0] != Response.Complete.DYE_COMPLETE
                    && data[0] != Response.Complete.CLEAN_COMPLETE
                    && data[0] != Response.Complete.FILL_COMPLETE_FROM_DYE
                    && data[0] != Response.Complete.FILL_COMPLETE_FROM_CLEAN
                    && data[0] != Response.Complete.FILL_COMPLETE) {
                return;
            }
            CommandBridge.getInstance().showToast("!!!!TYPE="+type+", data="+data[0]);
            call.workFinish(true);
            break;
        case RPT_OP_PROGRESS:
            setProgress(data);
            break;
		default:
			break;
        }
    }

    /**
     * 维护（流路检测，称重校验）
     */
    private void maintain(byte[] data, ProtocolType type) {
        switch (type) {
        case RPT_OP_RESPONSE:
            if (data[0] != Response.Complete.B_ONE_STEP_COMPLETE
                    && data[0] != Response.Complete.B_TWO_STEP_COMPLETE
                    && data[0] != Response.Complete.B_THREE_STEP_COMPLETE
                    && data[0] != Response.Complete.WEIGHT_ONE_STEP_COMPLETE
                    && data[0] != Response.Complete.WEIGHT_TWO_STEP_COMPLETE
                    && data[0] != Response.Complete.WEIGHT_THREE_STEP_COMPLETE
                    && data[0] != Response.Complete.WEIGHT_FOUR_STEP_COMPLETE) {
                return;
            }
            call.maintainFinish();
            break;
        case RPT_OP_PROGRESS:
            call.workSetMainTainProgress(data[2]);
            break;
		default:
			break;
        }

    }

    /**
     * 模式/流量 检测
     */
    private void check(byte[] data, ProtocolType type) {
        switch (type) {
        case RPT_OP_RESPONSE:
            if (data[0] != Response.Complete.CHECK_A_COMPLETE
                    && data[0] != Response.Complete.CHECK_B_COMPLETE
                    && data[0] != Response.Complete.CHECK_C_COMPLETE
                    && data[0] != Response.Complete.CHECK_D_COMPLETE
                    && data[0] != Response.Complete.CHECK_E_COMPLETE) {
                return;
            }
            // TODO: 15/9/3 流量监测完成toast，还差区分ABCDE和模式/流量
            CommandBridge.getInstance().linkJianCeFinish();
            break;
        case RPT_OP_PROGRESS:
            break;
		default:
			break;
        }
    }

    /**
     * 离心
     * @param data
     * @param type
     */
    private void centrifugal(byte[] data, ProtocolType type) {
        switch (type) {
            case RPT_OP_RESPONSE:
                if (data[0] != Response.Complete.CENTRIFUGAL_COMPLETE) {
                        return;
                }
                CommandBridge.getInstance().showToast("!!!!TYPE="+type+", data="+data[0]);
                call.workSetProgress(100);
                call.workFinish(true);
                break;
            case RPT_OP_PROGRESS:
                setProgress(data);
                break;
        }
    }



    final int colors[] = {0xffffffff,0xffcc00ff,0xffff9933,0xff0000ff,0xffffffff};
    private byte lastProgressResponse = -100; //上一次的进度命令

    /**
     * 重置上一次的命令
     */
    public void resetLastProgressResponse() {
        lastProgressResponse = -100;
    }
    /**
     * @param data 设置进度信息
     */
    private void setProgress(byte[] data) {
        if (lastProgressResponse != data[1]) { //和上一次命令不一样时（否则相同参数动画不连贯）
            call.workStopAllSpout();
            call.workStartDiskColorAnimation(colors[data[1] % 5], 2000, 0, 180); //颜色渐变
            switch (data[1]) {
                case Response.Progress.PROGRESS_SAFFRON:
                    call.workSetProgressText(R.string.clean_jet, R.string.saffron);
                    call.workStartSpout(SpoutView.A, false);
                    break;
                case Response.Progress.PROGRESS_ALCOHOL:
                    call.workSetProgressText(R.string.clean_jet, R.string.alcohol_fixed);
                    call.workStartSpout(SpoutView.E, false);
                    break;
                case Response.Progress.PROGRESS_IODINE:
                    call.workSetProgressText(R.string.clean_jet, R.string.iodine);
                    call.workStartSpout(SpoutView.B, false);
                    break;
                case Response.Progress.PROGRESS_CRYSTAL_VIOLET:
                    call.workSetProgressText(R.string.clean_jet, R.string.crystal_violet);
                    call.workStartSpout(SpoutView.C, false);
                    break;
                case Response.Progress.PROGRESS_CLEAN:
                    call.workSetProgressText(R.string.progress_clean_flow_path, R.string.etc);
                    call.workStartSpout(SpoutView.DF, true);
                    call.workStartSpout(SpoutView.DR, true);
                    break;
                case Response.Progress.PROGRESS_FILL:
                    call.workSetProgressText(R.string.progress_fill, R.string.etc);
                    call.workStartSpout(SpoutView.A, false);
                    call.workStartSpout(SpoutView.B, false);
                    call.workStartSpout(SpoutView.C, false);
                    break;
                case Response.Progress.PROGRESS_DRY:
                    call.workSetProgressText(R.string.progress_dry, R.string.etc);
                    call.workStartRotate(true);
                    break;
                case Response.Progress.PROGRESS_WAIT:
                    call.workSetProgressText(R.string.progress_wait, R.string.etc);
                    call.workStartRotate(false);
                    break;
                case Response.Progress.PROGRESS_HEAT:
                    call.workSetProgressText(R.string.progress_heat, R.string.etc);
                    break;
                case Response.Progress.PROGRESS_STONE_CARBONATE:
                    call.workSetProgressText(R.string.clean_jet, R.string.PROGRESS_STONE_CARBONATE);
                    call.workStartSpout(SpoutView.A, false);
                    break;
                case Response.Progress.PROGRESS_ACID_ALCOHOL:
                    call.workSetProgressText(R.string.clean_jet, R.string.PROGRESS_ACID_ALCOHOL);
                    call.workStartSpout(SpoutView.B, false);
                    break;
                case Response.Progress.PROGRESS_METHYLENE_BLUE:
                    call.workSetProgressText(R.string.clean_jet, R.string.PROGRESS_METHYLENE_BLUE);
                    call.workStartSpout(SpoutView.C, false);
                    break;
                case Response.Progress.PROGRESS_RHODAMINE_B:
                    call.workSetProgressText(R.string.clean_jet, R.string.PROGRESS_RHODAMINE_B);
                    call.workStartSpout(SpoutView.A, false);
                    break;
                case Response.Progress.PROGRESS_AURAMINE_O:
                    call.workSetProgressText(R.string.clean_jet, R.string.PROGRESS_AURAMINE_O);
                    call.workStartSpout(SpoutView.A, false);
                    break;
                case Response.Progress.PROGRESS_CENTRIFUGAL:
                    call.workSetProgressText(R.string.PROGRESS_CENTRIFUGAL, R.string.etc);
                    call.workStartRotate(false);
                    break;
                case Response.Progress.PROGRESS_CLEAN_PLUS:
                    call.workSetProgressText(R.string.PROGRESS_CLEAN_PLUS, R.string.etc);
                    call.workStartSpout(SpoutView.A, true);
                    call.workStartSpout(SpoutView.B, true);
                    call.workStartSpout(SpoutView.C, true);
                    call.workStartSpout(SpoutView.E, true);
                    call.workStartSpout(SpoutView.DF, true);
                    call.workStartSpout(SpoutView.DR, true);
                    break;
                case Response.Progress.PROGRESS_JETTING_KMNO4:
                    call.workSetProgressText(R.string.clean_jet, R.string.PROGRESS_JETTING_KMNO4);
                    call.workStartSpout(SpoutView.C, true);
                    break;
                case Response.Progress.PROGRESS_JETTING_RHODAMINE:
                    call.workSetProgressText(R.string.clean_jet, R.string.PROGRESS_JETTING_RHODAMINE);
                    call.workStartSpout(SpoutView.A, true);
                    break;
                case Response.Progress.PROGRESS_JETTING_FIRST_DYE:
                    call.workSetProgressText(R.string.clean_jet, R.string.PROGRESS_JETTING_FIRST_DYE);
                    call.workStartSpout(SpoutView.A, true);
                    break;
            }
            lastProgressResponse = data[1];
        }
        call.workSetProgress(data[2]);
    }


    @Override
    public void setContext(Context context) {
    	return;
    }
}
