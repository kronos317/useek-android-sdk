package com.useek.library_kt_beta

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import java.net.URL

/**
 * This is custom layout which you can easily drop in XML resource or create anywhere in your code.
 *
 * There are 2 ways to use USeekPlayerView.
 *
 * 1. Add to XML resource layout file.
 *
 *        com.useek.library_kt_beta.USeekPlayerView
 *        android:id="@+id/playerView"
 *        android:layout_width="match_parent"
 *        android:layout_height="match_parent"
 *
 * 2. Add as a subview programmatically
 *
 *       useekView = new USeekPlayerView(this);
 *       useekContainer.addView(
 *           useekView.getView(),
 *               new LinearLayout.LayoutParams(
 *                   ViewGroup.LayoutParams.MATCH_PARENT,
 *                   ViewGroup.LayoutParams.MATCH_PARENT
 *               )
 *           );
 *       useekView.loadVideo("{mGameId}", "{mUserId"});
 *       useekView.setPlayerListener(this);
 *
 */
class USeekPlayerView : FrameLayout {

    var gameId: String? = null
    var userId: String? = null
    var loadingText: String? = null

    var status = VideoLoadStatus.NONE
    var isLoadingMaskHidden: Boolean = false

    var playerListener: USeekPlayerListener? = null

    private var mContext: Context? = null
    private val mTagName = resources.getString(R.string.library_name)

    private var mWebView: WebView? = null
    private lateinit var mLoadingMaskView: FrameLayout
    private lateinit var mLoadingTextView: TextView
    private var mCustomView: View? = null
    private lateinit var mInflater: LayoutInflater

