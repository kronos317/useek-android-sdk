package com.useek.sdksample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.useek.sdk.UseekSDK;

public class MainActivity extends AppCompatActivity {
    UseekSDK sdk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sdk = new UseekSDK(this);
        sdk.setPublisherId("3543049ba603c93461697c43aa71fafd");
        sdk.setUserId("external-user-id");
    }

    public void openPlayer(View view) {
        sdk.setVideoId(125);
        sdk.playVideo();
    }

    public void showRewards(View view) {
        sdk.showRewards();
    }
}
