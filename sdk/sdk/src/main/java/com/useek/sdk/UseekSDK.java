package com.useek.sdk;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class UseekSDK {
    Context context;
    String publisherId;
    String userId;
    Integer videoId;

    public final static String PLAYER_URL = "com.useek.useeksdk.PLAYER_URL";

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
        if(publisherId == null || userId == null || videoId == null) return;
        if(Build.VERSION.SDK_INT < 19) return;

        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putExtra(PLAYER_URL, getPlayerUrl());
        context.startActivity(intent);
    }

    private String getPlayerUrl() {
        return "https://www.useek.com/videos/" + String.valueOf(videoId) + "/embed_play?sdk_publisher=" + publisherId + "&external_user_id="+userId;
    }
}
