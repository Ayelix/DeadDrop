package com.github.ayelix.deaddrop.androidclient;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public final class SettingsFragment extends PreferenceFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Load preferences
		addPreferencesFromResource(R.xml.preferences);
	}
}
