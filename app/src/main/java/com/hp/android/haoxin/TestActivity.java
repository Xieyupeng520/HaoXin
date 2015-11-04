package com.hp.android.haoxin;

import com.hp.android.haoxin.workview.WorkCleanView;

import android.app.Activity;
import android.os.Bundle;

public class TestActivity extends Activity{

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		

		setContentView(new WorkCleanView(this));
	}
}
