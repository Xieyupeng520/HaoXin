package com.hp.android.haoxin.callback;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

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
            CommandBridge.getInstance().showToast("检测完成！");
            CommandBridge.getInstance().dismissProgressDialog();
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
     * @param data 设置进度信息
     */
    private void setProgress(byte[] data) {
        if (lastProgressResponse != data[1]) { //和上一次命令不一样时（否则相同参数动画不连贯）
            call.workStopAllSpout();
            call.workStartDiskColorAnimation(colors[data[1] % 5], 2000, 0, 180); //颜色渐变
            switch (data[1]) {
                case Response.Progress.PROGRESS_JET_RED:
                    call.workSetProgressText("正在喷射: ", "番红");
                    call.workStartSpout(SpoutView.A, false);
                    break;
                case Response.Progress.PROGRESS_ALCOHOL:
                    call.workSetProgressText("正在喷射: ", "酒精固定");
                    call.workStartSpout(SpoutView.E, false);
                    break;
                case Response.Progress.PROGRESS_IODINE:
                    call.workSetProgressText("正在喷射: ", "碘酒");
                    call.workStartSpout(SpoutView.B, false);
                    break;
                case Response.Progress.PROGRESS_CRYSTAL_VIOLET:
                    call.workSetProgressText("正在喷射: ", "结晶紫");
                    call.workStartSpout(SpoutView.C, false);
                    break;
                case Response.Progress.PROGRESS_CLEAN:
                    call.workSetProgressText("正在流路清洗 ", "...");
                    call.workStartSpout(SpoutView.DF, true);
                    call.workStartSpout(SpoutView.DR, true);
                    break;
                case Response.Progress.PROGRESS_FILL:
                    call.workSetProgressText("正在填充", "...");
                    call.workStartSpout(SpoutView.A, false);
                    call.workStartSpout(SpoutView.B, false);
                    call.workStartSpout(SpoutView.C, false);
                    break;
                case Response.Progress.PROGRESS_DRY:
                    call.workSetProgressText("正在甩干", "...");
                    call.workStartRotate(true);
                    break;
                case Response.Progress.PROGRESS_WAIT:
                    call.workSetProgressText("等待", "...");
                    call.workStartRotate(false);
                    break;
                case Response.Progress.PROGRESS_HEAT:
                    call.workSetProgressText("正在加热", "...");
                    break;
                case Response.Progress.PROGRESS_STONE_CARBONATE:
                    call.workSetProgressText("石碳酸复红", "...");
                    call.workStartSpout(SpoutView.A, false);
                    break;
                case Response.Progress.PROGRESS_ACID_ALCOHOL:
                    call.workSetProgressText("正在喷射:", "酸性酒精");
                    call.workStartSpout(SpoutView.B, false);
                    break;
                case Response.Progress.PROGRESS_METHYLENE_BLUE:
                    call.workSetProgressText("正在喷射:", "亚甲基蓝");
                    call.workStartSpout(SpoutView.C, false);
                    break;
                case Response.Progress.PROGRESS_RHODAMINE_B:
                    call.workSetProgressText("正在喷射:", "罗丹明 B");
                    call.workStartSpout(SpoutView.A, false);
                    break;
                case Response.Progress.PROGRESS_AURAMINE_O:
                    call.workSetProgressText("正在喷射:", "金胺 O");
                    call.workStartSpout(SpoutView.A, false);
                    break;
                case Response.Progress.PROGRESS_CENTRIFUGAL:
                    call.workSetProgressText("正在细胞离心", "");
                    call.workStartRotate(false);
                    break;
                case Response.Progress.PROGRESS_CLEAN_PLUS:
                    call.workSetProgressText("正在流路清洗 ", "...");
                    call.workStartSpout(SpoutView.A, true);
                    call.workStartSpout(SpoutView.B, true);
                    call.workStartSpout(SpoutView.C, true);
                    call.workStartSpout(SpoutView.E, true);
                    call.workStartSpout(SpoutView.DF, true);
                    call.workStartSpout(SpoutView.DR, true);
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
