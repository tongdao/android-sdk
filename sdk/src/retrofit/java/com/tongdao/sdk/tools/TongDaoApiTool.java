package com.tongdao.sdk.tools;

import com.google.gson.Gson;
import com.tongdao.sdk.retrofit.Events;
import com.tongdao.sdk.retrofit.Message;
import com.tongdao.sdk.retrofit.MockNetworkIntercepter;
import com.tongdao.sdk.retrofit.TongDaoApi;
import com.tongdao.sdk.config.Constants;
import com.tongdao.sdk.interfaces.TdHttpResponseHandler;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Api Tool, retrofit style. This class is only here for testing.
 */
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
    private static final int BUFFER_SIZE = 4096;

    public void postRetrofit(String appKey, String deviceId, String url, ArrayList<String[]> requestProperties, String content, final TdHttpResponseHandler handler) throws ClientProtocolException, IOException, JSONException {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(new MockNetworkIntercepter())
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_URL +
                        Constants.API_VERSION)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        Events events = new Gson().fromJson(content,Events.class);

        TongDaoApi tongDaoApi = retrofit.create(TongDaoApi.class);

        Call<Void> call = tongDaoApi.postEvent(SDK_VERSION,appKey,deviceId, com.tongdao.sdk.tools.TongDaoCheckTool.getTimeStamp(TongDaoClockTool.currentTimeMillis()),events);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                try {
                    handler.onSuccess(response.code(),response.message());
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                try {
                    handler.onFailure(0, t.getMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getMessagesRetrofit(String appKey, String deviceId, boolean isPageCall, String url, ArrayList<String[]> requestProperties, final TdHttpResponseHandler handler, String userId) throws ClientProtocolException, IOException, JSONException {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(new MockNetworkIntercepter())
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_URL +
                        Constants.API_VERSION)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        TongDaoApi tongDaoApi = retrofit.create(TongDaoApi.class);
        Call<List<Message>> call = tongDaoApi.getInAppMessages(SDK_VERSION,appKey,deviceId, com.tongdao.sdk.tools.TongDaoCheckTool.getTimeStamp(TongDaoClockTool.currentTimeMillis()),isPageCall?AUTO_CLAIM_FLAG:null,userId);
        call.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                int resCode = response.code();
                List<Message> messages = response.body();
                if (resCode != RES_204 && resCode != RES_200) {

                    if (handler != null) {
                        try {
                            handler.onFailure(resCode, response.message());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    //success call back
                    if (handler != null) {
                        try {
                            handler.onSuccess(resCode, new Gson().toJson(messages));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ClientProtocolException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                if (handler != null) {
                    try {
                        handler.onFailure(0, t.getLocalizedMessage());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
