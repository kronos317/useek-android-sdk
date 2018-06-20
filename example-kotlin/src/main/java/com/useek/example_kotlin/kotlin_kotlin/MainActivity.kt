package com.useek.example_kotlin.kotlin_kotlin

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.useek.example_kotlin.ExampleSettingsManager
import com.useek.example_kotlin.R
import com.useek.example_kotlin.SettingsActivity
import com.useek.library_kt_beta.USeekManager
import com.useek.library_kt_beta.USeekPlayerActivity
import com.useek.library_kt_beta.USeekPlayerActivity.Companion.USEEK_GAME_ID
import com.useek.library_kt_beta.USeekPlayerActivity.Companion.USEEK_USER_ID
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        USeekManager.sharedInstance.publisherId = "60d95e35d89800b0ee499e60d0735fb8"

        buttonActivity.setOnClickListener { onPressPlayActivity() }

        buttonFragment.setOnClickListener { onPressedFragmentSampleActivity() }

        buttonCustomView.setOnClickListener { onPressedCustomViewSampleActivity() }

        buttonProgrammaticallyCustomView.setOnClickListener { onPressedProgrammaticallyCustomViewSampleActivity() }

        buttonRequestPoints.setOnClickListener { onPressedRequestPoint() }
    }

    private fun onPressPlayActivity() {

        val settingsManager = ExampleSettingsManager.sharedInstance

        val intent = Intent(this, USeekPlayerActivity::class.java)
        intent.putExtra(USEEK_USER_ID, settingsManager.userId)
        intent.putExtra(USEEK_GAME_ID, settingsManager.gameId)
        startActivity(intent)
    }

    private fun onPressedFragmentSampleActivity() {
        val intent = Intent(this, FragmentSampleActivity::class.java)
        startActivity(intent)
    }

    private fun onPressedCustomViewSampleActivity() {
        val intent = Intent(this, CustomViewSampleActivity::class.java)
        startActivity(intent)
    }

    private fun onPressedProgrammaticallyCustomViewSampleActivity() {
        val intent = Intent(this, CustomViewProgrammaticallyActivity::class.java)
        startActivity(intent)
    }

    private fun onPressedRequestPoint() {
        val settingsManager = ExampleSettingsManager.sharedInstance
        if (settingsManager.gameId == null) return
        buttonRequestPoints.text = "Please wait..."
        buttonRequestPoints.isEnabled = false
        USeekManager
                .sharedInstance
                .requestPoints(
                        settingsManager.gameId!!,
                        settingsManager.userId
                ) { lastPlayPoints, totalPlayPoints, error ->

                    if (error == null) {
                        Toast.makeText(
                                this@MainActivity,
                                String.format("Your last play points : %d\nYour total play points : %d", lastPlayPoints, totalPlayPoints),
                                Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                                this@MainActivity,
                                error.toString(),
                                Toast.LENGTH_LONG
                        ).show()
                    }
                    buttonRequestPoints.text = "Request Latest Points"
                    buttonRequestPoints.isEnabled = true

                }
    }

    private fun onPressedSettings() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main_tab, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_settings) {
            onPressedSettings()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

}
