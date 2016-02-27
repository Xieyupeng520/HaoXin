package com.hp.android.haoxin.workview;


import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.hp.android.haoxin.R;
import com.hp.android.haoxin.command.CommandBridge;
import com.hp.android.haoxin.global.Global;
import com.hp.android.haoxin.utils.Constant;
import com.hp.android.haoxin.utils.Tool;

public class WorkHomeView extends WorkBaseView{

	long mLastTime;
	int mCounter;
	boolean isOut;
	private float mLastMotionX;
	private float mLastMotionY;
	View mRoteView;
	TextView mBoPianText;
	//private int mRoteCenX;
	//private int mRoteCenY;
	//private float mTempLastX;
	private float mTempLastY;
	private View mBtnView;
	
	private TextView mRanSeText;
	private TextView mJieJingText;
	private TextView mDianJiuText;
	private TextView mGuDingText;
	
	public WorkHomeView(Context context) {
		super(context);
	}
	
	@Override
	public void initContainerId() {
		mContainerId = R.layout.work_view_home;
	}
	
	@Override
	public int getTitleId() {
		return R.drawable.index_title;
	}
	
	@Override
	public void init() {
		//initTitle(R.drawable.index_title);
		Tool.setTextType(this, R.id.tv_home_slide_title, Constant.FONT_TYPE_FANGZ);
		
		Tool.setTextType(this, R.id.tv_home_info_title, Constant.FONT_TYPE_FANGZ);
		Tool.setTextType(this, R.id.tv_home_info_ranse_title, Constant.FONT_TYPE_FANGZ);
		Tool.setTextType(this, R.id.tv_home_info_jiejing_title, Constant.FONT_TYPE_FANGZ);
		Tool.setTextType(this, R.id.tv_home_info_dianjiu_title, Constant.FONT_TYPE_FANGZ);
		Tool.setTextType(this, R.id.tv_home_info_guding_title, Constant.FONT_TYPE_FANGZ);
		
		mRanSeText = Tool.setTextType(this, R.id.tv_home_info_ranse_num, Constant.FONT_TYPE_HELVE);
		mJieJingText = Tool.setTextType(this, R.id.tv_home_info_jiejing_num, Constant.FONT_TYPE_FANGZ);
		mDianJiuText = Tool.setTextType(this, R.id.tv_home_info_dianjiu_num, Constant.FONT_TYPE_FANGZ);
		mGuDingText = Tool.setTextType(this, R.id.tv_home_info_guding_num, Constant.FONT_TYPE_FANGZ);
		
		mBoPianText = Tool.setTextType(this, R.id.tv_home_slide_number, Constant.FONT_TYPE_HELVE);
		
		mBoPianText.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if(isOut) return;
				long currTime = System.currentTimeMillis();
				if(currTime - mLastTime > 800){
					mCounter = 0;
				}else{
					mCounter++;
				}
				if(mCounter == 4){
					Tool.showSystemUI(getContext(), true);
					isOut = true;
				}
				mLastTime = currTime;
			}
		});
		
		mBtnView = findViewById(R.id.btn_home_start);
		mBtnView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ViewController.getInstance().changeView(ViewController.VIEW_RANSE);
			}
		});

		mRoteView = findViewById(R.id.rote_home_v);
		mRoteView.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View view, MotionEvent event) {
				int action = event.getAction();
				float x = event.getRawX();
				float y = event.getRawY();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					mLastMotionX = x;
					mLastMotionY = y;
					//mTempLastX = x;
					mTempLastY = y;
					
					setOnlySlidingMenuSCroll(false);
					
					break;
				case MotionEvent.ACTION_MOVE:
					int scale = 1;
					//确定旋转手势
					int[] pos = new int[2];
					mBtnView.getLocationOnScreen(pos);
					pos[0] += mBtnView.getWidth()/2;
					
					if(x > pos[0]){
						if(y > mTempLastY){
							scale = 1;
						}else if(y < mTempLastY){
							scale = -1;
						}
					}else{
						if(y < mTempLastY){
							scale = 1;
						}else if(y > mTempLastY){
							scale = -1;
						}
					}
					//mTempLastX = x;
					mTempLastY = y;
					
					//旋转条件，旋转
					float currRote = mRoteView.getRotation();
					float tagB = 3;
					if(currRote % 30 == 0){
						tagB = 45;
					}
					float dx = x - mLastMotionX;
					float dy = y - mLastMotionY;
					if((Math.abs(dx) > tagB || Math.abs(dy) > tagB)){
						//if(y < mLastMotionY)scale = -1;
							
						mRoteView.setRotation(currRote+5*scale);
						mLastMotionX = x;
						mLastMotionY = y;
						
						currRote = mRoteView.getRotation();
						if(currRote % 30 == 0){
							Global.mZaiBoPianNum += scale;
							if(Global.mZaiBoPianNum > Global.ZAIBOPIAN_NUM_MAX)Global.mZaiBoPianNum = Global.ZAIBOPIAN_NUM_MIN;
							else if(Global.mZaiBoPianNum < Global.ZAIBOPIAN_NUM_MIN)Global.mZaiBoPianNum = Global.ZAIBOPIAN_NUM_MAX;
							updateZaibopianNum();
						}
					}
					
					break;
				case MotionEvent.ACTION_UP:
					setOnlySlidingMenuSCroll(true);
					Global.setDataByName(getContext(), Global.DATA_ZAIBOPIAN_NAME, Global.mZaiBoPianNum);
					CommandBridge.getInstance().linkSetZaiBoPian(Global.mZaiBoPianNum);
					break;
				default:
					break;
				}
				
				return true;
			}
		});
	}
	
	public void updateZaibopianNum(){
		mBoPianText.setText(String.format("%02d", Global.mZaiBoPianNum));
	}
	
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		if(!isSlidMenuCanScroll()){
			setSlidingMenuSCroll(true);
		}
		
		updateZaibopianNum();
		mRanSeText.setText(getContext().getResources().getStringArray(R.array.site_btn_ranses)[Global.mRanSeHouDu]);
		mJieJingText.setText(getContext().getResources().getStringArray(R.array.site_btn_jiejings)[Global.mJieJingZiLev]);
		mDianJiuText.setText(getContext().getResources().getStringArray(R.array.site_btn_dianjius)[Global.mDianJiuLev]);
		mGuDingText.setText(getContext().getResources().getStringArray(R.array.site_btn_gudnigs)[Global.mGuDingQDState]);
	}
}
