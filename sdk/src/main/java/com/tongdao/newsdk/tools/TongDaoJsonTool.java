package com.tongdao.newsdk.tools;

import org.json.JSONObject;

public class TongDaoJsonTool {

    public static String optJsonString(JSONObject json, String key) {
        if (json == null)
            return null;
        if (json.isNull(key))
            return null;
        else
            return json.optString(key, null);
    }
}
