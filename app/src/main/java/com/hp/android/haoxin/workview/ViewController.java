package com.hp.android.haoxin.workview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.FrameLayout;

import com.hp.android.haoxin.MainActivity;
import com.hp.android.haoxin.R;
import com.hp.android.haoxin.global.Global;
import com.hp.android.haoxin.slidingmenu.MainMenuFragment;
import com.hp.android.haoxin.slidingmenu.MainWorkFragment;
import com.hp.android.haoxin.widgets.CustomDialog;
import com.hp.android.haoxin.workview.WorkBaseView.HadChange;

import java.util.HashMap;

public class ViewController {

	public static final int VIEW_HOME = 0;
	public static final int VIEW_CLEAN = 1;
	public static final int VIEW_SITE = 2;
	public static final int VIEW_SYSTEM = 3;
	public static final int VIEW_ABOUT = 4;
	public static final int VIEW_SYSTEM_LIULU = 5;
	public static final int VIEW_SYSTEM_CHENGZHONG = 6;
	public static final int VIEW_SYSTEM_FILL = 7;
	public static final int VIEW_RANSE = 8;
	public static final int VIEW_CENTRIFUGAL = 9;
	public static final int VIEW_ENGINEER = 10;

	
	private static ViewController mController;
	private static FrameLayout mContainer;
	private static Context mContext;
	private static MainWorkFragment mWorkFragment;
	private static MainMenuFragment mMenuFragment;
	
	private WorkBaseView mCurrentView;
	
	private boolean mIsBack2Home;
	
	//private static ArrayList<View> mViewList;
	private static HashMap<Integer, WorkBaseView> mViewMap;
	
	private ViewController(){};
	
	public static void initContainer(MainActivity activity){
		FrameLayout frame = (FrameLayout) activity.findViewById(R.id.fm_work_container);
		if(mContainer == null || mContainer != frame){
			mContainer = frame;
			mWorkFragment = activity.getWorkFragment();
			mMenuFragment = activity.getMenuFragment();
			mContext = activity;
			
			initAllView();
			/*mViewList.add(new WorkCleanView().getView(mContext));
			mViewList.add(new WorkSiteView().getView(mContext));
			mViewList.add(new WorkSystemView().getView(mContext));
			mViewList.add(new WorkAboutView().getView(mContext));*/
			getInstance().mCurrentView = mViewMap.get(VIEW_HOME);
			mContainer.addView(getInstance().mCurrentView);
		}
	}
	
	public static ViewController getInstance(){
		if(mController == null){
			mController = new ViewController();
		}
		return mController;
	}
	
	public WorkBaseView getCurrentView(){
		return mCurrentView;
	}
	
	public void setIdBack2Home(boolean isb){
		mIsBack2Home = isb;
	}
	
	public boolean isBack2Home(){
		return mIsBack2Home;
	}
	
	public MainWorkFragment getWorkFragment(){
		return mWorkFragment;
	}
	
	public MainMenuFragment getMenuFragment(){
		return mMenuFragment;
	}
	
	public void setMenuSelect(int id){
		mMenuFragment.setSelected(id);
	}
	
	public void curr2Success(int msgId,int backId){
		mContainer.removeAllViews();
		mCurrentView = new WorkSuccessView(mContext, msgId, backId);
		mContainer.addView(mCurrentView);
	}
	
	public void success2Home(int viewId){
		mContainer.removeAllViews();
		mCurrentView = getView(viewId);
		mContainer.addView(mCurrentView);
		if(viewId == VIEW_HOME){
			mMenuFragment.setSelected(0);
		}
	}
	/**
	 * 检查发现链路不通
	 */
	public void checkConnectFailed(final boolean ifFailedGoHome) {
		final CustomDialog dialog = CustomDialog.createExceptionDialog(mCurrentView.getContext(), R.string.exception_not_comm_actived);
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialogInterface) {
				dialog.dismiss();
				if (ifFailedGoHome) {
					mMenuFragment.setSelected(0);
					mContainer.removeAllViews();
					mCurrentView = getView(VIEW_HOME);
					mContainer.addView(mCurrentView);
				}
			}
		});
		dialog.setPositiveListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.dismiss();
				if (ifFailedGoHome) {
					mMenuFragment.setSelected(0);
					mContainer.removeAllViews();
					mCurrentView = getView(VIEW_HOME);
					mContainer.addView(mCurrentView);
				}
			}
		});
	}

	/**
	 * @return 检查连接，true表示连接OK，false表示连接异常
	 * @param ifFailedGoHome 如果连接失败是否要返回主界面
	 */
	public boolean checkConnect(boolean ifFailedGoHome) {
		if (!Global.isCommActived()) {
			checkConnectFailed(ifFailedGoHome);
			return false;
		}
		return true;
	}
	public void changeView(final int viewId){
		// FIXME: 15/8/30 屏蔽检查连接--如果被屏蔽，需要打开
		if (!checkConnect(true)) {
			return;
		}
		if(mCurrentView.mViewChange != null){
			mCurrentView.mViewChange.willHide(new HadChange() {
				public void hadHide() {
					mContainer.removeAllViews();
					mCurrentView = getView(viewId);
					mContainer.addView(mCurrentView);
				}
			});
		}else{
			mContainer.removeAllViews();
			mCurrentView = getView(viewId);
			mContainer.addView(mCurrentView);
		}
	}
	
	public void back2SystemHome(){
		mContainer.removeAllViews();
		mCurrentView = getView(VIEW_SYSTEM);
		mContainer.addView(mCurrentView);
		mMenuFragment.setSelected(3);
		System.gc();
	}
	
	public WorkBaseView getView(int viewId){
		WorkBaseView view = mViewMap.get(viewId);
		if(view == null){
			switch (viewId) {
			case VIEW_HOME:
				view = new WorkHomeView(mContext);
				break;
			case VIEW_CLEAN:
				view = new WorkCleanView(mContext);
				break;
			case VIEW_SITE:
				view = new WorkSiteView(mContext);
				break;
			case VIEW_SYSTEM:
				view = new WorkSystemView(mContext);
				break;
			case VIEW_ABOUT:
				view = new WorkAboutView(mContext);
				break;
			case VIEW_SYSTEM_LIULU:
				view = new WorkLiuView(mContext);
				break;
			case VIEW_SYSTEM_CHENGZHONG:
				view = new WorkChengView(mContext);
				break;
			case VIEW_SYSTEM_FILL:
				view = new WorkFillView(mContext);
				break;
			case VIEW_RANSE:
				view = new WorkDyeView(mContext);
				break;//return view;
			case VIEW_CENTRIFUGAL:
				view = new WorkCentrifugalView(mContext);
				break;
			case VIEW_ENGINEER:
				view = new WorkEngineerView(mContext);
				break;
			default:
				break;
			}
			
			mViewMap.put(viewId, view);
		}
		return view;
	}
	
	@SuppressLint("UseSparseArrays")
	private static void initAllView(){
		mViewMap = new HashMap<Integer, WorkBaseView>();
		mViewMap.put(VIEW_HOME, new WorkHomeView(mContext));
	}
}
