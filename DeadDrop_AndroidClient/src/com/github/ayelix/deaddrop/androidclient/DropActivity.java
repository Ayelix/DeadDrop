package com.github.ayelix.deaddrop.androidclient;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class DropActivity extends Activity {
	public static final String TAG = "DropActivity";

	/** Request code used when taking an image. */
	private static final int IMAGE_REQUEST_CODE = 1;

	private TextView m_dataTextView;
	private EditText m_dataEditText;
	private TextView m_accuracyTextView;
	private EditText m_accuracyEditText;
	private Button m_imageButton;
	private ImageView m_imageView;
	private Button m_dropButton;

	/** Location to store drop image. */
	private File m_imageFile;

	/** In-memory copy of image. */
	private Bitmap m_image;

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

				// Figure out the scale to limit dimensions to roughly 1000px
				final int scale = Math.max(imageW, imageH) / 1000;

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
}
