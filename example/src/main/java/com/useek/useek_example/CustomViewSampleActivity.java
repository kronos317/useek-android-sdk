package com.useek.useek_example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.widget.Button;
import android.widget.TextView;

import com.useek.library_beta.USeekManager;
import com.useek.library_beta.USeekPlayerListener;
import com.useek.library_beta.USeekPlayerView;

public class CustomViewSampleActivity extends AppCompatActivity implements USeekPlayerListener {

    USeekPlayerView useekPlayerView;
    TextView textViewScore;
    Button   buttonGetScore;

    ExampleSettingsManager settingsManager = ExampleSettingsManager.sharedInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view_sample);

        useekPlayerView = findViewById(R.id.custom_activity_useek_view);
        textViewScore   = findViewById(R.id.custom_activity_score_text);
        buttonGetScore  = findViewById(R.id.custom_activity_get_score_button);
        buttonGetScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPressedGetScore();
            }
        });

        USeekManager.sharedInstance().setPublisherId(settingsManager.getPublisherId());
        useekPlayerView.loadVideo(settingsManager.getGameId(), settingsManager.getUserId());
    }

    private void onPressedGetScore() {
        textViewScore.setText("Loading score...");
        buttonGetScore.setEnabled(false);
        USeekManager.sharedInstance().requestPoints(
                settingsManager.getGameId(),
                settingsManager.getUserId(),
                new USeekManager.RequestPointsListener() {
                    @Override
                    public void useekRequestForPlayPointsDidSuccess(int lastPlayPoints, int totalPlayPoints) {
                        textViewScore.setText(String.format("Your last play points : %d\nYour total play points : %d", lastPlayPoints, totalPlayPoints));
                        buttonGetScore.setEnabled(true);
                    }

                    @Override
                    public void useekRequestForPlayPointsDidFail(Error error) {
                        if (error != null)
                            textViewScore.setText(error.getLocalizedMessage());
                        else
                            textViewScore.setText("Sorry, we've encountered an error while loading points");

                        buttonGetScore.setEnabled(true);
                    }
                }
        );
    }

    /** USeekPlayerView listener */

    @Override
    public void useekPlayerDidFailWithError(USeekPlayerView useekPlayerView, WebResourceError error) {
        Log.d("USeek Sample", "useekPlayerDidFailWithError video");
    }

    @Override
    public void useekPlayerDidStartLoad(USeekPlayerView useekPlayerView) {
        Log.d("USeek Sample", "useekPlayerDidStartLoad video");
    }

    @Override
    public void useekPlayerDidFinishLoad(USeekPlayerView useekPlayerView) {
        Log.d("USeek Sample", "useekPlayerDidFinishLoad video");
    }
}
