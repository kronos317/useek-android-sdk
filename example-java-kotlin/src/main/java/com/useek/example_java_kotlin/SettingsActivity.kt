package com.useek.example_java_kotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        buttonSave.setOnClickListener {
            onPressedSaveButton()
        }
        loadSavedData()
    }

    private fun loadSavedData() {
        val settingsManager = ExampleSettingsManager.sharedInstance
        edtPublisherId.setText(settingsManager.publisherId)
        edtGameId.setText(settingsManager.gameId)
        edtUserId.setText(settingsManager.userId)
        edtLoadingText.setText(settingsManager.loadingText)
        switchShowCloseButton.isChecked = settingsManager.isShowCloseButton
    }

    private fun onPressedSaveButton() {
        val settingsManager = ExampleSettingsManager.sharedInstance
        settingsManager.publisherId = edtPublisherId.text.toString()
        settingsManager.gameId = edtGameId.text.toString()
        settingsManager.userId = edtUserId.text.toString()
        settingsManager.loadingText = edtLoadingText.text.toString()
        settingsManager.isShowCloseButton = switchShowCloseButton.isChecked
        finish()
    }
}
