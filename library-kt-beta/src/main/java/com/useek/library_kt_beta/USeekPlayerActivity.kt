package com.useek.library_kt_beta

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_useek.*

class USeekPlayerActivity : AppCompatActivity() {

    companion object {
        var listener: USeekPlayerCloseListener? = null

        /**
         * Key to set the user id in USeekPlayerActivity
         */
        val USEEK_USER_ID = "userId"

        /**
         * Key to set the game id in USeekPlayerActivity
         */
        val USEEK_GAME_ID = "gameId"
    }


    /**
     * Show / Hide close button
     */
    var isCloseButtonHidden: Boolean = false

    /**
     * Set loading text
     */
    var loadingText: String? = null


    var gameId: String? = null

    var userId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_useek)

        gameId = intent.getStringExtra(USEEK_GAME_ID)
        userId = intent.getStringExtra(USEEK_USER_ID)

        buttonClose.setOnClickListener {
            onCloseButtonPressed()
        }
        if (isCloseButtonHidden)
            buttonClose.visibility = View.INVISIBLE
        else
            buttonClose.visibility = View.VISIBLE

        playerView.playerListener = listener
        playerView.loadingText = loadingText
        loadVideo(gameId, userId)
    }

    override fun onStop() {
        super.onStop()
        if (playerView != null)
            playerView.destroy()
    }

    protected fun onCloseButtonPressed() {
        if (listener != null)
            listener!!.useekPlayerDidClosed(playerView)
        finish()
    }

    fun loadVideo(mGameId: String? = gameId, mUserId: String? = userId) {
        if (mGameId != null && mUserId != null)
            playerView.loadVideo(mGameId, mUserId)
    }
}
