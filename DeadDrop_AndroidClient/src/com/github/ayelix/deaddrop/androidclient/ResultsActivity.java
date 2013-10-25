package com.github.ayelix.deaddrop.androidclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;

import com.github.ayelix.deaddrop.Drop;
import com.github.ayelix.deaddrop.JSONParser;

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
		if (action.equals(Constants.ACTION_DROP)) {
			throw new UnsupportedOperationException(
					"Drop action not implemented yet.");
		} else if (action.equals(Constants.ACTION_PICKUP)) {
			// Get the extras
			Bundle extras = intent.getExtras();
			if (extras != null) {
				// Make sure enough extras are provided
				if (extras.size() >= 3) {
					final String idStr = intent.getExtras().getString(
							Constants.EXTRA_ID);
					final String latStr = intent.getExtras().getString(
							Constants.EXTRA_LAT);
					final String lonStr = intent.getExtras().getString(
							Constants.EXTRA_LON);

					// Make sure all extras were received
					if ((null == idStr) || (null == latStr) || (null == lonStr)) {
						Log.e(TAG, "One or more extras unavailable.");
						finish();
						return;
					}

					// Start the pickup attempt
					startPickup(idStr, latStr, lonStr);

				} else {
					Log.e(TAG, "Not enough extras provided with intent: "
							+ String.valueOf(extras.size()));
					finish();
					return;
				}

			} else {
				Log.e(TAG, "No extras provided with intent.");
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
				JSONObject respObj = new JSONObject();
				respObj.put("tag", idStr);
				respObj.put("lat", lat);
				respObj.put("long", lon);

				// Build the URI for the request
				final String uri = Constants.DEFAULT_SERVER_ADDR
						+ Constants.PICKUP_PATH;
				Log.d(TAG, "PickupTask requesting: " + uri);

				// Get the HTTP client and request
				final HttpClient client = new DefaultHttpClient();
				final HttpPost request = new HttpPost(uri);

				// Write the JSON string to the request
				request.setHeader("Content-Type", "application/json");
				try {
					request.setEntity(new StringEntity(respObj.toJSONString()));
				} catch (UnsupportedEncodingException e1) {
					Log.e(TAG, "Error writing POST.");
					e1.printStackTrace();
				}

				try {
					// Get the response
					final HttpResponse response = client.execute(request);
					BufferedReader responseReader = new BufferedReader(
							new InputStreamReader(response.getEntity()
									.getContent()));

					// Get the status code
					final int responseStatus = response.getStatusLine()
							.getStatusCode();

					/*
					 * // Get the results as a string String line = new
					 * String(); StringBuilder sb = new StringBuilder(); while
					 * ((line = responseReader.readLine()) != null) {
					 * sb.append(line + "\n"); } final String responseStr =
					 * sb.toString();
					 */

					// Create a JSONObject to parse to results
					JSONObject obj = (JSONObject) JSONValue
							.parse(responseReader);

					// Check the status code
					if (200 == responseStatus) {
						final Drop result = JSONParser.parseDrop(obj);
						final Double distance = JSONParser.parseDouble(obj,
								"distance");

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

				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
