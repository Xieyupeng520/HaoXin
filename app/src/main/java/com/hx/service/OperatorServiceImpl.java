package com.hx.service;

import java.io.IOException;
import java.io.OutputStream;

import com.hx.domain.ModeDetectBean;
import com.hx.domain.ParameterBean;
import com.hx.protocol.IComDevice;
import com.hx.protocol.IProtocol;
import com.hx.protocol.ProtocolImpl;

/**
 * 协议操作业务实现
 * @author qp.wang
 * @since 2016-08-02
 */
public class OperatorServiceImpl implements IOperatorService {
	private OutputStream mOs = null;
	private IProtocol mProtocol;//操作协议

	public OperatorServiceImpl(IComDevice comDevice) {
		if (comDevice == null) {
			System.out.println("[ERR][ReadDeviceData]:comDevice=NULL");
			return;
		}
		this.mOs = comDevice.getOutputStream();
		this.mProtocol = new ProtocolImpl();
	}

	/**
	 * 发送设置参数报文到下位机
	 * @param bean 参数数据，参数详见@ParameterBean
	 * @throws IOException
	 */
	@Override
	public void sendParameterPackets(ParameterBean bean) throws IOException {
		if (null == mOs || null == mProtocol) {
			System.out.println("[ERR][OperatorServiceImpl]:[sendParameterPackets]:mOs="+mOs+", mProtocol="+mProtocol);
			return;
		}
		mOs.write(mProtocol.createParameterPackets(bean));
	}

	/**
	 * 发送染色操作报文到下位机
	 * @param data 染色动作类型如下
	00:开始染色
	01:取消染色
	02:进行填充
	03:取消填充
	 * @throws IOException
	 */
	@Override
	public void sendDyeingPackets(byte data) throws IOException {
		if (null == mOs || null == mProtocol) {
			System.out.println("[ERR][OperatorServiceImpl]:[sendDyeingPackets]:mOs="+mOs+", mProtocol="+mProtocol);
			return;
		}
		mOs.write(mProtocol.createDyeingPackets(data));
	}

	/**
	 * 发送清洗报文到下位机
	 * @param data，清洗动作如下：
	0:开始清洗
	1:取消清洗
	2:开始填充
	3:取消填充
	 * @throws IOException
	 */
	@Override
	public void sendClearPackets(byte data) throws IOException {
		if (null == mOs || null == mProtocol) {
			System.out.println("[ERR][OperatorServiceImpl]:[sendClearPackets]:mOs="+mOs+", mProtocol="+mProtocol);
			return;
		}
		mOs.write(mProtocol.createClearPackets(data));
	}

	/**
	 * 发送填充报文到下位机
	 * @param data 填充动作：0:开始填充  1:取消填充 
	 * @throws IOException
	 */
	@Override
	public void sendFillPackets(byte data) throws IOException {
		if (null == mOs || null == mProtocol) {
			System.out.println("[ERR][OperatorServiceImpl]:[sendFillPackets]:mOs="+mOs+", mProtocol="+mProtocol);
			return;
		}
		mOs.write(mProtocol.createFillPackets(data));
	}

	/**
	 * 发送模式检测请求报文到下位机
	 * @param bean 详见ModeDetectBean
	 * @throws IOException
	 */
	@Override
	public void sendModeDetectPakets(ModeDetectBean bean) throws IOException {
		if (null == mOs || null == mProtocol) {
			System.out.println("[ERR][OperatorServiceImpl]:[sendModeDetectPakets]:mOs="+mOs+", mProtocol="+mProtocol);
			return;
		}
		mOs.write(mProtocol.createModeDetectPackets(bean));
	}

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
	@Override
	public void sendFlowPackets(byte data) throws IOException {
		if (null == mOs || null == mProtocol) {
			System.out.println("[ERR][OperatorServiceImpl]:[sendFlowPackets]:mOs="+mOs+", mProtocol="+mProtocol);
			return;
		}

		mOs.write(mProtocol.createFlowPackets(data));
	}

	/**
	 * 发送B流路检测报文到下位机
	 * @param data B流路类型：
	 *  0 ：退出当前操作，同时 UI 返回上一级页面 
	1：STEP1: B 流路 500ML 去离子水检测
	2：STEP2：B 流路 200ML 清洗剂检测
	3：STEP3：B 液填充检测
	 * @throws IOException
	 */
	@Override
	public void sendBPassPackets(byte data) throws IOException {
		if (null == mOs || null == mProtocol) {
			System.out.println("[ERR][OperatorServiceImpl]:[sendBPassPackets]:mOs="+mOs+", mProtocol="+mProtocol);
			return;
		}
		mOs.write(mProtocol.createBPassPackets(data));
	}

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
	@Override
	public void sendWeighPackets(byte data) throws IOException {
		if (null == mOs || null == mProtocol) {
			System.out.println("[ERR][OperatorServiceImpl]:[sendWeighPackets]:mOs="+mOs+", mProtocol="+mProtocol);
			return;
		}
		mOs.write(mProtocol.createWeighPackets(data));
	}

	/**
	 * 发送查询硬件版本报文到下位机
	 * @throws IOException
	 */
	public void sendHWVerPackets() throws IOException {
		if (null == mOs || null == mProtocol) {
			System.out.println("[ERR][OperatorServiceImpl]:[sendSWVerPackets]:mOs="+mOs+", mProtocol="+mProtocol);
			return;
		}
		mOs.write(mProtocol.createHWVerPackets());
		
	}

	/**
	 * 发送查询软件版本报文到下位机
	 * @throws IOException
	 */
	public void sendSWVerPackets() throws IOException {
		if (null == mOs || null == mProtocol) {
			System.out.println("[ERR][OperatorServiceImpl]:[sendSWVerPackets]:mOs="+mOs+", mProtocol="+mProtocol);
			return;
		}
		mOs.write(mProtocol.createSWVerPackets());
	}

	@Override
	public void sendPackets(byte[] data, int size) throws IOException {
		mOs.write(data);
	}
	
	/**
	 * 发送细胞离心报文到下位机
	 * @param data 0细胞离心，1取消
	 * @throws IOException
	 */
	@Override
	public void sendCytocentrifugationPackets(byte data) throws IOException {
		if (null == mOs || null == mProtocol) {
			System.out.println("[ERR][OperatorServiceImpl]:[sendSWVerPackets]:mOs="+mOs+", mProtocol="+mProtocol);
			return;
		}
		mOs.write(mProtocol.createCytocentrifugationPackets(data));
	}

}