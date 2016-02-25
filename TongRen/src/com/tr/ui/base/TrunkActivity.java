package com.tr.ui.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;



public abstract class TrunkActivity extends Activity {
	
	protected LayoutInflater inflater;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflater = LayoutInflater.from(this);
	}
	
}
