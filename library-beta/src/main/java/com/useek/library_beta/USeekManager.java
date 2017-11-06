package com.useek.library_beta;

import com.useek.library_beta.request.HttpClient;
import com.useek.library_beta.request.HttpRequest;
import com.useek.library_beta.request.RequestManager;

import java.util.HashMap;

/**
 * Created by Chris Lin on 11/4/2017.
 *
 * This is singleton class for management USeek global variables
 * like as Publisher id, requesting for score.
 *
 */

public class USeekManager {

    private static final USeekManager ourInstance = new USeekManager();

    public static USeekManager sharedInstance() {
        return ourInstance;
    }

    private USeekManager() {
    }

    /**
     * Mutable property to set / get publisher id.
     *
     * - Warning : You should set publisher id before loading video.
     *
     * ---
     *
     * You can set publisher id in Application.java or Main Activity
     *
     *      ```
     *      ex : USeekManager.sharedInstance().setPublisherId("{your publisher id}");
     *      ```
     *
     */
    private String publisherId;

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    /**
     *
     * Queries the points user has gained while playing the game.
     * The centralized server will return users's points based on gameId and userId.
     *
     * - Precondition : Publisher ID should be set.
     *
     * @param gameId    unique game id provided by USeek (Not should be NULL)
     * @param userId    user's unique id registered in USeek (Optional)
     * @param listener  listener which will be triggered after response is successfully retrieved.
     *
     */
    public void requestPoints(String gameId, String userId, final RequestPointsListener listener) {

        // Check validate publisher id
        if (this.publisherId == null || this.publisherId.length() == 0) {
            listener.didFailure(new Error("Not set publisher id"));
            return;
        }

        // Check validate game id
        if (gameId == null || gameId.length() == 0) {
            listener.didFailure(new Error("Invalid game id"));
            return;
        }

        // Create parameter
        HashMap<String, String> params = new HashMap<>();
        params.put("publisherId", this.publisherId);
        params.put("gameId", gameId);
        if (userId != null && userId.length() > 0) {
            params.put("userId", userId);
        } else {
            params.put("userId", "");
        }

        // Create request url
        String urlString =
                String.format(
                        "https://www.useek.com/sdk/1.0/%s/%s/get_points",
                        this.publisherId, gameId
                );

        // execute request
        request(urlString, params, HttpClient.GET, new RequestManager.RequestCompleteListener() {
            @Override
            public void didSuccess(String response) {
                USeekPlaybackResultDataModel model = new USeekPlaybackResultDataModel(response);
                listener.didSuccess(model);
            }

            @Override
            public void didFailure(Error error) {
                listener.didFailure(error);
            }
        });

    }

    /**
     * Make and execute http request with native android request methods.
     *
     * @param url           request url
     * @param params        parameters for request
     * @param requestType   integer constant for GET or POST (GET : 1, POST = 2)
     *                      (defined on HttpClient class)
     *
     * @param callback      callback for response of request
     */
    private void request(String url,
                         HashMap<String, String> params,
                         int requestType,
                         final RequestManager.RequestCompleteListener callback)
    {
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

    /**
     * Interface for response of score points
     */
    public interface RequestPointsListener {
        void didSuccess(USeekPlaybackResultDataModel resultDataModel);
        void didFailure(Error error);
    }
}
