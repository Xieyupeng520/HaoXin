package com.hp.android.haoxin.workview;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.hp.android.haoxin.R;
import com.hp.android.haoxin.global.Global;
import com.hp.android.haoxin.utils.Constant;
import com.hp.android.haoxin.utils.Tool;

public class WorkAboutView extends WorkBaseView{

	public WorkAboutView(Context context) {
		super(context);
	}


	@Override
	public void initContainerId() {
		mContainerId = R.layout.work_view_about;
	}
	
	/**
	　　* 获取APP版本号
	　　* @return 当前应用的版本号
	　　*/
	public String getVersion() {
		String version = "";
		try {
			PackageManager manager = getContext().getPackageManager();
			PackageInfo info = manager.getPackageInfo(getContext().getPackageName(), 0);
			version = info.versionName;
		} catch (Exception e) {
	    	e.printStackTrace();
	    }
		return version;
	}

	public void init() {
//		addView(LayoutInflater.from(getContext()).inflate(R.layout.work_view_about, null));
		Tool.setTextType(this, R.id.sw_version_text, Constant.FONT_TYPE_FANGZ);
		Tool.setTextType(this, R.id.hw_version_text, Constant.FONT_TYPE_FANGZ);
		Tool.setTextType(this, R.id.fw_version_text, Constant.FONT_TYPE_FANGZ);
		if (Global.HW_VERSION != null && Global.SW_VERSION != null) {
			((TextView) findViewById(R.id.sw_version_text)).setText(getResources().getString(R.string.sw_version) + getVersion());
			((TextView) findViewById(R.id.hw_version_text)).setText(getResources().getString(R.string.hw_version) + new String(Global.HW_VERSION));
			((TextView) findViewById(R.id.fw_version_text)).setText(getResources().getString(R.string.fw_version) + new String(Global.SW_VERSION)); //固件版本
		}

		findViewById(R.id.sw_version_text).setOnTouchListener(new OnTouchListener() {
			long waitTime = 2000;
			long touchTime = 0;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == KeyEvent.ACTION_DOWN) {
					long currentTime = System.currentTimeMillis();
					if((currentTime-touchTime) >= waitTime) {
						Log.d("wait", "再按一次进入工程师菜单");
						touchTime = currentTime;
					}else {
						ViewController.getInstance().changeView(ViewController.VIEW_ENGINEER);
					}
				}
				return false;
			}
		});
	}

	@Override
	public int getTitleId() {
		return R.drawable.about_title;
	}
}
