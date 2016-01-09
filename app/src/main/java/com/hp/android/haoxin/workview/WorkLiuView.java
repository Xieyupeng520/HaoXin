package com.hp.android.haoxin.workview;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hp.android.haoxin.R;
import com.hp.android.haoxin.command.CommandBridge;
import com.hp.android.haoxin.utils.Constant;
import com.hp.android.haoxin.utils.Tool;
import com.hp.android.haoxin.widgets.CustomDialog;

public class WorkLiuView extends WorkBaseView{

	protected ListView mListView;
	String[] mDatas; //具体步骤内容
	String[] STEP;
	private LiuChenAdapter mAdapter;
	private ProgressBar mProgressBar;
	private Button mNextButton;
	//	private Button mCancelButton;
	private Button mBackeButton;
	private TextView mProgressText;

	public WorkLiuView(Context context) {
		super(context);
	}

	@Override
	public void initContainerId() {
		mContainerId = R.layout.work_view_liu_cheng;
	}

	@Override
	public int getTitleId() {
		return R.drawable.detect_title;
	}

	public int getCancelMsgId(){
		return R.string.dialog_msg_cancel_liulu;
	}

	public int getStepInfosId(){
		return R.array.bliulu_steps;
	}
	public int getSuccessInfoId() {
		return R.string.succ_liulu;
	}


	@Override
	public void init() {
		mDatas = getContext().getResources().getStringArray(getStepInfosId());
		STEP = getContext().getResources().getStringArray(R.array.STEP);
		mProgressText = Tool.setTextType(this, R.id.tv_progress, Constant.FONT_TYPE_HELVE);
		mNextButton = (Button) Tool.setTextType(this, R.id.btn_cancel, Constant.FONT_TYPE_FANGZ);
		mBackeButton = (Button) Tool.setTextType(this, R.id.btn_back, Constant.FONT_TYPE_FANGZ);
		mProgressBar = (ProgressBar) findViewById(R.id.pb_liu_cheng);


		mBackeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				final CustomDialog dialog = getCancelDialog();
				dialog.setMessage(getCancelMsgId());
				dialog.setPositiveListener(new OnClickListener() {
					public void onClick(View arg0) {
						dismissCancelDialog();
						mNextButton.setEnabled(true);
						ViewController.getInstance().back2SystemHome();
						sendDataToDevice(0);
					}
				});
				dialog.setNegativeListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						dismissCancelDialog();
					}
				});

			}
		});

		mAdapter = new LiuChenAdapter();
		mListView = (ListView) findViewById(R.id.list_liu_chen);
		mListView.setAdapter(mAdapter);

		mNextButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mNextButton.setEnabled(false);
				int curStep = mAdapter.nextSetp();
				sendDataToDevice(curStep);
//				updateUI();
			}
		});
	}

	/**
	 * 发送数据到下位机
	 * @param step 当前步数（1开始，0为取消）
	 */
	public void sendDataToDevice(int step) {
		CommandBridge.getInstance().linkLiuluCheck(step);
	}

	public void cancel(int step){
		CommandBridge.getInstance().linkLiuluCancel(step);
	}

//	/**
//	 * 跟新进度信息(当前步数百分比）
//	 */
//	public void updateUI(){
//		int progress = mAdapter.getPoint();
//		mProgressBar.setProgress(progress);
//		mProgressText.setText(progress+"%");
//	}

	/**
	 * 更新当前步骤的当前进度
	 * @param progress
	 */
	public void updateProgress(int progress) {
		mProgressBar.setProgress(progress);
		mProgressText.setText(progress+"%");
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

		mAdapter = new LiuChenAdapter();
		mListView.setAdapter(mAdapter);
		updateProgress(0); //初始化恢复进度为0
		mBackeButton.setEnabled(true);
	}

	/**
	 * 完成每一步都会回调该函数
	 */
	public void finish(){
		mNextButton.setEnabled(true);
		if (mAdapter.getStep() >= mAdapter.getCount()) { //最后一步
			ViewController.getInstance().curr2Success(getSuccessInfoId(), ViewController.VIEW_SYSTEM);
		}
	}

	class LiuChenAdapter extends BaseAdapter{

		private int mStep;

		public void setStep(int step){
			mStep = step;
			notifyDataSetInvalidated();
			//notifyDataSetChanged();
		}

		public int nextSetp(){
			if(mStep < getCount()){
				mStep++;
				notifyDataSetChanged();
			}
			return mStep;
		}

		public void lastStep(){
			if(mStep > 0){
				mStep--;
				notifyDataSetChanged();
			}
		}

		public int getStep(){
			return mStep;
		}

		public int getPoint(){
			return (int)((mStep/(float)getCount())*100);
		}

		@Override
		public int getCount() {
			return mDatas.length;
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int options, View view, ViewGroup arg2) {
			TextView step = null;
			TextView msg = null;
			if(view == null){
				view = LayoutInflater.from(getContext()).inflate(R.layout.item_liu_cheng, arg2, false);
				step = Tool.setTextType(view, R.id.tv_list_title, Constant.FONT_TYPE_FANGZ);
				msg = Tool.setTextType(view, R.id.tv_list_msg, Constant.FONT_TYPE_FANGZ);
			}else{
				step = (TextView)view.findViewById(R.id.tv_list_title);
				msg = (TextView)view.findViewById(R.id.tv_list_msg);
			}


			step.setText(STEP[options]);
			msg.setText(mDatas[options]);

			boolean isSelected = options < mStep ? true : false;
			step.setSelected(isSelected);
			msg.setSelected(isSelected);
			view.findViewById(R.id.v_list_sign).setSelected(isSelected);

			return view;
		}
	}
}
