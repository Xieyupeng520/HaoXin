package com.hp.android.haoxin.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.hp.android.haoxin.R;

public class ImageButton extends android.widget.ImageButton {
	
	public ImageButton(Context context) {
		super(context);
	}

	public ImageButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		computeScaleRatio(context, attrs);
	}

	public ImageButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		computeScaleRatio(context, attrs);
	}

	private float mRatio = 1; 
	
	private void computeScaleRatio(Context context, AttributeSet attrs){
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ScaleViewRatio);
		float width = ta
				.getInt(R.styleable.ScaleViewRatio_scale_ratio_width, 0);
        float height = ta.getInt(R.styleable.ScaleViewRatio_scale_ratio_height, 0);
        ta.recycle();
        mRatio = (width == 0 || height == 0) ? -1 : height/width;
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		if(mRatio != -1){
			ViewGroup.LayoutParams lParams = getLayoutParams();
			lParams.width = right - left;
			lParams.height = (int)((right - left)*mRatio);
			setLayoutParams(lParams);
			mRatio = -1;
		}
		super.onLayout(changed, left, top, right, bottom);
	}
	
}
