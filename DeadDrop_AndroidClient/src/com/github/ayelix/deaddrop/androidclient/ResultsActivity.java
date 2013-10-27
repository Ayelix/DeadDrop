package com.github.ayelix.deaddrop.androidclient;

import org.json.simple.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.github.ayelix.deaddrop.Drop;
import com.github.ayelix.deaddrop.JSONParser;

public class ResultsActivity extends Activity {
	private static final String TAG = "ResultsActivity";
	
	private DropView m_dropView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_results);
		
		// Get the views
		m_dropView = (DropView) findViewById(R.id.DropView);

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

		// Get the extras
		Bundle extras = intent.getExtras();
		if (extras == null) {
			Log.e(TAG, "No extras provided with intent.");
			finish();
			return;
		}

		// The action must be one that this activity handles
		if (action.equals(Constants.ACTION_DROP)) {
			// Save the extras
			final String id = (String) extras.get(Constants.EXTRA_ID);
			final String data = (String) extras.get(Constants.EXTRA_DATA);
			final Double lat = (Double) extras.get(Constants.EXTRA_LAT);
			final Double lon = (Double) extras.get(Constants.EXTRA_LON);
			final Double accuracy = (Double) extras
					.get(Constants.EXTRA_ACCURACY);
			final String image = (String) extras.get(Constants.EXTRA_IMAGE);
			
			// Pass the values on to the DropView
			m_dropView.add("Tag ID", id);
			m_dropView.add("Data", data);
			m_dropView.add("Latitude", lat.toString());
			m_dropView.add("Longitude", lon.toString());
			m_dropView.add("Required Accuracy (mi)", accuracy.toString());
			m_dropView.addImage(image);
		} else if (action.equals(Constants.ACTION_PICKUP)) {
			// Make sure enough extras are provided
			if (extras.size() >= 3) {
				final String idStr = extras.getString(Constants.EXTRA_ID);
				final String latStr = extras.getString(Constants.EXTRA_LAT);
				final String lonStr = extras.getString(Constants.EXTRA_LON);

				// Make sure all extras were received
				if ((null == idStr) || (null == latStr) || (null == lonStr)) {
					Log.e(TAG, "One or more extras unavailable.");
					finish();
					return;
				}

				// Start the pickup attempt
				startPickup(idStr, latStr, lonStr);

			} else {
				Log.e(TAG,
						"Not enough extras provided with intent: "
								+ String.valueOf(extras.size()));
				finish();
				return;
			}

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
	private void startPickup(final String idStr, final String lat,
			final String lon) {
		new PickupTask().execute(idStr, lat, lon);
		Log.d(TAG, "PickupTask executed.");
	}

	/**
	 * Handles request and response for an attempted pickup.
	 * 
	 * @author Alex
	 * 
	 */
	private class PickupTask extends
			AsyncTask<String, Void, Pair<Integer, String>> {
		@Override
		protected Pair<Integer, String> doInBackground(String... params) {
			Pair<Integer, String> retVal = null;

			if (3 == params.length) {
				// Parse the parameters
				final String idStr = params[0];
				final Double lat = Double.parseDouble(params[1]);
				final Double lon = Double.parseDouble(params[2]);

				// Create a JSON object with the parameters
				JSONObject reqObj = new JSONObject();
				reqObj.put("tag", idStr);
				reqObj.put("lat", lat);
				reqObj.put("long", lon);

				// Build the URI for the request
				final String uri = Constants.DEFAULT_SERVER_ADDR
						+ Constants.PICKUP_PATH;
				Log.d(TAG, "PickupTask requesting: " + uri);

				// Build and execute the request
				JSONPost post = new JSONPost(uri, reqObj);
				if (post.execute()) {
					// Get the status code and JSON results
					final int postStatus = post.getStatus();
					final JSONObject obj = post.getJSON();

					// Check the status code
					if (200 == postStatus) {
						final Drop result = JSONParser.parseDrop(obj, true);
						final Double distance = JSONParser.parseDouble(obj,
								"distance", false);

						final String distanceStr = (null == distance) ? ("None provided.")
								: (String.valueOf(distance));
						Log.d(TAG,
								"Pickup succeeded, data: " + result.getData()
										+ ", image: " + result.getImage()
										+ ", distance: " + distanceStr);
					} else {
						String status = JSONParser.parseString(obj, "status");
						if (null == status)
							status = "No error status available";
						Log.d(TAG, "Pickup failed, status: " + status);
					}
				} else {
					Log.e(TAG, "Error posting pickup request.");
				}

			} else {
				Log.e(TAG,
						"PickupTask started with incorrect parameter count: "
								+ String.valueOf(params.length));
			}

			return retVal;
		}
	}
}
