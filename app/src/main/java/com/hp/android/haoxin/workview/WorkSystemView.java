package com.hp.android.haoxin.workview;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.hp.android.haoxin.R;

import java.util.ArrayList;

public class WorkSystemView extends WorkBaseView implements OnClickListener, OnCheckedChangeListener, RBListener.OnTouchOtherUneableListener {

	/*private RadioGroup mMoshiGroup;
	private RadioGroup mLiuliangGroup;*/
	
	public WorkSystemView(Context context) {
		super(context);
	}

	@Override
	public void initContainerId() {
		mContainerId = R.layout.work_view_system;
	}

	@Override
	public int getTitleId() {
		return R.drawable.maintain_title;
	}
	private ArrayList<Integer> allViewsId = new ArrayList<Integer>(){
		{
			add(new Integer(R.id.btn_system_bliulu));
			add(new Integer(R.id.btn_system_chenzhong));
			add(new Integer(R.id.btn_system_tianchong));

			add(new Integer(R.id.rb_moshi_1));
			add(new Integer(R.id.rb_moshi_2));
			add(new Integer(R.id.rb_moshi_3));
			add(new Integer(R.id.rb_moshi_4));
			add(new Integer(R.id.rb_moshi_5));

			add(new Integer(R.id.rb_liuliang_1));
			add(new Integer(R.id.rb_liuliang_2));
			add(new Integer(R.id.rb_liuliang_3));
			add(new Integer(R.id.rb_liuliang_4));
			add(new Integer(R.id.rb_liuliang_5));
		}
	};
	@Override
	public void init() {
		findViewById(R.id.btn_system_bliulu).setOnClickListener(this);
		findViewById(R.id.btn_system_chenzhong).setOnClickListener(this);
		findViewById(R.id.btn_system_tianchong).setOnClickListener(this);
		findViewById(R.id.btn_system_centrifugal).setOnClickListener(this);

		RBListener listener = new RBListener(this);
		listener.setContext(getContext());
		findViewById(R.id.rb_moshi_1).setOnTouchListener(listener);
		findViewById(R.id.rb_moshi_2).setOnTouchListener(listener);
		findViewById(R.id.rb_moshi_3).setOnTouchListener(listener);
		findViewById(R.id.rb_moshi_4).setOnTouchListener(listener);
		findViewById(R.id.rb_moshi_5).setOnTouchListener(listener);
		
		findViewById(R.id.rb_liuliang_1).setOnTouchListener(listener);
		findViewById(R.id.rb_liuliang_2).setOnTouchListener(listener);
		findViewById(R.id.rb_liuliang_3).setOnTouchListener(listener);
		findViewById(R.id.rb_liuliang_4).setOnTouchListener(listener);
		findViewById(R.id.rb_liuliang_5).setOnTouchListener(listener);

		/*mMoshiGroup = (RadioGroup) findViewById(R.id.rg_system_moshi);
		mLiuliangGroup = (RadioGroup) findViewById(R.id.rg_system_liuliang);
		
		mMoshiGroup.setOnCheckedChangeListener(this);
		mLiuliangGroup.setOnCheckedChangeListener(this);*/
	}

	@Override
	public void onClick(View view) {
		int viewId;
		switch(view.getId()){
		case R.id.btn_system_bliulu:
			viewId = ViewController.VIEW_SYSTEM_LIULU;
			break;
		case R.id.btn_system_chenzhong:
			viewId = ViewController.VIEW_SYSTEM_CHENGZHONG;
			break;
		case R.id.btn_system_tianchong:
			viewId = ViewController.VIEW_SYSTEM_FILL;
			break;
		case R.id.btn_system_centrifugal:
			viewId = ViewController.VIEW_CENTRIFUGAL;
			break;
		default:
			viewId = ViewController.VIEW_SYSTEM_LIULU;
			break;
		}
		ViewController.getInstance().changeView(viewId);
	}
	
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		if(!isSlidMenuCanScroll()){
			setSlidingMenuSCroll(true);
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		/*int index = group.indexOfChild(group.findViewById(checkedId));

		int type = 0;
		if(group.getId() == R.id.rg_system_moshi){
			if(-1 != mLiuliangGroup.getCheckedRadioButtonId()){
				mLiuliangGroup.setOnCheckedChangeListener(null);
				mLiuliangGroup.clearCheck();
				mLiuliangGroup.setOnCheckedChangeListener(this);
			}
		}else if(group.getId() == R.id.rg_system_liuliang){
			if(-1 != mMoshiGroup.getCheckedRadioButtonId()){
				mMoshiGroup.setOnCheckedChangeListener(null);
				mMoshiGroup.clearCheck();
				mMoshiGroup.setOnCheckedChangeListener(this);
			}
			type = 1;
		}
		CommandBridge.getInstance().linkJianCeChange(type, index+1);*/
	}


	/**
	 * onTouch回调，一个按键被点击，其他按键不允许被点击
	 */
	@Override
	public void onTouchCallBack(int theTouchingViewId, boolean otherViewEnable) {
		for (Integer view : allViewsId) {
			if (view.intValue() == theTouchingViewId) {
				continue;
			}
			try {
				findViewById(view).setEnabled(otherViewEnable);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
