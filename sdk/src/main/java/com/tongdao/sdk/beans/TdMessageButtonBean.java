package com.tongdao.sdk.beans;

import java.io.Serializable;

public class TdMessageButtonBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private double rateX;
    private double rateY;
    private double rateW;
    private double rateH;
    private String actionType;
    private String actionValue;

    public TdMessageButtonBean(double rateX, double rateY, double rateW, double rateH, String actionType, String actionValue) {
        this.rateX = rateX;
        this.rateY = rateY;
        this.rateW = rateW;
        this.rateH = rateH;
        this.actionType = actionType;
        this.actionValue = actionValue;
    }

    public double getRateX() {
        return rateX;
    }

    public double getRateY() {
        return rateY;
    }

    public double getRateW() {
        return rateW;
    }

    public double getRateH() {
        return rateH;
    }

    public String getActionType() {
        return actionType;
    }

    public String getActionValue() {
        return actionValue;
    }

}
