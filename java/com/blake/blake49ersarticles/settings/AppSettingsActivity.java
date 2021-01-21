package com.blake.blake49ersarticles.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class AppSettingsActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new AppSettingsFragment())
                .commit();
    }
}