package com.hx.service;

import java.io.IOException;

/**
 * 通信握手协议业务
 * @author qp.wang
 * @since 2015.7.31
 *
 */
public interface IShakeHandsService {
	/**
	 * 发送握手协议
	 * @throws IOException
	 */
	public void sendShakeHands() throws IOException;

	/**
	 * 判断是否是握手协议应答
	 * @return true是应答协议
	 */
	public boolean isShakeHandsResp(byte [] resp);

}
