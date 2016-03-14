package com.hp.android.haoxin.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;

import java.io.IOException;

public class SpoutView extends View {
	public static final int DF = 0;
	public static final int A = 1;
	public static final int B = 2;
	public static final int C = 3;
	public static final int E = 4;
	public static final int DR = 5;

	public int mId;
	public Point mAnimPoint;
	public Point mSpoutPoint;

	public Bitmap mSpoutBmp;
	public Bitmap mAnimBmp;
	public Bitmap[] mAnims;

	public int mBaseX;
	public int mBaseY;
	public int mPointX;
	public int mPointY;
	public int mHalfHeight;

	public Paint mPaint;

	private Rect disRect, srcRect, animSrcRect, animDisRect;

	public float mScale;

	public boolean isAnimIn, isAnimShow;

	public long animInTimer, animStartTimer, animInStartTime;

	public long animDelayTimer = 100;

	public long animInTime = 2000;

	public int mCurrentIndex;
	
	public String[] animPaths;

	public SpoutView(Context context, int id) {
		super(context);
		disRect = new Rect();
		srcRect = new Rect();
		mId = id;
	}

	public void initView(int x, int y, int halfH, float scale,String spoutPath, String[] anims) {
		mScale = scale;
		mBaseX = x;
		mBaseY = y;
		mHalfHeight = halfH;

		initAnimPointById();
		initSpoutPointById();

		if(animPaths != null){
			anims = animPaths;
			animPaths = null;
		}
		
		try {
			mSpoutBmp = BitmapFactory.decodeStream(getContext().getAssets()
					.open(spoutPath));
			int w = mSpoutBmp.getWidth();
			int h = mSpoutBmp.getHeight();
			srcRect.set(0, 0, w, h);
			disRect.set(mSpoutPoint.x, mSpoutPoint.y, (int) (mSpoutPoint.x + w
					* scale), (int) (h * scale + mSpoutPoint.y));

			initAnims(anims);
		} catch (IOException e) {
			e.printStackTrace();
		}

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setFilterBitmap(true);
	}

	public void showAnim(boolean isb) {
		isAnimIn = isb;
		isAnimShow = isb;
		if (isb) {
			animStartTimer = System.currentTimeMillis();
			animInStartTime = System.currentTimeMillis();
		} else {
			animStartTimer = -1;
			animInStartTime = -1;
		}
		invalidate();
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
				| Paint.FILTER_BITMAP_FLAG));
		if (mSpoutBmp != null)
			canvas.drawBitmap(mSpoutBmp, srcRect, disRect, null);

		if (isAnimShow) {
			long currentTime = System.currentTimeMillis();
			long useTime = currentTime - animStartTimer;
			if (useTime > animDelayTimer) {
				mCurrentIndex++;
				if (mCurrentIndex == mAnims.length)
					mCurrentIndex = 0;
				animStartTimer = currentTime;
			}
			if (isAnimIn) {
				useTime = currentTime - animInStartTime;
				if (useTime < animInTime) {
					// mAnims[mCurrentIndex].se
				} else {
					isAnimIn = false;
				}
			}
			canvas.drawBitmap(mAnims[mCurrentIndex], animSrcRect, animDisRect,
					null);
		}
		invalidate();
	}

	protected void initAnimPointById() {
		switch (mId) {
		case 0:
			mAnimPoint = new Point((int) (mBaseX + 21 * mScale), (int) (mBaseY
					- mHalfHeight / 5 * mId - 85 * mScale));
			break;
		case 1:
			mAnimPoint = new Point((int) (mBaseX + 21 * mScale), (int) (mBaseY
					- mHalfHeight / 5 * mId - 49 * mScale));
			break;
		case 2:
			mAnimPoint = new Point((int) (mBaseX + 16 * mScale), (int) (mBaseY
					- mHalfHeight / 5 * mId - 41 * mScale));
			break;
		case 3:
			mAnimPoint = new Point((int) (mBaseX + 11 * mScale), (int) (mBaseY
					- mHalfHeight / 5 * mId - 15 * mScale));
			break;
		case 4:
			mAnimPoint = new Point((int) (mBaseX + 9 * mScale), (int) (mBaseY
					- mHalfHeight / 5 * mId + 7 * mScale));
			break;
		case 5:
		case 6:
		default:
			mAnimPoint = new Point((int) (mBaseX + 13 * mScale), (int) (mBaseY
					- mHalfHeight / 5 * mId + 40 * mScale));
			break;
		}
	}

	protected void initSpoutPointById() {
		switch (mId) {
		case 0:
			mSpoutPoint = new Point(mBaseX + mHalfHeight, (int) (mBaseY
					- mHalfHeight / 5 * mId - 4 * mScale));
			break;
		case 1:
			mSpoutPoint = new Point((int) (mBaseX + mHalfHeight - 1 * mScale),
					(int) (mBaseY - mHalfHeight / 5 * mId - 4 * mScale));
			break;
		case 2:
			mSpoutPoint = new Point((int) (mBaseX + mHalfHeight - 7 * mScale),
					(int) (mBaseY - mHalfHeight / 5 * mId - 4 * mScale));
			break;
		case 3:
			mSpoutPoint = new Point((int) (mBaseX + mHalfHeight - 20 * mScale),
					(int) (mBaseY - mHalfHeight / 5 * mId - 4 * mScale));
			break;
		case 4:
			mSpoutPoint = new Point((int) (mBaseX + mHalfHeight - 38 * mScale),
					(int) (mBaseY - mHalfHeight / 5 * mId - 6 * mScale));
			break;
		case 5:
		case 6:
		default:
			mSpoutPoint = new Point((int) (mBaseX + mHalfHeight - 65 * mScale),
					(int) (mBaseY - mHalfHeight / 5 * mId + 2 * mScale));
			break;
		}
	}

	public void changeColor(int id, boolean isClean) {
		initAnimPathsById(id,isClean);
		initAnims(animPaths);
		invalidate();
	}
	
	public void initAnimPathsById(int id, boolean isClean){
		switch (id) {
		case 1:
			if(isClean){
				animPaths = new String[]{"spout/spout_aw_1.png","spout/spout_aw_2.png"};
			}else{
				animPaths = new String[]{"spout/spout_a_1.png","spout/spout_a_2.png"};
			}
			break;
		case 2:
			if(isClean){
				animPaths = new String[]{"spout/spout_bw_1.png","spout/spout_bw_2.png"};
			}else{
				animPaths = new String[]{"spout/spout_b_1.png","spout/spout_b_2.png"};
			}
			break;
		case 3:
		default:
			if(isClean){
				animPaths = new String[]{"spout/spout_cw_1.png","spout/spout_cw_2.png"};
			}else{
				animPaths = new String[]{"spout/spout_c_1.png","spout/spout_c_2.png"};
			}
			break;
		}
	}
	
	public void initAnims(String[] anims){
		mAnims = new Bitmap[anims.length];
		for (int i = 0; i < anims.length; i++) {
			try{
				Bitmap bmp = BitmapFactory.decodeStream(getContext().getAssets().open(anims[i]));
				int width = bmp.getWidth();
				int height = bmp.getHeight();
				if (animSrcRect == null || animDisRect == null) {
					animSrcRect = new Rect(0, 0, width, height);
					animDisRect = new Rect(mAnimPoint.x, mAnimPoint.y,
							(int) (mAnimPoint.x + width * mScale), (int) (height
									* mScale + mAnimPoint.y));
				}
				mAnims[i] = bmp;
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
