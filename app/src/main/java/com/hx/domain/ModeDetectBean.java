package com.hx.domain;
/**
 * 模式检测BEAN.
 * @author qp.wang
 * @since 2015-08-02
 *
 */
public class ModeDetectBean {
	/**
	 *  0：按键 A   
	 1：按键 B
	 2：按键 C
	 3：按键 D
	 4：按键 E
	 */
	private byte mData4 = 0;
	/**
	 *  0:按下按键    1:松开按键  
	 */
	private byte mData5 = 0;

	public ModeDetectBean(byte data4, byte data5){
		this.mData4 = data4;
		this.mData5 = data5;
	}
	
	public ModeDetectBean(){
	}

	public byte getData4() {
		return mData4;
	}
	public void setData4(byte data4) {
		this.mData4 = data4;
	}
	public byte getData5() {
		return mData5;
	}
	public void setData5(byte data5) {
		this.mData5 = data5;
	}
}
