package com.tongdao.sdk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

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
import com.tongdao.sdk.tools.TongDaoApiTool;
import com.tongdao.sdk.tools.TongDaoAppInfoTool;
import com.tongdao.sdk.tools.TongDaoCheckTool;
import com.tongdao.sdk.tools.TongDaoDataTool;
import com.tongdao.sdk.tools.TongDaoJsonTool;
import com.tongdao.sdk.tools.TongDaoSavingTool;
import com.tongdao.sdk.tools.TongDaoUrlTool;

public class TongDaoBridge {
    private static TongDaoBridge uniqueInstance = null;
    private String APP_KEY;
    private String USER_ID;
    private String PREVIOUS_ID;
    private boolean ANONYMOUS;
    private Context appContext;
    private ArrayList<TdEventBean> eventList = new ArrayList<TdEventBean>();
    private ArrayList<TdEventBean> waitingList = new ArrayList<TdEventBean>();
    private boolean canRun = true;
    private long startTime = 0;
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
    private String pageNameStart;
    private String pageNameEnd;

    private synchronized boolean isCanRun() {
        return canRun;
    }

    private synchronized void setCanRun(boolean canRun) {
        this.canRun = canRun;
    }

    private TongDaoBridge(Context appContext, String appKey, String userId) {
        this.appContext = appContext;
        this.APP_KEY = appKey;
        this.USER_ID = userId;
        TongDaoSavingTool.saveAppKeyAndUserId(appContext, appKey, userId);
    }

    private TongDaoBridge(Context appContext, String appKey, String userId, String previousId, boolean anonymous) {
        this.appContext = appContext;
        this.APP_KEY = appKey;
        this.USER_ID = userId;
        this.PREVIOUS_ID = previousId;
        this.ANONYMOUS = anonymous;
        TongDaoSavingTool.saveUserInfoData(appContext, appKey, userId, previousId, anonymous);
    }

    public static synchronized TongDaoBridge getInstance(Context appContext, String APP_KEY, String USER_ID) {
        if (uniqueInstance == null) {
            uniqueInstance = new TongDaoBridge(appContext, APP_KEY, USER_ID);
        }
        return uniqueInstance;
    }

    public static synchronized TongDaoBridge getInstance(Context appContext, String APP_KEY, String USER_ID, String PREVIOUS_ID, boolean ANONYMOUS) {
        if (uniqueInstance == null) {
            uniqueInstance = new TongDaoBridge(appContext, APP_KEY, USER_ID, PREVIOUS_ID, ANONYMOUS);
        }
        return uniqueInstance;
    }

    public void init() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (appContext != null) {
                    String gaid = TongDaoAppInfoTool.getGaid(appContext);
                    if (appContext != null) {
                        try {
                            JSONObject properties = TongDaoDataTool.makeInfoProperties(appContext, gaid);
                            if (properties != null && USER_ID != null) {
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

    /**
     * @param actionType 用户动作类型
     * @param previousId device
     * @param userIdid   用户设置的id
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
                                String gaid = TongDaoAppInfoTool.getGaid(appContext);
                                JSONObject properties = TongDaoDataTool.makeInfoProperties(appContext, gaid);
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
        Log.e("event string", eventsObj.toString());
        return eventsObj.toString();
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
        this.startTime = System.currentTimeMillis();

        if (this.pageNameStart == null || this.startTime == 0) {
            return;
        }

        HashMap<String, Object> values = new HashMap<String, Object>();
        values.put("!name", pageName);

        try {
            TdEventBean tempEb = new TdEventBean(ACTION_TYPE.track, this.USER_ID, "!open_page", TongDaoDataTool.makeUserProperties(values));
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
            TdEventBean tempEb = new TdEventBean(ACTION_TYPE.track, this.USER_ID, "!close_page", TongDaoDataTool.makeUserProperties(values));
            startTrackEvents(tempEb);
        } catch (JSONException e) {
            Log.e("onSessionEnd", "JSONException");
        } finally {
            this.startTime = 0;
            this.pageNameStart = null;
            this.pageNameEnd = null;
        }
    }

    public void startTrackEvents(final TdEventBean tdEventBean) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    trackEvents(tdEventBean);
                } catch (JSONException e) {
                    Log.e("startTrackEvents", "JSONException");
                }
            }
        }).start();
    }

    private void trackEvents(TdEventBean lqEventBean) throws JSONException {
        if (this.appContext != null && this.APP_KEY != null && this.USER_ID != null) {
            if (isCanRun()) {
                setCanRun(false);
                final ArrayList<TdEventBean> tempLqEventBeanArray = addAllLqEventBean(lqEventBean);
                try {
                    TongDaoApiTool.post(this.APP_KEY, TongDaoUrlTool.getTrackEventUrlV2(), null, makeEventsJsonString(tempLqEventBeanArray), new TdHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, String responseBody) throws ClientProtocolException, JSONException, IOException {
                            setCanRun(true);
                            if (isNeedRun()) {
                                trackEvents(null);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, String responseBody) throws JSONException {
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
                    Log.e("IOException", "trackEvents");
                    setEventList(tempLqEventBeanArray);
                    setCanRun(true);
                }
            } else {
                addWaitList(lqEventBean);
            }
        }
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
                    Log.e("startDownloadLandingPage", "ClientProtocolException");
                } catch (IOException e) {
                    Log.e("startDownloadLandingPage", "IOException");
                } catch (JSONException e) {
                    Log.e("startDownloadLandingPage", "JSONException");
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
                    Log.e("startDownloadInAppMessages", "ClientProtocolException");
                } catch (IOException e) {
                    Log.e("startDownloadInAppMessages", "IOException");
                } catch (JSONException e) {
                    Log.e("startDownloadInAppMessages", "JSONException");
                }
            }
        }).start();
    }

    private void downloadLandingPage(String pageId, final OnDownloadLandingPageListener onDownloadLandingPageListener, final OnErrorListener onErrorListener) throws ClientProtocolException, IOException, JSONException {
        if (this.appContext == null || APP_KEY == null || USER_ID == null) {
            return;
        }

        String url = TongDaoUrlTool.getLandingPageUrl(pageId, USER_ID);
        TongDaoApiTool.get(APP_KEY, true, url, null, new TdHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, String responseBody) throws ClientProtocolException, JSONException, IOException {
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


    private void downloadInAppMessages(final OnDownloadInAppMessageListener onDownloadInAppMessageListener, final OnErrorListener onErrorListener) throws ClientProtocolException, IOException, JSONException {
        if (this.appContext == null || APP_KEY == null || USER_ID == null) {
            return;
        }

        String url = TongDaoUrlTool.getInAppMessageUrl(USER_ID);
        TongDaoApiTool.get(APP_KEY, false, url, null, new TdHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, String responseBody) throws ClientProtocolException, JSONException, IOException {
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
