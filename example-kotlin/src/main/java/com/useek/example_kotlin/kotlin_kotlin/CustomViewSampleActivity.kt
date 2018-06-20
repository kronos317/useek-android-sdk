package com.useek.example_kotlin.kotlin_kotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebResourceError
import com.useek.example_kotlin.ExampleSettingsManager
import com.useek.example_kotlin.R
import com.useek.library_kt_beta.USeekManager
import com.useek.library_kt_beta.USeekPlayerListener
import com.useek.library_kt_beta.USeekPlayerView
import kotlinx.android.synthetic.main.activity_custom_view_sample.*

class CustomViewSampleActivity : AppCompatActivity(), USeekPlayerListener {

    private var settingsManager = ExampleSettingsManager.sharedInstance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_view_sample)

        buttonScore.setOnClickListener { onPressedGetScore() }

        USeekManager.sharedInstance.publisherId = settingsManager.publisherId
        if (settingsManager.gameId != null && settingsManager.userId != null)
            useekPlayerView.loadVideo(settingsManager.gameId!!, settingsManager.userId!!)

    }

    private fun onPressedGetScore() {
        if (settingsManager.gameId == null) return

        txtScore.text = "Loading score..."
        buttonScore.isEnabled = false
        USeekManager.sharedInstance.requestPoints(
                settingsManager.gameId!!,
                settingsManager.userId
        )  { lastPlayPoints, totalPlayPoints, error ->

            if (error == null) {
                txtScore.text = String.format("Your last play points : %d\nYour total play points : %d", lastPlayPoints, totalPlayPoints)
            } else {
                txtScore.text = error.toString()
            }
            buttonScore.isEnabled = true
        }
    }

    override fun onStop() {
        super.onStop()
        useekPlayerView.destroy()
    }

    /** USeekPlayerView listener */
    override fun useekPlayerDidFailWithError(useekPlayerView: USeekPlayerView, error: WebResourceError?) {
        print("useekPlayerDidFailWithError video")
    }

    override fun useekPlayerDidStartLoad(useekPlayerView: USeekPlayerView) {
        print("useekPlayerDidStartLoad video")
    }

    override fun useekPlayerDidFinishLoad(useekPlayerView: USeekPlayerView) {
        print("useekPlayerDidFinishLoad video")
    }
}
