package com.hp.android.haoxin.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class HppLog {

	public static boolean DEBUG = true;
	/**
	 * ȫ�־�̬����ֻ��Ҫ{@link #init(Context)}��ʼ��һ�Ρ�������UI����ʾToast��
	 */
	private static Context mContext;

	/**
	 * 
	 * @param context
	 *            ȫ�־�̬����
	 */
	public static void init(Context context) {
		mContext = context;
	}

	// -----------i-------------
	/**
	 * 
	 * @param tag
	 *            ���ڵ�ǰλ�õı�ʶ��һ��Ϊ��ǰ����
	 * @param msg
	 *            ��Ҫ��ʾ����Ϣ
	 */
	public static void i(String tag, String msg) {
		i(tag, msg, null);
	}

	/**
	 * 
	 * @param tag
	 *            ���ڵ�ǰλ�õı�ʶ��һ��Ϊ��ǰ����
	 * @param msg
	 *            ��Ҫ��ʾ����Ϣ
	 * @param context
	 *            ������ʾToast�����Ϊnull����ʾ
	 */
	public static void i(String tag, String msg, Context context) {
		if (DEBUG) {
			Log.i(tag, msg);
			if (context != null) {
				Toast.makeText(context, "i:" + tag + "\n" + msg, Toast.LENGTH_LONG).show();
			}
		}
	}

	/**
	 * 
	 * @param tag
	 *            ���ڵ�ǰλ�õı�ʶ��һ��Ϊ��ǰ����
	 * @param msg
	 *            ��Ҫ��ʾ����Ϣ
	 * @param isShowToast
	 *            ���ڿ����Ƿ���ʾToast��ʹ��ǰ��Ҫ��ʼ��{@link mContext}
	 *            ��true����ʾ��false������ʾ��
	 */
	public static void i(String tag, String msg, boolean isShowToast) {
		if (isShowToast) {
			i(tag, msg, mContext);
		} else {
			i(tag, msg);
		}
	}

	// -----------e-------------
	/**
	 * 
	 * @param tag
	 *            ���ڵ�ǰλ�õı�ʶ��һ��Ϊ��ǰ����
	 * @param msg
	 *            ��Ҫ��ʾ����Ϣ
	 */
	public static void e(String tag, String msg, Exception e) {
		e(tag, msg, e, null);
	}
	
	public static void e(String tag, String msg) {
		e(tag, msg, null, null);
	}

	/**
	 * 
	 * @param tag
	 *            ���ڵ�ǰλ�õı�ʶ��һ��Ϊ��ǰ����
	 * @param msg
	 *            ��Ҫ��ʾ����Ϣ
	 * @param context
	 *            ������ʾToast�����Ϊnull����ʾ
	 */
	public static void e(String tag, String msg, Exception e, Context context) {
//		if (DEBUG) {
		if(e == null){
			Log.e(tag,msg);
		}else{
			Log.e(tag, msg, e);
		}
			if (context != null) {
				Toast.makeText(context, "e:" + tag + "\n" + msg, Toast.LENGTH_LONG).show();
			}
//		}
	}

	/**
	 * 
	 * @param tag
	 *            ���ڵ�ǰλ�õı�ʶ��һ��Ϊ��ǰ����
	 * @param msg
	 *            ��Ҫ��ʾ����Ϣ
	 * @param isShowToast
	 *            ���ڿ����Ƿ���ʾToast��ʹ��ǰ��Ҫ��ʼ��{@link #mContext}
	 *            ��true����ʾ��false������ʾ��
	 */
	public static void e(String tag, String msg, Exception e, boolean isShowToast) {
		if (isShowToast) {
			e(tag, msg, e, mContext);
		} else {
			e(tag, msg, e, null);
		}
	}

	// -----------w-------------
	/**
	 * 
	 * @param tag
	 *            ���ڵ�ǰλ�õı�ʶ��һ��Ϊ��ǰ����
	 * @param msg
	 *            ��Ҫ��ʾ����Ϣ
	 */
	public static void v(String tag, String msg) {
		v(tag, msg, null);
	}

	/**
	 * 
	 * @param tag
	 *            ���ڵ�ǰλ�õı�ʶ��һ��Ϊ��ǰ����
	 * @param msg
	 *            ��Ҫ��ʾ����Ϣ
	 * @param context
	 *            ������ʾToast�����Ϊnull����ʾ
	 */
	public static void v(String tag, String msg, Context context) {
		if (DEBUG) {
			Log.v(tag, msg);
			if (context != null) {
				Toast.makeText(context, "v:" + tag + "\n" + msg, Toast.LENGTH_LONG).show();
			}
		}
	}

	/**
	 * 
	 * @param tag
	 *            ���ڵ�ǰλ�õı�ʶ��һ��Ϊ��ǰ����
	 * @param msg
	 *            ��Ҫ��ʾ����Ϣ
	 * @param isShowToast
	 *            ���ڿ����Ƿ���ʾToast��ʹ��ǰ��Ҫ��ʼ��{@link #mContext}
	 *            ��true����ʾ��false������ʾ��
	 */
	public static void v(String tag, String msg, boolean isShowToast) {
		if (isShowToast) {
			v(tag, msg, mContext);
		} else {
			v(tag, msg);
		}
	}

	// -----------w-------------
	/**
	 * 
	 * @param tag
	 *            ���ڵ�ǰλ�õı�ʶ��һ��Ϊ��ǰ����
	 * @param msg
	 *            ��Ҫ��ʾ����Ϣ
	 */
	public static void w(String tag, String msg,Exception e) {
		w(tag, msg, e, null);
	}
	
	public static void w(String tag, String msg) {
		w(tag, msg, null, null);
	}

	/**
	 * 
	 * @param tag
	 *            ���ڵ�ǰλ�õı�ʶ��һ��Ϊ��ǰ����
	 * @param msg
	 *            ��Ҫ��ʾ����Ϣ
	 * @param context
	 *            ������ʾToast�����Ϊnull����ʾ
	 */
	public static void w(String tag, String msg, Exception e, Context context) {
		if (DEBUG) {
			if(e != null){
				Log.w(tag, msg, e);
			}else{
				Log.w(tag, msg);
			}
			if (context != null) {
				Toast.makeText(context, "w:" + tag + "\n" + msg, Toast.LENGTH_LONG).show();
			}
		}
	}

	/**
	 * 
	 * @param tag
	 *            ���ڵ�ǰλ�õı�ʶ��һ��Ϊ��ǰ����
	 * @param msg
	 *            ��Ҫ��ʾ����Ϣ
	 * @param isShowToast
	 *            ���ڿ����Ƿ���ʾToast��ʹ��ǰ��Ҫ��ʼ��{@link #mContext}
	 *            ��true����ʾ��false������ʾ��
	 */
	public static void w(String tag, String msg, Exception e,boolean isShowToast) {
		if (isShowToast) {
			w(tag, msg, e,mContext);
		} else {
			w(tag, msg, e,null);
		}
	}
	
	
	//------------------d--------------------
	public static void d(String tag, String msg) {
		d(tag, msg, null);
	}

	public static void d(String tag, String msg, Context context) {
		if (DEBUG) {
			Log.d(tag, msg);
			if (context != null) {
				Toast.makeText(context, "d:" + tag + "\n" + msg, Toast.LENGTH_LONG).show();
			}
		}
	}

	public static void d(String tag, String msg, boolean isShowToast) {
		if (isShowToast) {
			d(tag, msg, mContext);
		} else {
			d(tag, msg);
		}
	}

}
