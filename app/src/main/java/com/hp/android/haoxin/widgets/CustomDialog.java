package com.hp.android.haoxin.widgets;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.hp.android.haoxin.R;

public class CustomDialog {

	public enum CustomDialogType {
		CustomDialogTypeException,
		CustomDialogTypeHint
	}

	Context mContext;
	android.app.AlertDialog mAlertDialog;
	TextView mTitleView;
	/**
	 * 额外信息（【提示/异常】）
	 */
	TextView mExtraMsgView;
	TextView mMsgView;
	/**
	 * 提示信息（最下面的小字）
	 */
	TextView mHintMsgView;
	Button mBtnSure;
	Button mBtnCancel;

	/**
	 * 【异常】
	 */
	public static final int EXTRA_MSG_EXCEPTION = R.string.exception_exception;
	/**
	 * 【提示】
	 */
	public static final int EXTRA_MSG_HINT = R.string.exception_hint;
	/**
	 * 构造函数
	 * @param context 上下文
	 */
	public CustomDialog(Context context) {
		this.mContext=context;
		mAlertDialog=new android.app.AlertDialog.Builder(context).create();
		mAlertDialog.show();
		//关键在下面的两行,使用window.setContentView,替换整个对话框窗口的布局
		Window window = mAlertDialog.getWindow();
		window.setContentView(R.layout.dialog_haoxin);
		//titleView=(TextView)window.findViewById(R.id.dialog_msg);
		mExtraMsgView = (TextView) window.findViewById(R.id.extra_dialog_msg);
		mMsgView=(TextView)window.findViewById(R.id.dialog_msg);
		mHintMsgView = (TextView) window.findViewById(R.id.hint_dialog_msg);
		mBtnSure = (Button) window.findViewById(R.id.btn_dialog_sure);
		mBtnCancel = (Button) window.findViewById(R.id.btn_dialog_cancel);
		//buttonLayout=(LinearLayout)window.findViewById(R.id.dialog_btn_linear);
	}
	
	public void setCanceledOnTouchOutside(boolean cancel){
		mAlertDialog.setCanceledOnTouchOutside(cancel);
	}
	
	public void setTitle(int resId)
	{
		mTitleView.setText(resId);
	}
	public void setTitle(String title) {
		mTitleView.setText(title);
	}


	/**
	 * 设置额外信息 见{@link #mExtraMsgView}
	 * @param resId 额外信息索引
	 */
	public void setExtraMsg(int resId) {
		mExtraMsgView.setVisibility(View.VISIBLE); //先设置可见（默认不可见）
		mExtraMsgView.setText(resId);
		//设置颜色
//		switch (resId) {
//			case EXTRA_MSG_EXCEPTION:
//				mExtraMsgView.setTextColor(mContext.getResources().getColor(R.color.dialog_extra_msg_exception));
//				break;
//			case EXTRA_MSG_HINT:
//				mExtraMsgView.setTextColor(mContext.getResources().getColor(R.color.dialog_extra_msg_hint));
//				break;
//		}
	}
	/**
	 * 设置额外信息 见{@link #mHintMsgView}
	 * @param resId 提示信息索引
	 */
	public void setHintMsg(int resId) {
		mHintMsgView.setVisibility(View.VISIBLE); //先设置可见（默认不可见）
		mHintMsgView.setText(resId);
	}
	/**
	 * 设置主要显示信息
	 * @param resId 信息资源id
	 */
	public void setMessage(int resId) {
		mMsgView.setText(resId);
	}

	/**
	 * 设置主要显示信息
	 * @param message 信息
	 */
	public void setMessage(String message)
	{
		mMsgView.setText(message);
	}
	/**
	 * 设置按钮
	 * @param text
	 * @param listener
	 */
	public void setPositiveButton(String text,final View.OnClickListener listener)
	{
		mBtnSure.setText(text);
		mBtnSure.setOnClickListener(listener);
	}
 
	/**
	 * 设置按钮
	 * @param text
	 * @param listener
	 */
	public void setNegativeButton(String text,final View.OnClickListener listener)
	{
		mBtnCancel.setText(text);
		mBtnCancel.setOnClickListener(listener);
	}
	
	public void setPositiveListener(View.OnClickListener listener){
		mBtnSure.setOnClickListener(listener);
	}
	
	public void setNegativeListener(View.OnClickListener listener){
		mBtnCancel.setOnClickListener(listener);
	}

	/**
	 * 关闭对话框
	 */
	public void dismiss() {
		mAlertDialog.dismiss();
	}

	public void setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
		mAlertDialog.setOnCancelListener(onCancelListener);
	}

	/**
	 * 设置取消按钮是否可见，默认可见
	 * @param visibilty View.VISIBLE & View.GONE
	 */
	public void setCancelBtnVisibilty(int visibilty) {
		mBtnCancel.setVisibility(visibilty);
	}

	/**
	 * 根据类型创建提示框
	 * @param context 上下文
	 * @param msgResId 提示资源id
	 * @param type 提示框类型
	 * @return
	 */
	public static CustomDialog createDialogByType(Context context, int msgResId, CustomDialogType type) {
		switch (type) {
			case CustomDialogTypeException:
				return createExceptionDialog(context, msgResId);
			case CustomDialogTypeHint:
			default:
				return createHintDialog(context, msgResId);
		}
	}
	/**
	 * 创建自定义异常提示框
	 * @param context 上下文
	 * @param msgResId 提示资源id
	 * @return CustomDialog
	 */
	public static CustomDialog createExceptionDialog(Context context, int msgResId) {
		CustomDialog exceptionDialog = createDialog(context, msgResId);
		exceptionDialog.setExtraMsg(EXTRA_MSG_EXCEPTION);
		exceptionDialog.setCancelBtnVisibilty(View.GONE);
		return exceptionDialog;
	}

	/**
	 * 创建自定义提示框
	 * @param context 上下文
	 * @param msgResId 提示资源id
	 * @return CustomDialog
	 */
	public static CustomDialog createHintDialog(Context context, int msgResId) {
		CustomDialog hintDialog = createDialog(context, msgResId);
		hintDialog.setExtraMsg(EXTRA_MSG_HINT);
		return hintDialog;
	}
	//重载
	public static CustomDialog createHintDialog(Context context, String msg) {
		CustomDialog hintDialog = createDialog(context, msg);
		hintDialog.setExtraMsg(EXTRA_MSG_HINT);
		return hintDialog;
	}
	/**
	 * 创建自定义提示框
	 * @param context 上下文
	 * @param msgResId 提示资源id ,使用 {@link #EXTRA_MSG_HINT} 或者 {@link #EXTRA_MSG_EXCEPTION}
	 * @return CustomDialog
	 */
	public static CustomDialog createDialog(Context context, int msgResId) {
		CustomDialog dialog = new CustomDialog(context);
		dialog.setMessage(msgResId);
		dialog.setCanceledOnTouchOutside(false);
		return dialog;
	}
	//重载
	public static CustomDialog createDialog(Context context, String msg) {
		CustomDialog dialog = new CustomDialog(context);
		dialog.setMessage(msg);
		dialog.setCanceledOnTouchOutside(false);
		return dialog;
	}
}
