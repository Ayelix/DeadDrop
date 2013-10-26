package com.github.ayelix.deaddrop.androidclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class DropActivity extends Activity {
	public static final String TAG = "DropActivity";

	private TextView m_dataTextView;
	private EditText m_dataEditText;
	private TextView m_accuracyTextView;
	private EditText m_accuracyEditText;
	private Button m_imageButton;
	private ImageView m_imageView;
	private Button m_dropButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drop);

		// Get the views
		m_dataTextView = (TextView) findViewById(R.id.dataTextView);
		m_dataEditText = (EditText) findViewById(R.id.dataEditText);
		m_accuracyTextView = (TextView) findViewById(R.id.accuracyTextView);
		m_accuracyEditText = (EditText) findViewById(R.id.accuracyEditText);
		m_imageButton = (Button) findViewById(R.id.imageButton);
		m_imageView = (ImageView) findViewById(R.id.imageView);
		m_dropButton = (Button) findViewById(R.id.dropButton);

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
		if (!action.equals(Constants.ACTION_DROP)) {
			Log.e(TAG, "Invalid action: " + action);
			finish();
			return;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.drop, menu);
		return true;
	}

}
