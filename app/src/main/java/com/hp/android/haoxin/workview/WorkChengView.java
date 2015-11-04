package com.hp.android.haoxin.workview;

import android.content.Context;

import com.hp.android.haoxin.R;
import com.hp.android.haoxin.command.CommandBridge;

public class WorkChengView extends WorkLiuView{

	public WorkChengView(Context context) {
		super(context);
	}

	@Override
	public int getTitleId() {
		return R.drawable.weigh_title;
	}

	@Override
	public int getCancelMsgId() {
		return R.string.dialog_msg_cancel_chengzhong;
	}

	@Override
	public int getStepInfosId() {
		return R.array.chenzhong_steps;
	}

	@Override
	public int getSuccessInfoId() {
		return R.string.succ_chengzhong;
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		if(isSlidMenuViewShow()){
			setSlidMenuViewShow();
		}
		if(isSlidMenuCanScroll()){
			setSlidingMenuSCroll(false);
		}
	}

	/**
	 * 发送数据到下位机
	 * @param step 当前步数（1开始，0为取消）
	 */
	@Override
	public void sendDataToDevice(int step) {
		CommandBridge.getInstance().linkChengzhongCheck(step);
	}

	public void cancel(int step){
		CommandBridge.getInstance().linkChengzhongCancel(step);
	}

}
