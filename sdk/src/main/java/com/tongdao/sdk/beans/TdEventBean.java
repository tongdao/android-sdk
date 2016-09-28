package com.tongdao.sdk.beans;

import android.support.annotation.NonNull;

import com.tongdao.sdk.tools.TongDaoCheckTool;
import com.tongdao.sdk.tools.TongDaoClockTool;

import org.json.JSONException;
import org.json.JSONObject;

public class TdEventBean {

    public enum ACTION_TYPE {
        merge, identify, track
    }

    private ACTION_TYPE action;
    private String userId;
    private String previousId;
    private String event;
    private JSONObject properties;
    private String timestamp;

    public TdEventBean(@NonNull ACTION_TYPE action, String userId, String event, JSONObject properties) {
        this.action = action;
        this.userId = userId;
        this.event = event;
        this.properties = properties;
        this.timestamp = TongDaoCheckTool.getTimeStamp(TongDaoClockTool.currentTimeMillis());
    }

    public TdEventBean(@NonNull ACTION_TYPE action, String userId, String event) {
        this.action = action;
        this.userId = userId;
        this.event = event;
        this.timestamp = TongDaoCheckTool.getTimeStamp(TongDaoClockTool.currentTimeMillis());
    }

    public TdEventBean(@NonNull ACTION_TYPE action, String userId, String previousId, String event, JSONObject properties) {
        this.action = action;
        this.previousId = previousId;
        this.userId = userId;
        this.event = event;
        this.properties = properties;
        this.timestamp = TongDaoCheckTool.getTimeStamp(TongDaoClockTool.currentTimeMillis());
    }

    public JSONObject getJsonObject() throws JSONException {
        JSONObject tempJsonObj = new JSONObject();
        tempJsonObj.put("action", this.action.toString());
        if (this.userId != null) {
            tempJsonObj.put("user_id", this.userId);
        }

        if (this.event != null && action != ACTION_TYPE.identify) {
            tempJsonObj.put("event", this.event);
        }

        if (this.previousId != null && ACTION_TYPE.merge == action) {
            tempJsonObj.put("previous_id", this.previousId);
        }

        if (this.properties != null) {
            tempJsonObj.put("properties", this.properties);
        }

        tempJsonObj.put("timestamp", this.timestamp);

        return tempJsonObj;
    }

}
