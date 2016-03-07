package com.hx.protocol;

import com.hx.domain.ModeDetectBean;
import com.hx.domain.ParameterBean;

/**
 * 业务协议接口
 * @author qp.wang
 * @since 2015.8.1
 *
 */

public interface IProtocol {
	/**
	 * 查询下位机硬件报文
	 * @return 下位机硬件版本查询报文
	 */
	public byte[] createHWVerPackets();

	/**
	 * 判断是否下位机硬件版本回应报文
	 * @param packets 回应报文
	 * @return true下位机硬件版本查询回应报文，
	 */
	public boolean isHWVerRespPackets(byte[] packets, int size);

	/**
	 * 获取下位机硬件版本数据
	 * @param packets 回应报文
	 * @return 下位机硬件版本
	 */
	public byte[] getHWVersion(byte[] packets);

	/**
	 * 查询下位机软件报文
	 * @return 下位机软件版本查询报文
	 */
	public byte[] createSWVerPackets();

	/**
	 * 判断是否下位软件版本回应报文
	 * @param packets 回应报文
	 * @return true下位机软件版本查询回应报文，
	 */
	public boolean isSWVerRespPackets(byte[] packets, int size);

	/**
	 * 获取下位软件版本
	 * @param packets 回应报文
	 * @return 下位机软件版本
	 */
	public byte[] getSWVersion(byte[] packets);

	/**
	 * 判断是否设备上报状态报文
	 * @param packets 回应报文
	 * @return true是设备状态，
	 */
	public boolean isDevStatusPackets(byte[] packets, int size);

	/**
	 * 获取设备上报状态数据
	 * @param packets 回应报文
	 * @return 状态有效数据
	 */
	public byte[] getDevStatus(byte[] packets);

	/**
	 * 判断是否设备上报异常事件报文
	 * @param packets 回应报文
	 * @return true设备异常报文，
	 */
	public boolean isDevExceptPackets(byte[] packets, int size);

	/**
	 * 获取设备上报异常事件
	 * @param packets 回应报文
	 * @return 设备异常数据
	 */
	public byte[] getDevExcept(byte[] packets);

	/**
	 * 判断是否操作状态回应报文
	 * @param packets 回应报文
	 * @return true操作状态回应报文，
	 */
	public boolean isOpStatusPackets(byte[] packets, int size);

	/**
	 * 获取操作回应状态
	 * @param packets 回应报文
	 * @return 操作回应状态
	 */
	public byte[] getOpStatus(byte[] packets);

	/**
	 * 判断是否操作进度回应报文
	 * @param packets 回应报文
	 * @return true操作状态回应报文，
	 */
	boolean isOpProgressPackets(byte[] respPackets, int size);

	/**
	 * 获取操作进度数据
	 * @param packets 回应报文
	 * @return 操作进度数据
	 */
	public byte[] getOpProgress(byte[] packets);

	/**
	 * 生成设置参数报文
	 * @param bean 参数数据
	 * @return 设置参数报文
	 */
	public byte[] createParameterPackets(ParameterBean bean);

	/**
	 * 生成染色操作报文
	 * @param bean 参数数据
	 * @return 染色报文
	 */
	public byte[] createDyeingPackets(byte data);

	/**
	 * 生成清洗操作报文
	 * @param bean 参数数据
	 * @return 清洗报文
	 */
	public byte[] createClearPackets(byte data);

	/**
	 * 生成填充流路操作报文
	 * @param data 参数数据
	 * @return 填充报文
	 */
	public byte[] createFillPackets(byte data);

	/**
	 * 生成模式检测操作报文
	 * @param bean 参数数据
	 * @return 模式检测报文
	 */
	public byte[] createModeDetectPackets(ModeDetectBean bean);

	/**
	 * 生成流量检测操作报文
	 * @param data 参数数据
	0：按键 A
	1：按键 B
	2：按键 C
	3：按键 D
	4：按键 E
	 * @return 流量检测报文
	 */
	public byte[] createFlowPackets(byte data);

	/**
	 * 生成B流路检测操作报文
	 * @param data 参数数据 
	0 ：退出当前操作，同时 UI 返回上一级页面
	1：STEP1：B 流路 500ML 去离子水检测
	2：STEP2：B 流路 200ML 清洗剂检测
	3：STEP3：B 液填充检测
	 * @return B流路检测报文
	 */
	public byte[] createBPassPackets(byte data);

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
	public byte[] createWeighPackets(byte data);

	/**
	 * 生成细胞离心报文
	 * @param data 参数数据 
	0 ：细胞离心
	1 ：取消
	 * @return 细胞离心报文
	 */
	byte[] createCytocentrifugationPackets(byte data);

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
	byte[] createEngineerPackets(byte a, byte b, byte c, byte d, byte e, byte speed);
}
