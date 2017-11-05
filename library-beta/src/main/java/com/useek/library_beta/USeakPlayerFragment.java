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
 * Activities that contain this fragment must implement the
 * {@link USeakPlayerCloseListener} interface
 * to handle interaction events.
 * Use the {@link USeakPlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class USeakPlayerFragment extends Fragment {

    private static final String ARG_GAME_ID = "gameId";
    private static final String ARG_USER_ID = "userId";

    private String mGameId;
    private String mUserId;

    private USeakPlayerCloseListener mListener;

    private ImageButton closeButton;
    private USeakPlayerView useakPlayerView;

    public USeakPlayerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param gameId
     * @param userId
     * @return A new instance of fragment USeakPlayerFragment.
     */
    public static USeakPlayerFragment newInstance(String gameId, String userId) {
        USeakPlayerFragment fragment = new USeakPlayerFragment();
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
        View view = inflater.inflate(R.layout.fragment_useak, container, false);
        closeButton = view.findViewById(R.id.useak_fragment_close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCloseButtonPressed();
            }
        });
        useakPlayerView = view.findViewById(R.id.useak_player_view);
        useakPlayerView.setPlayerListener(mListener);
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
        if (context instanceof USeakPlayerCloseListener) {
            mListener = (USeakPlayerCloseListener) context;
        }
        if (useakPlayerView != null) {
            useakPlayerView.loadVideo(this.mGameId, this.mUserId);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setUSeakPlayerCloseListener(USeakPlayerCloseListener listener) {
        this.mListener = listener;
        if (useakPlayerView != null) {
            useakPlayerView.setPlayerListener(mListener);
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
