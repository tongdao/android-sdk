package com.tongdao.sdk.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class TdMessageBean implements Serializable {

    /**
     * cid = campaign id, for statistics
     * mid = message id, for statistics
     *
     */
    private static final long serialVersionUID = 1L;
    private long minSdk;
    private long cid;
    private long mid;
    private String title;
    private String message;
    private String imageUrl;
    private long displayTime;
    private String layout;
    private TdMessageAction messageAction;
    private ArrayList<TdMessageButtonBean> buttons;

    public TdMessageBean(long minSdk, long cid, long mid, String title, String message, String imageUrl, long displayTime, String layout, TdMessageAction messageAction, ArrayList<TdMessageButtonBean> buttons) {
        this.minSdk = minSdk;
        this.cid = cid;
        this.mid = mid;
        this.title = title;
        this.message = message;
        this.imageUrl = imageUrl;
        this.displayTime = displayTime;
        this.layout = layout;
        this.messageAction = messageAction;
        this.buttons = buttons;
    }

    public String getMessage() {
        return message;
    }

    public long getDisplayTime() {
        return displayTime;
    }

    public String getLayout() {
        return layout;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public long getCid() {
        return this.cid;
    }

    public long getMid() {
        return this.mid;
    }

    public ArrayList<TdMessageButtonBean> getButtons() {
        return this.buttons;
    }

    public String getTitle() {
        return title;
    }

    public long getMinSdk() {
        return minSdk;
    }

    public TdMessageAction getMessageAction() {
        return messageAction;
    }
}
