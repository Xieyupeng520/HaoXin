package com.hp.android.haoxin.callback;

import android.content.Context;
import android.util.Log;

import com.hp.android.haoxin.global.Global;
import com.hx.protocol.ProtocolType;

/**
 * 读取软硬件版本回调
 * Created by AZZ on 15/8/27 22:29.
 */
public class OnReadDeviceVersionCallBackImpl implements OnReadDeviceDataCallBack {
    @Override
    public void onReadDeviceData(byte[] data, ProtocolType type) {
        switch (type) {
            case HW_VERSION:
                Log.i("OnReadDeviceVersionCall", "硬件版本：" + data);
                Global.HW_VERSION = data;
                break;
            case SW_VERSION:
                Log.i("OnReadDeviceVersionCall", "软件版本：" + data);
                Global.SW_VERSION = data;
                break;
            default:
                break;
        }
    }

    @Override
    public void setContext(Context context) {
    }
}
