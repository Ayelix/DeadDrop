package com.github.ayelix.deaddrop.androidclient;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Vibrator;
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

	/** List of NFC intent action strings that this activity will handle. */
	private static final List<String> NFC_ACTION_LIST = Arrays
			.asList(NfcAdapter.ACTION_NDEF_DISCOVERED,
					NfcAdapter.ACTION_TAG_DISCOVERED,
					NfcAdapter.ACTION_TECH_DISCOVERED);

	/** Vibration pattern for NFC read feedback. */
	private static final long[] VIBRATE_PATTERN = new long[] { 0, 150, 0, 300 };

	private Button m_tagButton;
	private EditText m_tagEditText;
	private Button m_goButton;

	/** Flag set while waiting for an NFC tag to be read. */
	private boolean m_waitForTag = false;

	/** Will hold the default NFC adapter for the device. */
	private NfcAdapter m_nfcAdapter;

	/** Will hold the vibrator for this device. */
	private Vibrator m_vibrator;

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

		// Get the vibrator
		m_vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);

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
					readTagFromUI();
					handled = true;
				}
				return handled;
			}
		});

		// Create the listener for the "go" button
		m_goButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				readTagFromUI();
			}
		});
	}

	@Override
	public void onNewIntent(final Intent intent) {
		super.onNewIntent(intent);

		// Make sure it's an NFC intent that should be handled
		final String action = intent.getAction();
		if (NFC_ACTION_LIST.contains(action)) {
			// Get the scanned tag's ID in a String format
			final Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			final String tagIDStr = Arrays.toString(tag.getId());

			// Clear the flag to indicate the read is complete
			m_waitForTag = false;

			// Move to the next activity
			tagReady(tagIDStr);
		}
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
		// If not, stop it in case it's running
		else {
			stopNFCRead();
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
	 * Launches the appropriate activity after a tag is read.
	 * 
	 * @param tagIDStr
	 *            ID of the tag in String format.
	 */
	private void tagReady(final String tagIDStr) {
		Log.d(TAG, "Scanned tag with ID: " + tagIDStr);

		// Provide vibration feedback for a successful read
		startFeedback();
	}

	/**
	 * Reads a tag ID entered in the UI as if it were from a scanned NFC tag.
	 */
	private void readTagFromUI() {
		String tagIDStr = m_tagEditText.getText().toString();
		
		// Ignore a blank or whitespace-only ID
		if (!tagIDStr.isEmpty() && !tagIDStr.trim().isEmpty()) {
			tagReady(tagIDStr);
		}
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

	/**
	 * Starts the vibration feedback.
	 */
	private void startFeedback() {
		m_vibrator.vibrate(VIBRATE_PATTERN, -1);
	}

}
