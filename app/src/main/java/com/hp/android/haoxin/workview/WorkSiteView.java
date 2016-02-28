package com.hp.android.haoxin.workview;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
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

public class WorkSiteView extends WorkBaseView implements OnItemClickListener{
	private PopupWindow mPopupWindow;
	private View mCurrentView;
	private String[] mArray;
	
	private TextView mRanSeText;
	private TextView mJieJingText;
	private TextView mDianJiuText;
	private TextView mGuDingText;
	private TextView mChengZhongText;

	private boolean mIsChange;
	private boolean mIsHit;
	
	private int mTempRanSe; //染色
	private int mTempGuding; //固定
	private int mTempJiejing; //结晶紫
	private int mTempDianjiu; //碘酒
	private int mTempChengzhong; //称重
	
	public WorkSiteView(Context context) {
		super(context);
	}
	
	@Override
	public void initContainerId() {
		mContainerId = R.layout.work_view_site;
	}
	
	@Override
	public int getTitleId() {
		return R.drawable.setting_title;
	}

	@Override
	public void init() {
		Tool.setTextType(this, R.id.tv_site_ransehoudu_title, Constant.FONT_TYPE_FANGZ);
		Tool.setTextType(this, R.id.tv_site_gudingqidong_title, Constant.FONT_TYPE_FANGZ);
		Tool.setTextType(this, R.id.tv_site_jiejingzi_title, Constant.FONT_TYPE_FANGZ);
		Tool.setTextType(this, R.id.tv_site_dianjiutiaozheng_title, Constant.FONT_TYPE_FANGZ);
		Tool.setTextType(this, R.id.tv_site_chenzhongqidong_title, Constant.FONT_TYPE_FANGZ);

		mRanSeText = Tool.setTextType(this, R.id.btn_site_ransehoudu_text, Constant.FONT_TYPE_FANGZ);
		mGuDingText = Tool.setTextType(this, R.id.btn_site_gudingqidong_text, Constant.FONT_TYPE_FANGZ);
		mJieJingText = Tool.setTextType(this, R.id.btn_site_jiejingzi_text, Constant.FONT_TYPE_FANGZ);
		mDianJiuText = Tool.setTextType(this, R.id.btn_site_dianjiutiaozheng_text, Constant.FONT_TYPE_FANGZ);
		mChengZhongText = Tool.setTextType(this, R.id.btn_site_chenzhongqidong_text, Constant.FONT_TYPE_FANGZ);

		SiteBtnClickListener listener = new SiteBtnClickListener();
		findViewById(R.id.btn_site_ransehoudu_text).setOnClickListener(listener);
		findViewById(R.id.btn_site_gudingqidong_text).setOnClickListener(listener);
		findViewById(R.id.btn_site_jiejingzi_text).setOnClickListener(listener);
		findViewById(R.id.btn_site_dianjiutiaozheng_text).setOnClickListener(listener);
		findViewById(R.id.btn_site_chenzhongqidong_text).setOnClickListener(listener);


		Button sure = (Button) Tool.setTextType(this, R.id.btn_sure, Constant.FONT_TYPE_FANGZ);
		Button cancel = (Button) Tool.setTextType(this, R.id.btn_cancel, Constant.FONT_TYPE_FANGZ);
		
		
		sure.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mIsHit = true;
				save();
				ViewController.getInstance().changeView(ViewController.VIEW_HOME);
				ViewController.getInstance().setMenuSelect(0);
			}
		});
		
		cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				mIsHit = true;
				ViewController.getInstance().changeView(ViewController.VIEW_HOME);
				ViewController.getInstance().setMenuSelect(0);
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
		Global.mRanSeHouDu = mTempRanSe;
		Global.mGuDingQDState = mTempGuding;
		Global.mJieJingZiLev = mTempJiejing;
		Global.mDianJiuLev = mTempDianjiu;
		Global.mChengZhong = mTempChengzhong;
		
		Global.saveDatas(getContext());
	}
	
	protected void initPopuptWindow() {
		View menuView = LayoutInflater.from(getContext()).inflate(R.layout.site_button_list, null,false);
		menuView.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);
		ListView list = (ListView) menuView.findViewById(R.id.lv_site);
		list.setOnItemClickListener(this);
		list.setAdapter(new BtnsAdapter());
		mPopupWindow = new PopupWindow(menuView, LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT, true);
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
		
		mTempRanSe = Global.mRanSeHouDu;
		mTempGuding = Global.mGuDingQDState;
		mTempJiejing = Global.mJieJingZiLev;
		mTempDianjiu = Global.mDianJiuLev;
		mTempChengzhong = Global.mChengZhong;

		mRanSeText.setText(getContext().getResources().getStringArray(R.array.site_btn_ranses)[Global.mRanSeHouDu]);
		mJieJingText.setText(getContext().getResources().getStringArray(R.array.site_btn_jiejings)[Global.mJieJingZiLev]);
		mDianJiuText.setText(getContext().getResources().getStringArray(R.array.site_btn_dianjius)[Global.mDianJiuLev]);
		mGuDingText.setText(getContext().getResources().getStringArray(R.array.site_btn_gudnigs)[Global.mGuDingQDState]);
		mChengZhongText.setText(getContext().getResources().getStringArray(R.array.site_btn_chengzhongs)[Global.mChengZhong]);
	}
	
	/*@Override
	protected void onDetachedFromWindow() {
		if(mIsChange){
			CommandBridge.getInstance().linkSiteDates(Global.mRanSeHouDu, Global.mGuDingQDState,
					Global.mJieJingZiLev, Global.mDianJiuLev, Global.mChengZhong);
		}
		super.onDetachedFromWindow();
	}*/
	
	private class SiteBtnClickListener implements OnClickListener{
		public void onClick(View view) {
			
			mCurrentView = view;
			int id = -1;
			switch (view.getId()) {
			case R.id.btn_site_ransehoudu_text:
				id = R.array.site_btn_ranses;
				break;
			case R.id.btn_site_gudingqidong_text:
				id = R.array.site_btn_gudnigs;
				break;
			case R.id.btn_site_jiejingzi_text:
				id = R.array.site_btn_jiejings;
				break;
			case R.id.btn_site_dianjiutiaozheng_text:
				id = R.array.site_btn_dianjius;
				break;
			case R.id.btn_site_chenzhongqidong_text:
				id = R.array.site_btn_chengzhongs;
				break;
			default:
				id = R.array.site_btn_jiejings;
				break;
			}
			
			mArray = getContext().getResources().getStringArray(id);
			
			if (mPopupWindow == null) {
				initPopuptWindow();
			}
			
			if (mPopupWindow.isShowing()) {
				mPopupWindow.dismiss();
			} else {
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
		case R.id.btn_site_ransehoudu_text:
			if(option != mTempRanSe){
				mTempRanSe = option;
				mIsChange = true;
			}
			break;
		case R.id.btn_site_gudingqidong_text:
			if(option != mTempGuding){
				mTempGuding = option;
				mIsChange = true;
			}
			break;
		case R.id.btn_site_jiejingzi_text:
			if(option != mTempJiejing){
				mTempJiejing = option;
				mIsChange = true;
			}
			break;
		case R.id.btn_site_dianjiutiaozheng_text:
			if(option != mTempDianjiu){
				mTempDianjiu = option;
				mIsChange = true;
			}
			break;
		case R.id.btn_site_chenzhongqidong_text:
			if(option != mTempChengzhong){
				mTempChengzhong = option;
				mIsChange = true;
			}
			break;
		default:
			break;
		}
	}
}
