package com.tongdao.sdk.tools;

import android.util.Log;

import com.tongdao.sdk.config.Constants;
import com.tongdao.sdk.interfaces.TdHttpResponseHandler;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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

    private static void generateHeaders(HttpURLConnection httpUrlConnection, String appKey, String deviceId, boolean isGet, boolean isPageCall, ArrayList<String[]> requestProperties) {
        ArrayList<String[]> allRequestProperties = new ArrayList<String[]>();
        allRequestProperties.add(new String[]{X_SDK_VERSION, SDK_VERSION});
        allRequestProperties.add(new String[]{X_APP_KEY, appKey});
        allRequestProperties.add(new String[]{ACCEPT_NAME, ACCEPT_VALUE});
        allRequestProperties.add(new String[]{X_DEVICE_KEY, deviceId});
        //add the local session time to head
        allRequestProperties.add(new String[]{X_LOCAL_TIME, TongDaoCheckTool.getTimeStamp(System.currentTimeMillis())});

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
//        DefaultHttpClient httpClient = new DefaultHttpClient(params);
//        TdSSLTrustManager.addSSLManagerForHttpClient(httpClient);
//
//        HttpPost httpPost = new HttpPost(url);
//        httpPost.setHeaders(generateHeaders(appKey, deviceId, false, false, requestProperties));
//        httpPost.setEntity(new StringEntity(content, "UTF-8"));
//        if(null != requestProperties){
//            android.util.Log.v("requestProperties == ", requestProperties.toString());
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

    public static void post(String appKey, String deviceId, String url, ArrayList<String[]> requestProperties, String content, TdHttpResponseHandler handler) throws ClientProtocolException, IOException, JSONException {
        int resCode = 0;

        URL requestUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
        connection.setConnectTimeout(TIME_OUT);
        connection.setReadTimeout(TIME_OUT);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");

        generateHeaders(connection, appKey, deviceId, false, false, requestProperties);

        // Write data
        OutputStream os = connection.getOutputStream();
        os.write(content.getBytes());

        // Read response
        StringBuilder responseSB = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String line;
        while ( (line = br.readLine()) != null)
            responseSB.append(line);

        if (connection != null) {
            resCode = connection.getResponseCode();
            Log.e("respCode", ":" + resCode);

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

    public static void get(String appKey, String deviceId, boolean isPageCall, String url, ArrayList<String[]> requestProperties, TdHttpResponseHandler handler) throws ClientProtocolException, IOException, JSONException {
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, TIME_OUT);
        HttpConnectionParams.setSoTimeout(params, TIME_OUT);
        DefaultHttpClient httpClient = new DefaultHttpClient(params);
        TdSSLTrustManager.addSSLManagerForHttpClient(httpClient);

        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeaders(generateHeaders(appKey, deviceId, true, isPageCall, requestProperties));

        HttpResponse httpResponse = httpClient.execute(httpGet);
        int resCode = httpResponse.getStatusLine().getStatusCode();
        String resJson = inputStreamTOString(httpResponse.getEntity());

        if (resCode != RES_204 && resCode != RES_200) {
            //error call back
            if (handler != null) {
                handler.onFailure(resCode, resJson);
            }
        } else {
            //success call back
            if (handler != null) {
                handler.onSuccess(resCode, resJson);
            }
        }
    }

    private static String inputStreamTOString(HttpEntity bodyEntity) {
        String jsonString = null;
        if (bodyEntity == null) {
            return jsonString;
        }

        InputStream inStream = null;
        ByteArrayOutputStream outStream = null;
        try {
            inStream = bodyEntity.getContent();
            if (inStream != null) {
                outStream = new ByteArrayOutputStream();
                byte[] data = new byte[BUFFER_SIZE];
                int count = -1;
                while ((count = inStream.read(data, 0, BUFFER_SIZE)) != -1) {
                    outStream.write(data, 0, count);
                }
                outStream.flush();
                data = null;
                jsonString = new String(outStream.toByteArray(), "UTF-8");
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                    inStream = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (outStream != null) {
                try {
                    outStream.close();
                    outStream = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return jsonString;
    }

    public static class TdSSLTrustManager implements X509TrustManager {
        public TdSSLTrustManager() {
        }

        public void checkClientTrusted(X509Certificate chain[], String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate chain[], String authType) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public static void addSSLManagerForHttpClient(HttpClient httpClient) {
            X509TrustManager xtm = new TdSSLTrustManager();
            TrustManager mytm[] = {xtm};
            SSLContext ctx;
            try {
                ctx = SSLContext.getInstance("TLS");
                ctx.init(null, mytm, null);
                org.apache.http.conn.ssl.SSLSocketFactory socketFactory = new LingQianSSLSocketFactory(ctx);
                httpClient.getConnectionManager().getSchemeRegistry().register(new org.apache.http.conn.scheme.Scheme("https", socketFactory, 443));
            } catch (NoSuchAlgorithmException e) {
                Log.e("NoSuchAlgorithmEx", "add ssl manager for connection");
            } catch (KeyManagementException e) {
                Log.e("KeyManagementEx", "add ssl manager for connection");
            } catch (KeyStoreException e) {
                Log.e("KeyStoreException", "add ssl manager for connection");
            } catch (UnrecoverableKeyException e) {
                Log.e("UnrecoverableKeyEx", "add ssl manager for connection");
            }
        }
    }

    private static class LingQianSSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        public LingQianSSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(truststore);
            sslContext.init(null, new TrustManager[]{new TdSSLTrustManager()}, null);
        }

        public LingQianSSLSocketFactory(SSLContext context) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
            super(null);
            sslContext = context;
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }
}
