package com.blake.blake49ersarticles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebSiteActiviy extends AppCompatActivity
{

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_site_activiy);

        Intent intent = getIntent();
        String url = intent.getStringExtra("URL");
        webView = findViewById(R.id.ArticelWebView);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient());
    }
}