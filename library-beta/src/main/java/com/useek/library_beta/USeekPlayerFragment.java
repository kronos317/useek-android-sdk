package com.useek.library_beta;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment can implement the
 * {@link USeekPlayerCloseListener} interface to handle interaction events.
 * Use the {@link USeekPlayerFragment#newInstance} construct method to
 * create an instance of this fragment.
 */
public class USeekPlayerFragment extends Fragment {

    private static final String ARG_GAME_ID = "gameId";
    private static final String ARG_USER_ID = "userId";

    private String mGameId;
    private String mUserId;

    private USeekPlayerCloseListener mListener;

    private ImageButton closeButton;
    private USeekPlayerView useekPlayerView;

    public USeekPlayerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this construct method to create a new instance of
     * this fragment using the provided parameters.
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
        closeButton = view.findViewById(R.id.useek_fragment_close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCloseButtonPressed();
            }
        });
        useekPlayerView = view.findViewById(R.id.useek_player_view);
        useekPlayerView.setPlayerListener(mListener);
        return view;
    }

    public void onCloseButtonPressed() {
        if (mListener != null) {
            mListener.didClosed();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof USeekPlayerCloseListener) {
            mListener = (USeekPlayerCloseListener) context;
        }
        if (useekPlayerView != null) {
            useekPlayerView.loadVideo(this.mGameId, this.mUserId);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Setting USeekPlayerListener like as start, completed, failed loading video
     *
     * @param listener  handle listener of USeekPlayerCloseListener
     *
     */
    public void setUSeekPlayerCloseListener(USeekPlayerCloseListener listener) {
        this.mListener = listener;
        if (useekPlayerView != null) {
            useekPlayerView.setPlayerListener(mListener);
        }
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
}
