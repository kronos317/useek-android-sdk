package com.useek.useek_example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    EditText editTextPublisherId;
    EditText editTextGameId;
    EditText editTextUserId;
    EditText editTextLoadingText;
    Switch   switchShowCloseButton;
    Button   buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        editTextPublisherId = findViewById(R.id.edit_text_publisher_id);
        editTextGameId = findViewById(R.id.edit_text_game_id);
        editTextUserId = findViewById(R.id.edit_text_user_id);
        editTextLoadingText = findViewById(R.id.edit_text_loading_text);
        switchShowCloseButton = findViewById(R.id.switch_show_close_button);
        buttonSave = findViewById(R.id.button_save_settings);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPressedSaveButton();
            }
        });

        loadSavedData();
    }

    void loadSavedData() {
        ExampleSettingsManager settingsManager = ExampleSettingsManager.sharedInstance();
        editTextPublisherId.setText(settingsManager.getPublisherId());
        editTextGameId.setText(settingsManager.getGameId());
        editTextUserId.setText(settingsManager.getUserId());
        editTextLoadingText.setText(settingsManager.getLoadingText());
        switchShowCloseButton.setChecked(settingsManager.isShowCloseButton());
    }

    void onPressedSaveButton() {
        ExampleSettingsManager settingsManager = ExampleSettingsManager.sharedInstance();
        settingsManager.setPublisherId(editTextPublisherId.getText().toString());
        settingsManager.setGameId(editTextGameId.getText().toString());
        settingsManager.setUserId(editTextUserId.getText().toString());
        settingsManager.setLoadingText(editTextLoadingText.getText().toString());
        settingsManager.setShowCloseButton(switchShowCloseButton.isChecked());
        finish();
    }
}
