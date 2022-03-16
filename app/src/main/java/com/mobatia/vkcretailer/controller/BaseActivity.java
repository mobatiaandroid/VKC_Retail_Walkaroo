package com.mobatia.vkcretailer.controller;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends Activity implements JSONParser {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(getLayoutResourceId());
	}

	protected abstract int getLayoutResourceId();
}
