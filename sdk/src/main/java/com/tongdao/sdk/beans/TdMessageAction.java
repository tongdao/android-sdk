package com.tongdao.sdk.beans;

/**
 * Created by agonch on 11/1/16.
 */

public class TdMessageAction {
    private String text;
    private String type;
    private String value;

    public TdMessageAction(String text, String type, String value) {
        this.text = text;
        this.type = type;
        this.value = value;
    }
}
