package com.useek.sdksample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.useek.sdk.UseekSDK;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openPlayer(View view) {
        UseekSDK sdk = new UseekSDK(this);
        sdk.setPublisherId("cinemark");
        sdk.setUserId("external-user-id");
        sdk.setVideoId(125);
        sdk.playVideo();
    }
}
