package com.useek.library_beta;

/**
 * Created by Chris Lin on 11/4/2017.
 *
 * Interface to retrieve close fragment or activity
 *
 */

public interface USeekPlayerCloseListener extends USeekPlayerListener {

    /**
     * Called when user clicked close button to dismiss the Activity or Fragment.
     *
     * @param useekPlayerView   USeekPlayerView object of closed
     */
    void useekPlayerDidClosed(USeekPlayerView useekPlayerView);

}
