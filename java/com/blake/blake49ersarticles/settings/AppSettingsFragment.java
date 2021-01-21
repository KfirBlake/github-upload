package com.blake.blake49ersarticles.settings;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.blake.blake49ersarticles.R;

/**
 * Created by Kfir Blake on 24/10/2020.
 */
public class AppSettingsFragment extends PreferenceFragmentCompat
{
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey)
    {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}
