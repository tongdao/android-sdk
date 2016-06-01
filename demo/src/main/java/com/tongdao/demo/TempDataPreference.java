package com.tongdao.demo;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by John on 15/9/15.
 */
public class TempDataPreference {
    public static final String TEMP_USER_DATA = "TEMP_USER_DATA";

    private static String REWARD_JSON_STRING = "REWARD_JSON_STRING";

    public static String getRewardJsonString(Context appContext) {
        SharedPreferences sp = appContext.getSharedPreferences(TEMP_USER_DATA, Context.MODE_PRIVATE);
        return sp.getString(REWARD_JSON_STRING, null);
    }

    public static void setRewardJsonString(Context appContext, String rewardJsonString) {
        SharedPreferences sp = appContext.getSharedPreferences(TEMP_USER_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(REWARD_JSON_STRING, rewardJsonString);
        editor.commit();
    }
}
