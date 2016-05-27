package com.tongdao.sdk.beans;

import org.json.JSONException;
import org.json.JSONObject;

import com.tongdao.sdk.tools.TongDaoCheckTool;

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

    public TdEventBean(ACTION_TYPE action, String userId, String event, JSONObject properties) {
        this.action = action;
        this.userId = userId;
        this.event = event;
        this.properties = properties;
        this.timestamp = TongDaoCheckTool.getTimeStamp(System.currentTimeMillis());
    }

    public TdEventBean(ACTION_TYPE action, String userId, String event) {
        this.action = action;
        this.userId = userId;
        this.event = event;
        this.timestamp = TongDaoCheckTool.getTimeStamp(System.currentTimeMillis());
    }

    public TdEventBean(ACTION_TYPE action, String userId, String previousId, String event, JSONObject properties) {
        this.action = action;
        this.previousId = previousId;
        this.userId = userId;
        this.event = event;
        this.properties = properties;
        this.timestamp = TongDaoCheckTool.getTimeStamp(System.currentTimeMillis());
    }

    public JSONObject getJsonObject() throws JSONException {
        JSONObject tempJsonObj = new JSONObject();
        tempJsonObj.put("action", this.action.toString());
        if (this.userId != null) {
            tempJsonObj.put("user_id", this.userId);
        }

        if (this.event != null) {
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
