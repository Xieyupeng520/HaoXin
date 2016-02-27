package com.hp.android.haoxin;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


public class BaseActivity extends Activity{

	protected int mContainerResId;
	protected ViewGroup mContainerLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		initContainer();
	}
	
	protected void initContainer(){
		if(mContainerResId != 0){
			mContainerLayout = (ViewGroup) getLayoutInflater().inflate(mContainerResId, null);
			((FrameLayout) findViewById(R.id.frame_container_base)).addView(mContainerLayout);
		}
	}
	
	protected FrameLayout getBaseLayout(){
		return (FrameLayout) findViewById(R.id.frame_container_base);
	}
}
