package com.useek.library_beta;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

/**
 *
 * USeekPlayerActivity
 *
 * Simple USeek player activity
 *
 * 1. When create activity intent you should set parameters for game id and user id
 *
            Intent intent = new Intent(this, USeekPlayerActivity.class);
            intent.putExtra(USEEK_USER_ID, "{user id}");
            intent.putExtra(USEEK_GAME_ID, "{game id}");
            startActivity(intent);
 *
 * 2. If you want to listen video loading events,
 *    you should set static listener to get event for start, completed, failed loading video.
 *
 *
            USeekPlayerActivity.setUSeekPlayerCloseListener(new USeekPlayerCloseListener() {
                public void useekPlayerDidClosed() {
                    Log.d("USeek Sample", "didClose()");
                }

                public void useekPlayerDidFailWithError(WebResourceError error) {
                    Log.d("USeek Sample", "useekPlayerDidFailWithError()");
                }

                public void useekPlayerDidStartLoad() {
                    Log.d("USeek Sample", "useekPlayerDidStartLoad()");
                }

                public void useekPlayerDidFinishLoad() {
                    Log.d("USeek Sample", "useekPlayerDidFinishLoad()");
                }
            });
 *
 */
public class USeekPlayerActivity extends AppCompatActivity {

    // constant for user id parameter name
    public static final String USEEK_USER_ID = "userId";

    // constant for game id parameter name
    public static final String USEEK_GAME_ID = "gameId";

    private static USeekPlayerCloseListener mListener;

    public static void setUSeekPlayerCloseListener(USeekPlayerCloseListener listener) {
        USeekPlayerActivity.mListener = listener;
    }

    private static boolean showCloseButton = true;

    public static void setShowCloseButton(boolean showCloseButton) {
        USeekPlayerActivity.showCloseButton = showCloseButton;
    }

    public static void setLoadingText(String loadingText) {
        USeekPlayerActivity.loadingText = loadingText;
    }

    private static String loadingText;


    private String mGameId;
    private String mUserId;

    private ImageButton closeButton;
    private USeekPlayerView useekPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_useek);

        mGameId = getIntent().getStringExtra(USEEK_GAME_ID);
        mUserId = getIntent().getStringExtra(USEEK_USER_ID);

        closeButton = findViewById(R.id.useek_activity_close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCloseButtonPressed();
            }
        });
        if (showCloseButton)
            closeButton.setVisibility(View.VISIBLE);
        else
            closeButton.setVisibility(View.INVISIBLE);

        useekPlayerView = findViewById(R.id.useek_activity_player_view);
        useekPlayerView.setPlayerListener(mListener);
        if (loadingText != null) {
            useekPlayerView.setLoadingTextString(loadingText);
        }
        useekPlayerView.loadVideo(mGameId, mUserId);
    }

    protected void onCloseButtonPressed() {
        if (mListener != null) {
            mListener.useekPlayerDidClosed(useekPlayerView);
        }
        this.finish();
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
     * Start loading the video in USeekPlayerView
     *
     * - Precondition: Publisher ID should be set on USeekManager
     *
     * @param gameId    unique game id provided by USeek, not nullable
     * @param userId    user's unique id registered in USeek, nullable
     *
     */
    public void loadVideo(String gameId, String userId) {
        if (useekPlayerView != null) {
            useekPlayerView.loadVideo(gameId, userId);
        }
    }
}
