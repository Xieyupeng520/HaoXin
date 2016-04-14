package com.hx.service;

import java.util.Vector;

import android.util.Log;
import com.hp.android.haoxin.callback.OnConnectedCallBack;
import com.hp.android.haoxin.callback.OnReadDeviceDataCallBack;
import com.hx.protocol.IProtocol;
import com.hx.protocol.ProtocolImpl;
import com.hx.protocol.ProtocolType;
import com.hx.service.ReadDeviceData.DataBuffer;
/**
 * 处理数据线程
 * @author qp.wang
 *
 */
public class ParseData extends Thread {

	private IProtocol mProtocol = null;
	private IShakeHandsService shakeHandsServiceImpl = null; 
	private OnReadDeviceDataCallBack mCallBack = null;
	private OnConnectedCallBack connListener = null;
	private ConnectErrService mConnectErrService = null;

	public void setCallBack(OnReadDeviceDataCallBack callBack) {
		this.mCallBack = callBack;
	}

	public void setConnListener(OnConnectedCallBack connListener) {
		this.connListener = connListener;

		//初始化握手异常通知
		mConnectErrService = new ConnectErrService();
		mConnectErrService.setConnListener(connListener);
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
	
	public void createShakeHand(IShakeHandsService shakeHandsServiceImpl2) {
		this.shakeHandsServiceImpl = shakeHandsServiceImpl2;
	}
	
	/**
	 * 解析收到的数据
	 * @param buffer 缓存数据
	 * @param size 数据长度
	 */
	private void parseData(byte[] buffer, int size) {
		try{
		Log.e("ParseData", "paresData(" + byte2HexString(buffer, size));
		byte []data = null;
		ProtocolType code = null;

		if (mProtocol.isHWVerRespPackets(buffer, size)) {
			data = mProtocol.getHWVersion(buffer);
			code = ProtocolType.HW_VERSION;
		} else if (mProtocol.isSWVerRespPackets(buffer, size)) {
			data = mProtocol.getSWVersion(buffer);
			code = ProtocolType.SW_VERSION;
		} else if (mProtocol.isDevStatusPackets(buffer, size)) {
			data = mProtocol.getDevStatus(buffer);
			code = ProtocolType.RPT_DEV_STATUS;
		} else if (mProtocol.isDevExceptPackets(buffer, size)) {
			data = mProtocol.getDevExcept(buffer);
			code = ProtocolType.RPT_DEV_EXCEPTION;
		} else if (mProtocol.isOpProgressPackets(buffer, size)) {
			data = mProtocol.getOpProgress(buffer);
			code = ProtocolType.RPT_OP_PROGRESS;
		} else if(mProtocol.isOpStatusPackets(buffer, size)) {
			data = mProtocol.getOpStatus(buffer);
			code = ProtocolType.RPT_OP_RESPONSE;
		} else if (shakeHandsServiceImpl.isShakeHandsResp(buffer)) {
			mConnectErrService.setCount(0);
			connListener.onConnected(true);
			return;
		}

		if (data != null) {
			mCallBack.onReadDeviceData(data, code);
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 读取来自串口的数据线程。
	 */
	@Override
	public void run() {
		//初始化协议
		mProtocol = new ProtocolImpl();
		//启动通信异常检测服务
		mConnectErrService.start();
		Vector<DataBuffer> dataBuffer = ReadDeviceData.buffer;
		do {
			if (dataBuffer != null && dataBuffer.size()>0) {
				try {
					DataBuffer buffer = dataBuffer.get(0);
					if (buffer != null) {
						parseData(buffer.getBuffer(), buffer.getBuffer().length);
						dataBuffer.remove(0);
					} else {
						Log.e("ParseData", "buffer is null");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} while(true);
	}
}