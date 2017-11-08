package com.useek.useek_example;

/**
 * Created by threek on 11/8/2017.
 */

public class ExampleSettingsManager
{
    private static final ExampleSettingsManager ourInstance = new ExampleSettingsManager();

    public static ExampleSettingsManager sharedInstance() {
        return ourInstance;
    }

    private ExampleSettingsManager() {
        this.publisherId = "60d95e35d89800b0ee499e60d0735fb8";
        this.gameId = "122";
        this.userId = "496953";
        this.loadingText = "Please wait while loading...";
        this.showCloseButton = true;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLoadingText() {
        return loadingText;
    }

    public void setLoadingText(String loadingText) {
        this.loadingText = loadingText;
    }

    public boolean isShowCloseButton() {
        return showCloseButton;
    }

    public void setShowCloseButton(boolean showCloseButton) {
        this.showCloseButton = showCloseButton;
    }

    private String publisherId;
    private String gameId;
    private String userId;
    private String loadingText;
    private boolean showCloseButton;

}
