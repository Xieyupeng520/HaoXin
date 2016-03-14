package com.hp.android.haoxin.workview;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hp.android.haoxin.R;
import com.hp.android.haoxin.global.Global;
import com.hp.android.haoxin.utils.Constant;
import com.hp.android.haoxin.utils.Tool;
import com.hp.android.haoxin.widgets.CustomDialog;

public class WorkEngineerView extends WorkBaseView implements OnItemClickListener{
	private PopupWindow mPopupWindow;
	private View mCurrentView;
	private String[] mArray;

	private boolean mIsChange;
	private boolean mIsHit;

	private int mAPump; //A泵
	private int mBPump; //B泵
	private int mCPump; //C泵
	private int mDPump; //D泵
	private int mEPump; //E泵
	private int mCellCentrifugalSpeed; //细胞离心速率

	public WorkEngineerView(Context context) {
		super(context);
	}

	@Override
	public void initContainerId() {
		mContainerId = R.layout.work_view_engineer;
	}

	@Override
	public int getTitleId() {
		return R.drawable.setting_title;
	}

	@Override
	public void init() {
		SiteBtnClickListener listener = new SiteBtnClickListener();
		findViewById(R.id.btn_engineer_a_pump).setOnClickListener(listener);
		findViewById(R.id.btn_engineer_b_pump).setOnClickListener(listener);
		findViewById(R.id.btn_engineer_c_pump).setOnClickListener(listener);
		findViewById(R.id.btn_engineer_d_pump).setOnClickListener(listener);
		findViewById(R.id.btn_engineer_e_pump).setOnClickListener(listener);
		findViewById(R.id.btn_engineer_cell_centrifugal_speed).setOnClickListener(listener);

		Button sure = (Button) Tool.setTextType(this, R.id.btn_sure, Constant.FONT_TYPE_FANGZ);
		Button cancel = (Button) Tool.setTextType(this, R.id.btn_cancel, Constant.FONT_TYPE_FANGZ);


		sure.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mIsHit = true;
				save();
				ViewController.getInstance().changeView(ViewController.VIEW_ABOUT);
				ViewController.getInstance().setMenuSelect(ViewController.VIEW_ABOUT);
			}
		});

		cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mIsHit = true;
				ViewController.getInstance().changeView(ViewController.VIEW_ABOUT);
				ViewController.getInstance().setMenuSelect(ViewController.VIEW_ABOUT);
			}
		});

		mViewChange = new ViewChange() {
			public void willHide(final HadChange hadChange) {
				if(mIsChange&&!mIsHit){
					final CustomDialog dialog = new CustomDialog(getContext());
					dialog.setMessage("是否保存参数");
					dialog.setNegativeListener(new OnClickListener() {
						public void onClick(View v) {
							dialog.dismiss();
							hadChange.hadHide();
						}
					});
					dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
						@Override
						public void onCancel(DialogInterface dialogInterface) {
							dialog.dismiss();
							hadChange.hadHide();
						}
					});
					dialog.setPositiveListener(new OnClickListener() {
						public void onClick(View v) {
							save();
							dialog.dismiss();
							hadChange.hadHide();
						}
					});
				}else{
					hadChange.hadHide();
				}
			}
		};
	}

	public void save(){
		//检查处于连接状态（不检查的话如果也可用设置，那么首页Start的时候就需要加上同步参数操作）
		Global.saveEngineerDatas(mAPump, mBPump, mCPump, mDPump, mEPump, mCellCentrifugalSpeed, getContext());
	}

	protected void initPopuptWindow() {
		View menuView = LayoutInflater.from(getContext()).inflate(R.layout.site_button_list, null,false);
		menuView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		ListView list = (ListView) menuView.findViewById(R.id.lv_site);
		list.setOnItemClickListener(this);
		list.setAdapter(new BtnsAdapter());
		mPopupWindow = new PopupWindow(menuView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		mPopupWindow.setAnimationStyle(R.style.btnListAnimation);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setBackgroundDrawable(new ColorDrawable());
		mPopupWindow.setFocusable(true);

		menuView.setFocusableInTouchMode(true);
		menuView.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((keyCode == KeyEvent.KEYCODE_MENU) && (mPopupWindow.isShowing())) {
					mPopupWindow.dismiss();
					return true;
				}
				return false;
			}
		});
	}
	
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		if(!isSlidMenuCanScroll()){
			setSlidingMenuSCroll(true);
		}
