package com.hp.android.haoxin;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.hp.android.haoxin.callback.OnReadDeviceDataCallBackImpl;
import com.hp.android.haoxin.global.Global;
import com.hp.android.haoxin.global.GlobalState;
import com.hp.android.haoxin.slidingmenu.MainMenuFragment;
import com.hp.android.haoxin.slidingmenu.MainWorkFragment;
import com.hp.android.haoxin.slidingmenu.SlidingMenu;
import com.hp.android.haoxin.workview.ViewController;


public class MainActivity extends FragmentActivity {
	SlidingMenu mSlidingMenu;
	MainWorkFragment mWorkFragment;
	MainMenuFragment mMenuFragment;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		Global.setState(GlobalState.HOME); //设置当前程序状态
		mSlidingMenu = new SlidingMenu(this);
		setContentView(mSlidingMenu,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		init();
		OnReadDeviceDataCallBackImpl.getInstance().setContext(this); //设置读取设备回调的上下文
	}

	private void init() {
		mSlidingMenu.setLeftView(getLayoutInflater().inflate(R.layout.slid_menu_frame, null));
		mSlidingMenu.setCenterView(getLayoutInflater().inflate(R.layout.slid_work_frame, null),0x000000);//R.drawable.backgroundimg
		
		FragmentTransaction trans = getSupportFragmentManager().beginTransaction();

		mMenuFragment = new MainMenuFragment();
		mWorkFragment = new MainWorkFragment();
		trans.replace(R.id.menu_frame, mMenuFragment);
		trans.replace(R.id.work_frame, mWorkFragment);
		
		trans.commit();

		//获取分辨率 --- 非必要代码
		Display display = getWindowManager().getDefaultDisplay(); //Activity#getWindowManager()
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;
		Log.d("MainActivity", "width = " + width + ", height = " + height);
	}

	@Override
	protected void onStart() {
		super.onStart();
		ViewController.initContainer(this);
	}
	
	public MainWorkFragment getWorkFragment(){
		return mWorkFragment;
	}
	
	public MainMenuFragment getMenuFragment(){
		return mMenuFragment;
	}
	
	public SlidingMenu getSlidingMenu(){
		return mSlidingMenu;
	}
	
	public void showMenu() {
		mSlidingMenu.showLeftView();
	}

	public void showRight() {
		mSlidingMenu.showRightView();
	}
	
	public boolean isSlidMenuShow(){
		return mSlidingMenu.isLeftViewShow();
	}
	
	public boolean isSlidMenuCanScroll(){
		return mSlidingMenu.isLeftViewCanScroll();
	}
	
	public void setSlidMenuScroll(boolean isb){
		mSlidingMenu.setCanSliding(isb, false);
	}
}
