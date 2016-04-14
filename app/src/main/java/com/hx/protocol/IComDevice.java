package com.hx.protocol;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 通信设备接口
 * @author qp.wang
 *
 */
public interface IComDevice {

	/**
	 * 获取输出流
	 * @return 输出流句柄
	 */
	public OutputStream getOutputStream();

	/**
	 * 获取输入流
	 * @return 输入流句柄
	 */
	public InputStream getInputStream();

	/**
	 * 关闭设备
	 */
	public void close();
}
