package com.useek.useak_beta;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.widget.Button;
import android.widget.FrameLayout;

import com.useek.library_beta.USeakPlayerCloseListener;
import com.useek.library_beta.USeakPlayerFragment;

public class FragmentSampleActivity extends AppCompatActivity implements View.OnClickListener {

    FrameLayout fragmentContainer;
    Button buttonShowUSeakView;
    Button buttonRemoveFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_sample);

        fragmentContainer = findViewById(R.id.fragment_container);
        buttonShowUSeakView = findViewById(R.id.fragment_sample_show_useak);
        buttonRemoveFragment = findViewById(R.id.fragment_sample_remove_fragment);

        buttonShowUSeakView.setOnClickListener(this);
        buttonRemoveFragment.setOnClickListener(this);

        buttonShowUSeakView.setEnabled(true);
        buttonRemoveFragment.setEnabled(false);
    }


    @Override
    public void onClick(View v) {
        if (v == buttonShowUSeakView) {
            showUSeakFragment();
        } else if (v == buttonRemoveFragment) {
            removeUSeakFragment();
        }
    }

    private void showUSeakFragment() {
        String gameId = "113";
        String userId = "496953";
        USeakPlayerFragment fragment = USeakPlayerFragment.newInstance(gameId, userId);
        fragment.setUSeakPlayerCloseListener(new USeakPlayerCloseListener() {
            @Override
            public void didClosed() {
                removeUSeakFragment();
            }

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
        });
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();

        buttonShowUSeakView.setEnabled(false);
        buttonRemoveFragment.setEnabled(true);
    }

    private void removeUSeakFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .remove(getSupportFragmentManager().findFragmentById(R.id.fragment_container))
                .commit();

        buttonShowUSeakView.setEnabled(true);
        buttonRemoveFragment.setEnabled(false);
    }
}
