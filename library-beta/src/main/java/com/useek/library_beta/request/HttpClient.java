package com.useek.library_beta.request;

import java.util.HashMap;

/**
 * Created by Chris Lin on 11/5/2017.
 */

public class HttpClient {

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