    constructor(context: Context) : super(context) {
        init(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(context, attrs, defStyle)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyle: Int) {
        mContext = context

        val view = LayoutInflater.from(context).inflate(R.layout.useek_view, this, true)
        mWebView = view.findViewById(R.id.useek_web_view)
        mWebView!!.settings.loadWithOverviewMode = true
        mWebView!!.settings.useWideViewPort = true

        mLoadingMaskView = view.findViewById(R.id.loadingMaskView)
        mLoadingTextView = view.findViewById(R.id.loadingText)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        progressBar.indeterminateDrawable.setColorFilter(0xAAA, android.graphics.PorterDuff.Mode.SRC_ATOP)
        this.isLoadingMaskHidden = false

        // Load attributes
        val a = getContext().obtainStyledAttributes(
                attrs, R.styleable.USeekPlayerView, defStyle, 0)

        if (a.hasValue(R.styleable.USeekPlayerView_useek_loadingText)) {
            this.loadingText = a.getString(R.styleable.USeekPlayerView_useek_loadingText)
            this.mLoadingTextView.text = this.loadingText
        }

        a.recycle()
    }

    /**
     * Create USeekPlayerView as subview programmatically
     *
     * @return instance object of USeekPlayerView
     */
    fun getView(): View {
        if (mCustomView == null) {
            mCustomView = mInflater.inflate(R.layout.useek_view, null)
            mWebView = mCustomView!!.findViewById(R.id.useek_web_view)
            mWebView!!.settings.loadWithOverviewMode = true
            mWebView!!.settings.useWideViewPort = true

            mLoadingMaskView = mCustomView!!.findViewById(R.id.useek_loading_mask_view)
            mLoadingTextView = mCustomView!!.findViewById(R.id.useek_loading_text)
            val progressBar = mCustomView!!.findViewById<ProgressBar>(R.id.useek_view_progressbar)
            progressBar.indeterminateDrawable.setColorFilter(0xAAA, android.graphics.PorterDuff.Mode.SRC_ATOP)

            this.isLoadingMaskHidden = false
        }
        return mCustomView!!
    }

    /**
     * Load Video with current game id and user id
     */
    fun loadVideo() {
        if (gameId != null && userId != null)
            this.loadVideo(this.gameId!!, this.userId!!)
    }

    /**
     * Load Video with game id and user id
     * @param gameId    unique game id provided by USeek, not nullable
     * @param userId    user's unique id registered in USeek, nullable
     */
    fun loadVideo(gameId: String, userId: String) {
        this.gameId = gameId
        this.userId = userId

        if ((!this.validateConfiguration())!!) return

        val url = this.generateVideoUrl()
        if (url == null) {
            Log.e(mTagName, "Invalid USeek URL")
            return
        }

        val webView = mWebView
        if (webView == null) {
            Log.e(mTagName, "Not created web view.")
            return
        }

        /** Initialize WebView  */
        this.initializeWebView()

        /** Set mWebView listener  */
        val me = this
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
                super.onPageStarted(view, url, favicon)
                me.didStartLoadingWebView()
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                me.didFinishLoadingWebView()
            }

            override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                super.onReceivedError(view, request, error)
                me.didFailLoadingWebView(error)
            }
        }

        /** Load Video URL  */
        webView.loadUrl(url.toString())
    }

    /**
     * Destroy view
     */
    fun destroy() {
        this.removeView(mWebView)
        mWebView?.destroy()
        mWebView = null
    }


    /**
     * Generate Video url
     * @return url object with generated video url with publisher id, game id and user id
     */
    private fun generateVideoUrl(): URL? {
        return if (this.validateConfiguration()) {
            var urlString = String.format("https://www.useek.com/sdk/1.0/%s/%s/play",
                    USeekManager.sharedInstance.publisherId,
                    this.gameId)
            if (this.userId != null && this.userId!!.isNotEmpty()) {
                urlString += String.format("?external_user_id=%s", this.userId)
            }
            try {
                URL(urlString)
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }

    /**
     * Check validation for publish id and game id
     * @return boolean for validate information
     */
    private fun validateConfiguration(): Boolean {
        var isValid = true

        val publisherId = USeekManager.sharedInstance.publisherId

        if (publisherId == null) {
            isValid = false
            Log.e(mTagName, "Not set publisher id")
        } else if (publisherId.isEmpty()) {
            isValid = false
            Log.e(mTagName, "Invalid publisher id")
        }

        if (this.gameId == null) {
            isValid = false
            Log.e(mTagName, "Not set game id")
        } else if (this.gameId!!.isEmpty()) {
            isValid = false
            Log.e(mTagName, "Invalid game id")
        }
        return isValid
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initializeWebView() {
        val webView = mWebView
        if (webView != null) {
            val webSettings = webView.settings
            webSettings.javaScriptEnabled = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                webSettings.mediaPlaybackRequiresUserGesture = false
            }
            webView.setBackgroundColor(Color.TRANSPARENT)

        } else {
            Log.e(mTagName, "Not initialized web view.")
        }
    }

    /**
     * Started loading video(web view)
     */
    private fun didStartLoadingWebView() {
        Log.d(mTagName, "WebView didStartLoadingWebView")

        if (this.status == VideoLoadStatus.NONE) {
            if (this.playerListener != null) {
                this.playerListener!!.useekPlayerDidStartLoad(this)
            }
        }

        this.status = VideoLoadStatus.LOAD_STARTED
        if (!this.isLoadingMaskHidden) {
            this.animateLoadingMaskToShow()
        } else {
            this.mLoadingMaskView.visibility = View.GONE
        }

    }

    /**
     * Finished loading video(web view)
     */
    private fun didFinishLoadingWebView() {
        Log.d(mTagName, "WebView didFinishLoadingWebView")

        if (this.status != VideoLoadStatus.LOAD_FAILED && this.status != VideoLoadStatus.LOADED) {
            if (this.playerListener != null) {
                this.playerListener!!.useekPlayerDidFinishLoad(this)
            }
        }

        if (this.status != VideoLoadStatus.LOAD_FAILED) {
            this.status = VideoLoadStatus.LOADED
        }

        if (!this.isLoadingMaskHidden) {
            this.animateLoadingMaskToHide()
        } else {
            this.mLoadingMaskView.visibility = View.GONE
        }

    }

    /**
     * Failed loading video(web view)
     */
    private fun didFailLoadingWebView(error: WebResourceError) {
        var errorString: String? = error.toString()
        if (errorString == null) {
            errorString = ""
        }
        Log.d(mTagName, "WebView didFailLoadingWebView with Error:\n$errorString")

        if (this.status != VideoLoadStatus.LOAD_FAILED) {
            if (this.playerListener != null) {
                this.playerListener!!.useekPlayerDidFailWithError(this, error)
            }
        }

        this.status = VideoLoadStatus.LOAD_FAILED

        if (!this.isLoadingMaskHidden) {
            this.animateLoadingMaskToHide()
        } else {
            this.mLoadingMaskView.visibility = View.GONE
        }
    }

    /**
     * Animation of showing mLoadingMaskView
     */
    private fun animateLoadingMaskToShow() {
        this.mLoadingMaskView.visibility = View.VISIBLE
    }

    /**
     * Animation of hiding mLoadingMaskView
     */
    private fun animateLoadingMaskToHide() {
        this.mLoadingMaskView.visibility = View.GONE
    }


    enum class VideoLoadStatus {
        NONE, // Not stated loading
        LOAD_STARTED, // Started loading
        LOADED, // Completed loading
        LOAD_FAILED // Failed loading
    }
}
