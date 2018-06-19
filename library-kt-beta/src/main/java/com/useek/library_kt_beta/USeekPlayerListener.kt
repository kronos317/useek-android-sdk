package com.useek.library_kt_beta

import android.webkit.WebResourceError

interface USeekPlayerListener {

    /**
     * Called when player detected an error while loading the video.
     *
     * @param useekPlayerView   USeekPlayerView object of failed
     * @param error             The Error object with error information
     */
    abstract fun useekPlayerDidFailWithError(useekPlayerView: USeekPlayerView, error: WebResourceError?)

    /**
     * Called when player did start loading the video.
     *
     * @param useekPlayerView   USeekPlayerView object of started loading
     */
    abstract fun useekPlayerDidStartLoad(useekPlayerView: USeekPlayerView)

    /**
     * Called when player did finish loading the video.
     *
     * @param useekPlayerView   USeekPlayerView object of finished loading
     */
    abstract fun useekPlayerDidFinishLoad(useekPlayerView: USeekPlayerView)

}