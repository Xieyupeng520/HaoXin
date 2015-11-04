package com.hp.android.haoxin.workview;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
	}

	@Override
	public int getTitleId() {
		return R.drawable.about_title;
	}
}
