package com.hp.android.haoxin;

import android.content.Intent;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;

import com.hp.android.haoxin.global.Global;
import com.hp.android.haoxin.utils.Tool;

public class LogoActivity extends BaseActivity{
	// FIXME: 15/8/30 handler
	public static final Handler handler = new Handler();
	@Override
	protected void initContainer() {
		mContainerResId = R.layout.activity_logo;
		super.initContainer();
		Global.init();

		Tool.showSystemUI(this, false);
		
		
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
		int W = mDisplayMetrics.widthPixels;
		int H = mDisplayMetrics.heightPixels;
		Log.e("Main", "Width = " + W);
		Log.e("Main", "Height = " + H);
		
		new Handler().postDelayed(new Runnable() {
			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {
						Intent intent = new Intent();
						intent.setClass(LogoActivity.this, LoadingActivity.class);
						startActivity(intent);
						overridePendingTransition(R.anim.activivity_fade_in, R.anim.activity_fade_out);
						finish();
					}
				});
			}
		}, 2000);
	}
}
