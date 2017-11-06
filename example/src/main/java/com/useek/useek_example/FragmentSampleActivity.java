package com.useek.useek_example;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.widget.Button;
import android.widget.FrameLayout;

import com.useek.library_beta.USeekPlayerCloseListener;
import com.useek.library_beta.USeekPlayerFragment;

public class FragmentSampleActivity extends AppCompatActivity implements View.OnClickListener {

    FrameLayout fragmentContainer;
    Button buttonShowUSeekView;
    Button buttonRemoveFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_sample);

        fragmentContainer = findViewById(R.id.fragment_container);
        buttonShowUSeekView = findViewById(R.id.fragment_sample_show_useek);
        buttonRemoveFragment = findViewById(R.id.fragment_sample_remove_fragment);

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
        String gameId = "113";
        String userId = "496953";
        USeekPlayerFragment fragment = USeekPlayerFragment.newInstance(gameId, userId);
        fragment.setUSeekPlayerCloseListener(new USeekPlayerCloseListener() {
            @Override
            public void didClosed() {
                removeUSeekFragment();
            }

            @Override
            public void didFailedWithError(WebResourceError error) {
                Log.d("USeek Sample", "didFailedWithError video");
            }

            @Override
            public void didStartLoad() {
                Log.d("USeek Sample", "didStartLoad video");
            }

            @Override
            public void didFinishLoad() {
                Log.d("USeek Sample", "didFinishLoad video");
            }
        });
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();

        buttonShowUSeekView.setEnabled(false);
        buttonRemoveFragment.setEnabled(true);
    }

    private void removeUSeekFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .remove(getSupportFragmentManager().findFragmentById(R.id.fragment_container))
                .commit();

        buttonShowUSeekView.setEnabled(true);
        buttonRemoveFragment.setEnabled(false);
    }
}
