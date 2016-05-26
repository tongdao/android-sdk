package com.tongdao.sdk.tools;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONObject;

public class TongDaoSavingTool {

    private static final String USER_INFO_DATA = "LQ_USER_INFO_DATA";
    private static final String USER_ID = "LQ_USER_ID";
    private static final String APP_KEY = "LQ_APP_KEY";
    private static final String PREVIOUS_ID = "LQ_PREVIOUS_ID";
    private static final String ANONYMOUS = "LQ_ANONYMOUS";
    private static final String ANONYMOUS_SET = "IS_ANONYMOUS";
    private static final String APPLICATION_DATA = "APPLICATION_DATA";
    private static final String CONNECTION_DATA = "CONNECTION_DATA";
    private static final String LOCATION_DATA = "LOCATION_DATA";
    private static final String DEVICE_DATA = "DEVICE_DATA";
    private static final String FINGERPRINT_DATA = "FINGERPRINT_DATA";
    private static final String APP_SESSION_DATA = "APP_SESSION";

    public static void saveAppKeyAndUserId(Context appContext, String appKey, String userId) {
        SharedPreferences sp = appContext.getSharedPreferences(USER_INFO_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(APP_KEY, appKey);
        editor.putString(USER_ID, userId);
        editor.commit();
    }
    public static void saveUserInfoData(Context appContext, String appKey, String userId, String previousId, Boolean anonymous) {
        TongDaoDataTool.setAnonymous(anonymous);
        SharedPreferences sp = appContext.getSharedPreferences(USER_INFO_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(APP_KEY, appKey);
        editor.putString(USER_ID, userId);
        editor.putString(PREVIOUS_ID, previousId);
        editor.putBoolean(ANONYMOUS, anonymous);
        editor.commit();
    }

    public static void saveUserInfoData(Context appContext, String userId, String previousId, Boolean anonymous) {
        TongDaoDataTool.setAnonymous(anonymous);
        SharedPreferences sp = appContext.getSharedPreferences(USER_INFO_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(USER_ID, userId);
        editor.putString(PREVIOUS_ID, previousId);
        editor.putBoolean(ANONYMOUS, anonymous);
        editor.commit();
    }

    public static void isAnonymousSet(Context appContext){
        SharedPreferences sp = appContext.getSharedPreferences(USER_INFO_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(ANONYMOUS_SET, true);
        editor.commit();
    }

    public static boolean getAnonymousSet(Context appContext){
        SharedPreferences sp = appContext.getSharedPreferences(USER_INFO_DATA, Context.MODE_PRIVATE);
        return sp.getBoolean(ANONYMOUS_SET, false);
    }

    public static Boolean getAnonymous(Context appContext){
        SharedPreferences sp = appContext.getSharedPreferences(USER_INFO_DATA, Context.MODE_PRIVATE);
        boolean anonymous = sp.getBoolean(ANONYMOUS, true);
        TongDaoDataTool.setAnonymous(anonymous);
        return anonymous;
    }

    public static void setAnonymous(Context appContext, boolean anonymous){
        TongDaoDataTool.setAnonymous(anonymous);
        SharedPreferences sp = appContext.getSharedPreferences(USER_INFO_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(ANONYMOUS, anonymous);
        editor.commit();
    }

    public static void setPermissionDenied(Context appContext, String permission) {
        SharedPreferences sp = appContext.getSharedPreferences(USER_INFO_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(permission, true);
        editor.commit();
    }

    public static boolean getPermissionDenied(Context appContext, String permission) {
        SharedPreferences sp = appContext.getSharedPreferences(USER_INFO_DATA, Context.MODE_PRIVATE);
        return sp.getBoolean(permission, false);
    }

    public static void setApplicationInfoData(Context appContext, String appData) {
        SharedPreferences sp = appContext.getSharedPreferences(USER_INFO_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(APPLICATION_DATA, appData);
        editor.commit();

    }

    public static String getApplicationInfoData(Context appContext) {
        SharedPreferences sp = appContext.getSharedPreferences(USER_INFO_DATA, Context.MODE_PRIVATE);
        return sp.getString(APPLICATION_DATA, null);
    }

    public static void setConnectionInfoData(Context appContext, String connectionData) {
        SharedPreferences sp = appContext.getSharedPreferences(USER_INFO_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(CONNECTION_DATA, connectionData);
        editor.commit();

    }

    public static String getConnectionInfoData(Context appContext) {
        SharedPreferences sp = appContext.getSharedPreferences(USER_INFO_DATA, Context.MODE_PRIVATE);
        return sp.getString(CONNECTION_DATA, null);
    }

    public static void setLocationInfoData(Context appContext, String locationData) {
        SharedPreferences sp = appContext.getSharedPreferences(USER_INFO_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(LOCATION_DATA, locationData);
        editor.commit();

    }

    public static String getLocationInfoData(Context appContext) {
        SharedPreferences sp = appContext.getSharedPreferences(USER_INFO_DATA, Context.MODE_PRIVATE);
        return sp.getString(LOCATION_DATA, null);
    }

    public static void setDeviceInfoData(Context appContext, String deviceData) {
        SharedPreferences sp = appContext.getSharedPreferences(USER_INFO_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(DEVICE_DATA, deviceData);
        editor.commit();

    }

    public static String getDeviceInfoData(Context appContext) {
        SharedPreferences sp = appContext.getSharedPreferences(USER_INFO_DATA, Context.MODE_PRIVATE);
        return sp.getString(DEVICE_DATA, null);
    }

    public static void setFingerprintInfoData(Context appContext, String fingerPrintData) {
        SharedPreferences sp = appContext.getSharedPreferences(USER_INFO_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(FINGERPRINT_DATA, fingerPrintData);
        editor.commit();

    }

    public static String getFingerprintInfoData(Context appContext) {
        SharedPreferences sp = appContext.getSharedPreferences(USER_INFO_DATA, Context.MODE_PRIVATE);
        return sp.getString(FINGERPRINT_DATA, null);
    }

    public static void setAppSessionData(Context appContext, String sessionData) {
        SharedPreferences sp = appContext.getSharedPreferences(USER_INFO_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(APP_SESSION_DATA, sessionData);
        editor.commit();
    }

    public static String getAppSessionData(Context appContext) {
        SharedPreferences sp = appContext.getSharedPreferences(USER_INFO_DATA, Context.MODE_PRIVATE);
        return sp.getString(APP_SESSION_DATA, null);
    }

}
