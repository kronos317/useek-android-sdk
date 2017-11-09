package com.useek.library_beta;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 *
 * USeekPlayerFragment
 *
 * Simple USeek player fragment
 *
 * 1. At the time of intent creation, you should put extras for game id and user id
 *
 *  USeekPlayerFragment fragment = USeekPlayerFragment.newInstance("{your game id}", "{your user id}");
 *  getSupportFragmentManager()
 *          .beginTransaction()
 *          .add(R.id.fragment_container, fragment)
 *          .commit();
 *  fragment.loadVideo("{game id}", "{user id}");
 *
 * 2. Implement {@link USeekPlayerCloseListener}
 *
 *      fragment.setUSeekPlayerCloseListener(new USeekPlayerCloseListener() {
 *          public void useekPlayerDidClosed(USeekPlayerView mUseekPlayerView) {
 *          }
 *
 *          public void useekPlayerDidFailWithError(USeekPlayerView mUseekPlayerView, WebResourceError error) {
 *          }
 *
 *          public void useekPlayerDidStartLoad(USeekPlayerView mUseekPlayerView) {
 *          }
 *
 *          public void useekPlayerDidFinishLoad(USeekPlayerView mUseekPlayerView) {
 *          }
 *      });
 *
 */
public class USeekPlayerFragment extends Fragment {

    private static final String ARG_GAME_ID = "gameId";
    private static final String ARG_USER_ID = "userId";

    private String mGameId;
    private String mUserId;

    private USeekPlayerCloseListener mListener;

    private ImageButton mCloseButton;
    private USeekPlayerView mUseekPlayerView;

    /**
     * Setter to show / hide close button
     * @param closeButtonHidden     true if we want to hide close button
     */
    public void setCloseButtonHidden(boolean closeButtonHidden) {
        this.mCloseButtonHidden = closeButtonHidden;
    }

    private boolean mCloseButtonHidden = true;

    /**
     * Setter for loading text
     * @param loadingText       String contents for loading text
     */
    public void setLoadingText(String loadingText) {
        this.mLoadingText = loadingText;
        if (this.mUseekPlayerView != null) {
            this.mUseekPlayerView.setLoadingText(loadingText);
        }
    }

    private String mLoadingText;

    /**
     * Setter for close button event listner
     * @param listener              Listener to consume the close event
     */
    public void setUSeekPlayerCloseListener(USeekPlayerCloseListener listener) {
        this.mListener = listener;
        if (mUseekPlayerView != null) {
            mUseekPlayerView.setPlayerListener(mListener);
        }
    }

    public USeekPlayerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this constructor to create a new instance of USeekPlayerFragment.
     *
     * @param gameId    unique game id provided by USeek, not nullable
     * @param userId    user's unique id registered in USeek, nullable
     * @return A new instance of USeekPlayerFragment.
     */
    public static USeekPlayerFragment newInstance(String gameId, String userId) {
        USeekPlayerFragment fragment = new USeekPlayerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_GAME_ID, gameId);
        args.putString(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mGameId = getArguments().getString(ARG_GAME_ID);
            mUserId = getArguments().getString(ARG_USER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_useek, container, false);
        mCloseButton = view.findViewById(R.id.useek_fragment_close_button);
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

        mUseekPlayerView = view.findViewById(R.id.useek_fragment_useek_player_view);
        mUseekPlayerView.setPlayerListener(mListener);
        if (mLoadingText != null) {
            mUseekPlayerView.setLoadingText(mLoadingText);
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof USeekPlayerCloseListener) {
            mListener = (USeekPlayerCloseListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        if (mUseekPlayerView != null)
            mUseekPlayerView.destroy();
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
        this.mGameId = gameId;
        this.mUserId = userId;
        if (mUseekPlayerView != null) {
            mUseekPlayerView.loadVideo(this.mGameId, this.mUserId);
        }
    }

    private void onCloseButtonPressed() {
        if (mListener != null) {
            mListener.useekPlayerDidClosed(mUseekPlayerView);
        }
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
}
