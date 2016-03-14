package com.hp.android.haoxin.callback;

import android.content.Context;

import com.hx.protocol.ProtocolType;

/**
 * 读取下位机设备信息回调
 * Created by AZZ on 15/8/19.
 */
public interface OnReadDeviceDataCallBack {
    /**
     * 回调读取设备数据
     * @param data 数据分类值
     * @param type 数据类型
     */
    void onReadDeviceData(byte[] data, ProtocolType type);

    void setContext(final Context context);
}
