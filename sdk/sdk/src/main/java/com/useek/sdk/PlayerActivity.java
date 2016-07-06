package com.useek.sdk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

public class PlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_player);
        loadPlayer();
    }

    private void loadPlayer() {
        Intent intent = getIntent();
        final LinearLayout loading = (LinearLayout) findViewById(R.id.loading);
        WebView player = (WebView) findViewById(R.id.player);

        player.getSettings().setJavaScriptEnabled(true);
        player.setWebChromeClient(new WebChromeClient());
        player.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                loading.setVisibility(View.GONE);
            }
        });
        player.loadUrl(intent.getStringExtra(UseekSDK.PLAYER_URL));
    }
}
