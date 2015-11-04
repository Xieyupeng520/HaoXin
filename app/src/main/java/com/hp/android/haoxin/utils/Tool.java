package com.hp.android.haoxin.utils;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.hp.android.haoxin.HaoXinApplication;

import org.apache.http.util.EncodingUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

public class Tool {

	private static float mScale;
	
	public static void init(Context context){
		mScale = context.getResources().getDisplayMetrics().density;
	}
	
	public static int dip2px(float dpValue) {
		return (int) (dpValue * mScale + 0.5f);
	}
	
	public static float getScale(){
		return mScale;
	}
	
	public static String getNameByPath(String path){
		if(path == null || "".equals(path)){
			HppLog.e("Tool", "Tool.getNameByPath(String path)参数path 错误", null);
			return "";
		}
		int index = path.lastIndexOf('/');
		if(index == -1) return path;
		String format = path.substring(index + 1);
		index = format.lastIndexOf('.');
		return index == -1 ? format : format.substring(0, index);
	}
	
	/**
	 * 显示消息框
	 * @param context
	 * @param title
	 * @param msg
	 */
	public static void showMsgBox(Context context, String title, String msg) {
		new AlertDialog.Builder(context).setTitle(title).setMessage(msg).setPositiveButton(context.getResources().getString(android.R.string.ok), new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).show();
	}
	
	public static String encoding(String str){
		str = str == null ?  "" : str;
		return EncodingUtils.getString(str.getBytes(), "UTF-8");
	}
	
	public static String optString(String str){
    	if(null == str || "null".equals(str)){
    		return "";
    	}else{
    		return str;
    	}
    }
	
/*	public static String getLanguageGender(String str){
		Context context = QQBXApplication.getAppContext();
		String gender = str.toLowerCase();
		if("male".equals(gender)){
			return context.getResources().getStringArray(R.array.gender)[1];
		}else{
			return context.getResources().getStringArray(R.array.gender)[0];
		}
	}*/
	
	/**
	 * 获取版本名
	 * 
	 * @return 当前应用的版本名
	 */
	public static String getVersionName(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public static void showSystemUI(Context context,boolean show) {
		DataOutputStream os = null;
        try {
        	File systemUIapkFile = new File("/system/app/SystemUI.apk");
        	Process p;
            p = Runtime.getRuntime().exec("su");
            
            os = new DataOutputStream(p.getOutputStream());
            os.writeBytes("mount -o remount,rw /dev/block/stl6 /system\n");

            boolean filexists = systemUIapkFile.exists();
            // Attempt to write a file to a root-only
            if(show){
            	if(!filexists){
            		os.writeBytes("mv /system/app/SystemUI.apkold /system/app/SystemUI.apk\n");
            	}
            }else{
            	if (filexists) {
            		os.writeBytes("mv /system/app/SystemUI.apk /system/app/SystemUI.apkold\n");
            	}
            }

            // Close the terminal
            os.writeBytes("mount -o remount,ro /dev/block/stl6 /system\n");
            os.writeBytes("exit\n");
            os.flush();
            p.waitFor();
            
            	Intent intent = new Intent();
            	intent.setComponent(new ComponentName(
            			"com.android.systemui","com.android.systemui.SystemUIService"));
            	context.startService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	if(os != null){
        		try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
    }
	
	public static TextView setTextType(View view,int id,String type){
		TextView textView = (TextView) view.findViewById(id);
		//TODO: 15/9/1 设置字体暂时取消，该设置字体的方法有待优化！createFromAsset重复调用多次非常耗费内存！
//		if(!Constant.FONT_TYPE_HELVE.equals(type))setTextType(textView, type);
		return textView;
	}
	
	public static void setTextType(TextView textView,String type){
		Typeface typeFace = Typeface.createFromAsset(HaoXinApplication.getAppContext().getAssets(),"fonts/"+type);
		textView.setTypeface(typeFace);
		//textView.setTextSize(TypedValue.COMPLEX_UNIT_PX , getFontSize((int)textView.getTextSize()));
	}
	
	private static float mScreenScale = -1;
	public static int getFontSize(int textSize) {
		if(mScreenScale == -1){
			DisplayMetrics dm = new DisplayMetrics();
			WindowManager windowManager = (WindowManager) HaoXinApplication.getAppContext()
					.getSystemService(Context.WINDOW_SERVICE);
			windowManager.getDefaultDisplay().getMetrics(dm);
			int screenHeight = dm.heightPixels;
			mScreenScale = (float) screenHeight / 1280;
		}
        // screenWidth = screenWidth > screenHeight ? screenWidth : screenHeight;
        int rate = (int) (textSize * mScreenScale);
        return rate;
	}
}
