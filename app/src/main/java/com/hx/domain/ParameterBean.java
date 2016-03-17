package com.hx.domain;

/**
 * 设置参数BEAN.
 * @author qp.wang
 * @since 2015-08-01
 *
 */
public class ParameterBean {
	//玻片数选择（1~24）
	private byte galssCount = 1;

	//染色厚度选择(0x00~0x08)
	private byte dyeingThickness = 0;

	//酒精固定选择（00：关闭 01：普通 02：高效） 
	private byte alcoholFix = 0;

	//结晶紫选择（00：高 01：中 02：低）
	private byte crystalViolet = 0;

	//碘酒选择（00：关闭 01：普通 02：高效）
	private byte iodine = 0;

	//称重使能选择（00：关闭 01：启动）
	private byte weigh = 0;

	//加热选择（00：关闭 01：启动）：上位机暂时不用**
	private byte heating = 0;

	//填充选择（00：填充   01：未填充）：上位机暂时不用 
	private byte filling = 0;

	//染色选择（00：抗酸 01：罗丹明 B  02：金胺 O）：上位机暂时不用
	private byte dyeing = 1; //荧光版本此位为1

	//NC（预留，待扩展）
	private byte reserve = 0;

	//NC（预留，待扩展）
	private byte reserve1 = 0;

	//NC（预留，待扩展）
	private byte reserve2 = 0;

	//NC（预留，待扩展）
	private byte reserve3 = 0;

	//NC（预留，待扩展）
	private byte reserve4 = 0;

	//NC（预留，待扩展）
	private byte reserve5 = 0;

	public ParameterBean(byte galssCount, byte dyeingThickness, byte alcoholFix,
						 byte crystalViolet, byte iodine, byte weigh) {
		//玻片数选择（1~24）
		this.galssCount = galssCount;

		//染色厚度选择(0x00~0x08)
		this.dyeingThickness = dyeingThickness;

		//酒精固定选择（00：关闭 01：普通 02：高效） 
		this.alcoholFix = alcoholFix;

		//结晶紫选择（00：高 01：中 02：低）
		this.crystalViolet = crystalViolet;

		//碘酒选择（00：关闭 01：普通 02：高效）
		this.iodine = iodine;

		//称重使能选择（00：关闭 01：启动）
		this.weigh = weigh;

	}

	public ParameterBean() {
	}

	public byte getGalssCount() {
		return galssCount;
	}

	public void setGalssCount(byte galssCount) {
		this.galssCount = galssCount;
	}

	public byte getDyeingThickness() {
		return dyeingThickness;
	}

	public void setDyeingThickness(byte dyeingThickness) {
		this.dyeingThickness = dyeingThickness;
	}

	public byte getAlcoholFix() {
		return alcoholFix;
	}

	public void setAlcoholFix(byte alcoholFix) {
		this.alcoholFix = alcoholFix;
	}

	public byte getCrystalViolet() {
		return crystalViolet;
	}

	public void setCrystalViolet(byte crystalViolet) {
		this.crystalViolet = crystalViolet;
	}

	public byte getIodine() {
		return iodine;
	}

	public void setIodine(byte iodine) {
		this.iodine = iodine;
	}

	public byte getWeigh() {
		return weigh;
	}

	public void setWeigh(byte weigh) {
		this.weigh = weigh;
	}

	public byte getHeating() {
		return heating;
	}

	public void setHeating(byte heating) {
		this.heating = heating;
	}

	public byte getFilling() {
		return filling;
	}

	public void setFilling(byte filling) {
		this.filling = filling;
	}

	public byte getDyeing() {
		return dyeing;
	}

	public void setDyeing(byte dyeing) {
		this.dyeing = dyeing;
	}

	public byte getReserve() {
		return reserve;
	}

	public void setReserve(byte reserve) {
		this.reserve = reserve;
	}

	public byte getReserve1() {
		return reserve1;
	}

	public void setReserve1(byte reserve1) {
		this.reserve1 = reserve1;
	}

	public byte getReserve2() {
		return reserve2;
	}

	public void setReserve2(byte reserve2) {
		this.reserve2 = reserve2;
	}

	public byte getReserve3() {
		return reserve3;
	}

	public void setReserve3(byte reserve3) {
		this.reserve3 = reserve3;
	}

	public byte getReserve4() {
		return reserve4;
	}

	public void setReserve4(byte reserve4) {
		this.reserve4 = reserve4;
	}

	public byte getReserve5() {
		return reserve5;
	}

	public void setReserve5(byte reserve5) {
		this.reserve5 = reserve5;
	}
}
