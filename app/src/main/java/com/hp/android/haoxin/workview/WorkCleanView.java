package com.hp.android.haoxin.workview;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Matrix;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hp.android.haoxin.R;
import com.hp.android.haoxin.command.CommandBridge;
import com.hp.android.haoxin.global.Global;
import com.hp.android.haoxin.global.GlobalState;
import com.hp.android.haoxin.test.TestCmd;
import com.hp.android.haoxin.utils.Constant;
import com.hp.android.haoxin.utils.Tool;
import com.hp.android.haoxin.widgets.CustomDialog;
import com.hp.android.haoxin.widgets.DiskCircleView;
import com.hp.android.haoxin.widgets.SpoutView;

public class WorkCleanView extends WorkBaseView{

	public WorkCleanView(Context context) {
		super(context);
	}

	ViewController controller = ViewController.getInstance();
	
	private ImageView mDiskView;
	private DiskCircleView mDiskMark;
	private ProgressBar mProgress;
	
	private TextView mTitleText;
	private TextView mDesText;
	private TextView mProgressText;
	
	private Button mCancelButton;
	
	protected WorkDiskListener mDiskListener;
	
	private int mCenterX;
	private int mCenterY;
	
	private FrameLayout mContainerFrame;
	private SpoutView mSpoutDF;
	private SpoutView mSpoutA;
	private SpoutView mSpoutB;
	private SpoutView mSpoutC;
	private SpoutView mSpoutE;
	private SpoutView mSpoutDR;
	
	@Override
	public void initContainerId() {
		mContainerId = R.layout.work_view_clean_fill;
	}

	/*public void initData(){
		getTitleId(R.drawable.clean_title);
	}*/
	
	@Override
	public int getTitleId() {
		return R.drawable.clean_title;
	}
	
	/*public void setListener(WorkDiskListener listener){
		mDiskListener = listener;
	}*/

