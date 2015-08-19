package com.tongdao.sdk.interfaces;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

public interface TdHttpResponseHandler {

    public void onSuccess(int statusCode, String responseBody) throws ClientProtocolException, JSONException, IOException;

    public void onFailure(int statusCode, String responseBody) throws JSONException;
}
