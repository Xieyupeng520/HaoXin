package com.hp.android.haoxin.workview;

import com.hp.android.haoxin.R;
import com.hp.android.haoxin.utils.Constant;
import com.hp.android.haoxin.utils.Tool;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.widget.TextView;

public class WorkSuccessView extends WorkBaseView{

	private TextView mMsgText;
	//private TextView mNextText;
	
	private int mMsgId;
	private int mBackViewId;
	
	public WorkSuccessView(Context context,int msgId,int backId) {
		super(context);
		mMsgId = msgId;
		mBackViewId = backId;
		init();
	}

	@Override
	public void initContainerId() {}

	@Override
	public int getTitleId() {
		return 0;
	}
	
	@Override
	public void initView() {
		addView(LayoutInflater.from(getContext()).inflate(R.layout.work_view_success, null));
		mMsgText = Tool.setTextType(this, R.id.tv_succ_msg, Constant.FONT_TYPE_FANGZ);
		Tool.setTextType(this, R.id.tv_succ_next, Constant.FONT_TYPE_FANGZ);
	}
	
	@Override
	public void init() {
		mMsgText.setText(mMsgId);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				ViewController.getInstance().success2Home(mBackViewId);
			}
		}, 3000);
	}
}
