package com.hp.android.haoxin.workview;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.hp.android.haoxin.R;
import com.hp.android.haoxin.global.Global;
import com.hp.android.haoxin.utils.Constant;
import com.hp.android.haoxin.utils.FomatTool;
import com.hp.android.haoxin.utils.Tool;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					long currentTime = System.currentTimeMillis();
					if ((currentTime - touchTime) >= waitTime) {
						Log.d("wait", "再按一次进入工程师菜单");
						touchTime = currentTime;
					} else {
						ViewController.getInstance().changeView(ViewController.VIEW_ENGINEER);
					}
				}
				return false;
			}
		});

		//日期和时间选择按钮
		initDateAndTime();

		//屏幕睡眠时间选择按钮
		initScreenSleepTime();
	}

	private void initDateAndTime() {
		final Button dataAndTimeBtn = ((Button) findViewById(R.id.date_and_time_btn));
		final TimePickerView pickerView = new TimePickerView(getContext(), TimePickerView.Type.ALL);
		pickerView.setTitle(getContext().getString(R.string.date_time));
		pickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
			@Override
			public void onTimeSelect(Date date) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH);
				int day = calendar.get(Calendar.DAY_OF_MONTH);
				int hours = calendar.get(Calendar.HOUR_OF_DAY);
				int minute = calendar.get(Calendar.MINUTE);

				//1.给系统设置时间
				Intent intentDate = new Intent("APP_DATE_SETTING");
				intentDate.putExtra("year", year);
				intentDate.putExtra("month", month);   //月份是从0开始的，0表示1月，2表示3月
				intentDate.putExtra("day", day);
				getContext().sendBroadcast(intentDate);
				Intent intentTime = new Intent("APP_TIME_SETTING");
				intentTime.putExtra("hourOfDay", hours);
				intentTime.putExtra("minute", minute);
				getContext().sendBroadcast(intentTime);

				//2.显示设置的时间
				dataAndTimeBtn.setText(FomatTool.formatDateTime(date));

				//3.刷新title界面
				initTime();
			}
		});

		dataAndTimeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				pickerView.show();
			}
		});

		dataAndTimeBtn.setText(FomatTool.formatDateTime(null)); //设置默认时间

		/***************************************************
		 * 通過反射，將“確定”和“取消”改為繁體
		 ***************************************************/
		changePickerViewButtonText_FromSimplifieldChinese_ToTraditionalChinese(TimePickerView.class, pickerView);
	}

	private void initScreenSleepTime() {
		final Button sleepTimeBtn = ((Button) findViewById(R.id.screen_sleep_time_btn));
		final OptionsPickerView pickerView = new OptionsPickerView(getContext());
		ArrayList<String> optionsItems = new ArrayList<String>();

		final String[] array = getContext().getResources().getStringArray(R.array.screen_sleep_time);
		for (String s : array) {
			optionsItems.add(s);
		}

		pickerView.setPicker(optionsItems);
		pickerView.setTitle(getContext().getString(R.string.screen_sleep_time));
		pickerView.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {
			int[] second = {15, 30, 60, 120, 300, 600, 1800}; //以秒来单位,120就是两分钟 有下面几挡：15秒，30秒，1、2、5、10、30分钟

			@Override
			public void onOptionsSelect(int option1, int option2, int option3) {
				//1.给系统设置
				Intent intent = new Intent("APP_BL_SETTING");
				intent.putExtra("seconds", second[option1]);
				getContext().sendBroadcast(intent);

				//2.显示设置
				sleepTimeBtn.setText(array[option1] + "   >");

				//3.本地数据保存一份
				saveSleepTimeSharedPreferences(option1);
			}
		});

		sleepTimeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				pickerView.show();
			}
		});

		//设置默认显示
		int defaultSleepTimeIndex = readSleepTimeSharedPreferences();
		Log.d("sleepTimeIndex", "defaultSleepTimeIndex = " + defaultSleepTimeIndex);
		if (defaultSleepTimeIndex == -1) { //如果没有数据，则设置默认为半小时
			Intent intent = new Intent("APP_BL_SETTING");
			intent.putExtra("seconds", 1800);
			getContext().sendBroadcast(intent);

			defaultSleepTimeIndex = array.length - 1; //1800的下标
		}
		sleepTimeBtn.setText(array[defaultSleepTimeIndex] + "   >");
		pickerView.setSelectOptions(defaultSleepTimeIndex); //默认选中

		/***************************************************
		 * 通過反射，將“確定”和“取消”改為繁體
		 ***************************************************/
		changePickerViewButtonText_FromSimplifieldChinese_ToTraditionalChinese(OptionsPickerView.class, pickerView);
	}

	private void changePickerViewButtonText_FromSimplifieldChinese_ToTraditionalChinese(Class pickerView, Object instance) {
		try {
			Field btnSubmitField = pickerView.getDeclaredField("btnSubmit");
			btnSubmitField.setAccessible(true);
			Button btnSubmit = (Button) btnSubmitField.get(instance);	//实例化该类型
			btnSubmit.setText(R.string.button_normal_sure);

			Field btnCancelField = pickerView.getDeclaredField("btnCancel");
			btnCancelField.setAccessible(true);
			Button btnCancel = (Button) btnCancelField.get(instance);	//实例化该类型
			btnCancel.setText(R.string.button_normal_cancel);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 保存相关设置
	 */
	public void saveSleepTimeSharedPreferences(int indexOfSleepTime) {
		SharedPreferences.Editor mEditor = getContext().getSharedPreferences("config", Context.MODE_PRIVATE).edit();
		mEditor.putInt("screen_sleep_time", indexOfSleepTime);
		mEditor.commit();
	}
	/**
	 * 读取相关设置
	 */
	public int readSleepTimeSharedPreferences() {
		SharedPreferences mSharedPreferences = getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
		return mSharedPreferences.getInt("screen_sleep_time", -1);
	}
	@Override
	public int getTitleId() {
		return R.drawable.about_title;
	}

}
