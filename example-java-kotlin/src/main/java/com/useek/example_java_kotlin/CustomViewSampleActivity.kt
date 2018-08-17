package com.useek.example_java_kotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.WebResourceError
import com.useek.library_beta.USeekManager
import com.useek.library_beta.USeekPlayerListener
import com.useek.library_beta.USeekPlayerView
import kotlinx.android.synthetic.main.activity_custom_view_sample.*

class CustomViewSampleActivity : AppCompatActivity(), USeekPlayerListener {

    private var settingsManager = ExampleSettingsManager.sharedInstance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_view_sample)

        buttonScore.setOnClickListener { onPressedGetScore() }

        USeekManager.sharedInstance().publisherId = settingsManager.publisherId
        if (settingsManager.gameId != null && settingsManager.userId != null)
            useekPlayerView.loadVideo(settingsManager.gameId!!, settingsManager.userId!!)

    }

    private fun onPressedGetScore() {
        if (settingsManager.gameId == null) return

        txtScore.text = "Loading score..."
        buttonScore.isEnabled = false
        USeekManager.sharedInstance().requestPoints(
                settingsManager.gameId!!,
                settingsManager.userId,
                object : USeekManager.RequestPointsListener {
                    override fun useekRequestForPlayPointsDidSuccess(lastPlayPoints: Int, totalPlayPoints: Int) {
                        txtScore.text = String.format("Your last play points : %d\nYour total play points : %d", lastPlayPoints, totalPlayPoints)
                        buttonScore.isEnabled = true
                    }

                    override fun useekRequestForPlayPointsDidFail(error: Error?) {
                        txtScore.text = error.toString()
                        buttonScore.isEnabled = true
                    }
                }
        )
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
