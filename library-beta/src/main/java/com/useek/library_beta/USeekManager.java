package com.useek.library_beta;

import android.os.AsyncTask;

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

    private String publisherId;


    /**
     * Mutable property to set / get publisher id.
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
        this.publisherId = publisherId;
    }

    public String getPublisherId() {
        return publisherId;
    }

    /**
     *
     * Queries the lastPlayPoints user has gained while playing the game.
     * The centralized server will return users's lastPlayPoints based on gameId and userId.
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
//        params.put("publisherId", this.publisherId);
//        params.put("gameId", gameId);
        if (userId != null && userId.length() > 0) {
            params.put("external_user_id", userId);
        } else {
            params.put("external_user_id", "");
        }

        // Create request url
        String urlString =
                String.format(
                        "https://www.useek.com/sdk/1.0/%s/%s/get_points",
                        this.publisherId, gameId
                );

        // execute request
        request(urlString, params, HttpClient.GET, new RequestCompleteListener() {
            @Override
            public void didSuccess(String response) {
                USeekPlaybackResultDataModel model = new USeekPlaybackResultDataModel(response);
                listener.didSuccess(model.getLastPlayPoints(), model.getTotalPlayPoints());
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
     * Interface for response of score lastPlayPoints
     */
    public interface RequestPointsListener {
        void didSuccess(int lastPlayPoints, int totalPlayPoints);
        void didFailure(Error error);
    }
}

class HttpClient {

    public static final int GET = 1;
    public static final int POST = 2;

    private String url;
    private int requestType;
    private HashMap<String,String> params ;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public void setParams(HashMap<String, String> params) {
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
    private String publisherId;
    private String gameId;
    private String userId;
    private String externalUserId;

    private Boolean finished = false;
    private int lastPlayPoints = 0;
    private int totalPlayPoints = 0;

    public String getGameId() {
        return gameId;
    }

    public String getUserId() {
        return userId;
    }

    public String getExternalUserId() {
        return externalUserId;
    }

    public int getLastPlayPoints() {
        return lastPlayPoints;
    }

    public int getTotalPlayPoints() {
        return totalPlayPoints;
    }

    public Boolean getFinished() {
        return finished;
    }

    public USeekPlaybackResultDataModel(String string) {
        try {
            JSONObject obj = new JSONObject(string);
            if (obj.has("publisherId"))
                this.publisherId = obj.getString("publisherId");
            if (obj.has("gameId"))
                this.gameId = obj.getString("gameId");
            if (obj.has("userId"))
                this.userId = obj.getString("userId");
            if (obj.has("externalUserId"))
                this.externalUserId = obj.getString("externalUserId");
            if (obj.has("lastPlayPoints"))
                this.lastPlayPoints = obj.getInt("lastPlayPoints");
            if (obj.has("totalPoints"))
                this.totalPlayPoints = obj.getInt("totalPoints");
            if (obj.has("finished"))
                this.finished = obj.getBoolean("finished");
        } catch (Throwable t) {

        }
    }

    public USeekPlaybackResultDataModel(HashMap<String, Object> data) {
        this.publisherId = data.get("publisherId").toString();
        this.gameId = data.get("gameId").toString();
        this.userId = data.get("userId").toString();
        this.lastPlayPoints = (int)data.get("lastPlayPoints");
        this.finished = (boolean) data.get("finished");
    }

    public USeekPlaybackResultDataModel(JSONObject jsonObject) {
        try {
            JSONObject obj = jsonObject;
            if (obj.has("publisherId"))
                this.publisherId = obj.getString("publisherId");
            if (obj.has("gameId"))
                this.gameId = obj.getString("gameId");
            if (obj.has("userId"))
                this.userId = obj.getString("userId");
            if (obj.has("lastPlayPoints"))
                this.lastPlayPoints = obj.getInt("lastPlayPoints");
            if (obj.has("totalPoints"))
                this.totalPlayPoints = obj.getInt("totalPoints");
            if (obj.has("finished"))
                this.finished = obj.getBoolean("finished");
        } catch (Throwable t) {

        }
    }

}

interface RequestCompleteListener {
    void didSuccess(String response);
    void didFailure(Error error);
}
