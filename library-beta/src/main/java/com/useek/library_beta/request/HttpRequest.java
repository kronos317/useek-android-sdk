package com.useek.library_beta.request;

import android.os.AsyncTask;

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
 * Created by Chris Lin on 11/5/2017.
 */

public class HttpRequest extends AsyncTask<HttpClient, String, String> {

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
