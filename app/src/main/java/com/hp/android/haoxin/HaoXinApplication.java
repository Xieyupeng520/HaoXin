package com.hp.android.haoxin;

import android.app.Application;
import android.content.Context;

public class HaoXinApplication extends Application{

	private static Context mContext;
	
	 public void onCreate(){ 
	        super.onCreate(); 
	       // CrashHandler crash =  CrashHandler.getInstance();
		   // crash.init(getApplicationContext());
			HaoXinApplication.mContext = getApplicationContext();

	 }

	    public static Context getAppContext() {
	        return HaoXinApplication.mContext; 
	    }

}
