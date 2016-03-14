package com.hx.protocol;

import com.hx.domain.ModeDetectBean;
import com.hx.domain.ParameterBean;
import com.hx.utils.Crc;

public class ProtocolImpl implements IProtocol {
	//CRC所在报文位置偏移量
	public final static int CRC_OFFSET = 2;

	//协议头长度
	public final static int PROTOCOL_HEAD_LEN = 4;

	//软件硬件版本数据长度
	private final static int VER_DATA_LEN = 6;

	//设备系统状态数据长度
	private final static int DEV_STATUS_DATA_LEN = 20;

	//异常事件数据长度
	private final static int DEV_EXCEPTION_DATA_LEN = 4;

	//操作回应数据长度
	private final static int DEV_OP_RESP_DATA_LEN = 1;

	//操作进度数据长度
	private final static int DEV_OP_PROGRESS_DATA_LEN = 3;
	/**
	 * 查询下位机硬件报文
	 * @return 下位机硬件版本查询报文
	 */
	@Override
	public byte[] createHWVerPackets() {
		byte[] packets = {0x55, (byte) 0xAA, 0x01, (byte) 0x00,  (byte) 0xFE, (byte) 0xFF};

		return packets;
	}

	/**
	 * 判断是否下位机硬件版本回应报文
	 * @param packets 回应报文
	 * @param version 输出版本号 6
	 * @return true下位机硬件版本查询回应报文，
	 */
	@Override
	public boolean isHWVerRespPackets(byte[] respPackets, int size) {
		byte[] packets = {0x55, (byte) 0xAA, 0x01, (byte) 0x06, 00, 00, 00, 00,00,00, (byte) 0xFE, (byte) 0xFF};

		return checkVerPackets(respPackets, packets, size);
	}

	/**
	 * 获取下位机硬件版本数据
	 * @param packets 回应报文
	 * @return 下位机硬件版本
	 */
	@Override
	public byte[] getHWVersion(byte[] respPackets) {
		return parseData(respPackets, VER_DATA_LEN);
	}

	/**
	 * 查询下位机软件版本报文
	 * @return 下位机硬件版本查询报文
	 */
	@Override
	public byte[] createSWVerPackets() {
		byte[] packets = {0x55, (byte) 0xAA, 0x02, (byte) 0x00, (byte) 0xFD, (byte) 0xFF};

		return packets;
	}

	/**
	 * 判断是否下位机软件版本回应报文
	 * @param packets 回应报文
	 * @return true下位机硬件版本查询回应报文，
	 */
	@Override
	public boolean isSWVerRespPackets(byte[] respPackets, int size) {
		byte[] packets = {0x55, (byte) 0xAA, 0x02, (byte) 0x06, 00, 00, 00, 00, 00, 00, (byte) 0xFE, (byte) 0xFF};

		return checkVerPackets(respPackets, packets, size);
	}

	/**
	 * 检查版本信息是否正确，带出版本信息在version中
	 * @param respPackets 回应的版本数据
	 * @param packets 待比较的版本
	 * @param version 带出版本信息 6
	 * @return true是正确的版本信息
	 */
	private boolean checkVerPackets(byte[] respPackets, byte[] packets, int size) {
		if (null == respPackets) {
			return false;
		}

		//判断头部
		for (int i = 0; i < PROTOCOL_HEAD_LEN; i++) {
			if (respPackets[i] != packets[i]) {
				return false;
			}
		}

		//判断CRC
		if (respPackets[packets.length - CRC_OFFSET + 1] == (byte)0xFF
				&& respPackets[packets.length - CRC_OFFSET] == Crc.calcCRC(respPackets, size)) {
			return true;
		}

		return false;
	}

	/**
	 * 获取下位软件版本
	 * @param packets 回应报文
	 * @return 下位机软件版本
	 */
	@Override
	public byte[] getSWVersion(byte[] respPackets) {
		return parseData(respPackets, VER_DATA_LEN);
	}

	/**
	 * 分析版本数据，并返回版本数据
	 * @param packets 回应报文
	 * @param dataLen 报文长度
	 * @return 报文数据
	 */
	private byte[] parseData(byte[] respPackets, int dataLen) {
		if (null == respPackets || respPackets.length < (PROTOCOL_HEAD_LEN + dataLen)) {
			return null;
		}

		byte[] data = new byte[dataLen];

		//取得版本号
		if (data != null) {
			for (int i = 0; i < dataLen; i++) {
				data[i] = respPackets[PROTOCOL_HEAD_LEN + i];
			}
		}
		return data;
	}

