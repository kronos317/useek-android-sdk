package com.useek.useak_beta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.useek.library_beta.USeakManager;
import com.useek.library_beta.USeakPlayerActivity;

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
    }

    public void onPressPlayActivity() {
        Intent intent = new Intent(this, USeakPlayerActivity.class);
        intent.putExtra(USEAK_USER_ID, "113");
        intent.putExtra(USEAK_GAME_ID, "496953");
        startActivity(intent);
    }
}
