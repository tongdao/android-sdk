package com.tongdao.sdk.tools;

import com.tongdao.sdk.config.Constants;
import com.tongdao.sdk.interfaces.TdHttpResponseHandler;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class TongDaoApiTool {

    private static final int TIME_OUT = 10000;
    private static final String X_LOCAL_TIME = "X-LOCAL-TIME";
    private static final String X_SDK_VERSION = "X-SDK-VERSION";
    private static final String X_AUTO_CLAIM = "X-AUTO-CLAIM";
    private static final String AUTO_CLAIM_FLAG = "1";
    private static final String SDK_VERSION = String.valueOf(Constants.SDK_VERSION);
    private static final String X_APP_KEY = "X-APP-KEY";
    private static final String X_DEVICE_KEY = "X-DEVICE-KEY";
    private static final String ACCEPT_NAME = "Accept";
    private static final String ACCEPT_VALUE = "application/json";
    private static final String CONTENT_TYPE_NAME = "Content-Type";
    private static final String CONTENT_TYPE_VALUE = "application/json";
    private static final int RES_204 = 204;
    private static final int RES_200 = 200;

    private static void generateHeaders(HttpURLConnection httpUrlConnection, String appKey, String deviceId, boolean isGet, boolean isPageCall, ArrayList<String[]> requestProperties) {
        ArrayList<String[]> allRequestProperties = new ArrayList<String[]>();
        allRequestProperties.add(new String[]{X_SDK_VERSION, SDK_VERSION});
        allRequestProperties.add(new String[]{X_APP_KEY, appKey});
        allRequestProperties.add(new String[]{ACCEPT_NAME, ACCEPT_VALUE});
        allRequestProperties.add(new String[]{X_DEVICE_KEY, deviceId});
        //add the local session time to head
        allRequestProperties.add(new String[]{X_LOCAL_TIME, TongDaoCheckTool.getTimeStamp(new TongDaoClockTool().currentTimeMillis())});

        if (!isGet) {
            allRequestProperties.add(new String[]{CONTENT_TYPE_NAME, CONTENT_TYPE_VALUE});
        }

        if (isPageCall) {
            allRequestProperties.add(new String[]{X_AUTO_CLAIM, AUTO_CLAIM_FLAG});
        }

        if (requestProperties != null && requestProperties.size() > 0) {
            allRequestProperties.addAll(requestProperties);
        }

        int headerSize = allRequestProperties.size();
        for (int i = 0; i < headerSize; i++) {
            String[] nameAndValue = allRequestProperties.get(i);
            httpUrlConnection.setRequestProperty(nameAndValue[0], nameAndValue[1]);
        }
    }

    public void post(String appKey, String deviceId, String url, ArrayList<String[]> requestProperties, String content, TdHttpResponseHandler handler) throws IOException, JSONException {
        int resCode = 0;

        URL requestUrl = new URL(url);
        HttpsURLConnection connection = (HttpsURLConnection) requestUrl.openConnection();
        connection.setConnectTimeout(TIME_OUT);
        connection.setReadTimeout(TIME_OUT);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");


        generateHeaders(connection, appKey, deviceId, false, false, requestProperties);

        // Write data
        OutputStream os = connection.getOutputStream();
        if( content == null )
            return;
        os.write(content.getBytes());

        // Read response
        StringBuilder responseSB = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String line;
        while ( (line = br.readLine()) != null)
            responseSB.append(line);

        if (connection != null) {
            resCode = connection.getResponseCode();
        }

        // Close streams
        br.close();
        os.close();

        if (resCode != RES_204 && resCode != RES_200) {

            if (handler != null) {
                handler.onFailure(resCode, responseSB.toString());
            }
        } else {
            //success call back
            if (handler != null) {
                handler.onSuccess(resCode, null);
            }
        }
    }

    public void get(String appKey, String deviceId, boolean isPageCall, String url, ArrayList<String[]> requestProperties, TdHttpResponseHandler handler) throws IOException, JSONException {
        int resCode = 0;

        URL requestUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
        connection.setConnectTimeout(TIME_OUT);
        connection.setReadTimeout(TIME_OUT + 15000);
        connection.setRequestMethod("GET");

        generateHeaders(connection, appKey, deviceId, false, isPageCall, requestProperties);

        // Read response
        StringBuilder responseSB = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String line;
        while ( (line = br.readLine()) != null)
            responseSB.append(line);

        if (connection != null) {
            resCode = connection.getResponseCode();
        }

        // Close streams
        br.close();

        if (resCode != RES_204 && resCode != RES_200) {

            if (handler != null) {
                handler.onFailure(resCode, responseSB.toString());
            }
        } else {
            //success call back
            if (handler != null) {
                handler.onSuccess(resCode, responseSB.toString());
            }
        }
    }

}
