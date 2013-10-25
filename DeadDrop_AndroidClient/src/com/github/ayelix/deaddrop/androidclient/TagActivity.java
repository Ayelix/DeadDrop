package com.github.ayelix.deaddrop.androidclient;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

/**
 * Allows user to scan a tag or enter a tag ID manually. This Activity is the
 * entry point for the app.
 * 
 * @author Alex
 * 
 */
public class TagActivity extends Activity {
	private static final String TAG = "TagActivity";

	private Button m_tagButton;
	private EditText m_tagEditText;
	private Button m_goButton;

	/** Flag set while waiting for an NFC tag to be read. */
	private boolean m_waitForTag = false;

	/** Will hold the default NFC adapter for the device. */
	private NfcAdapter m_nfcAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tag);

		// Get the NFC adapter
		m_nfcAdapter = NfcAdapter.getDefaultAdapter(this);

		// Make sure NFC is present on this device
		if (null == m_nfcAdapter) {
			Toast.makeText(this,
					"Device must support NFC to use this application.",
					Toast.LENGTH_LONG).show();
			finish();
			return;
		}

		// Make sure NFC is enabled
		if (!m_nfcAdapter.isEnabled()) {
			Toast.makeText(this,
					"You must enable NFC to use this application.",
					Toast.LENGTH_LONG).show();
			startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
			finish();
			return;
		}

		// Get the views
		m_tagButton = (Button) findViewById(R.id.tagButton);
		m_tagEditText = (EditText) findViewById(R.id.tagEditText);
		m_goButton = (Button) findViewById(R.id.goButton);

		// Create the listener for the tag button
		m_tagButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// If waiting for a tag, the button will cancel the read
				if (m_waitForTag) {
					stopNFCRead();
				}
				// Otherwise, the button will start a read
				else {
					startNFCRead();
				}
				// Invert the read state flag
				m_waitForTag = !m_waitForTag;
			}
		});

		// Create the listener for the tag entry EditText "Go" action
		m_tagEditText.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
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

	/**
	 * If an NFC read is in progress, restarts the read.
	 */
	@Override
	public void onResume() {
		super.onResume();

		// If an NFC read is supposed to be in progress, restart it
		if (m_waitForTag) {
			startNFCRead();
		}
	}

	/**
	 * Disables the NFC foreground dispatch when the Activity is paused.
	 */
	@Override
	public void onPause() {
		super.onPause();

		// In case the NFC foreground dispatch is enabled, stop it
		stopNFCRead();
	}

	/**
	 * Updates the UI to reflect the NFC read in progress and enables the NFC
	 * foreground dispatch.
	 */
	private void startNFCRead() {
		// Update the button text while waiting for a tag
		m_tagButton.setText(R.string.tagButton_text_stop);

		// Disable the manual tag entry views
		m_tagEditText.setEnabled(false);
		m_goButton.setEnabled(false);

		// Create the PendingIntent for NFC read results
		PendingIntent nfcPendingIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, getClass())
						.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		// Enable a foreground dispatch for all tag types
		m_nfcAdapter.enableForegroundDispatch(this, nfcPendingIntent, null,
				null);
		Log.d(TAG, "NFC foreground dispatch enabled.");
	}

	/**
	 * Updates the UI to reflect no NFC read in progress and disables the NFC
	 * foreground dispatch.
	 */
	private void stopNFCRead() {
		// Update tag button text to indicate its new function
		m_tagButton.setText(R.string.tagButton_text_start);

		// Enable to manual tag entry views
		m_tagEditText.setEnabled(true);
		m_goButton.setEnabled(true);

		// Disable NFC foreground dispatch
		m_nfcAdapter.disableForegroundDispatch(this);
		Log.d(TAG, "NFC foreground dispatch disabled.");
	}

}
