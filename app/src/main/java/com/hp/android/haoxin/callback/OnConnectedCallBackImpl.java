package com.hp.android.haoxin.callback;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.hp.android.haoxin.global.Global;

import java.util.ArrayList;

/**
 * 监听连接状态
 * Created by AZZ on 15/8/26 21:21.
 */
public class OnConnectedCallBackImpl  implements OnConnectedCallBack {
    /**
     * 单例
     */
    private static OnConnectedCallBackImpl instance = null;
    /**
     * 第一次连接回调
     */
    OnFirstConnectedCallBack firstCallBack;
    /**
     * 连接状态发生改变时
     */
    ArrayList<OnConnectChangedCallBack> changedCallBacks;
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    /**
     * 单例模式
     * @return 唯一单例
     */
    public static OnConnectedCallBackImpl getInstance() {
        if (instance == null) {
            instance = new OnConnectedCallBackImpl();
        }
        return instance;
    }
    /**
     * 监听连接
     *
     * @param isConnected true 连接成功；false 连接失败
     */
    @Override
    public void onConnected(final boolean isConnected) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (isConnected) {
                    onConnectSuccessed();
                } else {
                    onConnectFailed();
                }
            }
        });
    }

    /**
     * 连接成功
     */
    private void onConnectSuccessed() {
        if (Global.isCommActived()) { //如果本来就连接成功，就忽略（因为500ms就会回调一次）
            return;
        } else {
            if (Global.getCommActived() == -1) { //如果为初始化时连接成功
                if (firstCallBack != null) {
                    firstCallBack.onFirstConnected(true);
                }
            } else {

            }
            Global.setCommActived(1);
        }
    }
    /**
     * 连接失败
     */
    private void onConnectFailed() {
        Log.e("OnConnectedCallBackImpl", "Global.getCommActived() = " + Global.getCommActived());
        if (Global.getCommActived() == -1) { //如果为初始化时连接失败
            if (firstCallBack != null) {
                firstCallBack.onFirstConnected(false);
            }
            return;
        } else {

        }
        Global.setCommActived(0);
    }

    /**----------------------------/ getters and setters /----------------------------**/
    public OnFirstConnectedCallBack getFirstCallBack() {
        return firstCallBack;
    }

    public void setFirstCallBack(OnFirstConnectedCallBack firstCallBack) {
        this.firstCallBack = firstCallBack;
    }

    public ArrayList<OnConnectChangedCallBack> getChangedCallBacks() {
        return changedCallBacks;
    }

    /**
     * @param listener 注册监听器
     */
    public void registerChangedListener(OnConnectChangedCallBack listener) {
        changedCallBacks.add(listener);
    }

    /**
     * @param listener 移除监听器
     */
    public void removeChangedListener(OnConnectChangedCallBack listener) {
        changedCallBacks.remove(listener);
    }
}
