package com.github.ayelix.deaddrop.androidclient;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ayelix.deaddrop.Drop;

public final class DropView extends LinearLayout {
	private final Context m_context;

	/** Layout parameters for a label. */
	private final LinearLayout.LayoutParams m_labelParams = new LinearLayout.LayoutParams(
			0, LayoutParams.WRAP_CONTENT, 0.3f);
	/** Layout parameters for a value. */
	private final LinearLayout.LayoutParams m_valueParams = new LinearLayout.LayoutParams(
			0, LayoutParams.WRAP_CONTENT, 0.7f);

	public DropView(Context context, AttributeSet attrs) {
		super(context, attrs);

		m_context = context;

		// Complete the layout parameters
		m_labelParams.gravity = Gravity.RIGHT;
		m_labelParams.setMargins(0, 25, 0, 0);
		m_valueParams.gravity = Gravity.LEFT;
		m_valueParams.setMargins(0, 25, 0, 0);

		// Set up size and orientation
		LayoutParams mainParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		this.setOrientation(LinearLayout.VERTICAL);
		this.setLayoutParams(mainParams);
	}

	public void addDrop(final Drop drop) {
		add("Tag ID", drop.getTag());
		add("Data", drop.getData());
		add("Latitude", drop.getLocation().getLatitude().toString());
		add("Longitude", drop.getLocation().getLongitude().toString());
	}

	public void add(String label, String value) {
		if (null == label) {
			label = "";
		}

		if (null == value) {
			value = "";
		}

		// Create a horizontal layout for the label and value
		final LinearLayout rowLayout = new LinearLayout(m_context);
		rowLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		// Create a TextView for the label
		final TextView labelView = new TextView(m_context);
		labelView.setLayoutParams(m_labelParams);
		labelView.setText(label);
		labelView.setTextAppearance(m_context,
				android.R.style.TextAppearance_Medium);
		rowLayout.addView(labelView);

		// Configure and add the value TextView (created when the property
		// was constructed)
		final TextView valueView = new TextView(m_context);
		valueView.setLayoutParams(m_valueParams);
		valueView.setText(value);
		valueView.setHint("None");
		valueView.setTextAppearance(m_context,
				android.R.style.TextAppearance_Medium);
		valueView.setTypeface(null, Typeface.BOLD);
		rowLayout.addView(valueView);

		// Add the row
		this.addView(rowLayout);
	}
}
