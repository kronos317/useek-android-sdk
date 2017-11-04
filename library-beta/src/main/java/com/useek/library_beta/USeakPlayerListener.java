package com.useek.library_beta;

import android.webkit.WebResourceError;

/**
 * Created by Chris Lin on 11/4/2017.
 */

public interface USeakPlayerListener {

    /**
     * Called when player detected an error while loading the video.
     *
     * @param error : The Error object with error information
     */
    void didFailedWithError(WebResourceError error);

    /**
     * Called when player starts loading the video.
     */
    void didStartLoad();

    /**
     * Called when player finished loading the video.
     */
    void didFinishLoad();

}