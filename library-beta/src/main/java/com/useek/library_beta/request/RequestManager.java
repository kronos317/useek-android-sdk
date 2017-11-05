package com.useek.library_beta.request;

import java.util.HashMap;

/**
 * Created by Chris Lin on 11/5/2017.
 */

public class RequestManager {

    public static void request(String url, HashMap<String, String> params, int requestType, final RequestCompleteListener callback) {
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

    public interface RequestCompleteListener {
        void didSuccess(String response);
        void didFailure(Error error);
    }

}
