package com.github.ayelix.deaddrop.androidclient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.json.simple.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.ayelix.deaddrop.Drop;
import com.github.ayelix.deaddrop.JSONParser;

public class DropActivity extends Activity {
	public static final String TAG = "DropActivity";

	/** Request code used when taking an image. */
	private static final int IMAGE_REQUEST_CODE = 1;

	private EditText m_dataEditText;
	private EditText m_accuracyEditText;
	private Button m_imageButton;
	private ImageView m_imageView;
	private Button m_dropButton;

	/** Tag ID provided by TagActivity. */
	private String m_id;
	/** Latitude provided by TagActivity. */
	private String m_lat;
	/** Longitude provided by TagActivity. */
	private String m_lon;

	/** Location to store drop image. */
	private File m_imageFile;

	/** In-memory copy of image. */
	private Bitmap m_image;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drop);

		// Get the views
		m_dataEditText = (EditText) findViewById(R.id.dataEditText);
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

		// Make sure enough extras are provided
		Bundle extras = intent.getExtras();
		if (extras.size() >= 3) {
			// Save the extras
			m_id = intent.getExtras().getString(Constants.EXTRA_ID);
			m_lat = intent.getExtras().getString(Constants.EXTRA_LAT);
			m_lon = intent.getExtras().getString(Constants.EXTRA_LON);

			// Make sure all extras were received
			if ((null == m_id) || (null == m_lat) || (null == m_lon)) {
				Log.e(TAG, "One or more extras unavailable.");
				finish();
				return;
			}

		} else {
			Log.e(TAG,
					"Not enough extras provided with intent: "
							+ String.valueOf(extras.size()));
			finish();
			return;
		}

		// Get the location for the image file
		m_imageFile = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"DeadDrop");
		m_imageFile.mkdirs();
		m_imageFile = new File(m_imageFile, "drop_image.jpeg");

		// Create image button listener
		m_imageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// Launch a camera app to take a picture
				Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				imageIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(m_imageFile));
				startActivityForResult(imageIntent, IMAGE_REQUEST_CODE);
			}
		});

		// Create drop button listener
		m_dropButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Start the drop attempt
				startDrop();
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Only handle known request codes
		if (IMAGE_REQUEST_CODE == requestCode) {
			// Only handle successful image captures
			if (RESULT_OK == resultCode) {
				// Get the path to the saved image
				final String imagePath = m_imageFile.toString();

				// Read the image size
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(m_imageFile.toString(), options);
				final int imageW = options.outWidth;
				final int imageH = options.outHeight;

				// Figure out the scale to limit dimensions to roughly 500px
				final int scale = Math.max(imageW, imageH) / 500;

				// Read the image, scaled if needed
				options.inJustDecodeBounds = false;
				options.inSampleSize = scale;
				options.inPurgeable = true;
				m_image = BitmapFactory.decodeFile(imagePath, options);

				// See if the image needs to be rotated
				int rotation = 0;
				try {
					ExifInterface exif = new ExifInterface(imagePath);
					final int orientation = exif.getAttributeInt(
							ExifInterface.TAG_ORIENTATION, 100);
					switch (orientation) {
					case ExifInterface.ORIENTATION_NORMAL:
						rotation = 0;
						break;
					case ExifInterface.ORIENTATION_ROTATE_90:
						rotation = 90;
						break;
					case ExifInterface.ORIENTATION_ROTATE_180:
						rotation = 180;
						break;
					case ExifInterface.ORIENTATION_ROTATE_270:
						rotation = 270;
						break;
					default:
						Log.d(TAG, String.format("Unknown orientation: %d",
								orientation));
					}
				} catch (IOException e) {
					// Just let the image possibly be rotated incorrectly
					Log.e(TAG, "Error reading EXIF data from image.");
				}

				// Rotate the image - only use up the memory from calling
				// createBitmap if actually rotating
				if (rotation != 0) {
					Log.d(TAG, String.format("Rotating %d degrees", rotation));
					final Matrix matrix = new Matrix();
					matrix.postRotate(rotation);
					m_image = Bitmap.createBitmap(m_image, 0, 0,
							m_image.getWidth(), m_image.getHeight(), matrix,
							true);
				}

				// Update the ImageView
				m_imageView.setImageBitmap(m_image);

				// Delete the file
				m_imageFile.delete();
			}
		}
	}

	private void startDrop() {
		// Make sure data was entered
		final String data = m_dataEditText.getText().toString();
		if (data.isEmpty() || data.trim().isEmpty()) {
			Toast.makeText(this,
					"Error creating drop - you must enter some data!",
					Toast.LENGTH_LONG).show();
			return;
		}

		// Convert number Strings to Doubles
		Double lat, lon, accuracy;
		try {
			lat = Double.valueOf(m_lat);
			lon = Double.valueOf(m_lon);
		} catch (NumberFormatException nfe) {
			Toast.makeText(this,
					"Error creating drop - invalid location coordinates.",
					Toast.LENGTH_LONG).show();
			Log.e(TAG, String.format(
					"Error parsing latitude and longitude: %s, %s", m_lat,
					m_lon));
			return;
		}
		try {
			accuracy = Double.valueOf(m_accuracyEditText.getText().toString());
		} catch (NumberFormatException nfe) {
			accuracy = 1.0;
			Toast.makeText(
					this,
					String.format("Using default accuracy of %f mi.", accuracy),
					Toast.LENGTH_LONG).show();
		}

		// Create a Location from the lat/long values
		final com.github.ayelix.deaddrop.Location location = new com.github.ayelix.deaddrop.Location(
				lat, lon);

		// Create a Drop without the image - that will be encoded in the
		// DropTask
		Drop drop = new Drop(m_id, data, location, accuracy, null);

		// Disable the drop button while a drop is in progress
		m_dropButton.setEnabled(false);

		// Start a DropTask with the Drop
		new DropTask().execute(drop);
	}

	private class DropTask extends AsyncTask<Drop, Void, Drop> {
		private int m_status;
		private String m_statusString;

		@Override
		protected Drop doInBackground(Drop... params) {
			Drop retVal = null;

			// Get the parameter
			if (1 == params.length) {
				final Drop drop = params[0];

				// Encode the image as a Base64 String (if an image is present)
				String imageStr = null;
				if (m_image != null) {
					final ByteArrayOutputStream s = new ByteArrayOutputStream();
					m_image.compress(Bitmap.CompressFormat.PNG, 100, s);
					imageStr = Base64.encodeToString(s.toByteArray(),
							Base64.DEFAULT);
					Log.d(TAG,
							String.format("Image string length: %d",
									imageStr.length()));
				}

				// Save the image to the Drop
				drop.setImage(imageStr);

				// Write the Drop to a JSON object
				JSONObject reqObj = new JSONObject();
				JSONParser.writeDrop(drop, reqObj);

				// Build the URI for the request
				final String uri = Constants.DEFAULT_SERVER_ADDR
						+ Constants.DROP_PATH;
				Log.d(TAG, "DropTask requesting: " + uri);

				// Build and execute the request
				JSONPost post = new JSONPost(uri, reqObj);
				if (post.execute()) {
					// Get the status code and JSON results
					m_status = post.getStatus();
					final JSONObject obj = post.getJSON();

					// Parse the status string from the response
					m_statusString = JSONParser.parseString(obj, "status");
					if (null == m_statusString)
						m_statusString = "No status available";

					retVal = drop;

				} else {
					Log.e(TAG, "Drop failed, POST error.");
				}
			}

			return retVal;
		}

		@Override
		protected void onPostExecute(Drop result) {
			// Drop complete, reenable the button
			m_dropButton.setEnabled(true);

			// Check the response status code
			if (200 == m_status) {
				// Create and populate an intent to display the results
				Intent resultsIntent = new Intent(getApplicationContext(), ResultsActivity.class);
				resultsIntent.setAction(Constants.ACTION_DROP);
				resultsIntent.putExtra(Constants.EXTRA_ID, result.getTag());
				resultsIntent.putExtra(Constants.EXTRA_DATA, result.getData());
				resultsIntent.putExtra(Constants.EXTRA_LAT, result.getLocation().getLatitude());
				resultsIntent.putExtra(Constants.EXTRA_LON, result.getLocation().getLongitude());
				resultsIntent.putExtra(Constants.EXTRA_ACCURACY, result.getLocationAccuracy());
				resultsIntent.putExtra(Constants.EXTRA_IMAGE, result.getImage());
				
				// Start the activity to display the results
				startActivity(resultsIntent);
			} else {
				Toast.makeText(getApplicationContext(),
						"Drop failed,  status: " + m_statusString,
						Toast.LENGTH_LONG).show();
				Log.d(TAG, "Drop failed, status: " + m_statusString);
			}
		}

	} // End class DropTask
}
