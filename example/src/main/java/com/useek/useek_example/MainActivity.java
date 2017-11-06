package com.useek.useek_example;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.widget.Button;

import com.useek.library_beta.USeekManager;
import com.useek.library_beta.USeekPlayerActivity;
import com.useek.library_beta.USeekPlayerCloseListener;

import static com.useek.library_beta.USeekPlayerActivity.USEEK_GAME_ID;
import static com.useek.library_beta.USeekPlayerActivity.USEEK_USER_ID;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        USeekManager.sharedInstance().setPublisherId("a839793e879c8d0237124a8400e31477");

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

        Button button3 = findViewById(R.id.main_activity_programmatically_custom_view_button);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPressedProgrammaticallyCustomViewSampleActivity();
            }
        });

    }

    public void onPressPlayActivity() {

        USeekPlayerActivity.setUSeekPlayerCloseListener(new USeekPlayerCloseListener() {
            @Override
            public void didClosed() {
                Log.d("USeek Sample", "didClose()");
            }

            @Override
            public void didFailedWithError(WebResourceError error) {
                Log.d("USeek Sample", "didFailedWithError()");
            }

            @Override
            public void didStartLoad() {
                Log.d("USeek Sample", "didStartLoad()");
            }

            @Override
            public void didFinishLoad() {
                Log.d("USeek Sample", "didFinishLoad()");
            }
        });

        Intent intent = new Intent(this, USeekPlayerActivity.class);
        intent.putExtra(USEEK_USER_ID, "113");
        intent.putExtra(USEEK_GAME_ID, "496953");
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

    public void onPressedProgrammaticallyCustomViewSampleActivity() {
        Intent intent = new Intent(this, CustomViewProgrammaticallyActivity.class);
        startActivity(intent);
    }
}
