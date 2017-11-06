package com.useek.useak_beta;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.useek.library_beta.USeakManager;
import com.useek.library_beta.USeakPlaybackResultDataModel;
import com.useek.library_beta.USeakPlayerListener;
import com.useek.library_beta.USeakPlayerView;

public class CustomViewSampleActivity extends AppCompatActivity implements USeakPlayerListener {

    USeakPlayerView useakView;
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

        useakView = findViewById(R.id.custom_activity_useak_view);
        textViewScore = findViewById(R.id.custom_activity_score);
        buttonGetScore = findViewById(R.id.custom_activity_get_score_button);
        buttonGetScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPressedGetScore();
            }
        });
        editTextGameId = findViewById(R.id.custom_activity_game_id);
        editTextUserId = findViewById(R.id.custom_activity_user_id);
        buttonPlay = findViewById(R.id.custom_activity_play_button);
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
            USeakManager.sharedInstance().requestPoints(
                    this.editTextGameId.getText().toString(),
                    this.editTextUserId.getText().toString(),
                    new USeakManager.RequestPointsListener() {
                        @Override
                        public void didSuccess(USeakPlaybackResultDataModel resultDataModel) {
                            textViewErrorLog.setText("");
                            textViewScore.setText(String.valueOf(resultDataModel.getPoints()));
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

            useakView.loadVideo(gameId, userId);
            useakView.setPlayerListener(this);
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


    /** USeakPlayerView listener */

    @Override
    public void didFailedWithError(WebResourceError error) {
        Log.d("USeak Sample", "didFailedWithError video");
    }

    @Override
    public void didStartLoad() {
        Log.d("USeak Sample", "didStartLoad video");
    }

    @Override
    public void didFinishLoad() {
        Log.d("USeak Sample", "didFinishLoad video");
    }
}
