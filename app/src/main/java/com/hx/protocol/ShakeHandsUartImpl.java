package com.hx.protocol;
/**
 * 通信握手协议，串口握手协议实现
 * @author qp.wang
 * @since 2015-07-31
 *
 */
public class ShakeHandsUartImpl implements IShakeHands {
	@Override
	public byte[] getShakeHands() {
		byte[] SHAKE_HANDS_REQ = {0x55, (byte) 0xAA, (byte) 0xAA, 0x01, (byte) 0xA5,  (byte) 0xAF, (byte) 0xFF};

		return SHAKE_HANDS_REQ;
	}

	@Override
	public boolean isShakeHandsResp(byte[] resp) {
		try {
			byte[] SHAKE_HANDS_RESP = {0x55, (byte) 0xAA, (byte) 0xAA, 0x01, (byte) 0x5A, (byte) 0xFA, (byte) 0xFF};
			int len = SHAKE_HANDS_RESP.length;
			
			for (int i = 0; i < len; i++) {
				if (resp[i] != SHAKE_HANDS_RESP[i]) {
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
