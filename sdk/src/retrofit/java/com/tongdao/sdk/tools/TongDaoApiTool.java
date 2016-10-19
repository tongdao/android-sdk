package com.tongdao.sdk.tools;

import com.google.gson.Gson;
import com.tongdao.sdk.config.Constants;
import com.tongdao.sdk.interfaces.TdHttpResponseHandler;
import com.tongdao.sdk.retrofit.Events;
import com.tongdao.sdk.retrofit.Message;
import com.tongdao.sdk.retrofit.MockNetworkIntercepter;
import com.tongdao.sdk.retrofit.TongDaoApi;

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

    private void generateHeaders(HttpURLConnection httpUrlConnection, String appKey, String deviceId, boolean isGet, boolean isPageCall, ArrayList<String[]> requestProperties) {
        ArrayList<String[]> allRequestProperties = new ArrayList<String[]>();
        allRequestProperties.add(new String[]{X_SDK_VERSION, SDK_VERSION});
        allRequestProperties.add(new String[]{X_APP_KEY, appKey});
        allRequestProperties.add(new String[]{ACCEPT_NAME, ACCEPT_VALUE});
        allRequestProperties.add(new String[]{X_DEVICE_KEY, deviceId});
        //add the local session time to head
        allRequestProperties.add(new String[]{X_LOCAL_TIME, TongDaoCheckTool.getTimeStamp(TongDaoClockTool.currentTimeMillis())});

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

//    public static void post(String appKey, String deviceId, String url, ArrayList<String[]> requestProperties, String content, TdHttpResponseHandler handler) throws ClientProtocolException, IOException, JSONException {
//        HttpParams params = new BasicHttpParams();
//        HttpConnectionParams.setConnectionTimeout(params, TIME_OUT);
//        HttpConnectionParams.setSoTimeout(params, TIME_OUT);
//        DefaultHttpClient httpClient = new Defa(params);
//        TdSSLTrustManager.addSSLManagerForHttpClient(httpClient);
//
//        HttpPost httpPost = new HttpPost(url);
//        httpPost.setHeaders(generateHeaders(appKey, deviceId, false, false, requestProperties));
//        httpPost.setEntity(new StringEntity(content, "UTF-8"));
//        if(null != requestProperties){
//            com.tongdao.sdk.tools.Log.v("requestProperties == ", requestProperties.toString());
//        }
//
//        HttpResponse httpResponse = httpClient.execute(httpPost);
//        int resCode = httpResponse.getStatusLine().getStatusCode();
//        if (resCode != RES_204 && resCode != RES_200) {
//            //error call back
//            String errorJsonString = inputStreamTOString(httpResponse.getEntity());
//            if (handler != null) {
//                handler.onFailure(resCode, errorJsonString);
//            }
//        } else {
//            //success call back
//            if (handler != null) {
//                handler.onSuccess(resCode, null);
//            }
//        }
//    }

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

        Call<Void> call = tongDaoApi.postEvent(SDK_VERSION,appKey,deviceId,TongDaoCheckTool.getTimeStamp(TongDaoClockTool.currentTimeMillis()),events);

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
        Call<List<Message>> call = tongDaoApi.getInAppMessages(SDK_VERSION,appKey,deviceId,TongDaoCheckTool.getTimeStamp(TongDaoClockTool.currentTimeMillis()),isPageCall?AUTO_CLAIM_FLAG:null,userId);
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

    public void getLandingPageRetrofit(String appKey, String deviceId, boolean isPageCall, String url, ArrayList<String[]> requestProperties, final TdHttpResponseHandler handler, String userId) throws ClientProtocolException, IOException, JSONException {
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
        Call<List<Message>> call = tongDaoApi.getLandingPage(SDK_VERSION,appKey,deviceId,TongDaoCheckTool.getTimeStamp(TongDaoClockTool.currentTimeMillis()),isPageCall?AUTO_CLAIM_FLAG:null,userId);
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

//    public static void post(String appKey, String deviceId, String url, ArrayList<String[]> requestProperties, String content, TdHttpResponseHandler handler) throws ClientProtocolException, IOException, JSONException {
//        int resCode = 0;
//
//        URL requestUrl = new URL(url);
//        HttpsURLConnection connection = (HttpsURLConnection) requestUrl.openConnection();
//        connection.setConnectTimeout(TIME_OUT);
//        connection.setReadTimeout(TIME_OUT);
//        connection.setDoOutput(true);
//        connection.setRequestMethod("POST");
//
//
//        generateHeaders(connection, appKey, deviceId, false, false, requestProperties);
//
//        // Write data
//        OutputStream os = connection.getOutputStream();
//        if( content == null )
//            return;
//        os.write(content.getBytes());
//
//        // Read response
//        StringBuilder responseSB = new StringBuilder();
//        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//
//        String line;
//        while ( (line = br.readLine()) != null)
//            responseSB.append(line);
//
//        if (connection != null) {
//            resCode = connection.getResponseCode();
//        }
//
//        // Close streams
//        br.close();
//        os.close();
//
//        if (resCode != RES_204 && resCode != RES_200) {
//
//            if (handler != null) {
//                handler.onFailure(resCode, responseSB.toString());
//            }
//        } else {
//            //success call back
//            if (handler != null) {
//                handler.onSuccess(resCode, null);
//            }
//        }
//    }

    public void get(String appKey, String deviceId, boolean isPageCall, String url, ArrayList<String[]> requestProperties, TdHttpResponseHandler handler) throws ClientProtocolException, IOException, JSONException {
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

//    private static String inputStreamTOString(HttpEntity bodyEntity) {
//        String jsonString = null;
//        if (bodyEntity == null) {
//            return jsonString;
//        }
//
//        InputStream inStream = null;
//        ByteArrayOutputStream outStream = null;
//        try {
//            inStream = bodyEntity.getContent();
//            if (inStream != null) {
//                outStream = new ByteArrayOutputStream();
//                byte[] data = new byte[BUFFER_SIZE];
//                int count = -1;
//                while ((count = inStream.read(data, 0, BUFFER_SIZE)) != -1) {
//                    outStream.write(data, 0, count);
//                }
//                outStream.flush();
//                data = null;
//                jsonString = new String(outStream.toByteArray(), "UTF-8");
//            }
//        } catch (IllegalStateException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (inStream != null) {
//                try {
//                    inStream.close();
//                    inStream = null;
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            if (outStream != null) {
//                try {
//                    outStream.close();
//                    outStream = null;
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        return jsonString;
//    }


}