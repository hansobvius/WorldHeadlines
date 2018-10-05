package com.example.android.worldheadlines;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;

import static com.example.android.worldheadlines.utilitaries.Constants.URL_WEB_KEY;

public class WebActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_web);
        mToolbar.inflateMenu(R.menu.main_menu);
        setSupportActionBar(mToolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) ab.setDisplayHomeAsUpEnabled(true);
        mToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.ice_white), PorterDuff.Mode.SRC_ATOP);

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Intent intent = getIntent();
        intent.hasExtra(URL_WEB_KEY);
        String mUrl = intent.getStringExtra(URL_WEB_KEY);

        WebView webView = (WebView) findViewById(R.id.web_view);
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(mUrl);

        AdView mAdView = (AdView) findViewById(R.id.adView_webView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