	/**
	 * 检查上报报文是否正确，带出上报信息在data中
	 * @param respPackets 上报的报文数据
	 * @param packets 待比较对应上报报文头
	 * @return true是正确的报文
	 */
	private boolean checkReportPackets(byte[] respPackets, byte[] packets, int size) {
		if (null == respPackets) {
			return false;
		}

		//判断头部
		for (int i = 0; i < PROTOCOL_HEAD_LEN; i++) {
			if (respPackets[i] != packets[i]) {
				return false;
			}
		}

		//判断CRC
		if (respPackets[size - 1] == (byte)0xFF
				&& respPackets[size - CRC_OFFSET] == Crc.calcCRC(respPackets,size)) {
			return true;
		}

		return false;
	}

	/**
	 * 判断是否设备状态报文
	 * @param packets 回应报文
	 * @return true是设备状态，
	 */
	@Override
	public boolean isDevStatusPackets(byte[] respPackets, int size) {
		byte[] packets = {0x55, (byte) 0xAA, (byte)0x80, (byte) 0x14};
		return checkReportPackets(respPackets, packets,size);
	}

	/**
	 * 判断是否设备状态报文
	 * @param packets 回应报文
	 * @return true是设备状态，
	 */
	@Override
	public byte[] getDevStatus(byte[] respPackets) {
		return parseData(respPackets, DEV_STATUS_DATA_LEN);
	}

	/**
	 * 判断是否设备异常事件报文
	 * @param packets 回应报文
	 * @return true设备异常报文，
	 */
	@Override
	public boolean isDevExceptPackets(byte[] respPackets, int size) {
		byte[] packets = {0x55, (byte) 0xAA, (byte)0x81, (byte) 0x04};
		return checkReportPackets(respPackets, packets, size);
	}

	/**
	 * 判断是否下位软件版本回应报文
	 * @param packets 回应报文
	 * @return true操作状态回应报文，
	 */
	@Override
	public boolean isOpStatusPackets(byte[] respPackets, int size) {
		byte[] packets = {0x55, (byte) 0xAA, (byte)0x82, (byte) 0x01};
		return checkReportPackets(respPackets, packets, size);
	}

	/**
	 * 判断是否下位软件版本回应报文
	 * @param packets 回应报文
	 * @param data 输出有效数据
	 * @param outDataLen 有效数据长度 3
	 * @return true 进度信息，
	 */
	@Override
	public boolean isOpProgressPackets(byte[] respPackets, int size) {
		byte[] packets = {0x55, (byte) 0xAA, (byte)0x83, (byte) 0x03};
		return checkReportPackets(respPackets, packets, size);
	}

	/**
	 * 获取设置参数报文
	 * @param bean 回应报文
	 * @return 设置参数报文
	 */
	@Override
	public byte[] createParameterPackets(ParameterBean bean) {
		byte[] packets = {0x55, (byte) 0xAA, (byte)0x03, (byte) 0x0F,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, (byte)0xFF
		};
		packets[4] = bean.getGalssCount();
		packets[5] = bean.getDyeingThickness();
		packets[6] = bean.getAlcoholFix();
		packets[7] = bean.getCrystalViolet();
		packets[8] = bean.getIodine();
		packets[9] = bean.getWeigh();
		packets[10] = bean.getHeating();
		packets[11] = bean.getFilling();
		packets[12] = bean.getDyeing();
		packets[13] = bean.getReserve();
		packets[14] = bean.getReserve1();
		packets[15] = bean.getReserve2();
		packets[16] = bean.getReserve3();
		packets[17] = bean.getReserve4();
		packets[18] = bean.getReserve5();
		packets[19] = Crc.calcCRC(packets, packets.length);
		return packets;
	}

	/**
	 * 生成染色操作报文
	 * @param data 数据
	 *  00:开始染色 
	01:取消染色
	02:进行填充
	03:取消填充
	 * @return 染色报文
	 */
	@Override
	public byte[] createDyeingPackets(byte data) {
		byte[] packets = {0x55, (byte)0xAA, (byte)0x04, (byte)0x01, 0, 0, (byte)0xFF};
		packets[4] = data;
		packets[5] = Crc.calcCRC(packets, packets.length);
		return packets;
	}

	/**
	 * 生成清洗操作报文
	 * @param data 参数数据
	0:开始清洗
	1:取消清洗
	2:开始填充
	3:取消填充
	 * @return 清洗报文
	 */
	@Override
	public byte[] createClearPackets(byte data) {
		byte[] packets = {0x55, (byte)0xAA, (byte)0x05, (byte)0x01, 0, 0, (byte)0xFF};
		packets[4] = data;
		packets[5] = Crc.calcCRC(packets, packets.length);
		return packets;
	}

