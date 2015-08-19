package com.tongdao.sdk.tools;

import android.content.Context;
import android.content.SharedPreferences;

public class TongDaoSavingTool {

    private static final String USER_INFO_DATA = "LQ_USER_INFO_DATA";
    private static final String USER_ID = "LQ_USER_ID";
    private static final String APP_KEY = "LQ_APP_KEY";

    public static void saveAppKeyAndUserId(Context appContext, String appKey, String UserId) {
        SharedPreferences sp = appContext.getSharedPreferences(USER_INFO_DATA, Context.MODE_PRIVATE);
        sp.edit().putString(APP_KEY, appKey);
        sp.edit().putString(USER_ID, UserId);
        sp.edit().commit();
    }

    public static String getUserId(Context appContext) {
        SharedPreferences sp = appContext.getSharedPreferences(USER_INFO_DATA, Context.MODE_PRIVATE);
        return sp.getString(USER_ID, null);
    }

    public static String getAppKey(Context appContext) {
        SharedPreferences sp = appContext.getSharedPreferences(USER_INFO_DATA, Context.MODE_PRIVATE);
        return sp.getString(APP_KEY, null);
    }
}
