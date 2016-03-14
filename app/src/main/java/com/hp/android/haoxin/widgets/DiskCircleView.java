package com.hp.android.haoxin.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 染色过程中中间会变色的圆盘View
 */
public class DiskCircleView extends View {

	// private float mX; // 圆心横坐标
	// private float mY; // 圆心纵坐标
	private float mRadius; // 半径
	private int mAlpha = 0;

	private long mStartAnimTime = -1;
	private float mDuralyTime = -1;
	private float mFromAlpha;
	private float mToAlpha;

	private Paint mPaint;

	// private Paint mPaint;
	private int mCenterX = 0;
	private int mCenterY = 0;

	public DiskCircleView(Context context) {
		super(context);
		initPaint();
	}

	public DiskCircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		computeScaleRatio(context, attrs);
		initPaint();
	}

	public DiskCircleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		computeScaleRatio(context, attrs);
		initPaint();
	}

	private void computeScaleRatio(Context context, AttributeSet attrs) {
//		int[] attrsArray = new int[] { android.R.attr.id, // 0
//				android.R.attr.layout_width, // 1
//				android.R.attr.layout_height // 2
//		};
//		TypedArray ta = context.obtainStyledAttributes(attrs, attrsArray);
		//int id = ta.getResourceId(0, View.NO_ID);
		//int layout_width = ta.getDimensionPixelSize(1, ViewGroup.LayoutParams.MATCH_PARENT);
		//int layout_height = ta.getDimensionPixelSize(2, ViewGroup.LayoutParams.MATCH_PARENT);
		//int layout_width_dp = DensityUtil.px2dip(context, layout_width);
		//int layout_height_dp = DensityUtil.px2dip(context, layout_height);
//		ta.recycle();
		
		//mViewWidth = layout_width;
		//mViewHeight = layout_height;
		
//		DisplayMetrics metrics = new DisplayMetrics();
//		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//		wm.getDefaultDisplay().getMetrics(metrics);
//		int screenWidth = metrics.heightPixels;
//		int screenHeight = metrics.widthPixels;		
	}

	private void initPaint() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true); //抗锯齿
		mPaint.setColor(Color.TRANSPARENT);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// mX = widthMeasureSpec/2;
		// mY = heightMeasureSpec/2;
		// mRadius = mViewWidth / 2; // 200;
	}

	/**
	 * 确定圆的位置和大小
	 * @param centerX 中心X
	 * @param centerY 中心Y
	 * @param diameter 直径
	 */
	public void initLayout(int centerX,int centerY, int diameter){
		mCenterX = centerX;
		mCenterY = centerY;
		mRadius = diameter / 2 - 60;
	}

	public void setColor(int color) {
		mDuralyTime = -1;
		mPaint.setColor(color);
		/*
		 * if(color >> 24 == 0){ mPaint.setAlpha(0xff); }
		 */
		setVisibility(VISIBLE);
		invalidate();
	}

	@Override
	protected void onDetachedFromWindow() {
		mAlpha = 0;
		mPaint.setAlpha(mAlpha);
		super.onDetachedFromWindow();
	}

	public void startAnimation(float duration, float fromAlpha, float toAlpha) {
		mDuralyTime = duration;
		mFromAlpha = fromAlpha;
		mToAlpha = toAlpha;
		mStartAnimTime = System.currentTimeMillis();
		invalidate();
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mDuralyTime != -1) {
			long currentTime = System.currentTimeMillis();
			long useTime = currentTime - mStartAnimTime;
			if (useTime > mDuralyTime) {
				mDuralyTime = -1;
				mPaint.setAlpha((int) mToAlpha);
			} else {
				float cha = (mToAlpha - mFromAlpha) / mDuralyTime * (useTime);
				mAlpha = (int) (mFromAlpha + cha);
				mPaint.setAlpha(mAlpha);
			}
			// canvas.drawCircle(230, 230, mRadius, mPaint);
			canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);
			invalidate();
		} else {
			// canvas.drawCircle(230, 230, mRadius, mPaint);
			canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);
		}
	}
}
