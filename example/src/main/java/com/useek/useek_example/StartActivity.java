package com.useek.useek_example;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button buttonJava = findViewById(R.id.button_java_java);
        buttonJava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, com.useek.useek_example.java_java.MainActivity.class);
                startActivity(intent);
            }
        });

        Button buttonKotlin = findViewById(R.id.button_java_kotlin);
        buttonKotlin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(StartActivity.this, com.useek.useek_example.java_java.MainActivity.class);
//                startActivity(intent);
            }
        });
    }
}
