package com.useek.library_beta;

import android.webkit.WebResourceError;

/**
 * Created by Chris Lin on 11/4/2017.
 */

public interface USeekPlayerListener {

    /**
     * Called when player detected an error while loading the video.
     *
     * @param useekPlayerView   USeekPlayerView object of failed
     * @param error             The Error object with error information
     */
    void useekPlayerDidFailWithError(USeekPlayerView useekPlayerView, WebResourceError error);

    /**
     * Called when player did start loading the video.
     *
     * @param useekPlayerView   USeekPlayerView object of started loading
     */
    void useekPlayerDidStartLoad(USeekPlayerView useekPlayerView);

    /**
     * Called when player did finish loading the video.
     *
     * @param useekPlayerView   USeekPlayerView object of finished loading
     */
    void useekPlayerDidFinishLoad(USeekPlayerView useekPlayerView);

}