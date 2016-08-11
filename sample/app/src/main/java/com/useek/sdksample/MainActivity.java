package com.useek.sdksample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.useek.sdk.UseekSDK;

public class MainActivity extends AppCompatActivity {
    UseekSDK sdk = new UseekSDK(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sdk.setPublisherId("5a857cb7958adcf9a1f53239b0c2eced");
        sdk.setUserId("lukasz-tackowiak");
    }

    public void openPlayer(View view) {
        sdk.setVideoId(1);
        sdk.playVideo();
    }

    public void showRewards(View view) {
        sdk.showRewards();
    }
}
