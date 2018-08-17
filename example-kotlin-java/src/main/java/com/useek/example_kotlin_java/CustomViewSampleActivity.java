package com.useek.example_kotlin_java;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.widget.Button;
import android.widget.TextView;

import com.useek.library_kt_beta.USeekManager;
import com.useek.library_kt_beta.USeekPlayerListener;
import com.useek.library_kt_beta.USeekPlayerView;

import kotlin.Unit;
import kotlin.jvm.functions.Function3;

public class CustomViewSampleActivity extends AppCompatActivity implements USeekPlayerListener {

    USeekPlayerView useekPlayerView;
    TextView textViewScore;
    Button   buttonGetScore;

    ExampleSettingsManager settingsManager = ExampleSettingsManager.sharedInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view_sample);

        useekPlayerView = findViewById(R.id.useekPlayerView);
        textViewScore   = findViewById(R.id.txtScore);
        buttonGetScore  = findViewById(R.id.buttonScore);
        buttonGetScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPressedGetScore();
            }
        });

        USeekManager.Companion.getSharedInstance().setPublisherId(settingsManager.getPublisherId());
        useekPlayerView.loadVideo(settingsManager.getGameId(), settingsManager.getUserId());
    }

    private void onPressedGetScore() {
        textViewScore.setText("Loading score...");
        buttonGetScore.setEnabled(false);
        Function3<? super Integer, ? super Integer, ? super Error, Unit> requestPointLambda =
                new Function3<Integer, Integer, Error, Unit>() {
                    @Override
                    public Unit invoke(Integer lastPlayPoints, Integer totalPlayPoints, Error error) {
                        if (error == null) {
                            textViewScore.setText(String.format("Your last play points : %d\nYour total play points : %d", lastPlayPoints, totalPlayPoints));
                        } else {
                            textViewScore.setText(error.getLocalizedMessage());
                        }
                        buttonGetScore.setEnabled(true);
                        return null;
                    }
                };
        USeekManager.Companion.getSharedInstance().requestPoints(
                settingsManager.getGameId(),
                settingsManager.getUserId(),
                requestPointLambda
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        useekPlayerView.destroy();
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
