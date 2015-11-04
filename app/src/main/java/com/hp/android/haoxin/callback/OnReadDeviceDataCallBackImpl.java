package com.hp.android.haoxin.callback;

import android.content.Context;
import android.util.Log;

import com.hp.android.haoxin.R;
import com.hp.android.haoxin.command.CommandBridge;
import com.hp.android.haoxin.global.Global;
import com.hp.android.haoxin.global.GlobalException;
import com.hx.protocol.ProtocolType;

/**
 * 读取设备信息回调实现
 * Created by AZZ on 15/8/26 22:17.
 */
public class OnReadDeviceDataCallBackImpl implements OnReadDeviceDataCallBack {
    private static final String TAG = "OnReadDeviceDataCall";
    /**
     * 单例
     */
    private static OnReadDeviceDataCallBackImpl instance = null;
    private CommandBridge commandBridge = CommandBridge.getInstance();
    private Context mContext;
    /**
     * 读取版本号信息
     */
    private OnReadDeviceDataCallBack readVersionCallBack = new OnReadDeviceVersionCallBackImpl();
    /**
     * 读取操作回调
     */
    private OnReadDeviceDataCallBack readOptionCallBack = new OnReadDeviceOptionCallBackImpl();
    /**
     * 单例模式
     * @return 唯一单例
     */
    public static OnReadDeviceDataCallBackImpl getInstance() {
        if (instance == null) {
            instance = new OnReadDeviceDataCallBackImpl();
        }
        return instance;
    }

    /**
     * @param context 正确的话应该是MainActivity的上下文
     */
    public void setContext(final Context context) {
        mContext = context;
        if (readVersionCallBack != null) {
            readVersionCallBack.setContext(context);
        }
        if (readOptionCallBack != null) {
            readOptionCallBack.setContext(context);
        }
    }
    
    private void disposeException (byte data) {
    	GlobalException exception = new GlobalException(data);
    	int id = 0;
    	if (exception.isDevMotorExeception()) {
    		//电机异常
    		id = R.string.exception_of_electric_speed;
    	} else if(exception.isCoverOpenByDye()) {
    		id = R.string.exception_top_cover_opened_dyeing;
    	} else if(exception.isCoverOpenByClean()) {
    		id = R.string.exception_top_cover_opened_cleaning;
    	} else if(exception.isCoverOpenByFill()) {
    		id = R.string.exception_top_cover_opened_filling;
    	} else if(exception.isCoverOpenByCytocentrifugation()) {
    		id = R.string.exception_top_cover_opened_centrifugal;
    	} else if(exception.isHeatingException()) {
    		id = R.string.exception_heating_failure;
    	} else if(exception.isGlassCardException()) {
    		id = R.string.exception_detect_glass_position_failure;
    	} else if(exception.isWeighException()) {
    		id = R.string.exception_weighing;
    	}
    	if (id != 0) {
            commandBridge.showExceptionDialog(id, mContext);
    	}
    }
    
    @Override
    public void onReadDeviceData(byte[] data, ProtocolType type) {
        Log.d(TAG, "onReadDeviceData(" + data + ", " + type + ")" );

        switch (type) {
            case HW_VERSION:
            case SW_VERSION:
                if (readVersionCallBack != null) {
                    readVersionCallBack.onReadDeviceData(data, type);
                }
                break;
            case RPT_DEV_STATUS:
            	Global.getSystemStateBean().setStatDevLid(data[0]);
//            	{
//            		commandBridge.showToast("!!!收到了系统状态数据：门状态："+data[0]);
//            	}
            	Global.getSystemStateBean().setStatAReagent(data[1]);
            	Global.getSystemStateBean().setStatBReagent(data[2]);
            	Global.getSystemStateBean().setStatCReagent(data[3]);
            	Global.getSystemStateBean().setStatDReagent(data[4]);
            	Global.getSystemStateBean().setStatEReagent(data[5]);
            	Global.getSystemStateBean().setStatDevFilled(data[6]);
            	break;
            case RPT_DEV_EXCEPTION:
            	disposeException(data[0]);
            	
            	break;
            case RPT_OP_RESPONSE:
            case RPT_OP_PROGRESS:
                if (readOptionCallBack != null) {
                    readOptionCallBack.onReadDeviceData(data, type);
                }
                break;
            default:
                break;
        }
    }
}
