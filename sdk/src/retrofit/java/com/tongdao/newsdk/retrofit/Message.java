
package com.tongdao.sdk.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Message {

    @SerializedName("cid")
    @Expose
    private Integer cid;
    @SerializedName("mid")
    @Expose
    private Integer mid;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("display_time")
    @Expose
    private Integer displayTime;
    @SerializedName("layout")
    @Expose
    private String layout;
    @SerializedName("action")
    @Expose
    private Action action;

    /**
     *
     * @return
     *     The cid
     */
    public Integer getCid() {
        return cid;
    }

    /**
     *
     * @param cid
     *     The cid
     */
    public void setCid(Integer cid) {
        this.cid = cid;
    }

    /**
     *
     * @return
     *     The mid
     */
    public Integer getMid() {
        return mid;
    }

    /**
     *
     * @param mid
     *     The mid
     */
    public void setMid(Integer mid) {
        this.mid = mid;
    }

    /**
     *
     * @return
     *     The message
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message
     *     The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     *
     * @return
     *     The imageUrl
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     *
     * @param imageUrl
     *     The image_url
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     *
     * @return
     *     The displayTime
     */
    public Integer getDisplayTime() {
        return displayTime;
    }

    /**
     *
     * @param displayTime
     *     The display_time
     */
    public void setDisplayTime(Integer displayTime) {
        this.displayTime = displayTime;
    }

    /**
     *
     * @return
     *     The layout
     */
    public String getLayout() {
        return layout;
    }

    /**
     *
     * @param layout
     *     The layout
     */
    public void setLayout(String layout) {
        this.layout = layout;
    }

    /**
     *
     * @return
     *     The action
     */
    public Action getAction() {
        return action;
    }

    /**
     *
     * @param action
     *     The action
     */
    public void setAction(Action action) {
        this.action = action;
    }

}
