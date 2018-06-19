package com.useek.library_kt_beta

import android.webkit.WebResourceError

typealias USeekPlayerDidFailHandler = (USeekPlayerView, WebResourceError?) -> Unit
typealias USeekPlayerDidStartHandler = (USeekPlayerView) -> Unit
typealias USeekPlayerDidFinishHandler = (USeekPlayerView) -> Unit
typealias RequestPointsHandler = (Int?, Int?, Error?) -> Unit
typealias RequestCompleteHandler = (String?, Error?) -> Unit

class Utilities {
}