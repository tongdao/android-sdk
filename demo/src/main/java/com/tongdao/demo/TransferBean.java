package com.tongdao.demo;

import java.util.HashMap;

public class TransferBean {

    private Type type;

    private String buttonName;

    private String eventName;

    private HashMap<String, Object> datas;

    public TransferBean(Type type, String buttonName, String eventName, HashMap<String, Object> datas) {
        this.type = type;
        this.buttonName = buttonName;
        this.eventName = eventName;
        this.datas = datas;
    }

    public Type getType() {
        return type;
    }

    public String getButtonName() {
        return buttonName;
    }

    public String getEventName() {
        return eventName;
    }

    public HashMap<String, Object> getDatas() {
        return datas;
    }
}
