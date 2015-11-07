package com.hp.android.haoxin.workview;

import com.hp.android.haoxin.R;
import com.hp.android.haoxin.command.CommandBridge;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class RBListener implements OnTouchListener {

	public interface OnTouchOtherUneableListener {
		/**
		 * @param theTouchingViewId 被触摸的view
		 * @param otherViewEnable 是否允许其他view可点击
		 */
		void onTouchCallBack(int theTouchingViewId, boolean otherViewEnable);
	}
	/**
	 * 正在被touch，其他按键不能touch
	 */
	private OnTouchOtherUneableListener onTouchOtherUneableListener;

	public RBListener() {}
	public RBListener(OnTouchOtherUneableListener listener) {
		onTouchOtherUneableListener = listener;
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		Log.v("RBListener", "onTouch: v = " + v + ", event = " + event);

		int action = event.getAction();
		byte keyMode = -1;
		boolean otherViewEnable;
		switch (action) {
			case MotionEvent.ACTION_DOWN://按下
				keyMode = 0;
				otherViewEnable = false;
				break;
			case MotionEvent.ACTION_CANCEL://取消
			case MotionEvent.ACTION_UP://弹起
				keyMode = 1;
				otherViewEnable = true;
				break;
			default: //ACTION_MOVE
				return true;
		}
		Log.d("RBListener", "onTouch: v = " + v + ", event = " + event);
		if (onTouchOtherUneableListener != null) {
			onTouchOtherUneableListener.onTouchCallBack(v.getId(), otherViewEnable);
		}

		int id = v.getId();
		workForId(id, keyMode);
		return false;
	}

	private void workForId(int id, byte keyMode) {
		byte key = -1;
		byte type = 0;//模式
		switch (id) {
			case R.id.rb_moshi_1:
				key = 0;
				break;
			case R.id.rb_moshi_2:
				key = 1;
				break;
			case R.id.rb_moshi_3:
				key = 2;
				break;
			case R.id.rb_moshi_4:
				key = 3;
				break;
			case R.id.rb_moshi_5:
				key = 4;
				break;
			case R.id.rb_liuliang_1:
				key = 0;
				type = 1;//流量
				break;
			case R.id.rb_liuliang_2:
				key = 1;
				type = 1;
				break;
			case R.id.rb_liuliang_3:
				type = 1;
				key = 2;
				break;
			case R.id.rb_liuliang_4:
				type = 1;
				key = 3;
				break;
			case R.id.rb_liuliang_5:
				type = 1;
				key = 4;
				break;
			default:
				return; //按钮不正确
		}
		if (!(type == 1 && keyMode == 0)) { //当为流量检测时，按下不发送报文（弹起发送）
			CommandBridge.getInstance().linkJianCeChange(type, key, keyMode, mContext);
		}
	}
	private Context mContext;
	public void setContext(Context context) {
		mContext = context;
	}

}
