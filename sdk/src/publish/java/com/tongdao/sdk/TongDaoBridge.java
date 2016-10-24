package com.tongdao.sdk;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.tongdao.sdk.beans.TdErrorBean;
import com.tongdao.sdk.beans.TdEventBean;
import com.tongdao.sdk.beans.TdEventBean.ACTION_TYPE;
import com.tongdao.sdk.beans.TdMessageBean;
import com.tongdao.sdk.beans.TdMessageButtonBean;
import com.tongdao.sdk.beans.TdPageBean;
import com.tongdao.sdk.beans.TdRewardBean;
import com.tongdao.sdk.interfaces.OnDownloadInAppMessageListener;
import com.tongdao.sdk.interfaces.OnDownloadLandingPageListener;
import com.tongdao.sdk.interfaces.OnErrorListener;
import com.tongdao.sdk.interfaces.TdHttpResponseHandler;
import com.tongdao.sdk.tools.Log;
import com.tongdao.sdk.tools.TongDaoApiTool;
import com.tongdao.sdk.tools.TongDaoAppInfoTool;
import com.tongdao.sdk.tools.TongDaoCheckTool;
import com.tongdao.sdk.tools.TongDaoClockTool;
import com.tongdao.sdk.tools.TongDaoDataTool;
import com.tongdao.sdk.tools.TongDaoJsonTool;
import com.tongdao.sdk.tools.TongDaoSavingTool;
import com.tongdao.sdk.tools.TongDaoUrlTool;
import com.tongdao.sdk.tools.TongDaoUtils;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class TongDaoBridge {
    //constants
    private static final String TD_MESSAGE_IMG_URL = "image_url";
    private static final String TD_MESSAGE = "message";
    private static final String TD_MESSAGE_DISPLAY_TIME = "display_time";
    private static final String TD_MESSAGE_LAYOUT = "layout";
    private static final String TD_MESSAGE_ACTION = "action";
    private static final String TD_MESSAGE_ACTION_TYPE = "type";
    private static final String TD_MESSAGE_ACTION_VALUE = "value";
    private static final String TD_MESSAGE_IS_PORTRAIT = "is_portrait";
    private static final String TD_MESSAGE_BUTTONS = "buttons";
    private static final String TD_MESSAGE_CLOSE_BUTTON = "close_btn";
    private static final String TD_MESSAGE_CID = "cid";
    private static final String TD_MESSAGE_MID = "mid";
    private final String LOCK = "lock";

    //unique instance of bridge
    private static TongDaoBridge uniqueInstance = null;

    //instance variables
    private String APP_KEY;
    private String USER_ID;
    private String PREVIOUS_ID;
    private String DEVICE_ID;
    private boolean ANONYMOUS;
    private Context appContext;
    private ArrayList<TdEventBean> eventList = new ArrayList<TdEventBean>();
    private ArrayList<TdEventBean> waitingList = new ArrayList<TdEventBean>();
    private boolean canRun = true;
    private long startTime = 0;
    private long startTimeForCloseApp = 0;
    private String pageNameStart;
    private String pageNameEnd;

    //dependent classes
    private TongDaoApiTool apiTool;
    private TongDaoUtils utils;
    private TongDaoUrlTool urlTool;
    private TongDaoAppInfoTool appInfoTool;
    private TongDaoDataTool dataTool;
    private TongDaoSavingTool savingTool;

    private synchronized boolean isCanRun() {
        return canRun;
    }

    private synchronized void setCanRun(boolean canRun) {
        this.canRun = canRun;
    }

    private TongDaoBridge(Context appContext, String appKey, String deviceId, String userId) {
        this.appContext = appContext;
        this.APP_KEY = appKey;
        this.USER_ID = userId;
        this.DEVICE_ID = deviceId;
        this.apiTool = new TongDaoApiTool();
        this.utils = new TongDaoUtils(appContext);
        this.urlTool = new TongDaoUrlTool();
        this.appInfoTool = new TongDaoAppInfoTool();
        this.dataTool = new TongDaoDataTool();
        this.savingTool = new TongDaoSavingTool();
        savingTool.saveAppKeyAndUserId(appContext, appKey, userId);
    }

    private TongDaoBridge(Context appContext, String appKey, String deviceId, String userId, String previousId, boolean anonymous) {
        this.appContext = appContext;
        this.APP_KEY = appKey;
        this.USER_ID = userId;
        this.DEVICE_ID = deviceId;
        this.PREVIOUS_ID = previousId;
        this.ANONYMOUS = anonymous;
        this.apiTool = new TongDaoApiTool();
        this.utils = new TongDaoUtils(appContext);
        this.urlTool = new TongDaoUrlTool();
        this.appInfoTool = new TongDaoAppInfoTool();
        this.dataTool = new TongDaoDataTool();
        this.savingTool = new TongDaoSavingTool();
        savingTool.saveUserInfoData(appContext, appKey, userId, previousId, anonymous);
    }

    public static synchronized TongDaoBridge getInstance(Context appContext, String APP_KEY, String DEVICE_ID, String USER_ID) {
        if (uniqueInstance == null) {
            uniqueInstance = new TongDaoBridge(appContext, APP_KEY, DEVICE_ID, USER_ID);
        }
        return uniqueInstance;
    }

    public static synchronized TongDaoBridge getInstance(Context appContext, String APP_KEY, String DEVICE_ID, String USER_ID, String PREVIOUS_ID, boolean ANONYMOUS) {
        if (uniqueInstance == null) {
            uniqueInstance = new TongDaoBridge(appContext, APP_KEY, DEVICE_ID, USER_ID, PREVIOUS_ID, ANONYMOUS);
        }
        return uniqueInstance;
    }

    public void init() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (appContext != null) {
                    String gaid = appInfoTool.getGaid(appContext);
                    if (appContext != null) {
                        try {
                            JSONObject properties = dataTool.makeInfoProperties(appContext, gaid);
                            System.out.println("Properties ->" + properties.toString());
                            if (properties != null && properties.keys().hasNext() && USER_ID != null) {
                                TdEventBean tempLqEventBean = new TdEventBean(ACTION_TYPE.identify, USER_ID, null, properties);
                                trackEvents(tempLqEventBean);
                            }
                        } catch (JSONException e) {
                            Log.e("init properties", "JSONException");
                        }
                    }
                }
            }
        }).start();
    }

    public void trackIdentify() {
        if (appContext != null) {
            try {
                String gaid = appInfoTool.getGaid(appContext);
                JSONObject properties = dataTool.makeInfoProperties(appContext, gaid);
                if (properties != null && properties.keys().hasNext() && USER_ID != null) {
                    TdEventBean tempLqEventBean = new TdEventBean(ACTION_TYPE.identify, USER_ID, null, properties);
                    trackEvents(tempLqEventBean);
                }
            } catch (JSONException e) {
                Log.e("init properties", "JSONException");
            }
        }
    }

    /**
     * @param actionType 用户动作类型
     * @param previousId device
     * @param userId   用户设置的id
     */
    public void changePropertiesAndUserId(final ACTION_TYPE actionType, final String previousId, final String userId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (appContext != null) {
                    try {
                        USER_ID = userId;
                        if (USER_ID != null) {
                            TdEventBean tempLqEventBean = null;
                            PREVIOUS_ID = previousId;

                            if (actionType == ACTION_TYPE.merge) {
                                tempLqEventBean = new TdEventBean(actionType, USER_ID, PREVIOUS_ID, null, null);
                            } else {
                                if (PREVIOUS_ID != null) {
                                    tempLqEventBean = new TdEventBean(ACTION_TYPE.merge, USER_ID, PREVIOUS_ID, null, null);
                                    trackEvents(tempLqEventBean);
                                }
                                String gaid = appInfoTool.getGaid(appContext);
                                JSONObject properties = dataTool.makeInfoProperties(appContext, gaid);
                                tempLqEventBean = new TdEventBean(actionType, USER_ID, PREVIOUS_ID, properties);

                            }

                            trackEvents(tempLqEventBean);
                        }
                    } catch (JSONException e) {
                        Log.e("init properties", "JSONException");
                    }
                }
            }
        }).start();
    }

    public static TongDaoBridge getGeneratedInstance() {
        return uniqueInstance;
    }

    public String getAppKey() {
        return this.APP_KEY;
    }

    public String getUserId() {
        return this.USER_ID;
    }

    private String makeEventsJsonString(ArrayList<TdEventBean> transEvents) throws JSONException {
        if (transEvents == null || transEvents.size() == 0) {
            return null;
        }

        JSONObject eventsObj = new JSONObject();
        //make json array
        JSONArray eventsArray = new JSONArray();
        for (TdEventBean eachBean : transEvents) {
            eventsArray.put(eachBean.getJsonObject());
        }

        eventsObj.put("events", eventsArray);
        Log.i("event string", eventsObj.toString());
        return eventsObj.toString();
    }

    private void makeSessionEventJsonString(TdEventBean sessionEvent, boolean isNeedToSendRequest) throws JSONException {
        if (sessionEvent == null) {
            return;
        }

        JSONObject eventObj;

        String appSessionData = savingTool.getAppSessionData(appContext);
        if (TextUtils.isEmpty(appSessionData)) {
            eventObj = new JSONObject();
        } else {
            eventObj = new JSONObject(appSessionData);
        }
        JSONArray eventArray = eventObj.optJSONArray("events");
        if (eventArray == null) {
            eventArray = new JSONArray();
        }
        eventArray.put(sessionEvent.getJsonObject());

        eventObj.put("events", eventArray);
        Log.i("session event string", eventObj.toString());

        savingTool.setAppSessionData(appContext, eventObj.toString());

        if (isNeedToSendRequest) {
            trackAppSessionEvent();
        }
    }

    private synchronized ArrayList<TdEventBean> addAllLqEventBean(TdEventBean lqEventBean) {
        ArrayList<TdEventBean> tempLqEventBeanArray = new ArrayList<TdEventBean>();
        tempLqEventBeanArray.addAll(this.eventList);
        tempLqEventBeanArray.addAll(this.waitingList);
        if (lqEventBean != null) {
            tempLqEventBeanArray.add(lqEventBean);
        }
        this.eventList.clear();
        this.waitingList.clear();
        return tempLqEventBeanArray;
    }

    private synchronized void setEventList(ArrayList<TdEventBean> tempLqEventBeanArray) {
        this.eventList.addAll(tempLqEventBeanArray);
    }

    private synchronized void addWaitList(TdEventBean lqEventBean) {
        this.waitingList.add(lqEventBean);
    }

    private synchronized boolean isNeedRun() {
        return this.waitingList.size() > 0;
    }

    public void onSessionStart(Activity activity) {
        onSessionStart(activity.getClass().getSimpleName());
    }

    public void onSessionEnd(Activity activity) {
        onSessionEnd(activity.getClass().getSimpleName());
    }

    public void onSessionStart(String pageName) {
        this.pageNameStart = pageName;
        this.startTime = TongDaoClockTool.currentTimeMillis();

        if (this.pageNameStart == null || this.startTime == 0) {
            return;
        }

        HashMap<String, Object> values = new HashMap<String, Object>();
        values.put("!name", pageName);

        try {
            TdEventBean tempEb = new TdEventBean(ACTION_TYPE.track, this.USER_ID, "!open_page", dataTool.makeUserProperties(values));
            startTrackEvents(tempEb);
        } catch (JSONException e) {
            Log.e("onSessionStart", "JSONException");
        }
    }

    public void onSessionEnd(String pageName) {
        this.pageNameEnd = pageName;
        if (this.pageNameStart == null || this.pageNameEnd == null || !(this.pageNameStart.equals(this.pageNameEnd))) {
            return;
        }

        if (this.startTime == 0) {
            return;
        }

        HashMap<String, Object> values = new HashMap<String, Object>();
        values.put("!name", pageName);
        values.put("!started_at", TongDaoCheckTool.getTimeStamp(this.startTime));
        try {
            TdEventBean tempEb = new TdEventBean(ACTION_TYPE.track, this.USER_ID, "!close_page", dataTool.makeUserProperties(values));
            startTrackEvents(tempEb);
        } catch (JSONException e) {
            Log.e("onSessionEnd", "JSONException");
        } finally {
            this.startTime = 0;
            this.pageNameStart = null;
            this.pageNameEnd = null;
        }
    }

    public void onAppSessionEnd() {
        if (this.startTimeForCloseApp == 0) {
            return;
        }

        HashMap<String, Object> values = new HashMap<String, Object>();
        values.put("!started_at", TongDaoCheckTool.getTimeStamp(this.startTimeForCloseApp));

        try {
            TdEventBean tempEb = new TdEventBean(ACTION_TYPE.track, this.USER_ID, "!close_app", dataTool.makeUserProperties(values));
            makeSessionEventJsonString(tempEb, false);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        finally {
            this.startTimeForCloseApp = 0;
        }
    }

    public void onAppSessionStart() {
        this.startTimeForCloseApp = TongDaoClockTool.currentTimeMillis();

        if (this.startTimeForCloseApp == 0) {
            return;
        }

        TdEventBean tempEb = new TdEventBean(ACTION_TYPE.track, this.USER_ID, "!open_app");

        try {
            makeSessionEventJsonString(tempEb, true);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public TdEventBean onNotificationStatus() {
        int status = (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT || utils.isNotificationEnabled()) ? 1 : 0;
        this.startTime = TongDaoClockTool.currentTimeMillis();

        if (this.startTime == 0 ||
                savingTool.getNotificationData(appContext) == status) {
            return null;
        }

        savingTool.setNotificationData(appContext, status);

        HashMap<String, Object> values = new HashMap<String, Object>();
        String strStatus = status == 1 ? "true" : "false";
        values.put("!push_enable", strStatus);

        try {
            return new TdEventBean(ACTION_TYPE.identify, this.USER_ID, null, dataTool.makeUserProperties(values));
        } catch (JSONException e) {
            Log.e("onSessionStart", "JSONException");
        }
        return null;
    }


    public void startTrackEvents(final TdEventBean tdEventBean) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (LOCK) {
                        trackEvents(tdEventBean);
                    }
                } catch (JSONException e) {
                    if (!Thread.currentThread().isInterrupted()) {
                        Thread.currentThread().interrupt();
                    }
                    Log.e("startTrackEvents", "JSONException");
                }
            }
        }).start();
    }

    private void trackEvents(TdEventBean lqEventBean) throws JSONException {
        if (this.appContext != null && this.APP_KEY != null && this.USER_ID != null && this.DEVICE_ID != null) {
            if (isCanRun()) {
                setCanRun(false);
                final ArrayList<TdEventBean> tempLqEventBeanArray = addAllLqEventBean(lqEventBean);

                TdEventBean tdEventBean = onNotificationStatus();
                if( tdEventBean != null ) {
                    tempLqEventBeanArray.add(tdEventBean);
                }
                try {
                    apiTool.post(this.APP_KEY, this.DEVICE_ID, urlTool.getTrackEventUrlV2(), null, makeEventsJsonString(tempLqEventBeanArray), new TdHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, String responseBody) throws JSONException, IOException {
                            Log.i("Event response", "" + statusCode);
                            setCanRun(true);
                            if (isNeedRun()) {
                                trackEvents(null);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, String responseBody) throws JSONException {
                            Log.e("Event response","Error: " + statusCode + ", " + responseBody);
                            setEventList(tempLqEventBeanArray);
                            setCanRun(true);
                            if (responseBody != null) {
                                JSONObject errorResponse = new JSONObject(responseBody);
                                onServerError(statusCode, errorResponse, null);
                            }
                        }
                    });
                } catch (ClientProtocolException e) {
                    Log.e("ClientProtocolException", "trackEvents");
                    setEventList(tempLqEventBeanArray);
                    setCanRun(true);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("IOException", "trackEvents");
                    setEventList(tempLqEventBeanArray);
                    setCanRun(true);
                }
            } else {
                addWaitList(lqEventBean);
            }
        }
    }

    private void trackAppSessionEvent() throws JSONException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (LOCK) {
                        apiTool.post(TongDaoBridge.this.APP_KEY, TongDaoBridge.this.DEVICE_ID, urlTool.getTrackEventUrlV2(), null, savingTool.getAppSessionData(appContext), new TdHttpResponseHandler() {

                            @Override
                            public void onSuccess(int statusCode, String responseBody) throws JSONException, IOException {
                                Log.i("Session event response", "" + statusCode);
                                savingTool.setAppSessionData(appContext, null);
                            }

                            @Override
                            public void onFailure(int statusCode, String responseBody) throws JSONException {
                                Log.e("Session event response","Error: " + statusCode + ", " + responseBody);
                            }
                        });
                    }
                } catch (ClientProtocolException e) {
                    Log.e("ClientProtocolException", "trackEvents");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void onServerError(int statusCode, JSONObject errorResponse, OnErrorListener onErrorListener) {
        if (errorResponse == null) {
            if (onErrorListener != null) {
                TdErrorBean trackErrorBean = new TdErrorBean();
                trackErrorBean.setErrorCode(statusCode);
                trackErrorBean.setErrorMsg("无具体错误信息!");
                onErrorListener.onError(trackErrorBean);
            }
        } else {
            if (onErrorListener != null) {
                TdErrorBean trackErrorBean = new TdErrorBean();
                String errorMessage = errorResponse.optString("message", "无具体错误信息!");
                trackErrorBean.setErrorCode(statusCode);
                trackErrorBean.setErrorMsg(errorMessage);
                onErrorListener.onError(trackErrorBean);
            }
        }
    }

    public void startDownloadLandingPage(final String pageId, final OnDownloadLandingPageListener onDownloadLandingPageListener, final OnErrorListener onErrorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    downloadLandingPage(pageId, onDownloadLandingPageListener, onErrorListener);
                } catch (ClientProtocolException e) {
                    Log.e("startDownloadLandingPg", "ClientProtocolException");
                } catch (IOException e) {
                    Log.e("startDownloadLandingPg", "IOException");
                } catch (JSONException e) {
                    Log.e("startDownloadLandingPg", "JSONException");
                }
            }
        }).start();
    }

    public void startDownloadInAppMessages(final OnDownloadInAppMessageListener onDownloadInAppMessageListener, final OnErrorListener onErrorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    downloadInAppMessages(onDownloadInAppMessageListener, onErrorListener);
                } catch (ClientProtocolException e) {
                    Log.e("startDownloadInAppMsgs", "ClientProtocolException");
                } catch (IOException e) {
                    Log.e("startDownloadInAppMsgs", "IOException");
                } catch (JSONException e) {
                    Log.e("startDownloadInAppMsgs", "JSONException");
                }
            }
        }).start();
    }

    private void downloadLandingPage(String pageId, final OnDownloadLandingPageListener onDownloadLandingPageListener, final OnErrorListener onErrorListener) throws IOException, JSONException {
        if (this.appContext == null || APP_KEY == null || USER_ID == null || DEVICE_ID == null) {
            return;
        }

        String url = urlTool.getLandingPageUrl(pageId, USER_ID);
        apiTool.get(APP_KEY, DEVICE_ID, true, url, null, new TdHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, String responseBody) throws JSONException, IOException {
                if (responseBody != null) {
                    JSONObject response = new JSONObject(responseBody);
                    TdPageBean tempLandingPageBean = new TdPageBean();
                    tempLandingPageBean.setImage(TongDaoJsonTool.optJsonString(response, "image"));

                    JSONArray rewards = response.optJSONArray("rewards");
                    if (rewards != null && rewards.length() > 0) {
                        ArrayList<TdRewardBean> rewardList = new ArrayList<TdRewardBean>();
                        for (int i = 0; i < rewards.length(); i++) {
                            JSONObject tempReward = rewards.getJSONObject(i);
                            TdRewardBean tempRewardBean = new TdRewardBean();
                            tempRewardBean.setId(tempReward.optInt("id"));
                            tempRewardBean.setName(TongDaoJsonTool.optJsonString(tempReward, "name"));
                            tempRewardBean.setSku(TongDaoJsonTool.optJsonString(tempReward, "sku"));
                            tempRewardBean.setQuantity(tempReward.optInt("quantity"));
                            rewardList.add(tempRewardBean);
                        }

                        tempLandingPageBean.setRewardList(rewardList);
                    }

                    if (onDownloadLandingPageListener != null) {
                        onDownloadLandingPageListener.onSuccess(tempLandingPageBean);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String responseBody) throws JSONException {
                if (responseBody != null) {
                    JSONObject errorResponse = new JSONObject(responseBody);
                    onServerError(statusCode, errorResponse, onErrorListener);
                }
            }
        });
    }


    private void downloadInAppMessages(final OnDownloadInAppMessageListener onDownloadInAppMessageListener, final OnErrorListener onErrorListener) throws IOException, JSONException {
        if (this.appContext == null || APP_KEY == null || USER_ID == null || DEVICE_ID == null) {
            return;
        }

        String url = urlTool.getInAppMessageUrl(USER_ID);
        apiTool.get(APP_KEY, DEVICE_ID, false, url, null, new TdHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String responseBody) throws JSONException, IOException {
                if (responseBody != null) {
                    ArrayList<TdMessageBean> messageBeans = getInAppMessageBeans(responseBody);
                    if (onDownloadInAppMessageListener != null) {
                        onDownloadInAppMessageListener.onSuccess(messageBeans);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String responseBody) throws JSONException {
                if (responseBody != null) {
                    JSONObject errorResponse = new JSONObject(responseBody);
                    onServerError(statusCode, errorResponse, onErrorListener);
                }
            }
        });
    }

    private ArrayList<TdMessageBean> getInAppMessageBeans(String messageJsonString) throws JSONException {
        ArrayList<TdMessageBean> beanList = new ArrayList<TdMessageBean>();
        JSONArray arrayObj = new JSONArray(messageJsonString);
        int size = arrayObj.length();
        for (int i = 0; i < size; i++) {
            JSONObject contentObj = arrayObj.getJSONObject(i);
            String imageUrl = TongDaoJsonTool.optJsonString(contentObj, TD_MESSAGE_IMG_URL);
            String message = TongDaoJsonTool.optJsonString(contentObj, TD_MESSAGE);
            long displayTime = contentObj.optLong(TD_MESSAGE_DISPLAY_TIME);
            long cid = contentObj.optLong(TD_MESSAGE_CID);
            long mid = contentObj.optLong(TD_MESSAGE_MID);
            String layout = TongDaoJsonTool.optJsonString(contentObj, TD_MESSAGE_LAYOUT);
            String actionType = null;
            String actionValue = null;
            JSONObject actionObj = contentObj.optJSONObject(TD_MESSAGE_ACTION);
            if (actionObj != null) {
                actionType = TongDaoJsonTool.optJsonString(actionObj, TD_MESSAGE_ACTION_TYPE);
                actionValue = TongDaoJsonTool.optJsonString(actionObj, TD_MESSAGE_ACTION_VALUE);
            }

            boolean isPortrait = contentObj.optBoolean(TD_MESSAGE_IS_PORTRAIT);
            //the close button image url
            String closeBtnUrl = TongDaoJsonTool.optJsonString(contentObj, TD_MESSAGE_CLOSE_BUTTON);

            //in app message buttons
            ArrayList<TdMessageButtonBean> buttonsList = new ArrayList<TdMessageButtonBean>();
            JSONArray buttonsArrayJson = contentObj.optJSONArray(TD_MESSAGE_BUTTONS);
            if (buttonsArrayJson != null && buttonsArrayJson.length() > 0) {
                for (int j = 0; j < buttonsArrayJson.length(); j++) {
                    JSONObject tempButtonJson = buttonsArrayJson.getJSONObject(j);
                    double x = tempButtonJson.optDouble("x");
                    double y = tempButtonJson.optDouble("y");
                    double w = tempButtonJson.optDouble("w");
                    double h = tempButtonJson.optDouble("h");

                    String buttonActionType = null;
                    String buttonActionValue = null;
                    JSONObject buttonActionObj = tempButtonJson.optJSONObject(TD_MESSAGE_ACTION);
                    if (buttonActionObj != null) {
                        buttonActionType = TongDaoJsonTool.optJsonString(buttonActionObj, TD_MESSAGE_ACTION_TYPE);
                        buttonActionValue = TongDaoJsonTool.optJsonString(buttonActionObj, TD_MESSAGE_ACTION_VALUE);
                    }

                    TdMessageButtonBean tempTdMessageButtonBean = new TdMessageButtonBean(x, y, w, h, buttonActionType, buttonActionValue);
                    buttonsList.add(tempTdMessageButtonBean);
                }
            }

            beanList.add(new TdMessageBean(imageUrl, message, displayTime, layout, actionType, actionValue, cid, mid, buttonsList, isPortrait, closeBtnUrl));
        }

        return beanList;
    }
}
