package com.useek.library_beta;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by threek on 11/5/2017.
 */

public class USeakPlaybackResultDataModel {

    private String publisherId;
    private String gameId;
    private String userId;
    private Boolean finished = false;
    private int points = 0;

    public USeakPlaybackResultDataModel(String string) {
        try {
            JSONObject obj = new JSONObject(string);
            this.publisherId = obj.getString("publisherId");
            this.gameId = obj.getString("gameId");
            this.userId = obj.getString("userId");
            this.points = obj.getInt("lastPlayPoints");
            this.finished = obj.getBoolean("finished");
        } catch (Throwable t) {

        }
    }

    public USeakPlaybackResultDataModel(HashMap<String, Object> data) {
        this.publisherId = data.get("publisherId").toString();
        this.gameId = data.get("gameId").toString();
        this.userId = data.get("userId").toString();
        this.points = (int)data.get("lastPlayPoints");
        this.finished = (boolean) data.get("finished");
    }

    public USeakPlaybackResultDataModel(JSONObject jsonObject) {
        try {
            this.publisherId = jsonObject.getString("publisherId");
            this.gameId = jsonObject.getString("gameId");
            this.userId = jsonObject.getString("userId");
            this.points = jsonObject.getInt("lastPlayPoints");
            this.finished = jsonObject.getBoolean("finished");
        } catch (Throwable t) {

        }
    }

}
