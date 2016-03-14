package com.hp.android.haoxin.workview;

import com.hp.android.haoxin.HaoXinApplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public abstract class WorkChildView {

	protected int mContainerId = -1;
	protected WorkBaseView mWorkView;
	
	public WorkChildView(WorkBaseView workView){
		mWorkView = workView;
	}
	
	public abstract void initContainerId();
	
	public View getView(){
		initContainerId();
		if(mContainerId == -1)return null;
		Context context = HaoXinApplication.getAppContext();
		
		View view = LayoutInflater.from(context).inflate(mContainerId, null);
		init();
		
		return view;
	}
	
	public abstract void init();
}
