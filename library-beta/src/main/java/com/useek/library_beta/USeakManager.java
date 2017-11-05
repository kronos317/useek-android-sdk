package com.useek.library_beta;

import android.os.AsyncTask;

import com.useek.library_beta.request.HttpClient;
import com.useek.library_beta.request.HttpRequest;
import com.useek.library_beta.request.RequestManager;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

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

    public void requestPoints(String gameId, String userId, final RequestPointsListener listener) {
        if (this.publisherId == null || this.publisherId.length() == 0) {
            listener.didFailure(new Error("Not set publisher id"));
            return;
        }
        if (gameId == null || gameId.length() == 0) {
            listener.didFailure(new Error("Invalid game id"));
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("publisherId", this.publisherId);
        params.put("gameId", gameId);
        if (userId != null && userId.length() > 0) {
            params.put("userId", userId);
        } else {
            params.put("userId", "");
        }

        String urlString = String.format("https://www.useek.com/sdk/1.0/%s/%s/get_points", this.publisherId, gameId);

        request(urlString, params, HttpClient.GET, new RequestManager.RequestCompleteListener() {
            @Override
            public void didSuccess(String response) {
                USeakPlaybackResultDataModel model = new USeakPlaybackResultDataModel(response);
                listener.didSuccess(model);
            }

            @Override
            public void didFailure(Error error) {
                listener.didFailure(error);
            }
        });

    }

    private void request(String url, HashMap<String, String> params, int requestType, final RequestManager.RequestCompleteListener callback) {
        HttpClient httpCall = new HttpClient();
        httpCall.setRequestType(requestType);
        httpCall.setUrl(url);
        httpCall.setParams(params);
        HttpRequest request = new HttpRequest(){
            @Override
            public void onResponse(String response) {
                callback.didSuccess(response);
            }

            @Override
            public void onError(Error error) {
                callback.didFailure(error);
            }
        };
        request.execute(httpCall);
    }

    public interface RequestPointsListener {
        void didSuccess(USeakPlaybackResultDataModel resultDataModel);
        void didFailure(Error error);
    }
}
