package com.tongdao.sdk.interfaces;

import org.json.JSONException;

import java.io.IOException;

public interface TdHttpResponseHandler {

    void onSuccess(int statusCode, String responseBody) throws JSONException, IOException;

    void onFailure(int statusCode, String responseBody) throws JSONException;
}
