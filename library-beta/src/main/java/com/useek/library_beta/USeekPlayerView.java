package com.useek.library_beta;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;

/**
 * This is custom layout which you can easily drop in XML resource or create anywhere in your code.
 *
 * There are 2 ways to use USeekPlayerView.
 *
 * 1. Add to XML resource layout file.
 *
 *        com.useek.library_beta.USeekPlayerView
 *        android:id="@+id/useek_player_view"
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
public class USeekPlayerView extends FrameLayout {

    private Context mContext;
    private final String mTagName = getResources().getString(R.string.library_name);

    private WebView mWebView;
    private FrameLayout mLoadingMaskView;
    private TextView mLoadingTextView;
    private View mCustomView;
    private LayoutInflater mInflater;

    private String mGameId;
    private String mUserId;
    private String mLoadingText;

    private VideoLoadStatus mStatus = VideoLoadStatus.NONE;
    private Boolean mIsLoadingMaskHidden = false;

    private USeekPlayerListener mPlayerListener;


    public USeekPlayerView(Context context) {
        super(context);
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        init(context, null, 0);
    }

    public USeekPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public USeekPlayerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {

        mContext = context;

        View view = LayoutInflater.from(context).inflate(R.layout.useek_view, this, true);
        mWebView = view.findViewById(R.id.useek_web_view);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);

        mLoadingMaskView = view.findViewById(R.id.useek_loading_mask_view);
        mLoadingTextView = view.findViewById(R.id.useek_loading_text);
        ProgressBar progressBar = view.findViewById(R.id.useek_view_progressbar);
        progressBar.getIndeterminateDrawable().setColorFilter(0xAAA, android.graphics.PorterDuff.Mode.SRC_ATOP);
        this.mIsLoadingMaskHidden = false;

        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.USeekPlayerView, defStyle, 0);

        if (a.hasValue(R.styleable.USeekPlayerView_useek_loadingText)) {
            this.mLoadingText = a.getString(R.styleable.USeekPlayerView_useek_loadingText);
            this.mLoadingTextView.setText(this.mLoadingText);
        }

        a.recycle();
    }

    /**
     * Set placeholder text for loading video
     * @param loadingText     String to set placeholder text while loading video
     */
    public void setLoadingText(String loadingText) {
        this.mLoadingText = loadingText;
    }

    /**
     * Create USeekPlayerView as subview programmatically
     *
     * @return instance object of USeekPlayerView
     */
    public View getView() {
        if (mCustomView == null) {
            mCustomView = mInflater.inflate(R.layout.useek_view, null);
            mWebView = mCustomView.findViewById(R.id.useek_web_view);
            mWebView.getSettings().setLoadWithOverviewMode(true);
            mWebView.getSettings().setUseWideViewPort(true);

            mLoadingMaskView = mCustomView.findViewById(R.id.useek_loading_mask_view);
            mLoadingTextView = mCustomView.findViewById(R.id.useek_loading_text);
            ProgressBar progressBar = mCustomView.findViewById(R.id.useek_view_progressbar);
            progressBar.getIndeterminateDrawable().setColorFilter(0xAAA, android.graphics.PorterDuff.Mode.SRC_ATOP);

            this.mIsLoadingMaskHidden = false;
        }
        return mCustomView;
    }

    /**
     * Set unique game id provided by USeek
     * @param gameId    String object of unique game id provided by USeek, Non-nullable
     */
    public void setGameId(String gameId) {
        this.mGameId = gameId;
    }

    /**
     * Get unique game id
     * @return mGameId   String object of unique game id
     */
    public String getGameId() {
        return mGameId;
    }

    /**
     * Set user's unique id registered on USeek
     * @param userId    String object of user's unique id registered on USeek, Nullable
     */
    public void setUserId(String userId) {
        this.mUserId = userId;
    }

    /**
     * Get user's unique id registered on USeek
     * @return mUserId   String object of user's unique id
     */
    public String getUserId() {
        return mUserId;
    }

    /**
     * Set listener for video loading mStatus
     * @param playerListener    USeekPlayerListener instance
     */
    public void setPlayerListener(USeekPlayerListener playerListener) {
        this.mPlayerListener = playerListener;
    }

    private USeekPlayerListener getPlayerListener() {
        return mPlayerListener;
    }

    /**
     * Load Video with current game id and user id
     */
    public void loadVideo() {
        this.loadVideo(this.mGameId, this.mUserId);
    }

    /**
     * Load Video with game id and user id
     * @param gameId    unique game id provided by USeek, not nullable
     * @param userId    user's unique id registered in USeek, nullable
     */
    public void loadVideo(String gameId, String userId) {
        this.setGameId(gameId);
        this.setUserId(userId);

        if (!this.validateConfiguration()) return;

        URL url = this.generateVideoUrl();
        if (url == null) {
            Log.e(mTagName, "Invalid USeek URL");
            return;
        }

        /** Initialize WebView */
        this.initializeWebView();

        /** Set mWebView listener */
        final USeekPlayerView _this = this;
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                _this.didStartLoadingWebView();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                _this.didFinishLoadingWebView();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                _this.didFailLoadingWebView(error);
            }
        });

        /** Load Video URL */
        mWebView.loadUrl(url.toString());

    }

    /**
     * Generate Video url
     * @return url object with generated video url with publisher id, game id and user id
     */
    private URL generateVideoUrl() {
        if (this.validateConfiguration()) {
            String urlString = String.format("https://www.useek.com/sdk/1.0/%s/%s/play",
                    USeekManager.sharedInstance().getPublisherId(),
                    this.getGameId());
            if (this.getUserId() != null && this.getUserId().length() > 0) {
                urlString = urlString + String.format("?external_user_id=%s", this.getUserId());
            }
            try {
                URL url = new URL(urlString);
                return url;
            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Check validation for publish id and game id
     * @return boolean for validate information
     */
    private Boolean validateConfiguration() {
        Boolean isValid = true;

        String publisherId = USeekManager.sharedInstance().getPublisherId();

        if (publisherId == null) {
            isValid = false;
            Log.e(mTagName, "Not set publisher id");
        } else if (publisherId.length() == 0){
            isValid = false;
            Log.e(mTagName, "Invalid publisher id");
        }

        if (this.getGameId() == null) {
            isValid = false;
            Log.e(mTagName, "Not set game id");
        } else if (this.getGameId().length() == 0) {
            isValid = false;
            Log.e(mTagName, "Invalid game id");
        }
        return isValid;
    }

    private void initializeWebView() {
        if (mWebView != null) {
            WebSettings webSettings = mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                webSettings.setMediaPlaybackRequiresUserGesture(false);
            }
            mWebView.setBackgroundColor(Color.TRANSPARENT);

        } else {
            Log.e(mTagName, "Not initialized web view.");
        }
    }

    /**
     * Started loading video(web view)
     */
    private void didStartLoadingWebView() {
        Log.d(mTagName, "WebView didStartLoadingWebView");

        if (this.mStatus == VideoLoadStatus.NONE) {
            if (this.mPlayerListener != null) {
                this.mPlayerListener.useekPlayerDidStartLoad(this);
            }
        }

        this.mStatus = VideoLoadStatus.LOAD_STARTED;
        if (!this.mIsLoadingMaskHidden) {
            this.animateLoadingMaskToShow();
        } else {
            this.mLoadingMaskView.setVisibility(GONE);
        }

    }

    /**
     * Finished loading video(web view)
     */
    private void didFinishLoadingWebView() {
        Log.d(mTagName, "WebView didFinishLoadingWebView");

        if (this.mStatus != VideoLoadStatus.LOAD_FAILED &&
                this.mStatus != VideoLoadStatus.LOADED) {
            if (this.mPlayerListener != null) {
                this.mPlayerListener.useekPlayerDidFinishLoad(this);
            }
        }

        if (this.mStatus != VideoLoadStatus.LOAD_FAILED) {
            this.mStatus = VideoLoadStatus.LOADED;
        }

        if (!this.mIsLoadingMaskHidden) {
            this.animateLoadingMaskToHide();
        } else {
            this.mLoadingMaskView.setVisibility(GONE);
        }

    }

    /**
     * Failed loading video(web view)
     */
    private void didFailLoadingWebView(WebResourceError error) {
        String errorString = error.toString();
        if (errorString == null) {
            errorString = "";
        }
        Log.d(mTagName, "WebView didFailLoadingWebView with Error:\n" + errorString);

        if (this.mStatus != VideoLoadStatus.LOAD_FAILED) {
            if (this.mPlayerListener != null) {
                this.mPlayerListener.useekPlayerDidFailWithError(this, error);
            }
        }

        this.mStatus = VideoLoadStatus.LOAD_FAILED;

        if (!this.mIsLoadingMaskHidden) {
            this.animateLoadingMaskToHide();
        } else {
            this.mLoadingMaskView.setVisibility(GONE);
        }
    }

    /**
     * Animation of showing mLoadingMaskView
     */
    private void animateLoadingMaskToShow() {
        this.mLoadingMaskView.setVisibility(VISIBLE);
    }

    /**
     * Animation of hiding mLoadingMaskView
     */
    private void animateLoadingMaskToHide() {
        this.mLoadingMaskView.setVisibility(GONE);
    }


    enum VideoLoadStatus {
        NONE,           // Not stated loading
        LOAD_STARTED,   // Started loading
        LOADED,         // Completed loading
        LOAD_FAILED,    // Failed loading
    }
}
