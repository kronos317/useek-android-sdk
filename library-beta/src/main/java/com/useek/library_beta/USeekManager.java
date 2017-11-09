package com.useek.library_beta;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chris Lin on 11/4/2017.
 *
 * This singleton class provides the following features
 *
 *  - Set / Retrieve publisher id
 *  - Request server for the points of certain user based on game id and publisher id
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
     * Setter for publisher id
     *
     * - Warning : You should set publisher id before loading video.
     *
     * You can set publisher id in Application.java or Main Activity
     *
     *      ex : USeekManager.sharedInstance().setPublisherId("{your publisher id}");
     *
     * @param publisherId   publisher id provided by USeek
     */
    public void setPublisherId(String publisherId) {
        this.mPublisherId = publisherId;
    }

    /**
     * Getter for publisher id
     *
     * @return              String for publisher id
     *
     */
    public String getPublisherId() {
        return mPublisherId;
    }

    private String mPublisherId;

    /**
     *
     * Queries the mLastPlayPoints user has gained while playing the game.
     * The centralized server will return users's mLastPlayPoints based on mGameId and mUserId.
     *
     * - Precondition : Publisher ID should be set.
     *
     * @param gameId    unique game id provided by USeek (Not should be NULL)
     * @param userId    user's unique id registered in USeek (Optional)
     * @param listener  RequestPointsListener instance which will be triggered
     *                  after response is successfully retrieved.
     *
     */
    public void requestPoints(String gameId, String userId, final RequestPointsListener listener) {

        // Check validate publisher id
        if (this.mPublisherId == null || this.mPublisherId.length() == 0) {
            listener.useekRequestForPlayPointsDidFail(new Error("Not set publisher id"));
            return;
        }

        // Check validate game id
        if (gameId == null || gameId.length() == 0) {
            listener.useekRequestForPlayPointsDidFail(new Error("Invalid game id"));
            return;
        }

        // Create parameter
        HashMap<String, String> params = new HashMap<>();
        if (userId != null && userId.length() > 0) {
            params.put("external_user_id", userId);
        } else {
            params.put("external_user_id", "");
        }

        // Create request url
        String urlString =
                String.format(
                        "https://www.useek.com/sdk/1.0/%s/%s/get_points",
                        this.mPublisherId, gameId
                );

        // execute request
        request(urlString, params, HttpClient.GET, new RequestCompleteListener() {
            @Override
            public void didSuccess(String response) {
                USeekPlaybackResultDataModel model = new USeekPlaybackResultDataModel(response);
                listener.useekRequestForPlayPointsDidSuccess(model.getLastPlayPoints(), model.getTotalPlayPoints());
            }

            @Override
            public void didFailure(Error error) {
                listener.useekRequestForPlayPointsDidFail(error);
            }
        });

    }

    /**
     * Make and execute http request with native android request methods.
     *
     * @param url           request url
     * @param params        parameters for request
     * @param requestType   integer constant for GET or POST
     *                      (GET : 1, POST = 2 : defined on HttpClient class)
     *
     * @param callback      callback for response of request
     */
    private void request(String url,
                         HashMap<String, String> params,
                         int requestType,
                         final RequestCompleteListener callback)
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
     * Interface to retrieve points for the game plays
     */
    public interface RequestPointsListener {
        void useekRequestForPlayPointsDidSuccess(int lastPlayPoints, int totalPlayPoints);
        void useekRequestForPlayPointsDidFail(Error error);
    }
}

class HttpClient {

    static final int GET = 1;
    static final int POST = 2;

    private String url;
    private int mRequestType;
    private HashMap<String,String> params ;

    String getUrl() {
        return url;
    }

    void setUrl(String url) {
        this.url = url;
    }

    int getRequestType() {
        return mRequestType;
    }

    void setRequestType(int requestType) {
        this.mRequestType = requestType;
    }

    HashMap<String, String> getParams() {
        return params;
    }

    void setParams(HashMap<String, String> params) {
        this.params = params;
    }

}

class HttpRequest extends AsyncTask<HttpClient, String, String> {

    private static final String UTF_8 = "UTF-8";

