package com.tongdao.sdk.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class TdMessageBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String message;
    private long displayTime;
    private String layout;
    private String actionType;
    private String actionValue;
    private String imageUrl;
    private long cid;
    private long mid;
    private ArrayList<TdMessageButtonBean> buttons;
    private boolean isPortrait;
    private String closeBtn;

    public TdMessageBean(String imageUrl, String message, long displayTime, String layout, String actionType, String actionValue, long cid, long mid, ArrayList<TdMessageButtonBean> buttons, boolean isPortrait, String closeBtn) {
        this.imageUrl = imageUrl;
        this.message = message;
        this.displayTime = displayTime;
        this.layout = layout;
        this.actionType = actionType;
        this.actionValue = actionValue;
        this.cid = cid;
        this.mid = mid;
        this.buttons = buttons;
        this.isPortrait = isPortrait;
        this.closeBtn = closeBtn;
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

    public String getActionType() {
        return actionType;
    }

    public String getActionValue() {
        return actionValue;
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

    public boolean isPortrait() {
        return isPortrait;
    }

    public String getCloseBtn() {
        return closeBtn;
    }

}
