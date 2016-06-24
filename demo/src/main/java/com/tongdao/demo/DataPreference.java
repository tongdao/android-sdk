package com.tongdao.demo;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by John on 15/9/15.
 */
public class DataPreference {

    public static final String USER_DATA = "USER_DATA";

    private static final String BTN_JSON_STRING = "BTN_JSON_STRING";

    private static final String BK_STRING = "BK_STRING";

    private static final String REWARD_JSON_STRING = "REWARD_JSON_STRING";

    public static String getBtnJsonString(Context appContext) {
        SharedPreferences sp = appContext.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
        return sp.getString(BTN_JSON_STRING, null);
    }

    public static void setBtnJsonString(Context appContext, String btnJsonString) {
        SharedPreferences sp = appContext.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(BTN_JSON_STRING, btnJsonString);
        editor.commit();
    }

    public static String getBkString(Context appContext) {
        SharedPreferences sp = appContext.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
        return sp.getString(BK_STRING, null);
    }

    public static void setBkString(Context appContext, String bkString) {
        SharedPreferences sp = appContext.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(BK_STRING, bkString);
        editor.commit();
    }

    public static String getRewardJsonString(Context appContext) {
        SharedPreferences sp = appContext.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
        return sp.getString(REWARD_JSON_STRING, null);
    }

    public static void setRewardJsonString(Context appContext, String rewardJsonString) {
        SharedPreferences sp = appContext.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(REWARD_JSON_STRING, rewardJsonString);
        editor.commit();
    }
}
