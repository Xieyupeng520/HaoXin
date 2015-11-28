package com.hp.android.haoxin.test;

import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.hp.android.haoxin.callback.OnReadDeviceDataCallBack;
import com.hp.android.haoxin.callback.OnReadDeviceDataCallBackImpl;
import com.hp.android.haoxin.global.Response;
import com.hx.protocol.ProtocolType;

/**
 * Created by AZZ on 15/9/26 18:36.
 */
public class TestCmd {
    private static TestCmd mInstance; //单例

    public static TestCmd getInstance() {
        if (mInstance == null) {
            mInstance = new TestCmd();
        }
        return mInstance;
    }


    /**
     * 得到数据报文 回调
     * @return
     */
    public OnReadDeviceDataCallBack getReadDeviceCallBack() {
        return OnReadDeviceDataCallBackImpl.getInstance();
    }

    /**
     * 测试：buglist-18: 填充过程，清洗过程，染色过程中，在进度显示等待和甩干的时候，转盘动画停止，不喷液；应该是旋转，不喷液。
     */
    public void testDryAndWaitRotate() {
        byte[] data = new byte[]{0x00, Response.Progress.PROGRESS_DRY, 20};
        getReadDeviceCallBack().onReadDeviceData(data, ProtocolType.RPT_OP_PROGRESS);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                byte[] data = new byte[]{0x00, Response.Progress.PROGRESS_WAIT, 20};
                getReadDeviceCallBack().onReadDeviceData(data, ProtocolType.RPT_OP_PROGRESS);
            }
        }, 5000);
    }


    int progress = 0;
    final Handler handler = new Handler();
    Runnable runnable;
    /**
     * 测试清洗
     */
    public void testClean() {
//        byte[] data = new byte[]{0x00, Response.Progress.PROGRESS_CLEAN, 0x60};
//        byte[] data = new byte[]{0x00};
//        getReadDeviceCallBack().onReadDeviceData(data, ProtocolType.RPT_OP_PROGRESS);

        progress = 0;
        runnable = new Runnable() {
            @Override
            public void run() {
                progress += 1;
                if (progress < 20) {
                    byte[] data = new byte[]{0x00, Response.Progress.PROGRESS_CLEAN_PLUS, (byte) progress};
                    getReadDeviceCallBack().onReadDeviceData(data, ProtocolType.RPT_OP_PROGRESS);
                } else if (progress < 40) {
                    byte[] data = new byte[]{0x00, Response.Progress.PROGRESS_CENTRIFUGAL, (byte) progress};
                    getReadDeviceCallBack().onReadDeviceData(data, ProtocolType.RPT_OP_PROGRESS);
                } else if (progress < 60) {
                    byte[] data = new byte[]{0x00, Response.Progress.PROGRESS_FILL, (byte) progress};
                    getReadDeviceCallBack().onReadDeviceData(data, ProtocolType.RPT_OP_PROGRESS);
                } else if (progress < 80) {
                    byte[] data = new byte[]{0x00, Response.Progress.PROGRESS_WAIT, (byte) progress};
                    getReadDeviceCallBack().onReadDeviceData(data, ProtocolType.RPT_OP_PROGRESS);
                } else if (progress < 100) {
                    byte[] data = new byte[]{0x00, Response.Progress.PROGRESS_DRY, (byte) progress};
                    getReadDeviceCallBack().onReadDeviceData(data, ProtocolType.RPT_OP_PROGRESS);
                } else if (progress == 100){
                    testFinish();
                }
                if (progress < 100) {
                    handler.postDelayed(this, 500);
                }
            }
        };
        handler.post(runnable);
    }

    /**
     * 测试取消染色/清洗/填充
     */
    public void testCancel() {
        handler.removeCallbacks(runnable);
    }
    /**
     * 测试清洗完成（提示填充）
     * 清洗染色填充发送报文都是一样的，所以可合并为一个方法。
     */
    public void testFinish() {
        byte[] data = new byte[]{0x00};
        getReadDeviceCallBack().onReadDeviceData(data, ProtocolType.RPT_OP_RESPONSE);
    }

    /**
     * 测试流路检测
     */
    public void testLiuLu() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (progress > 3) {
                    progress = 0;
                }
                byte[] data = null;
                switch (progress++) {
                    case 1:
                        data = new byte[]{Response.Complete.B_ONE_STEP_COMPLETE};
                        break;
                    case 2:
                        data = new byte[]{Response.Complete.B_TWO_STEP_COMPLETE};
                        break;
                    default:
                        data = new byte[]{Response.Complete.B_THREE_STEP_COMPLETE};
                        break;
                }

                getReadDeviceCallBack().onReadDeviceData(data, ProtocolType.RPT_OP_RESPONSE);

            }
        };
        handler.postDelayed(runnable, 3000);
    }

    /**
     * 测试流路检测进度
     */
    public void testLiuLuProgress() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.d("testLiuLuProgress", "progress = " + progress);
                if (progress >= 100) {
                    progress = 0;
                }
                progress += 5;
                byte[] data = new byte[]{Response.Complete.B_THREE_STEP_COMPLETE, 0x00, (byte) progress};
                getReadDeviceCallBack().onReadDeviceData(data, ProtocolType.RPT_OP_PROGRESS);
                if (progress < 100) {
                    handler.postDelayed(this, 500);
                } else {
                    getReadDeviceCallBack().onReadDeviceData(data, ProtocolType.RPT_OP_RESPONSE);
                }
            }
        };
        handler.post(runnable);
    }

    /**
     * 测试模式/流量 检测
     */
    public void testJianCe() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                byte[] data = new byte[]{Response.Complete.CHECK_A_COMPLETE};
                getReadDeviceCallBack().onReadDeviceData(data, ProtocolType.RPT_OP_RESPONSE);
            }
        };
        handler.postDelayed(runnable, 3000);
    }

    public void testDisposeException() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                byte[] data = new byte[]{Response.Exception.WEIGHING_EXCEPTION};
                getReadDeviceCallBack().onReadDeviceData(data, ProtocolType.RPT_DEV_EXCEPTION);
            }
        };
        handler.postDelayed(runnable, 3000);
    }
}
