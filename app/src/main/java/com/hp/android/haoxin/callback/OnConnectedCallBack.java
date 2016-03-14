package com.hp.android.haoxin.callback;

/**
 * 与下机位连接通信回调
 * Created by AZZ on 15/8/19.
 */

public interface OnConnectedCallBack {
    /**
     * 监听连接
     * @param isConnected true 连接成功；false 连接失败
     */
    void onConnected(final boolean isConnected);

    /**
     * 第一次连接通信回调
     */
    interface OnFirstConnectedCallBack {
        /**
         * 初始化连接
         * @param isConnected
         */
        void onFirstConnected(boolean isConnected);

    }

    /**
     * 连接状态发生改变时
     */
    interface OnConnectChangedCallBack {
        /**
         * 连接中断
         */
        abstract void onConnectedInterrupt();

        /**
         * 连接恢复
         */
        abstract void onConnectedResume();
    }
}
