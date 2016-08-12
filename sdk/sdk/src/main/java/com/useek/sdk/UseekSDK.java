package com.useek.sdk;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class UseekSDK {
    Activity activity;
    LocationTracker tracker;

    String publisherId;
    String userId;
    Integer videoId;

    public final static String SDK_URL = "com.useek.useeksdk.SDK_URL";
    public final static String SDK_VERSION = "1.0";

    public UseekSDK(Activity activity) {
        this.activity = activity;
        try {
            this.tracker = new LocationTracker(activity);
        } catch (Error err) {
            Log.e("UseekSDK", "LocationTracker trigger error, probably due to missing com.google.android.gms:play-services-location.");
        }
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
        loadWebview(getSdkUrl(String.valueOf(videoId)));
    }

    public void showRewards() {
        if (publisherId == null || userId == null) return;
        String location = "";
        if(tracker != null) location = "location=" + tracker.getCoordinates();
        loadWebview(getSdkUrl("rewards", location));
    }

    private String getSdkUrl(String path) {
        return getSdkUrl(path, "");
    }

    private String getSdkUrl(String path, String queryString) {
        if(userId != null) {
            if(queryString.length() > 0) queryString += "&";
            queryString += "external_user_id=" + userId;
        }
        return "https://www.useek.com/sdk/" + SDK_VERSION + "/" + publisherId + "/" + path + "?" + queryString;
    }

    private void loadWebview(String url) {
        Intent intent = new Intent(activity, WebviewActivity.class);
        intent.putExtra(SDK_URL, url);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }
}
