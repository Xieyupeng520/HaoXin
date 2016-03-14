package com.hp.android.haoxin.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * FIXME：15/8/23 not used
 * Created by AZZ on 15/8/23 15:13.
 */
public class ConnectService extends Service {
    /**
     * 打印标志
     */
    public static final String TAG = "ConnectService";
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "ConnectService OnCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "ConnectService onStart");
        return START_NOT_STICKY;
    }
}
