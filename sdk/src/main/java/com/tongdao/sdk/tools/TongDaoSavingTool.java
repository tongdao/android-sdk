package com.tongdao.sdk.tools;

import android.content.Context;
import android.content.SharedPreferences;

public class TongDaoSavingTool {

    private static final String USER_INFO_DATA = "LQ_USER_INFO_DATA";
    private static final String USER_ID = "LQ_USER_ID";
    private static final String APP_KEY = "LQ_APP_KEY";
    private static final String PREVIOUS_ID = "LQ_PREVIOUS_ID";
    private static final String ANONYMOUS = "LQ_ANONYMOUS";

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
}
