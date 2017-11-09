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
 * 1. At the time of intent creation, you should put extras for game id and user id
 *
            Intent intent = new Intent(this, USeekPlayerActivity.class);
            intent.putExtra(USEEK_USER_ID, "{user id}");
            intent.putExtra(USEEK_GAME_ID, "{game id}");
            startActivity(intent);
 *
 * 2. To listen the events,
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

    private static String mLoadingText;

    private String mGameId;
    private String mUserId;

    private ImageButton     mCloseButton;
    private USeekPlayerView mUseekPlayerView;

    /**
     * Key to set the user id in USeekPlayerActivity
     */
    public static final String USEEK_USER_ID = "userId";

    /**
     * Key to set the game id in USeekPlayerActivity
     */
    public static final String USEEK_GAME_ID = "gameId";

    private static USeekPlayerCloseListener mListener;

    /**
     * Setter for close button event listner
     * @param listener              Listener to consume the close event
     */
    public static void setUSeekPlayerCloseListener(USeekPlayerCloseListener listener) {
        USeekPlayerActivity.mListener = listener;
    }

    private static boolean mCloseButtonHidden = false;

    /**
     * Setter to show / hide close button
     * @param closeButtonHidden     true if we want to hide close button
     */
    public static void setCloseButtonHidden(boolean closeButtonHidden) {
        USeekPlayerActivity.mCloseButtonHidden = closeButtonHidden;
    }

    /**
     * Setter for loading text
     * @param loadingText       String contents for loading text
     */
    public static void setLoadingText(String loadingText) {
        USeekPlayerActivity.mLoadingText = loadingText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_useek);

        mGameId = getIntent().getStringExtra(USEEK_GAME_ID);
        mUserId = getIntent().getStringExtra(USEEK_USER_ID);

        mCloseButton = findViewById(R.id.useek_activity_close_button);
        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCloseButtonPressed();
            }
        });
        if (mCloseButtonHidden)
            mCloseButton.setVisibility(View.INVISIBLE);
        else
            mCloseButton.setVisibility(View.VISIBLE);

        mUseekPlayerView = findViewById(R.id.useek_activity_player_view);
        mUseekPlayerView.setPlayerListener(mListener);
        if (mLoadingText != null) {
            mUseekPlayerView.setLoadingText(mLoadingText);
        }
        mUseekPlayerView.loadVideo(mGameId, mUserId);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mUseekPlayerView != null)
            mUseekPlayerView.destroy();
    }

    protected void onCloseButtonPressed() {
        if (mListener != null) {
            mListener.useekPlayerDidClosed(mUseekPlayerView);
        }

        this.finish();
    }


    /**
     * Setter for game id
     * @param gameId        Game ID to attend
     */
    public void setGameId(String gameId) {
        this.mGameId = gameId;
    }

    /**
     * Getter for game id
     * @return              Returns current game id
     */
    public String getGameId() {
        return mGameId;
    }

    /**
     * Setter for user id
     * @param userId        Current user id
     */
    public void setUserId(String userId) {
        this.mUserId = userId;
    }

    /**
     * Getter for user id
     * @return              Returns current user id
     */
    public String getUserId() {
        return mUserId;
    }

    /**
     * Starts loading the video in USeekPlayerView
     */
    public void loadVideo() {
        this.loadVideo(this.mGameId, this.mUserId);
    }

    /**
     * Start loading the video in USeekPlayerView
     *
     * - Precondition: Publisher ID should be set
     *
     * @param gameId    unique game id provided by USeek, not nullable
     * @param userId    user's unique id registered in USeek, nullable
     *
     */
    public void loadVideo(String gameId, String userId) {
        if (mUseekPlayerView != null) {
            mUseekPlayerView.loadVideo(gameId, userId);
        }
    }
}
