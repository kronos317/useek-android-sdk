package com.useek.library_beta;

/**
 * Created by Chris Lin on 11/4/2017.
 */

public interface USeekPlayerCloseListener extends USeekPlayerListener {

    /**
     * Called when user clicked close button to dismiss the Activity or Fragment.
     */
    void didClosed();

}
