package com.hp.android.haoxin.command;

import android.app.ProgressDialog;
import android.content.Context;

public interface CommandInterface {
	
	void usbOTGinit();
	void usbClose();
	void sendData();
	void receiveData();
	void showToast(String text);


	/**
	 * 显示异常对话框
	 * @param resId 显示资源id
	 * @param context 显示dialog需要的上下文
	 */
	void showExceptionDialog(final int resId, final Context context);

	/**
	 * 重置系统状态
	 */
	void resetState();
	/**
	 * 加载模块，程序启动时触发
	 * @param context 传入上下文（创建Dialog需要用Activity的上下文）
	 */
	void loadingStart(Context context);
	
	/**
	 * 染色模块，设置染色载玻片的数目变化时触发
	 * @param number 载玻片的数目，范围为1~12
	 */
	void dyeSetZaiBoPian(int number);

	/**
	 * 系统维护模块，模式、流量检测值发生改变时触发
	 * @param type  哪种检测模式： 0表示模式检测，1表示流量检测
	 * @param index 新的模式检测的值0~4（对应A-E）
	 * @param keyMode 0按下，1弹起
	 */
	void systemJianCeChange(byte type, byte key, byte keyMode, Context context);

	/**
	 * 系统维护模块，模式、流量检测结束
	 */
	void systemJianCeFinish();
	/**
	 * 流路检测模块，当前的步数
	 * @param step 当前步数(从1开始算步数，0为取消)
	 */
	void liuluNext(int step);
	/**
	 * 流路检测模块，流路检测取消步凑变时触发
	 * @param step 取消的步凑1~5
	 */
	void liuluCancel(int step);

	/**
	 * 称重检测模块，当前的步数
	 * @param step 当前步数(从1开始算步数，0为取消)
	 */
	void weighNext(int step);
	
	/**
	 * 称重校验模块，称重校验取消步凑变时触发
	 * @param step 取消的步凑1~4
	 */
	void chengzhongCancel(int step);
	
	
	
	/**
	 * 参数设置模块，当参数设置发生变化时触发
	 * @param ranse 染色的厚度，范围：1-9
	 * @param guding 固定启动终止，范围：0-2，分别表示关闭、普通、高效
	 * @param jiejing 结晶紫调整， 范围：0-2，分别表示高、中、低
	 * @param dianjiu 碘酒调整，范围：0-2，分别表示高、中、低
	 * @param chengzhong 称重启动终止，值：0,1，分别表示关闭、开启
	 */
	void siteDates(int ranse, int guding, int jiejing, int dianjiu, int chengzhong);

	/**
	 * 参数设置模块，当参数设置发生变化时触发
	 * @param glassCount 玻片数选择（1~24）
	 * @param dyeingThickness 染色厚度选择(0x00~0x08)
	 * @param alcoholFix 酒精固定选择（00：关闭 01：普通 02：高效）
	 * @param crystalViolet 结晶紫选择（00：高 01：中 02：低）
	 * @param iodine 碘酒选择（00：关闭 01：普通 02：高效）
	 * @param weigh 称重使能选择（00：关闭 01：启动）
	 */
	void siteChanged(int glassCount, int dyeingThickness, int alcoholFix, int crystalViolet, int iodine, int weigh);
	
	/**
	 * 通知硬件开始染色，染色开始时触发
	 */
	void dyeStart(Context context);

	
	
	/**
	 * 通知硬件开始清洗，开始清洗时触发
	 */
	void cleanStart(Context context);
	
	
	
	/**
	 * 通知硬件开始填充，开始填充时触发
	 */
	void fillStart(Context context);
	
	
	/**
	 * 通知硬件取消染色，取消染色时触发
	 */
	void dyeCancel();
	
	
	/**
	 * 通知硬件取消清洗，取消清洗时触发
	 */
	void cleanCancel();
	
	
	/**
	 * 通知硬件取消填充，取消填充时触发
	 */
	void fillCancel();

	/**
	 * 离心开始
	 */
	void centrifugalStart(Context context);

	/**
	 * 取消离心
	 */
	void centrifugalCancel();
}
