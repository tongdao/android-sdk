package com.tongdao.newsdk.retrofit;

import com.tongdao.sdk.config.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * Created by agonch on 10/11/16.
 */

public interface TongDaoApi {
    String X_APP_KEY = "X-APP-KEY";
    String X_DEVICE_KEY = "X-DEVICE-KEY";
    String X_SDK_VERSION = "X-SDK-VERSION";
    String X_LOCAL_TIME = "X-LOCAL-TIME";
    String X_AUTO_CLAIM = "X-AUTO-CLAIM";

    @Headers({"Accept:application/json","Content-type:application/json","X-APP-USE:Retrofit"})
    @POST(Constants.API_URI_EVENTS)
    Call<Void> postEvent(@Header(X_SDK_VERSION) String sdkVersion,
                         @Header(X_APP_KEY) String appKey,
                         @Header(X_DEVICE_KEY) String deviceKey,
                         @Header(X_LOCAL_TIME) String localTime,
                         @Body Events events);

    @Headers({"Accept:application/json","Content-type:application/json","X-APP-USE:Retrofit"})
    @GET(Constants.API_URI_MESSAGES)
    Call<List<Message>> getInAppMessages(@Header(X_SDK_VERSION) String sdkVersion,
                                         @Header(X_APP_KEY) String appKey,
                                         @Header(X_DEVICE_KEY) String deviceKey,
                                         @Header(X_LOCAL_TIME) String localTime,
                                         @Header(X_AUTO_CLAIM) String autoClaim,
                                         @Query("user_id") String userId);

    @Headers({"Accept:application/json","Content-type:application/json","X-APP-USE:Retrofit"})
    @GET(Constants.API_URI_PAGE)
    Call<List<Message>> getLandingPage(@Header(X_SDK_VERSION) String sdkVersion,
                                       @Header(X_APP_KEY) String appKey,
                                       @Header(X_DEVICE_KEY) String deviceKey,
                                       @Header(X_LOCAL_TIME) String localTime,
                                       @Header(X_AUTO_CLAIM) String autoClaim,
                                       @Query("user_id") String userId);
}
