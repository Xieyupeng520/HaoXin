package com.hx.protocol;
/**
 * 交互协议枚举
 * @author qp.wang
 * @since 2015-08-01
 */
public enum ProtocolType {

	PK_HEAD_1			((byte)0x55),/*包协议头1*/
	PK_HEAD_2			((byte)0xAA),/*包协议头2*/

	SHAKEHANDS			((byte)0XAA),/*握手协议*/

	HW_VERSION			((byte)0x01),/*硬件版本*/
	SW_VERSION			((byte)0x02),/*软件版本*/

	UI_KEY				((byte)0x03),/*UI按键触发消息*/

	OP_DYE				((byte)0x04),/*染色操作*/
	OP_CLEAR			((byte)0x05),/*清洗操作*/
	OP_FILL				((byte)0x06),/*填充*/
	OP_MODE_DETECT		((byte)0x07),/*模式检测*/
	OP_FLOW_DETECT		((byte)0x08),/*流量检测*/
	OP_B_FLOW_DETECT	((byte)0x09),/*B流路检测*/
	OP_WEIGH_DETECT		((byte)0x0A),/*称重检测*/

	OP_CELL_CENTRIFUGAL	((byte)0x0B),/*细胞离心，保留*/
	OP_LARGE_WASH_PUMP	((byte)0x0C),/*大力清洗泵，保留*/
	SET_PUMP			((byte)0x0D),/*泵占比，保留*/

	RPT_DEV_STATUS		((byte)0x80),/*上报设备状态，250ms一次*/
	RPT_DEV_EXCEPTION	((byte)0x81),/*上报设备异常事件*/
	RPT_OP_RESPONSE		((byte)0x82),/*执行操作命令回应信息*/
	RPT_OP_PROGRESS		((byte)0x83);/*执行命令进度信息*/

	/**
	 * 枚举值.
	 */
	private byte mCode;

	private ProtocolType(byte i) {
		this.mCode = i;
	}

	public byte code() {
		return this.mCode;
	}
}