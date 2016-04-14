package com.hx.protocol;

/**
 * 通信握手协议接口
 * @author qp.wang
 * @since 2015-07-31
 *
 */

public interface IShakeHands {
	/**
	 * 获取握手协议报文
	 * @return 报文
	 */
	public byte[] getShakeHands();

	/**
	 * 判断是否是握手协议回应报文
	 * @param resp 被判断报文
	 * @return true是正确报文
	 */
	public boolean isShakeHandsResp(byte[] resp);

}
