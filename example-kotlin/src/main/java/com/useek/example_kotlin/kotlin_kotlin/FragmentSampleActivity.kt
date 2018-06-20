package com.useek.example_kotlin.kotlin_kotlin

import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.webkit.WebResourceError
import com.useek.example_kotlin.ExampleSettingsManager
import com.useek.example_kotlin.R
import com.useek.library_kt_beta.USeekManager
import com.useek.library_kt_beta.USeekPlayerCloseListener
import com.useek.library_kt_beta.USeekPlayerFragment
import com.useek.library_kt_beta.USeekPlayerView
import kotlinx.android.synthetic.main.activity_fragment_sample.*

class FragmentSampleActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_sample)

        buttonPlayView.setOnClickListener(this)
        buttonRemoveFragment.setOnClickListener(this)

        buttonPlayView.isEnabled = true
        buttonRemoveFragment.isEnabled = false
    }

    override fun onClick(v: View?) {
        when (v) {
            buttonPlayView -> showUSeekFragment()
            buttonRemoveFragment -> removeUSeekFragment()
        }
    }

    private fun showUSeekFragment() {
        val settingsManager = ExampleSettingsManager.sharedInstance
        USeekManager.sharedInstance.publisherId = settingsManager.publisherId
        if (settingsManager.gameId != null && settingsManager.userId != null) {

            val fragment = USeekPlayerFragment.newInstance(
                    settingsManager.gameId!!,
                    settingsManager.userId!!
            )
            fragment.isCloseButtonHidden = !settingsManager.isShowCloseButton
            fragment.loadingText = settingsManager.loadingText
            fragment.listener = object : USeekPlayerCloseListener {
                override fun useekPlayerDidClosed(useekPlayerView: USeekPlayerView) {
                    removeUSeekFragment()
                }

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

            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit()
            fragment.loadVideo()

            buttonPlayView.isEnabled = false
            buttonRemoveFragment.isEnabled = true
        }
    }

    private fun removeUSeekFragment() {
        supportFragmentManager
                .beginTransaction()
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .remove(supportFragmentManager.findFragmentById(R.id.fragmentContainer))
                .commit()

        buttonPlayView.isEnabled = true
        buttonRemoveFragment.isEnabled = false
    }
}
