package com.useek.library_beta;

/**
 * Created by threek on 11/4/2017.
 */

public interface USeakPlayerCloseListener extends USeakPlayerListener {

    /**
     * Called when user clicked close button to dismiss the Activity or Fragment.
     */
    void didClosed();

}
