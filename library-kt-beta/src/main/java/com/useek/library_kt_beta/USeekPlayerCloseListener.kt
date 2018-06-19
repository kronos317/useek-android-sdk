package com.useek.library_kt_beta

interface USeekPlayerCloseListener: USeekPlayerListener {
    /**
     * Called when user clicked close button to dismiss the Activity or Fragment.
     *
     * @param useekPlayerView   USeekPlayerView object of closed
     */
    abstract fun useekPlayerDidClosed(useekPlayerView: USeekPlayerView)

}