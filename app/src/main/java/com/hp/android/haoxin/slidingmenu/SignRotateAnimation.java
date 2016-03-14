package com.hp.android.haoxin.slidingmenu;

import android.os.Handler;
import android.view.View;

public class SignRotateAnimation {

	private View mSign;
	
	private final static int TIME_COUNT = 10;
	
	public SignRotateAnimation(View view){
		mSign = view;
	}
	
	public void setRotate(float dx, float des){
		mSign.setRotation(180.f*dx/des);
	}
	
	public void startRotateAnimation(float toDegrees, int duration){
		float currDegress = mSign.getRotation();
		float dxDegress = toDegrees - currDegress;
		
		final int time = duration/TIME_COUNT;
		final float once = dxDegress/(float)TIME_COUNT;
		
		final Handler updateTimeHandler = new Handler();
		new Handler().post(new Runnable() {
			int t = -1;
			public void run() {
				t++;
				float degress = mSign.getRotation();
				if(degress <= 0 && degress >= -180){
					float last = degress + once;
					if(last > 0)last = 0;
					else if(last < -180)last = -180;
					mSign.setRotation(last);
				}
				if(t < TIME_COUNT){
					updateTimeHandler.postDelayed(this, time);
				}else{
					System.gc();
				}
			}
		});
	}
}
