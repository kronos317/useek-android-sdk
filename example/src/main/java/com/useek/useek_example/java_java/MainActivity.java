package com.useek.useek_example.java_java;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.useek.library_beta.USeekManager;
import com.useek.library_beta.USeekPlayerActivity;
import com.useek.useek_example.ExampleSettingsManager;
import com.useek.useek_example.R;
import com.useek.useek_example.SettingsActivity;

import static com.useek.library_beta.USeekPlayerActivity.USEEK_GAME_ID;
import static com.useek.library_beta.USeekPlayerActivity.USEEK_USER_ID;

public class MainActivity extends AppCompatActivity {

    Button buttonRequestPoints;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        USeekManager.sharedInstance().setPublisherId("60d95e35d89800b0ee499e60d0735fb8");

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
        intent.putExtra(USEEK_USER_ID, settingsManager.getUserId());
        intent.putExtra(USEEK_GAME_ID, settingsManager.getGameId());
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
        USeekManager.sharedInstance().requestPoints(
                settingsManager.getGameId(),
                settingsManager.getUserId(),
                new USeekManager.RequestPointsListener() {
                    @Override
                    public void useekRequestForPlayPointsDidSuccess(int lastPlayPoints, int totalPlayPoints) {
                        Toast.makeText(
                                MainActivity.this,
                                String.format("Your last play points : %d\nYour total play points : %d", lastPlayPoints, totalPlayPoints),
                                Toast.LENGTH_LONG)
                                .show();
                        
                        buttonRequestPoints.setText("Request Latest Points");
                        buttonRequestPoints.setEnabled(true);

                    }

                    @Override
                    public void useekRequestForPlayPointsDidFail(Error error) {
                        String errorString;
                        if (error != null)
                            errorString = error.getLocalizedMessage();
                        else
                            errorString = "Error to loading score.";
                        Toast.makeText(
                                MainActivity.this,
                                errorString,
                                Toast.LENGTH_LONG)
                                .show();

                        buttonRequestPoints.setText("Request Latest Points");
                        buttonRequestPoints.setEnabled(true);
                    }
                }
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
