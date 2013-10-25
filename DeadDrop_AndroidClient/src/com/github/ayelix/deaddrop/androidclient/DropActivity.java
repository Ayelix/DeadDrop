package com.github.ayelix.deaddrop.androidclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class DropActivity extends Activity {
	public static final String TAG = "DropActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drop);

		// Get the intent that started the activity
		Intent intent = getIntent();
		Log.d(TAG, intent.getAction());
		Log.d(TAG, String.valueOf(intent.getExtras().size()));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.drop, menu);
		return true;
	}

}
