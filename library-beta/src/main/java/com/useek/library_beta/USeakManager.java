package com.useek.library_beta;

/**
 * Created by threek on 11/4/2017.
 */

public class USeakManager {

    private static final USeakManager ourInstance = new USeakManager();

    public static USeakManager sharedInstance() {
        return ourInstance;
    }

    private String publisherId;

    private USeakManager() {
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }
}
