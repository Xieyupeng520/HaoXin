package com.hx.utils;

/**
 * CRC校验：算法为发送方将byte[2：N-2]进行加法运算，产生8bit结果，
 * 按位取反后 作为CRC填充到报文byte[N-2],N为数据报文长度
 * @author qp.wang
 * @since 2015-07-31
 *
 */
public class Crc {
	private static int CRC_START = 2;
	private static int CRC_OFFSET = 2;
	/**
	 * 计算指定b[]数组的开始到结束下标的CRC值
	 * @param b 报文
	 * @return
	 */
	public static byte calcCRC(byte[] b, int size) {
		byte crc = 0;
		int len = size - CRC_OFFSET;

		for (int i = CRC_START; i < len; i++) {
			crc += b[i];
		}
		return (byte) ~crc;
	}

	public static void main(String arg[]) {
		byte[] packets = {0x55, (byte) 0xAA, 0x02, (byte) 0x00,  (byte) 0xFD, (byte) 0xFF};
		for (int i=0;i<packets.length;i++) {
			System.out.println(packets[i]);
		}
		System.out.println("crc=="+calcCRC(packets, packets.length));
	}
}