	@Override
	public void init() {
		mTitleText = Tool.setTextType(this, R.id.tv_clean_fill_title, Constant.FONT_TYPE_FANGZ);
		mDesText = Tool.setTextType(this, R.id.tv_clean_des, Constant.FONT_TYPE_FANGZ);
		mProgressText = Tool.setTextType(this, R.id.tv_clean_dial, Constant.FONT_TYPE_HELVE);
		
		mContainerFrame = (FrameLayout) findViewById(R.id.img_disk_container);
		
		mProgress = (ProgressBar) findViewById(R.id.progress_clean);
		mDiskView = (ImageView) findViewById(R.id.img_disk);
		mDiskMark = (DiskCircleView) findViewById(R.id.dcv_disk_mark);
		//mDiskSpout = findViewById(R.id.img_disk_spout);
		mCancelButton = (Button) this.findViewById(R.id.clean_btn_cancel);
		mCancelButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				final CustomDialog dialog = getCancelDialog();
				dialog.setMessage(getCancelMsgId());
				dialog.setPositiveListener(new OnClickListener() {
					public void onClick(View arg0) {
						cancel();
						dismissCancelDialog();
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
		
		setListener();
		
		mSpoutDF = new SpoutView(getContext(), 0);
		mSpoutA = new SpoutView(getContext(), 1);
		mSpoutB = new SpoutView(getContext(), 2);
		mSpoutC = new SpoutView(getContext(), 3);
		mSpoutE = new SpoutView(getContext(), 4);
		mSpoutDR = new SpoutView(getContext(), 5);
		
		mDiskView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@SuppressWarnings("deprecation")
			public void onGlobalLayout() {
				mCenterX = mDiskView.getLeft()+mDiskView.getWidth()/2;
				mCenterY = mDiskView.getTop()+mDiskView.getHeight()/2;
				
				float h = mDiskView.getDrawable().getBounds().height();
				Matrix m = mDiskView.getImageMatrix();
				float[] values = new float[10];
				m.getValues(values);
				
				float scale = values[4];
				
				scale = h/331.f*scale;
				float tmp = Math.min(mDiskView.getWidth(), mDiskView.getHeight());
				scale = tmp/331.f;
				mDiskMark.initLayout(mCenterX, mCenterY, mDiskView.getHeight());
//				mDiskView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
				mDiskView.getViewTreeObserver().removeGlobalOnLayoutListener(this);

				String ids[] = {"spout/spout_df_1.png","spout/spout_df_2.png"};
				mSpoutDF.initView(mCenterX,mCenterY,mDiskView.getHeight()/2,scale,"spout/sp_df.png", ids);
				mContainerFrame.addView(mSpoutDF);
				
				ids = new String[]{"spout/spout_a_1.png","spout/spout_a_2.png"};
				mSpoutA.initView(mCenterX,mCenterY,mDiskView.getHeight()/2,scale,"spout/sp_a.png", ids);
				mContainerFrame.addView(mSpoutA);
				
				ids = new String[]{"spout/spout_b_1.png","spout/spout_b_2.png"};
				mSpoutB.initView(mCenterX,mCenterY,mDiskView.getHeight()/2,scale,"spout/sp_b.png", ids);
				mContainerFrame.addView(mSpoutB);
				
				ids = new String[]{"spout/spout_c_1.png","spout/spout_c_2.png"};
				mSpoutC.initView(mCenterX,mCenterY,mDiskView.getHeight()/2,scale,"spout/sp_c.png", ids);
				mContainerFrame.addView(mSpoutC);
				
				ids = new String[]{"spout/spout_e_1.png","spout/spout_e_2.png"};
				mSpoutE.initView(mCenterX,mCenterY,mDiskView.getHeight()/2,scale,"spout/sp_e.png", ids);
				mContainerFrame.addView(mSpoutE);
				
				ids = new String[]{"spout/spout_dr_1.png","spout/spout_dr_2.png"};
				mSpoutDR.initView(mCenterX,mCenterY,mDiskView.getHeight()/2,scale,"spout/sp_dr1.png", ids);
				mContainerFrame.addView(mSpoutDR);
			}
		});
	}
	
	public void setListener(){
		mDiskListener = new WorkDiskListener();
	}
	
	public WorkDiskListener getListener(){
		return mDiskListener;
	}
	
	protected int getCancelMsgId(){
		return R.string.dialog_msg_cancel_clean;
	}

	/**
	 * 点击取消，返回HOME界面
	 */
	protected void cancelListener(){
		controller.changeView(ViewController.VIEW_HOME);
		controller.getMenuFragment().setSelected(0);
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
		mCancelButton.setEnabled(true);
		start();
	}
	
	protected void start(){
		CommandBridge.getInstance().linkCleanStart(getContext());
	}
	
	protected void cancel(){
		CommandBridge.getInstance().linkCleanCancel();
	}
	
	protected void finished(){
		dismissCancelDialog();
		//填充对话框
		final CustomDialog dialog = new CustomDialog(getContext());
		dialog.setMessage(R.string.dialog_msg_over_clean);
		dialog.setPositiveListener(new OnClickListener() {
			public void onClick(View arg0) {
				//因在CommandBridge中的workFinish方法中调用了resetState()，导致系统状态变为了HOME，这里从清洗跳转至填充界面，需要设置回状态为CLEAN，否则发送的报文不对
				Global.setState(GlobalState.CLEAN);
				dialog.dismiss();
				ViewController controller = ViewController.getInstance();
				controller.setIdBack2Home(true);
				controller.changeView(ViewController.VIEW_SYSTEM_FILL);
			}
		});
		dialog.setNegativeListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//因在CommandBridge中的workFinish方法中调用了resetState()，导致系统状态变为了HOME，这里从清洗跳转至填充界面，需要设置回状态为CLEAN，否则发送的报文不对
				Global.setState(GlobalState.CLEAN);
				dialog.dismiss();
				cancelFill();
			}
		});
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialogInterface) {
				//因在CommandBridge中的workFinish方法中调用了resetState()，导致系统状态变为了HOME，这里从清洗跳转至填充界面，需要设置回状态为CLEAN，否则发送的报文不对
				Global.setState(GlobalState.CLEAN);
				dialog.dismiss();
				cancelFill();
			}
		});
		dialog.setCanceledOnTouchOutside(false);
	}

	/**
	 * 取消填充操作
	 */
	private void cancelFill() {
//		Global.getSystemStateBean().setStatDevFilled(getContext().getResources().getInteger(R.integer.stat_no)); //设置状态填充未完成
		CommandBridge.getInstance().linkFillCancel(); //发送取消的命令 15/10/14
		cancelListener();
	}
	protected void showToast(String text){
		Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
	}
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		mDiskListener.stopAll();
	}
	
	public class WorkDiskListener{
		
		//private ArrayList<View> mWorkSpoutArray;
		//private DiskCircleView mColorMarkView;
		private boolean isDiskRotate = false;
		
		/*public WorkDiskListener(){
			mWorkSpoutArray = new ArrayList<View>();
		}*/

		public void setDiskMarkColor(int color){
			mDiskMark.setColor(color);
		}
		
		public void setDiskMarkColor (int color,int duration,float fromAlpha,float toAlpha){
			mDiskMark.setColor(color);
			if(duration != 0){
				mDiskMark.startAnimation(duration, fromAlpha, toAlpha);
			}
		}
		
		public void setProgress(int progress){
			Log.d("WorkCleanView","setProgress = " + progress);
			mProgress.setProgress(progress);
			mProgressText.setText(progress+"%");
			if(progress == 100){
				mCancelButton.setEnabled(false);
			}
		}
		
		public void setProgressText(String title,String des){
			mTitleText.setText(title);
			mDesText.setText(des);
		}

		/**
		 * 圆盘（染色、清洗、填充）模块，开始盘子转动
		 * @param isoffCenter 是否为离心转动。true为空转，false为离心
		 */
		public void startRotate(boolean isoffCenter){
			if(!isDiskRotate){
				int durlay = isoffCenter ? 1500 : 3000;
				RotateAnimation animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
				animation.setDuration(durlay);
				animation.setRepeatCount(Animation.INFINITE);
				animation.setInterpolator(new LinearInterpolator());
				animation.setFillAfter(true);
				mDiskView.startAnimation(animation);
				isDiskRotate = true;
			}else {
				int curdy = (int)mDiskView.getAnimation().getDuration();
				if(isoffCenter && curdy != 1500){
					mDiskView.getAnimation().setDuration(1500);
				}else if(!isoffCenter && curdy != 3000){
					mDiskView.getAnimation().setDuration(3000);
				}
			}
		}
		
		public void stopRotate(){
			isDiskRotate = false;
			mDiskView.clearAnimation();
		}
		
		public void startSpout(int index,boolean isClean){
			//public void startSpout(int color,int index,boolean isClean){
			//int id = getIdByIndex(index);
			startRotate(false);
			
//			ImageView spout = (ImageView) findViewById(id);
//			spout.setVisibility(View.VISIBLE);
			getSpoutById(index).showAnim(true);
			
			if(index > 0 && index < 6){
//				int srcid = getIdType(isClean, index);
//				if(srcid != -1)spout.setImageResource(srcid);
				getSpoutById(index).initAnimPathsById(index, isClean);
//				getSpoutById(index).changeColor(index, isClean);
			}

			//setSpoutAnimation(spout);
			//spout.startAnimation(AnimationUtils.loadAnimation(getContext(), getAnimIdByIndex(index)));
		}
		
		public void stopSpout(int index){
			getSpoutById(index).showAnim(false);
			//int id = getIdByIndex(index);
			//findViewById(id).setVisibility(View.GONE);
		}
		
		public void stopAll(){
			for(int i = 0; i < 6; i++){
				//findViewById(getIdByIndex(i)).setVisibility(View.GONE);
				getSpoutById(i).showAnim(false);
			}
			mDiskListener.stopRotate();
			mProgress.setProgress(0);
		}

		/**
		 * @param isFinished 是否是正常结束，true 正常；false 异常（比如取消）
		 */
		public void finish(boolean isFinished){
			if (isFinished) {
				finished();
			} else {
				cancelListener();
			}
		}
	}

	private SpoutView getSpoutById(int id){
		switch (id) {
		case SpoutView.DF:
			return mSpoutDF;
		case SpoutView.A:
			return mSpoutA;
		case SpoutView.B:
			return mSpoutB;
		case SpoutView.C:
			return mSpoutC;
		case SpoutView.E:
			return mSpoutE;
		case SpoutView.DR:
		default:
			return  mSpoutDR;
		}
	}
	
//	private int getIdByIndex(int index){
//		switch (index) {
//		case 0:
//			return R.id.img_spout_df;
//		case 1:
//			return R.id.img_spout_a;
//		case 2:
//			return R.id.img_spout_b;
//		case 3:
//			return R.id.img_spout_c;
//		case 4:
//			return R.id.img_spout_e;
//		case 5:
//			return R.id.img_spout_dr;
//		default:
//			return  R.id.img_spout_a;
//		}
//	}
	
	@SuppressWarnings("unused")
	private int getIdType(boolean isClean,int index){
		int id = -1;
		switch (index) {
		case 1:
			id = isClean ? R.anim.spout_aw:R.anim.spout_a;
			break;
		case 2:
			id = isClean ? R.anim.spout_bw:R.anim.spout_b;
			break;
		case 3:
			id = isClean ? R.anim.spout_cw:R.anim.spout_c;
			break;
		default:
			break;
		}
		return id;
	}
}
