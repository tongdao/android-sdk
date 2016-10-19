
package com.tongdao.newsdk.retrofit;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Event {

    @SerializedName("action")
    @Expose
    private String action;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("properties")
    @Expose
    private Properties properties;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;

    /**
     *
     * @return
     *     The action
     */
    public String getAction() {
        return action;
    }

    /**
     *
     * @param action
     *     The action
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     *
     * @return
     *     The userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     *
     * @param userId
     *     The user_id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     *
     * @return
     *     The properties
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     *
     * @param properties
     *     The properties
     */
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    /**
     * 
     * @return
     *     The timestamp
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * 
     * @param timestamp
     *     The timestamp
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