	/**
	 * 生成填充流路操作报文
	 * @param data 参数数据
	0:开始填充
	1:取消填充
	 * @return 填充报文
	 */
	@Override
	public byte[] createFillPackets(byte data) {
		byte[] packets = {0x55, (byte)0xAA, (byte)0x06, (byte)0x01, 0, 0, (byte)0xFF};
		packets[4] = data;
		packets[5] = Crc.calcCRC(packets, packets.length);
		return packets;
	}

	/**
	 * 生成模式检测操作报文
	 * @param data 参数数据
	 * @return 模式检测报文
	 */
	@Override
	public byte[] createModeDetectPackets(ModeDetectBean bean) {
		byte[] packets = {0x55, (byte)0xAA, (byte)0x07, (byte)0x02, 0, 0, 0, (byte)0xFF};
		packets[4] = bean.getData4();
		packets[5] = bean.getData5();
		packets[6] = Crc.calcCRC(packets, packets.length);
		return packets;
	}

	/**
	 * 生成流路检测操作报文
	 * @param data 参数数据 
	0：按键 A
	1：按键 B
	2：按键 C
	3：按键 D
	4：按键 E
	 * @return 流路检测报文
	 */
	@Override
	public byte[] createFlowPackets(byte data) {
		byte[] packets = {0x55, (byte)0xAA, (byte)0x08, (byte)0x01, 0, 0, (byte)0xFF};
		packets[4] = data;
		packets[5] = Crc.calcCRC(packets, packets.length);
		return packets;
	}

	/**
	 * 生成B流路检测操作报文
	 * @param data 参数数据 
	0 ：退出当前操作，同时 UI 返回上一级页面
	1：STEP1：B 流路 500ML 去离子水检测
	2：STEP2：B 流路 200ML 清洗剂检测
	3：STEP3：B 液填充检测
	 * @return B流路检测报文
	 */
	@Override
	public byte[] createBPassPackets(byte data) {
		byte[] packets = {0x55, (byte)0xAA, (byte)0x09, (byte)0x01, 0, 0, (byte)0xFF};
		packets[4] = data;
		packets[5] = Crc.calcCRC(packets, packets.length);
		return packets;
	}

	/**
	 * 生成称重校验报文
	 * @param data 参数数据 
	0 ：退出当前操作，同时 UI 返回上一级页
	1 ：STEP1：非常重要，请仔细阅读,请严格按照提示步骤操作！
	2 ：STEP2：请确定载玻盘移除
	3 ：STEP3：请将每个瓶子放入 50mL 试剂
	4 ：STEP4：定标正在进行中，请等待 5S
	 * @return 称重校验报文
	 */
	@Override
	public byte[] createWeighPackets(byte data) {
		byte[] packets = {0x55, (byte)0xAA, (byte)0x0A, (byte)0x01, 0, 0, (byte)0xFF};
		packets[4] = data;
		packets[5] = Crc.calcCRC(packets, packets.length);
		return packets;
	}

	/**
	 * 生成细胞离心报文
	 * @param data 参数数据 
	0 ：细胞离心
	1 ：取消
	 * @return 细胞离心报文
	 */
	@Override
	public byte[] createCytocentrifugationPackets(byte data) {
		byte[] packets = {0x55, (byte)0xAA, (byte)0x0B, (byte)0x01, 0, 0, (byte)0xFF};
		packets[4] = data;
		packets[5] = Crc.calcCRC(packets, packets.length);
		return packets;
	}

	@Override
	public byte[] getDevExcept(byte[] packets) {
		return parseData(packets, DEV_EXCEPTION_DATA_LEN);
	}

	@Override
	public byte[] getOpStatus(byte[] packets) {
		return parseData(packets, DEV_OP_RESP_DATA_LEN);
	}

	/**
	 * 获取操作进度报文
	 * @param packets 输入的报文
	 * @return 去除头部数据，留下报文体
	 */
	@Override
	public byte[] getOpProgress(byte[] packets) {
		return parseData(packets, DEV_OP_PROGRESS_DATA_LEN);
	}
	
	/**
	 * 生成工程师报文
	 * @param a a泵参数数据 
	 * @param b b泵参数数据 
	 * @param c c泵参数数据 
	 * @param d d泵参数数据 
	 * @param e e泵参数数据 
	 * @param speed 离心转速
	 * @return 工程师报文
	 */
	@Override
	public byte[] createEngineerPackets(byte a, byte b, byte c, byte d, byte e, byte speed) {
		byte[] packets = {0x55, (byte)0xAA, (byte)0x0D, (byte)0x06, 0, 0, 0, 0, 0, 0, 0, (byte)0xFF};
		packets[4] = a;
		packets[5] = b;
		packets[6] = c;
		packets[7] = d;
		packets[8] = e;
		packets[9] = speed;
		packets[10] = Crc.calcCRC(packets, packets.length);
		return packets;		
	}
}