package com.appdazzle_innovations.testingcenter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class ViewLinkActivity extends AppCompatActivity {
    private String centerID;
    private String destination;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_link);

        centerID = getIntent().getStringExtra("uid");
        destination = getIntent().getStringExtra("from");
        WebView webView = findViewById(R.id.webView);

        // Enable JavaScript in WebView
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Force links and redirects to open in the WebView instead of in a browser
        webView.setWebViewClient(new WebViewClient());

        // Retrieve the URL from the intent
        Uri uri = getIntent().getData();
        if (uri != null) {
            webView.loadUrl(uri.toString());
        }
    }
    @Override
    public void onBackPressed() {
        if ("admin".equals(destination)) {
            Intent intent = new Intent(ViewLinkActivity.this, ViewCenter.class);
            intent.putExtra("uid", centerID);
            startActivity(intent);
            overridePendingTransition(R.anim.animate_fade_enter, R.anim.animate_fade_exit);
        } else if ("user".equals(destination)) {
            String admin = getIntent().getStringExtra("admin");
            Intent intent = new Intent(ViewLinkActivity.this, Login.class);
            intent.putExtra("uid", centerID);
            intent.putExtra("admin", admin != null ? admin.trim() : "");
            startActivity(intent);
            overridePendingTransition(R.anim.animate_fade_enter, R.anim.animate_fade_exit);
        } else {
            super.onBackPressed(); // Call super method if destination is not recognized
        }
    }
}
