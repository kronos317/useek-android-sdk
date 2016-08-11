package com.useek.sdk;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class UseekSDK {
    Context context;
    String publisherId;
    String userId;
    Integer videoId;

    public final static String SDK_URL = "com.useek.useeksdk.SDK_URL";
    public final static String SDK_VERSION = "1.0";

    public UseekSDK(Context context) {
        this.context = context;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
    }

    public void playVideo() {
        if (publisherId == null || videoId == null) return;
        if (Build.VERSION.SDK_INT < 19) return;
        loadWebview(getSdkUrl() + String.valueOf(videoId));
    }

    public void showRewards() {
        if (publisherId == null || userId == null) return;
        loadWebview(getSdkUrl() + "rewards");
    }

    private String appendUserId(String url) {
        if(userId != null) url += "?external_user_id=" + userId;
        return url;
    }

    private String getSdkUrl() {
        return "http://www.useek.com/sdk/" + SDK_VERSION + '/' + publisherId + '/';
    }

    private void loadWebview(String url) {
        Intent intent = new Intent(context, WebviewActivity.class);
        intent.putExtra(SDK_URL, appendUserId(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
