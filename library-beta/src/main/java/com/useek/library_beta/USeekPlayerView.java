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
         useekView = new USeekPlayerView(this);
         useekContainer.addView(
             useekView.getView(),
                 new LinearLayout.LayoutParams(
                     ViewGroup.LayoutParams.MATCH_PARENT,
                     ViewGroup.LayoutParams.MATCH_PARENT
                 )
             );
         useekView.loadVideo("{gameId}", "{userId"});
         useekView.setPlayerListener(this);
 *
 */

public class USeekPlayerView extends FrameLayout {

    private final String tagName = getResources().getString(R.string.library_name);

    private WebView webView;
    private FrameLayout loadingMaskView;
    private TextView loadingTextView;

    private String gameId;
    private String userId;

    private VideoLoadStatus status = VideoLoadStatus.NONE;
    private Boolean isLoadingMaskHidden = false;

    private USeekPlayerListener playerListener;

    private String mLoadingTextString;

    private View mCustomView;
    private LayoutInflater mInflater;

    /**
     * Structures
     * @param context Activity Context
     */
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

        View view = LayoutInflater.from(context).inflate(R.layout.useek_view, this, true);
        webView = view.findViewById(R.id.useek_web_view);
        loadingMaskView = view.findViewById(R.id.useek_loading_mask_view);
        loadingTextView = view.findViewById(R.id.useek_loading_text);

        this.isLoadingMaskHidden = false;

        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.USeekPlayerView, defStyle, 0);

        if (a.hasValue(R.styleable.USeekPlayerView_useek_loadingText)) {
            this.mLoadingTextString = a.getString(R.styleable.USeekPlayerView_useek_loadingText);
            this.loadingTextView.setText(this.mLoadingTextString);
        }

        a.recycle();
    }

    /**
     * Create USeekPlayerView programmatically
     *
     * @return instance object of USeekPlayerView
     */
    public View getView() {
        if (mCustomView == null) {
            mCustomView = mInflater.inflate(R.layout.useek_view, null);
            webView = mCustomView.findViewById(R.id.useek_web_view);
            loadingMaskView = mCustomView.findViewById(R.id.useek_loading_mask_view);
            loadingTextView = mCustomView.findViewById(R.id.useek_loading_text);

            this.isLoadingMaskHidden = false;
        }
        return mCustomView;
    }
    /**
     * Set unique game id provided by USeek
     * @param gameId String, Non-nullable
     */
    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    /**
     * Get unique game id
     * @return gameId: String
     */
    public String getGameId() {
        return gameId;
    }

    /**
     * Set user's unique id registered on USeek
     * @param userId    Nullable
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Get user's unique id registered on USeek
     * @return userId: String
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Get status of video loading
     *
     * @return one of NONE, LOAD_STARTED, LOADED, LOAD_FAILED
     */
    public VideoLoadStatus getStatus() {
        return status;
    }

    private void setStatus(VideoLoadStatus status) {
        this.status = status;
    }

    /**
     * Set listener for video loading status
     * @param playerListener    USeekPlayerListener
     */
    public void setPlayerListener(USeekPlayerListener playerListener) {
        this.playerListener = playerListener;
    }

    private USeekPlayerListener getPlayerListener() {
        return playerListener;
    }

    /**
     * Generate Video url
     * @return url object with generated video url
     */
    public URL generateVideoUrl() {
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
    public Boolean validateConfiguration() {
        Boolean isValid = true;

        String publisherId = USeekManager.sharedInstance().getPublisherId();

        if (publisherId == null) {
            isValid = false;
            Log.e(tagName, "Not set publisher id");
        } else if (publisherId.length() == 0){
            isValid = false;
            Log.e(tagName, "Invalid publisher id");
        }

        if (this.getGameId() == null) {
            isValid = false;
            Log.e(tagName, "Not set game id");
        } else if (this.getGameId().length() == 0) {
            isValid = false;
            Log.e(tagName, "Invalid game id");
        }
        return isValid;
    }

    /**
     * Load Video with existing GameId and UserId
     */
    public void loadVideo() {
        this.loadVideo(this.gameId, this.userId);
    }

    /**
     * Load Video with GameId and UserId
     * @param gameId    unique game id provided by USeek, not nullable
     * @param userId    user's unique id registered in USeek, nullable
     */
    public void loadVideo(String gameId, String userId) {
        this.setGameId(gameId);
        this.setUserId(userId);

        if (!this.validateConfiguration()) return;

        URL url = this.generateVideoUrl();
        if (url == null) {
            Log.e(tagName, "Invalid USeek URL");
            return;
        }

        /** Initialize WebView */
        this.initializeWebView();

        /** Set webView listener */
        final USeekPlayerView _this = this;
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                _this.startedLoadingWebView();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                _this.finishedLoadingWebView();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                _this.failedLoadingWebView(error);
            }
        });

        /** Load Video URL */
        webView.loadUrl(url.toString());

    }

    private void initializeWebView() {
        if (webView != null) {
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                webSettings.setMediaPlaybackRequiresUserGesture(false);
            }
            webView.setBackgroundColor(Color.TRANSPARENT);

        } else {
            Log.e(tagName, "Not initialized web view.");
        }
    }

    /**
     * Start loading video(web view)
     */
    private void startedLoadingWebView() {
        Log.d(tagName, "WebView didStartLoad");

        if (this.getStatus() == VideoLoadStatus.NONE) {
            if (this.playerListener != null) {
                this.playerListener.didStartLoad();
            }
        }

        this.setStatus(VideoLoadStatus.LOAD_STARTED);
        if (!this.isLoadingMaskHidden) {
            this.animateLoadingMaskToShow();
        } else {
            this.loadingMaskView.setVisibility(GONE);
        }

    }

    /**
     * Finished loading video(web view)
     */
    private void finishedLoadingWebView() {
        Log.d(tagName, "WebView didFinishLoad");

        if (this.status != VideoLoadStatus.LOAD_FAILED &&
                this.status != VideoLoadStatus.LOADED) {
            if (this.playerListener != null) {
                this.playerListener.didFinishLoad();
            }
        }

        if (this.status != VideoLoadStatus.LOAD_FAILED) {
            this.setStatus(VideoLoadStatus.LOADED);
        }

        if (!this.isLoadingMaskHidden) {
            this.animateLoadingMaskToHide();
        } else {
            this.loadingMaskView.setVisibility(GONE);
        }

    }

    /**
     * Failed loading video(web view)
     */
    private void failedLoadingWebView(WebResourceError error) {
        String errorString = error.toString();
        if (errorString == null) {
            errorString = "";
        }
        Log.d(tagName, "WebView didFailLoad with Error:\n" + errorString);

        if (this.status != VideoLoadStatus.LOAD_FAILED) {
            if (this.playerListener != null) {
                this.playerListener.didFailedWithError(error);
            }
        }

        this.setStatus(VideoLoadStatus.LOAD_FAILED);

        if (!this.isLoadingMaskHidden) {
            this.animateLoadingMaskToHide();
        } else {
            this.loadingMaskView.setVisibility(GONE);
        }
    }

    /**
     * Animation of showing loadingMaskView
     */
    private void animateLoadingMaskToShow() {
        this.loadingMaskView.setVisibility(VISIBLE);
    }

    /**
     * Animation of hiding loadingMaskView
     */
    private void animateLoadingMaskToHide() {
        this.loadingMaskView.setVisibility(GONE);
    }
}
