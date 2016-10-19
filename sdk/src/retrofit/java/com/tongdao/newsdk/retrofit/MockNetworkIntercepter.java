package com.tongdao.newsdk.retrofit;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by agonch on 10/13/16.
 * This class intercepts HTTP calls in retrofit when we are testing our
 */

public class MockNetworkIntercepter implements Interceptor{
    final static String TAG = "MockNetworkIntercepter";
    // FAKE RESPONSES.
    private final static String TEACHER_ID_1 = "{\"id\":1,\"age\":28,\"name\":\"Victor Apoyan\"}";
    private final static String TEACHER_ID_2 = "{\"id\":1,\"age\":16,\"name\":\"Tovmas Apoyan\"}";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = null;
        if(chain.request().method().equalsIgnoreCase("post")){
            response = new Response.Builder()
                    .code(204)
                    .message("")
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_0)
                    .body(ResponseBody.create(MediaType.parse("application/json"), ""))
                    .addHeader("content-type", "application/json")
                    .build();
        }else{
            response = new Response.Builder()
                    .code(404)
                    .message("")
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_0)
                    .body(ResponseBody.create(MediaType.parse("application/json"), ""))
                    .addHeader("content-type", "application/json")
                    .build();
        }

        return response;
    }
}
