package com.useek.library_kt_beta

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_useek.*


/**
 *
 * USeekPlayerFragment
 *
 * Simple USeek player fragment
 *
 * 1. At the time of intent creation, you should put extras for game id and user id
 *
 *  USeekPlayerFragment fragment = USeekPlayerFragment.newInstance("{your game id}", "{your user id}");
 *  getSupportFragmentManager()
 *          .beginTransaction()
 *          .add(R.id.fragment_container, fragment)
 *          .commit();
 *  fragment.loadVideo("{game id}", "{user id}");
 *
 * 2. Implement {@link USeekPlayerCloseListener}
 *
 *      fragment.setUSeekPlayerCloseListener(new USeekPlayerCloseListener() {
 *          public void useekPlayerDidClosed(USeekPlayerView mUseekPlayerView) {
 *          }
 *
 *          public void useekPlayerDidFailWithError(USeekPlayerView mUseekPlayerView, WebResourceError error) {
 *          }
 *
 *          public void useekPlayerDidStartLoad(USeekPlayerView mUseekPlayerView) {
 *          }
 *
 *          public void useekPlayerDidFinishLoad(USeekPlayerView mUseekPlayerView) {
 *          }
 *      });
 *
 */

private const val ARG_GAME_ID = "gameId"
private const val ARG_USER_ID = "userId"

class USeekPlayerFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance(gameId: String, userId: String): USeekPlayerFragment =
            USeekPlayerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_GAME_ID, gameId)
                    putString(ARG_USER_ID, userId)
                }
            }
    }

    var gameId: String? = null

    var userId: String? = null

    var isCloseButtonHidden = true

    var loadingText: String? = null

    var listener: USeekPlayerCloseListener? = null
        set(value) {
            field = value
            playerView?.playerListener = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            gameId = it.getString(ARG_GAME_ID)
            userId = it.getString(ARG_USER_ID)
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_useek, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonClose.setOnClickListener {
            onCloseButtonPressed()
        }
        if (isCloseButtonHidden)
            buttonClose.visibility = View.INVISIBLE
        else
            buttonClose.visibility = View.VISIBLE

        playerView.playerListener = listener
        playerView.loadingText = loadingText
        loadVideo()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is USeekPlayerCloseListener)
            listener = context
    }

    override fun onDetach() {
        playerView?.destroy()
        listener = null
        super.onDetach()
    }

    /**
     * Start loading the video in USeekPlayerView
     *
     * - Precondition:  Publisher ID should be set
     * Game ID and User ID should be set when create Fragment instance.
     * This method will use game id and user id which set by newInstance method.
     *
     */
    fun loadVideo() {
        if (gameId != null && userId != null) {
            playerView.loadVideo(gameId!!, userId!!)
        }
    }

    /**
     * Start loading the video in USeekPlayerView
     *
     * - Precondition: Publisher ID should be set
     *
     * @param gameId    unique game id provided by USeek, not nullable
     * @param userId    user's unique id registered in USeek, nullable
     */
    fun loadVideo(gameId: String, userId: String) {
        this.gameId = gameId
        this.userId = userId
        playerView.loadVideo(gameId, userId)
    }

    private fun onCloseButtonPressed() {
        if (listener != null) {
            listener!!.useekPlayerDidClosed(playerView)
        }
    }

}
