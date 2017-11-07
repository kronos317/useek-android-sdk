package com.useek.useek_example;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.widget.Button;

import com.useek.library_beta.USeekManager;
import com.useek.library_beta.USeekPlayerActivity;
import com.useek.library_beta.USeekPlayerCloseListener;
import com.useek.library_beta.USeekPlayerView;

import static com.useek.library_beta.USeekPlayerActivity.USEEK_GAME_ID;
import static com.useek.library_beta.USeekPlayerActivity.USEEK_USER_ID;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        USeekManager.sharedInstance().setPublisherId("a839793e879c8d0237124a8400e31477");

        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.main_activity_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPressPlayActivity();
            }
        });

        Button button1 = findViewById(R.id.main_activity_fragment_button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPressedFragmentSampleActivity();
            }
        });

        Button button2 = findViewById(R.id.main_activity_custom_view_button);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPressedCustomViewSampleActivity();
            }
        });

        Button button3 = findViewById(R.id.main_activity_programmatically_custom_view_button);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPressedProgrammaticallyCustomViewSampleActivity();
            }
        });

        Button button4 = findViewById(R.id.main_activity_setting_button);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPressedSettings();
            }
        });
    }

    public void onPressPlayActivity() {

        ExampleSettingsManager settingsManager = ExampleSettingsManager.sharedInstance();
        USeekManager.sharedInstance().setPublisherId(settingsManager.getPublisherId());

        USeekPlayerActivity.setUSeekPlayerCloseListener(new USeekPlayerCloseListener() {
            @Override
            public void useekPlayerDidClosed(USeekPlayerView useekPlayerView) {
                Log.d("USeek Sample", "didClose()");
            }

            @Override
            public void useekPlayerDidFailWithError(USeekPlayerView useekPlayerView, WebResourceError error) {
                Log.d("USeek Sample", "useekPlayerDidFailWithError()");
            }

            @Override
            public void useekPlayerDidStartLoad(USeekPlayerView useekPlayerView) {
                Log.d("USeek Sample", "useekPlayerDidStartLoad()");
            }

            @Override
            public void useekPlayerDidFinishLoad(USeekPlayerView useekPlayerView) {
                Log.d("USeek Sample", "useekPlayerDidFinishLoad()");
            }
        });
        USeekPlayerActivity.setShowCloseButton(settingsManager.isShowCloseButton());
        USeekPlayerActivity.setLoadingText(settingsManager.getLoadingText());

        Intent intent = new Intent(this, USeekPlayerActivity.class);
        intent.putExtra(USEEK_USER_ID, settingsManager.getUserId());
        intent.putExtra(USEEK_GAME_ID, settingsManager.getGameId());
        startActivity(intent);
    }

    public void onPressedFragmentSampleActivity() {
        Intent intent = new Intent(this, FragmentSampleActivity.class);
        startActivity(intent);
    }

    public void onPressedCustomViewSampleActivity() {
        Intent intent = new Intent(this, CustomViewSampleActivity.class);
        startActivity(intent);
    }

    public void onPressedProgrammaticallyCustomViewSampleActivity() {
        Intent intent = new Intent(this, CustomViewProgrammaticallyActivity.class);
        startActivity(intent);
    }

    public void onPressedSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
