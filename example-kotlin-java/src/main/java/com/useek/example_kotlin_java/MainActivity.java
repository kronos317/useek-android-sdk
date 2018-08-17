package com.useek.example_kotlin_java;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.useek.library_kt_beta.USeekManager;
import com.useek.library_kt_beta.USeekPlayerActivity;

import kotlin.Unit;
import kotlin.jvm.functions.Function3;

public class MainActivity extends AppCompatActivity {

    Button buttonRequestPoints;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        USeekManager.Companion.getSharedInstance().setPublisherId("60d95e35d89800b0ee499e60d0735fb8");

        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.buttonActivity);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPressPlayActivity();
            }
        });

        Button button1 = findViewById(R.id.buttonFragment);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPressedFragmentSampleActivity();
            }
        });

        Button button2 = findViewById(R.id.buttonCustomView);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPressedCustomViewSampleActivity();
            }
        });

        Button button3 = findViewById(R.id.buttonProgrammaticallyCustomView);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPressedProgrammaticallyCustomViewSampleActivity();
            }
        });

        buttonRequestPoints = findViewById(R.id.buttonRequestPoints);
        buttonRequestPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPressedRequestPoint();
            }
        });
    }

    public void onPressPlayActivity() {

        ExampleSettingsManager settingsManager = ExampleSettingsManager.sharedInstance();

        /*
        USeekPlayerActivity.setUSeekPlayerCloseListener(new USeekPlayerCloseListener() {
            @Override
            public void useekPlayerDidClosed(USeekPlayerView useekPlayerView) {
                Log.d("USeek Sample", "didClose()");
                promptRequestPoints();
            }

            @Override
            public void useekPlayerDidFailWithError(USeekPlayerView useekPlayerView, WebResourceError error) {
                Log.d("USeek Sample", "useekPlayerDidFailWithError()");
            }

            @Override
            public void useekPlayerDidStartLoad(USeekPlayerView useekPlayerView) {
                Log.d("USeek Sample", "useekPlayerDidStartLoad()");
            }

            @Override
            public void useekPlayerDidFinishLoad(USeekPlayerView useekPlayerView) {
                Log.d("USeek Sample", "useekPlayerDidFinishLoad()");
            }
        });
        USeekPlayerActivity.setCloseButtonHidden(!settingsManager.isShowCloseButton());
        USeekPlayerActivity.setLoadingText(settingsManager.getLoadingText());
        */

        Intent intent = new Intent(this, USeekPlayerActivity.class);
        intent.putExtra(USeekPlayerActivity.Companion.getUSEEK_USER_ID(), settingsManager.getUserId());
        intent.putExtra(USeekPlayerActivity.Companion.getUSEEK_GAME_ID(), settingsManager.getGameId());
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

    public void onPressedRequestPoint() {
        ExampleSettingsManager settingsManager = ExampleSettingsManager.sharedInstance();
        buttonRequestPoints.setText("Please wait...");
        buttonRequestPoints.setEnabled(false);
        Function3<? super Integer, ? super Integer, ? super Error, Unit> requestPointLambda =
                new Function3<Integer, Integer, Error, Unit>() {
                    @Override
                    public Unit invoke(Integer lastPlayPoints, Integer totalPlayPoints, Error error) {
                        if (error == null) {
                            Toast.makeText(
                                    MainActivity.this,
                                    String.format("Your last play points : %d\nYour total play points : %d", lastPlayPoints, totalPlayPoints),
                                    Toast.LENGTH_LONG)
                                    .show();
                        } else {
                            Toast.makeText(
                                    MainActivity.this,
                                    error.getLocalizedMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                        buttonRequestPoints.setText("Request Latest Points");
                        buttonRequestPoints.setEnabled(true);
                        return null;
                    }
                };
        USeekManager.Companion.getSharedInstance().requestPoints(
                settingsManager.getGameId(),
                settingsManager.getUserId(),
                requestPointLambda
        );

    }

    public void onPressedSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            onPressedSettings();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void promptRequestPoints() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Confirmation");
        alertDialog.setMessage("Will you request for your latest points?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        onPressedRequestPoint();
                    }
                });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
