package com.hp.android.haoxin.workview;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.Context;
import android.os.Handler;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hp.android.haoxin.R;
import com.hp.android.haoxin.utils.Constant;
import com.hp.android.haoxin.utils.Tool;
import com.hp.android.haoxin.widgets.CustomDialog;

public abstract class WorkBaseView extends FrameLayout{

	private static final String[] weeks = {"日","一","二","三","四","五","六"};
	
	protected View mContainer;
	protected int mContainerId;
	
	protected ImageView mTitleImg;
	private TextView mTimeText;
	private TextView mDayText;
	private TextView mDateText;
	private TextView mWeekText;
	private int mOldHour = -1;
	protected ViewChange mViewChange;
	
	public WorkBaseView(Context context) {
		super(context);
		initView();
	}
	public void initView(){
		addView(LayoutInflater.from(getContext()).inflate(R.layout.work_view_base, null));
		
		initContainerId();
		if(mContainerId != 0)mContainer = LayoutInflater.from(getContext()).inflate(mContainerId, null);
		((FrameLayout) findViewById(R.id.work_base_container_frame)).addView(mContainer);
		
		mTitleImg = (ImageView) findViewById(R.id.main_head_title_img);
		mTimeText = Tool.setTextType(this, R.id.main_head_time_txt, Constant.FONT_TYPE_HELVE);
		mDayText = Tool.setTextType(this, R.id.main_head_day_txt, Constant.FONT_TYPE_FANGZ);
		mDateText = Tool.setTextType(this, R.id.main_head_date_txt, Constant.FONT_TYPE_FANGZ);
		mWeekText = Tool.setTextType(this, R.id.main_head_week_txt, Constant.FONT_TYPE_FANGZ);
		
		mTitleImg.setImageResource(getTitleId());
		
		initTime();
		init();
	}
	
	public abstract void initContainerId();

	public abstract int getTitleId();

	public void init(){};
	
	
	public void initTime(){
		final Handler updateTimeHandler = new Handler();
		new Handler().post(new Runnable() {
			public void run() {
				Calendar calender = Calendar.getInstance();
				SimpleDateFormat df = new SimpleDateFormat("HH:mm");
				//df.applyPattern("HH:MM");
				//String timeStr = (String)DateUtils.formatDateTime(mContext, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME);
				mTimeText.setText(df.format(calender.getTime()));
				
				int hour = calender.get(Calendar.HOUR_OF_DAY);
				if(mOldHour != hour){
					String smPmStr = DateUtils.getAMPMString(Calendar.getInstance().get(Calendar.AM_PM));
					String pm = smPmStr.toLowerCase();
					mDayText.setText(pm.equals("pm")?R.string.pm:R.string.am);
					//String dateStr = (String)DateUtils.formatDateTime(mContext, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_DATE);
					df.applyPattern("MM月dd日");
					mDateText.setText(df.format(calender.getTime()));
					
					int week = calender.get(Calendar.DAY_OF_WEEK);
					mWeekText.setText(getContext().getString(R.string.week, weeks[week-1]));
					mOldHour = hour;
				}
				updateTimeHandler.postDelayed(this, 5000);
			}
		});
	}
	
	public boolean isSlidMenuCanScroll(){
		return ViewController.getInstance().getWorkFragment().isSlidMenuCanScroll(); 
	}
	
	public void setSlidingMenuSCroll(boolean isb){
		ViewController.getInstance().getWorkFragment().setSlidingMenuScroll(isb);
	}
	
	public void setOnlySlidingMenuSCroll(boolean isb){
		ViewController.getInstance().getWorkFragment().setOnlySlidingMenuScroll(isb);
	}
	
	public boolean isSlidMenuViewShow(){
		return ViewController.getInstance().getWorkFragment().isSlidMenuViewShow();
	}
	
	public void setSlidMenuViewShow(){
		ViewController.getInstance().getWorkFragment().setSlidingMenuShow();
	}
	
	public interface ViewChange{
		public void willHide(HadChange hadChange);
	}
	
	public interface HadChange{
		public void hadHide();
	}

	private CustomDialog cancelDialog;
	/**
	 *
	 * @return
	 */
	protected CustomDialog getCancelDialog() {
		cancelDialog = new CustomDialog(getContext());
		return cancelDialog;
	}

	/**
	 * 取消对话框消失（在有进度条显示的操作（清洗，清洗下的填充，填充流路，染色等）下，如果按下进度条右侧“X”键，弹出提示对话框，如果用户既不点击取消键，也不点击确认键，直至整个操作结束，软件返回到上一级界面，此时提示对话框依然存在；按照使用来说，操作结束，提示对话框消失。）
	 */
	protected void dismissCancelDialog() {
		if (cancelDialog != null) {
			cancelDialog.dismiss();
		}
	}
}
