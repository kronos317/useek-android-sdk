package com.useek.useek_example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.useek.library_beta.USeekManager;
import com.useek.library_beta.USeekPlayerListener;
import com.useek.library_beta.USeekPlayerView;

public class CustomViewSampleActivity extends AppCompatActivity implements USeekPlayerListener {

    USeekPlayerView useekView;
    TextView textViewScore;
    Button   buttonGetScore;
    EditText editTextGameId;
    EditText editTextUserId;
    Button   buttonPlay;
    TextView textViewErrorLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view_sample);

        useekView       = findViewById(R.id.custom_activity_useek_view);
        textViewScore   = findViewById(R.id.custom_activity_score);
        buttonGetScore  = findViewById(R.id.custom_activity_get_score_button);
        buttonGetScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPressedGetScore();
            }
        });
        editTextGameId  = findViewById(R.id.custom_activity_game_id);
        editTextUserId  = findViewById(R.id.custom_activity_user_id);
        buttonPlay      = findViewById(R.id.custom_activity_play_button);
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPressedPlayVideo();
            }
        });
        textViewErrorLog = findViewById(R.id.custom_activity_error_text);
    }

    private void onPressedGetScore() {
        if (checkValidate()) {
            textViewErrorLog.setText("Loading score...");
            buttonGetScore.setEnabled(false);
            USeekManager.sharedInstance().requestPoints(
                    this.editTextGameId.getText().toString(),
                    this.editTextUserId.getText().toString(),
                    new USeekManager.RequestPointsListener() {
                        @Override
                        public void didSuccess(int points) {
                            textViewErrorLog.setText("");
                            textViewScore.setText(String.valueOf(points));
                            buttonGetScore.setEnabled(true);
                        }

                        @Override
                        public void didFailure(Error error) {
                            if (error != null)
                                textViewErrorLog.setText(error.getLocalizedMessage());
                            else
                                textViewErrorLog.setText("Error to loading score.");

                            buttonGetScore.setEnabled(true);
                        }
                    }
            );
        }
    }

    private void onPressedPlayVideo() {
        if (checkValidate()) {
            String gameId = this.editTextGameId.getText().toString();
            String userId = this.editTextUserId.getText().toString();

            useekView.loadVideo(gameId, userId);
            useekView.setPlayerListener(this);
        }
    }

    private boolean checkValidate() {
        boolean isValid = true;
        String gameId = editTextGameId.getText().toString();
        if (gameId == null || gameId.length() == 0) {
            textViewErrorLog.setText("Invalid Game Id");
            isValid = false;
        }
        return isValid;
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
