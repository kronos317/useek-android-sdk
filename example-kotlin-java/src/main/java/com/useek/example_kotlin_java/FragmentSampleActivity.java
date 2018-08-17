package com.useek.example_kotlin_java;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.widget.Button;
import android.widget.FrameLayout;

import com.useek.library_kt_beta.USeekManager;
import com.useek.library_kt_beta.USeekPlayerCloseListener;
import com.useek.library_kt_beta.USeekPlayerFragment;
import com.useek.library_kt_beta.USeekPlayerView;

public class FragmentSampleActivity extends AppCompatActivity implements View.OnClickListener {

    FrameLayout fragmentContainer;
    Button buttonShowUSeekView;
    Button buttonRemoveFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_sample);

        fragmentContainer = findViewById(R.id.fragmentContainer);
        buttonShowUSeekView = findViewById(R.id.buttonPlayView);
        buttonRemoveFragment = findViewById(R.id.buttonRemoveFragment);

        buttonShowUSeekView.setOnClickListener(this);
        buttonRemoveFragment.setOnClickListener(this);

        buttonShowUSeekView.setEnabled(true);
        buttonRemoveFragment.setEnabled(false);

    }


    @Override
    public void onClick(View v) {
        if (v == buttonShowUSeekView) {
            showUSeekFragment();
        } else if (v == buttonRemoveFragment) {
            removeUSeekFragment();
        }
    }

    private void showUSeekFragment() {

        ExampleSettingsManager settingsManager = ExampleSettingsManager.sharedInstance();
        USeekManager.Companion.getSharedInstance().setPublisherId(settingsManager.getPublisherId());

        USeekPlayerFragment fragment = USeekPlayerFragment.newInstance(
                settingsManager.getGameId(),
                settingsManager.getUserId()
        );
        fragment.setCloseButtonHidden(!settingsManager.isShowCloseButton());
        fragment.setLoadingText(settingsManager.getLoadingText());
        fragment.setListener(new USeekPlayerCloseListener() {
            @Override
            public void useekPlayerDidClosed(USeekPlayerView useekPlayerView) {
                promptRequestScore();
                removeUSeekFragment();
            }

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
        });
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentContainer, fragment)
                .commit();
        fragment.loadVideo();

        buttonShowUSeekView.setEnabled(false);
        buttonRemoveFragment.setEnabled(true);
    }

    private void promptRequestScore() {

    }

    private void removeUSeekFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .remove(getSupportFragmentManager().findFragmentById(R.id.fragmentContainer))
                .commit();

        buttonShowUSeekView.setEnabled(true);
        buttonRemoveFragment.setEnabled(false);
    }
}
