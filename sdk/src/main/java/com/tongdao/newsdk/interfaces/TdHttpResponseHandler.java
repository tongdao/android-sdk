package com.tongdao.newsdk.interfaces;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import java.io.IOException;

public interface TdHttpResponseHandler {

    public void onSuccess(int statusCode, String responseBody) throws ClientProtocolException, JSONException, IOException;

    public void onFailure(int statusCode, String responseBody) throws JSONException;
}
