package com.github.ayelix.deaddrop.androidclient;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/**
 * Allows user to scan a tag or enter a tag ID manually. This Activity is the
 * entry point for the app.
 * 
 * @author Alex
 * 
 */
public class TagActivity extends Activity {
	private static String TAG = "TagActivity";
	
	private Button m_tagButton;
	private EditText m_tagEditText;
	private Button m_goButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tag);
		
		// Get the views
		m_tagButton = (Button) findViewById(R.id.tagButton);
		m_tagEditText = (EditText) findViewById(R.id.tagEditText);
		m_goButton = (Button) findViewById(R.id.goButton);
		
		// Create the listener for the tag button
		m_tagButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, "Tag button clicked.");
			}
		});
		
		// Create the listener for the tag entry EditText "Go" action
		m_tagEditText.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				boolean handled = false;
				if (EditorInfo.IME_ACTION_GO == actionId) {
					Log.d(TAG, "Keyboard Go button pressed.");
					handled = true;
				}
				return handled;
			}
		});
		
		// Create the listener for the "go" button
		m_goButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.d(TAG, "Go button clicked.");
			}
		});
	}

}
