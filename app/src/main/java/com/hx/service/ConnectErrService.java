package com.hx.service;

import com.hp.android.haoxin.callback.OnConnectedCallBack;

public class ConnectErrService extends Thread {
	//连接回调
	private OnConnectedCallBack connListener = null;
	//计数器
	private int mCount = 0;
	//重试次数
	private static int RETRY_MAX = 5;

	public void setCount(int iCount) {
		this.mCount = iCount;
	}

	public void setConnListener(OnConnectedCallBack connListener) {
		this.connListener = connListener;
	}
	
	@Override
	public void run () {
		while (true) {
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			mCount++;
			if (mCount == RETRY_MAX) {
				connListener.onConnected(false);
				mCount = 0;
			}
		}
	}
}
