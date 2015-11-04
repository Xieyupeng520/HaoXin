package com.hx.service;

import java.io.IOException;
import java.io.OutputStream;

import com.hx.protocol.IComDevice;
import com.hx.protocol.IShakeHands;
import com.hx.protocol.ShakeHandsUartImpl;

public class ShakeHandsServiceImpl extends Thread implements IShakeHandsService {
	private OutputStream mOs = null;//输出流
	private IShakeHands mShakeHands;//握手协议

	public ShakeHandsServiceImpl(IComDevice comDevice) {
		if (comDevice == null) {
			System.out.println("[ERR][ShakeHandsServiceImpl]:mComDevice=NULL");
			return;
		}
		this.mOs = comDevice.getOutputStream();
		this.mShakeHands = new ShakeHandsUartImpl();
	}

	/**
	 * 发送握手协议
	 * @throws IOException
	 */
	@Override
	public void sendShakeHands() throws IOException {
		if (null == mOs || null == mShakeHands) {
			System.out.println("[ShakeHandsServiceImpl]:[sendShakeHands]:mOs="+mOs+", mShakeHands="+mShakeHands);
			return;
		}
		//if (os != null) {
		this.mOs.write(mShakeHands.getShakeHands());
		//}
	}

	/**
	 * 判断是否是握手协议应答
	 * @return true是应答协议
	 */
	@Override
	public boolean isShakeHandsResp(byte []b) {
		if (null == mShakeHands || null == b) {
			System.out.println("[ShakeHandsServiceImpl]:[isShakeHandsResp]:mShakeHands="+mShakeHands+",b="+b);
			return false;
		}
		return mShakeHands.isShakeHandsResp(b);
	}

	@Override
	public void run () {
		//int i = 0;
		while(true) {
			//i++;
			try {
				//if (i <= 3)
				sendShakeHands();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
