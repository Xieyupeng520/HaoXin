package com.hp.android.haoxin.workview;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.hp.android.haoxin.R;
import com.hp.android.haoxin.command.CommandBridge;
import com.hp.android.haoxin.request.iRequest;
import com.hp.android.haoxin.widgets.CustomDialog;

/**
 * 染色
 */
public class WorkDyeView extends WorkCleanView implements iRequest {

	public WorkDyeView(Context context) {
		super(context);
	}

	@Override
	public int getTitleId() {
		return R.drawable.index_title;
	}
	
	@Override
	protected int getCancelMsgId() {
		return R.string.dialog_msg_cancel_ranse;
	}
	
	public void setListener(){
		mDiskListener = new WorkDiskListener();
		//CommandBridge.getInstance().appSetDyeListener(mDiskListener);
	}
	
	@Override
	protected void start() {
		CommandBridge.getInstance().linkRanseStart(getContext());
	}
	
	@Override
	protected void cancel() {
		CommandBridge.getInstance().linkDyeCancel();
	}
	
	@Override
	protected void finished() {
		dismissCancelDialog();
		ViewController controller = ViewController.getInstance();
		controller.curr2Success(R.string.succ_ranse, ViewController.VIEW_HOME);
		controller.getMenuFragment().setSelected(0);

		CommandBridge.getInstance().resetState(); //重置系统状态
	}

	/** 实现接口方法 */
	@Override
	public void onRequest() {
		final CustomDialog devFillDialog = CustomDialog.createHintDialog(getContext(),R.string.dialog_msg_dev_fill_request);
		devFillDialog.setHintMsg(R.string.dialog_msg_hint_liulutianchong);
		devFillDialog.setPositiveButton("继续", new OnClickListener() {
			@Override
			public void onClick(View view) {
				//(11)开始进行流路填充操作
				ViewController.getInstance().setIdBack2Home(true); //结束后返回主界面
				ViewController.getInstance().changeView(ViewController.VIEW_SYSTEM_FILL);
				devFillDialog.dismiss();
			}
		});
		devFillDialog.setNegativeListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				cancelListener();
				devFillDialog.dismiss();
			}
		});
		devFillDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialogInterface) {
				cancelListener();
				devFillDialog.dismiss();
			}
		});
	}
}
