package com.hx.service;


import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidParameterException;
import java.util.Vector;

import com.hp.android.haoxin.callback.OnConnectedCallBack;
import com.hp.android.haoxin.callback.OnReadDeviceDataCallBack;
import com.hp.android.haoxin.command.CommandBridge;
import com.hx.protocol.IComDevice;
import com.hx.protocol.ProtocolImpl;
import com.hx.protocol.ProtocolType;

public class ReadDeviceData extends Thread {

	private InputStream mIs = null;
	private static int DATA_MAX_LEN = 64;
	private IShakeHandsService shakeHandsServiceImpl = null; 
	private OnReadDeviceDataCallBack mCallBack = null;
	private OnConnectedCallBack connListener = null;
	public static Vector<Buffer_C> buffer = new Vector<Buffer_C>();

	private CommandBridge commandBridge = CommandBridge.getInstance();

	public void setCallBack(OnReadDeviceDataCallBack callBack) {
		this.mCallBack = callBack;
	}

	public void setConnListener(OnConnectedCallBack connListener) {
		this.connListener = connListener;
	}

	public ReadDeviceData(IComDevice comDevice) {
		if (null == comDevice) {
			System.out.println("[ERR][ReadDeviceData]:comDevice=NULL");
			return;
		}
		
		try {
			//初始化握手协议
			shakeHandsServiceImpl = new ShakeHandsServiceImpl(comDevice);
			((ShakeHandsServiceImpl) shakeHandsServiceImpl).start();
		} catch (InvalidParameterException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}

		mIs = comDevice.getInputStream();
	}

	
	/**
	 * 对数组buf清零
	 * @param buf 被清零胡数组
	 */
	private void setZero(byte []buf) {
		for (int i=0; buf != null && i < buf.length; i++) {
			buf[i] = 0;
		}
	}
	
	/**
	 * 将buf指定长度size转换成16进制字符串
	 * @param buf 
	 * @param size
	 * @return
	 */
	private String byte2HexString(byte[] buf, int size) {
		StringBuffer sb = new StringBuffer();
		for (int i=0; i<buf.length && i <size; i++) {
			sb.append(Integer.toHexString(buf[i] & 0xFF)).append(" ");
		}
		return sb.toString().toUpperCase();
	}
	
	/**
	 * 系统延迟ms毫秒推出
	 * @param ms
	 */
	private void exitSys(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	/**
	 * 读取一字节数据
	 * @return 数据
	 */
	private byte readByteDataByWait() {
		int ret = -1;
		do {
			try {
				ret = mIs.read();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} while(ret == -1);
		
		return (byte)ret;
	}
	
	/**
	 * 读取来自串口的数据线程。
	 */
	@Override
	public void run() {
		if (mIs == null) {
			commandBridge.showToast("串口尚未初始化，请先初始化...，5s后系统退出");
			exitSys(5000);
			return;
		}
		
		ParseData pd = new ParseData();
		pd.setCallBack(mCallBack);
		pd.setConnListener(connListener);
		pd.createShakeHand(shakeHandsServiceImpl);
		pd.start();
		
		byte[] buffer = new byte[DATA_MAX_LEN];
		do {
			setZero(buffer);

			//head1
			int i = 0;
			buffer[i] = readByteDataByWait();
			if (buffer[i] != ProtocolType.PK_HEAD_1.code()) {
				commandBridge.showToast("read head1 error data="+ buffer[i]);
				continue;
			}
			
			//head2
			i++;
			buffer[i] = readByteDataByWait();
			if (buffer[i] != ProtocolType.PK_HEAD_2.code()) {
				commandBridge.showToast("read head2 error data="+ buffer[i]);
				continue;
			}

			i++;
			//message type
			buffer[i] = readByteDataByWait();
			
			i++;
			//DLC
			buffer[i] = readByteDataByWait();
			int size = buffer[i];
			if (size > (DATA_MAX_LEN - 4)) {
				commandBridge.showToast("read dlc data="+ size + ", is too long...");
				continue;
			}
			
			//data+CRC+EOF
			i++;
			size +=  ProtocolImpl.CRC_OFFSET;
			for (int j = 0; j < size; j++) {
				buffer[i + j] = readByteDataByWait();
			}

			size +=  i;
			commandBridge.showToast("read data= "+ byte2HexString(buffer, size) + ",size="+size);
			Buffer_C dest = new Buffer_C();
			dest.setSize(size);
			dest.setBuffer(buffer);
			this.buffer.add(dest);
		} while(true);
	}
		
	class Buffer_C {
		byte [] buffer = new byte[DATA_MAX_LEN];
		int size;
		public byte[] getBuffer() {
			return buffer;
		}
		public void setBuffer(byte[] buffer) {
			System.arraycopy(buffer, 0, this.buffer, 0, DATA_MAX_LEN);
		}
		public int getSize() {
			return size;
		}
		public void setSize(int size) {
			this.size = size;
		}
	}
}