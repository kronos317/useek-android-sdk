package com.useek.useak_beta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.widget.Button;

import com.useek.library_beta.USeakManager;
import com.useek.library_beta.USeakPlayerActivity;
import com.useek.library_beta.USeakPlayerCloseListener;

import static com.useek.library_beta.USeakPlayerActivity.USEAK_GAME_ID;
import static com.useek.library_beta.USeakPlayerActivity.USEAK_USER_ID;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        USeakManager.sharedInstance().setPublisherId("a839793e879c8d0237124a8400e31477");

        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.main_activity_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPressPlayActivity();
            }
        });

        Button button1 = findViewById(R.id.main_activity_fragment_button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPressedFragmentSampleActivity();
            }
        });

        Button button2 = findViewById(R.id.main_activity_custom_view_button);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPressedCustomViewSampleActivity();
            }
        });
    }

    public void onPressPlayActivity() {

        USeakPlayerActivity.setUSeakPlayerCloseListener(new USeakPlayerCloseListener() {
            @Override
            public void didClosed() {
                Log.d("USeak Sample", "didClose()");
            }

            @Override
            public void didFailedWithError(WebResourceError error) {
                Log.d("USeak Sample", "didFailedWithError()");
            }

            @Override
            public void didStartLoad() {
                Log.d("USeak Sample", "didStartLoad()");
            }

            @Override
            public void didFinishLoad() {
                Log.d("USeak Sample", "didFinishLoad()");
            }
        });

        Intent intent = new Intent(this, USeakPlayerActivity.class);
        intent.putExtra(USEAK_USER_ID, "113");
        intent.putExtra(USEAK_GAME_ID, "496953");
        startActivity(intent);
    }

    public void onPressedFragmentSampleActivity() {
        Intent intent = new Intent(this, FragmentSampleActivity.class);
        startActivity(intent);
    }

    public void onPressedCustomViewSampleActivity() {
        Intent intent = new Intent(this, CustomViewSampleActivity.class);
        startActivity(intent);
    }
}