    @Override
    protected String doInBackground(HttpClient... httpClients) {
        HttpURLConnection urlConnection = null;
        HttpClient httpCall = httpClients[0];
        StringBuilder response = new StringBuilder();
        try{
            String dataParams = getDataString(httpCall.getParams(), httpCall.getRequestType());
            URL url = new URL(httpCall.getRequestType() == HttpClient.GET ? httpCall.getUrl() + dataParams : httpCall.getUrl());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(httpCall.getRequestType() == HttpClient.GET ? "GET" : "POST");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(30000 /* milliseconds */);
            if(httpCall.getParams() != null && httpCall.getRequestType() == HttpClient.POST){
                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, UTF_8));
                writer.append(dataParams);
                writer.flush();
                writer.close();
                os.close();
            }
            int responseCode = urlConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                String line ;
                BufferedReader br = new BufferedReader( new InputStreamReader(urlConnection.getInputStream()));
                while ((line = br.readLine()) != null){
                    response.append(line);
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            onError(new Error(e.toString()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            onError(new Error(e.toString()));
        } catch (IOException e) {
            e.printStackTrace();
            onError(new Error(e.toString()));
        }finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
        return response.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        onResponse(s);
    }

    public void onResponse(String response){

    }

    public void onError(Error error) {

    }

    private String getDataString(HashMap<String,String> params, int methodType) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean isFirst = true;
        for(Map.Entry<String,String> entry : params.entrySet()){
            if (isFirst){
                isFirst = false;
                if(methodType == HttpClient.GET){
                    result.append("?");
                }
            }else{
                result.append("&");
            }
            result.append(URLEncoder.encode(entry.getKey(), UTF_8));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), UTF_8));
        }
        return result.toString();
    }

}

class USeekPlaybackResultDataModel {

    /// Properties
    private String mPublisherId;
    private String mGameId;
    private String mUserId;
    private String mInternalUserId;

    private Boolean mFinished = false;
    private int mLastPlayPoints = 0;
    private int mTotalPlayPoints = 0;

    String getGameId() {
        return mGameId;
    }

    String getUserId() {
        return mUserId;
    }

    String getInternalUserId() {
        return mInternalUserId;
    }

    int getLastPlayPoints() {
        return mLastPlayPoints;
    }

    int getTotalPlayPoints() {
        return mTotalPlayPoints;
    }

    public Boolean getmFinished() {
        return mFinished;
    }

    USeekPlaybackResultDataModel(String string) {
        try {
            JSONObject obj = new JSONObject(string);
            if (obj.has("publisherId"))
                this.mPublisherId = obj.getString("publisherId");
            if (obj.has("gameId"))
                this.mGameId = obj.getString("gameId");
            if (obj.has("externalUserId"))
                this.mUserId = obj.getString("externalUserId");
            if (obj.has("userId"))
                this.mInternalUserId = obj.getString("userId");
            if (obj.has("lastPlayPoints"))
                this.mLastPlayPoints = obj.getInt("lastPlayPoints");
            if (obj.has("totalPoints"))
                this.mTotalPlayPoints = obj.getInt("totalPoints");
            if (obj.has("finished"))
                this.mFinished = obj.getBoolean("finished");
        } catch (Throwable t) {
            Log.e("USeek", "Incomplete parsing response data");
        }
    }

    public USeekPlaybackResultDataModel(HashMap<String, Object> data) {
        this.mPublisherId = data.get("publisherId").toString();
        this.mGameId = data.get("gameId").toString();
        this.mUserId = data.get("externalUserId").toString();
        this.mInternalUserId = data.get("userId").toString();
        this.mLastPlayPoints = (int)data.get("lastPlayPoints");
        this.mTotalPlayPoints = (int)data.get("totalPoints");
        this.mFinished = (boolean) data.get("finished");
    }

    public USeekPlaybackResultDataModel(JSONObject jsonObject) {
        try {
            JSONObject obj = jsonObject;
            if (obj.has("publisherId"))
                this.mPublisherId = obj.getString("publisherId");
            if (obj.has("gameId"))
                this.mGameId = obj.getString("gameId");
            if (obj.has("externalUserId"))
                this.mUserId = obj.getString("externalUserId");
            if (obj.has("userId"))
                this.mInternalUserId = obj.getString("userId");
            if (obj.has("lastPlayPoints"))
                this.mLastPlayPoints = obj.getInt("lastPlayPoints");
            if (obj.has("totalPoints"))
                this.mTotalPlayPoints = obj.getInt("totalPoints");
            if (obj.has("finished"))
                this.mFinished = obj.getBoolean("finished");
        } catch (Throwable t) {
            Log.e("USeek", "Incomplete parsing response data");
        }
    }

}

interface RequestCompleteListener {
    void didSuccess(String response);
    void didFailure(Error error);
}
