package com.hp.android.haoxin;

import android.content.Intent;
import android.widget.TextView;

import com.hp.android.haoxin.command.CommandBridge;
import com.hp.android.haoxin.global.Global;
import com.hp.android.haoxin.utils.Constant;
import com.hp.android.haoxin.utils.Tool;

public class LoadingActivity extends BaseActivity{

	private TextView mProgressText;
	private TextView mDescriptionText;
	
	@Override
	protected void initContainer() {
		mContainerResId = R.layout.activity_loading;
		super.initContainer();
		
		Tool.setTextType(getBaseLayout(), R.id.loading_degree_txt, Constant.FONT_TYPE_HELVE);
		Tool.setTextType(getBaseLayout(), R.id.tv_loading_des, Constant.FONT_TYPE_FANGZ);
		
		mProgressText = (TextView) findViewById(R.id.loading_degree_txt);
		mDescriptionText = (TextView) findViewById(R.id.tv_loading_des);

		//初始化系统状态
		Global.initSystemState(HaoXinApplication.getAppContext());

		CommandBridge command = CommandBridge.getInstance();
		command.appSetLoadingLinstener(new LoadingListener());
		command.linkLoadingStart(this);


		// FIXME: 16/3/7 调试用，用于调出“返回”悬浮框，正常情况下屏蔽
		command.openBackButton(this);
	}
	
	public class LoadingListener{
		final int[] progresses = {12,56,75,95,100};
		final String[] descriptions = HaoXinApplication.getAppContext().getResources().getStringArray(R.array.loading_msgs);

		public void setProgress(int progress,String des){
			mProgressText.setText(progress+"%");
			mDescriptionText.setText(des);
			
			if(progress == 100){
				startActivity(new Intent(LoadingActivity.this, MainActivity.class));
				finish();
			}
		}
		
		public void updateLoadingStep(int id){
			setProgress(progresses[id], descriptions[id]);
		}
		
		public void initDatas(int zaibopian,int ranse, int guding, int jiejing,
				int dianjiu,int chengzhong){
			Global.mZaiBoPianNum = zaibopian;
			Global.mRanSeHouDu = ranse;
			Global.mGuDingQDState = guding;
			Global.mJieJingZiLev = jiejing;
			Global.mDianJiuLev = dianjiu;
			Global.mChengZhong = chengzhong;
			Global.saveDatas(LoadingActivity.this);
		}
	}

}
