package com.useek.library_beta;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class USeakPlayerActivity extends AppCompatActivity {

    public static final String USEAK_USER_ID = "userId";

    public static final String USEAK_GAME_ID = "gameId";

    private String mGameId;
    private String mUserId;

    private static USeakPlayerCloseListener mListener;

    private ImageButton closeButton;
    private USeakPlayerView useakPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_useak);

        mGameId = getIntent().getStringExtra(USEAK_GAME_ID);
        mUserId = getIntent().getStringExtra(USEAK_USER_ID);

        closeButton = findViewById(R.id.useak_activity_close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCloseButtonPressed();
            }
        });

        useakPlayerView = findViewById(R.id.useak_activity_player_view);
        useakPlayerView.setPlayerListener(mListener);

        useakPlayerView.loadVideo(mGameId, mUserId);
    }

    public void onCloseButtonPressed() {
        if (mListener != null) {
            mListener.didClosed();
        }
        this.finish();
    }

    public static void setUSeakPlayerCloseListener(USeakPlayerCloseListener listener) {
        USeakPlayerActivity.mListener = listener;
    }

    public void setGameId(String gameId) {
        this.mGameId = gameId;
    }

    public String getGameId() {
        return mGameId;
    }

    public void setUserId(String userId) {
        this.mUserId = userId;
    }

    public String getUserId() {
        return mUserId;
    }

    public void loadVideo() {
        this.loadVideo(this.mGameId, this.mUserId);
    }

    /**
     * Load Video with GameId and UserId
     * @param gameId
     * @param userId
     */
    public void loadVideo(String gameId, String userId) {
        if (useakPlayerView != null) {
            useakPlayerView.loadVideo(gameId, userId);
        }
    }
}
