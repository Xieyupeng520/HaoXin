package com.hp.android.haoxin.workview;

import android.content.Context;
import android.util.Log;

import com.hp.android.haoxin.R;
import com.hp.android.haoxin.command.CommandBridge;
import com.hp.android.haoxin.global.Global;
import com.hp.android.haoxin.test.TestCmd;

/**
 * 填充
 */
public class WorkFillView extends WorkCleanView{

	public WorkFillView(Context context) {
		super(context);
	}

	@Override
	public int getTitleId() {
		return R.drawable.fil_titlel;
	}
	
	@Override
	protected int getCancelMsgId() {
		return R.string.dialog_msg_cancel_tianchong;
	}
	
	public void setListener(){
		mDiskListener = new WorkDiskListener();
		//CommandBridge.getInstance().appSetFillListener(mDiskListener);
	}
	
	@Override
	protected void cancelListener() {
		Log.d("WorkFillView","cancelListener - 是否回主页：" + controller.isBack2Home());
		ViewController controller = ViewController.getInstance();
		if(controller.isBack2Home()){
			controller.setIdBack2Home(false);
			controller.changeView(ViewController.VIEW_HOME);
			controller.getMenuFragment().setSelected(0);
		}else{
			controller.back2SystemHome();
		}
	}
	
	@Override
	protected void start() {
		CommandBridge.getInstance().linkFillStart(getContext());
	}
	
	@Override
	protected void cancel() {
		CommandBridge.getInstance().linkFillCancel();
	}
	
	@Override
	protected void finished() {
		dismissCancelDialog();
		ViewController controller = ViewController.getInstance();
//		Global.getSystemStateBean().setStatDevFilled(getContext().getResources().getInteger(R.integer.stat_yes)); //设置状态填充已完成
		if(controller.isBack2Home()){
			controller.setIdBack2Home(false);
			ViewController.getInstance().curr2Success(R.string.succ_fill, ViewController.VIEW_HOME);
		}else{
			ViewController.getInstance().curr2Success(R.string.succ_fill, ViewController.VIEW_SYSTEM);
		}

		CommandBridge.getInstance().resetState(); //重置系统状态
	}
}
