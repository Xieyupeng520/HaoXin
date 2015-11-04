package com.hx.service;

import java.io.IOException;
import com.hx.domain.ModeDetectBean;
import com.hx.domain.ParameterBean;

/**
 * 协议操作业务接口
 * @author qp.wang
 * @since 2016-08-02
 */
public interface IOperatorService {
	/**
	 * 发送染色操作报文到下位机
	 * @param data 染色动作类型如下
	00:开始染色
	01:取消染色
	02:进行填充
	03:取消填充
	 * @throws IOException
	 */
	public void sendPackets(byte[] data, int size) throws IOException;

	/**
	 * 发送设置参数报文到下位机
	 * @param bean 参数数据，参数详见@ParameterBean
	 * @throws IOException
	 */
	public void sendParameterPackets(ParameterBean bean) throws IOException;

	/**
	 * 发送染色操作报文到下位机
	 * @param data 染色动作类型如下
	00:开始染色
	01:取消染色
	02:进行填充
	03:取消填充
	 * @throws IOException
	 */
	public void sendDyeingPackets(byte data) throws IOException;

	/**
	 * 发送清洗报文到下位机
	 * @param data，清洗动作如下：
	0:开始清洗
	1:取消清洗
	2:开始填充
	3:取消填充
	 * @throws IOException
	 */
	public void sendClearPackets(byte data) throws IOException;

	/**
	 * 发送填充报文到下位机
	 * @param data 填充动作：0:开始填充  1:取消填充 
	 * @throws IOException
	 */
	public void sendFillPackets(byte data) throws IOException;

	/**
	 * 发送模式检测请求报文到下位机
	 * @param data 详见ModeDetectBean
	 * @throws IOException
	 */
	public void sendModeDetectPakets(ModeDetectBean bean) throws IOException;

	/**
	 * 发送流量检测报文到下位机
	 * @param data 流量类型：
	0：按键 A
	1：按键 B
	2：按键 C
	3：按键 D
	4：按键 E
	 * @throws IOException
	 */
	public void sendFlowPackets(byte data) throws IOException;

	/**
	 * 发送B流路检测报文到下位机
	 * @param data B流路类型：
	 *  0 ：退出当前操作，同时 UI 返回上一级页面 
	1：STEP1: B 流路 500ML 去离子水检测
	2：STEP2：B 流路 200ML 清洗剂检测
	3：STEP3：B 液填充检测
	 * @throws IOException
	 */
	public void sendBPassPackets(byte data) throws IOException;

	/**
	 * 发送称重报文到下位机
	 * @param data 称重参数
	 * 0 ：退出当前操作，同时 UI 返回上一级页面 
	1 ：STEP1：非常重要，请仔细阅读,请严格按照提示步骤操作！
	2 ：STEP2：请确定载玻盘移除
	3 ：STEP3：请将每个瓶子放入 50mL 试剂
	4 ：STEP4：定标正在进行中，请等待 5S
	 * @throws IOException
	 */
	public void sendWeighPackets(byte data) throws IOException;

	/**
	 * 发送查询硬件版本报文到下位机
	 * @throws IOException
	 */
	public void sendHWVerPackets() throws IOException;

	/**
	 * 发送查询软件版本报文到下位机
	 * @throws IOException
	 */
	public void sendSWVerPackets() throws IOException;

	/**
	 * 发送细胞离心报文到下位机
	 * @param data 0细胞离心，1取消
	 * @throws IOException
	 */
	void sendCytocentrifugationPackets(byte data) throws IOException;
}