//		if(isSlidMenuViewShow()){
//			setSlidMenuViewShow();
//		}
		
		mIsChange = false;
		mIsHit = false;
		
		mAPump = Global.mAPump;
		mBPump = Global.mBPump;
		mCPump = Global.mCPump;
		mDPump = Global.mDPump;
		mEPump = Global.mEPump;
		mCellCentrifugalSpeed = Global.mCellCentrifugalSpeed;

		((Button) findViewById(R.id.btn_engineer_a_pump)).setText(String.valueOf(mAPump + 1));
		((Button) findViewById(R.id.btn_engineer_b_pump)).setText(String.valueOf(mBPump + 1));
		((Button) findViewById(R.id.btn_engineer_c_pump)).setText(String.valueOf(mCPump + 1));
		((Button) findViewById(R.id.btn_engineer_d_pump)).setText(String.valueOf(mDPump + 1));
		((Button) findViewById(R.id.btn_engineer_e_pump)).setText(String.valueOf(mEPump + 1));
		((Button) findViewById(R.id.btn_engineer_cell_centrifugal_speed)).setText(
				getContext().getResources().getStringArray(R.array.engineer_btn_cell_centrifugal_speed)[mCellCentrifugalSpeed]);
	}
	
	/*@Override
	protected void onDetachedFromWindow() {
		if(mIsChange){
			CommandBridge.getInstance().linkSiteDates(Global.mRanSeHouDu, Global.mGuDingQDState,
					Global.mJieJingZiLev, Global.mDianJiuLev, Global.mChengZhong);
		}
		super.onDetachedFromWindow();
	}*/
	private static final String[] array_50 = new String[50];
	static {
		for (int i = 0; i < 50; i++) {
			array_50[i] = String.valueOf(i + 1);
		}
	}
	private class SiteBtnClickListener implements OnClickListener{
		public void onClick(View view) {
			mCurrentView = view;
			int id = -1;
			int height = LayoutParams.WRAP_CONTENT;
			switch (view.getId()) {
				case R.id.btn_engineer_a_pump:
					mArray = array_50;
					break;
				case R.id.btn_engineer_b_pump:
					mArray = array_50;
					break;
				case R.id.btn_engineer_c_pump:
					mArray = array_50;
					break;
				case R.id.btn_engineer_d_pump:
					mArray = array_50;
					height = 450;
					break;
				case R.id.btn_engineer_e_pump:
					mArray = array_50;
					height = 500;
					break;
				case R.id.btn_engineer_cell_centrifugal_speed:
					id = R.array.engineer_btn_cell_centrifugal_speed;
					mArray = getContext().getResources().getStringArray(id);
					height = 130;
					break;
			}

			if (mPopupWindow == null) {
				initPopuptWindow();
			}
			if (mPopupWindow.isShowing()) {
				mPopupWindow.dismiss();
			} else {
				mPopupWindow.setHeight(height);
				mPopupWindow.showAsDropDown(view);
			}
		}
	}
	
	private class BtnsAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			return mArray.length;
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int options, View view, ViewGroup arg2) {
			if(view == null){
				view = LayoutInflater.from(getContext()).inflate(R.layout.site_button_item, null);
			}
			
			TextView textView = (TextView)view.findViewById(R.id.tv_btn_list_text);
			
			if(options == 0)textView.setBackgroundResource(R.drawable.selector_button_site_list_item_top);
			else if(options == getCount()-1)textView.setBackgroundResource(R.drawable.selector_button_site_list_item_bottom);
			else textView.setBackgroundResource(R.drawable.selector_button_site_list_item);
			
			textView.setText(mArray[options]);
			
			return view;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int option, long arg3) {
		if(mPopupWindow != null)mPopupWindow.dismiss();

		if(mCurrentView == null) return;
		((TextView)mCurrentView.findViewWithTag("btntxt")).setText(mArray[option]);
		
		switch (mCurrentView.getId()) {
		case R.id.btn_engineer_a_pump:
			if(option != mAPump){
				mAPump = option;
				mIsChange = true;
			}
			break;
		case R.id.btn_engineer_b_pump:
			if(option != mBPump){
				mBPump = option;
				mIsChange = true;
			}
			break;
		case R.id.btn_engineer_c_pump:
			if(option != mCPump){
				mCPump = option;
				mIsChange = true;
			}
			break;
		case R.id.btn_engineer_d_pump:
			if(option != mDPump){
				mDPump = option;
				mIsChange = true;
			}
			break;
		case R.id.btn_engineer_e_pump:
			if(option != mEPump){
				mEPump = option;
				mIsChange = true;
			}
			break;
		case R.id.btn_engineer_cell_centrifugal_speed:
			if(option != mCellCentrifugalSpeed){
				mCellCentrifugalSpeed = option;
				mIsChange = true;
			}
			break;
		default:
			break;
		}
	}
}
