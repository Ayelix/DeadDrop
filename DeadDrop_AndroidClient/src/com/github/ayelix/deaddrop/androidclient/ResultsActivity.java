package com.github.ayelix.deaddrop.androidclient;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class ResultsActivity extends Activity {
	private static final String TAG = "ResultsActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_results);

		// Get the intent that started the activity
		final Intent intent = getIntent();

		// Get the intent action
		final String action = intent.getAction();

		// An action must be specified
		if (null == action) {
			Log.e(TAG, "Unspecified action.");
			finish();
			return;
		}

		// The action must be one that this activity handles
		if (action.equals(IntentConstants.ACTION_DROP)) {
			throw new UnsupportedOperationException(
					"Drop action not implemented yet.");
		} else if (action.equals(IntentConstants.ACTION_PICKUP)) {
			startPickup();
		} else {
			Log.e(TAG, "Invalid action: " + action);
			finish();
			return;
		}
	}

	/**
	 * Starts the HTTP request for a pickup attempt and performs associated
	 * operations.
	 */
	private void startPickup() {

	}
}
