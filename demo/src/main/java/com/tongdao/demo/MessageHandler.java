package com.tongdao.demo;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.umeng.message.UHandler;
import com.umeng.message.entity.UMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kinjal.patel on 23/08/16.
 */
public class MessageHandler implements UHandler {

    private Context cxt = null;
    private String TAG = "";

    @Override
    public void handleMessage(Context context, UMessage uMessage) {
        this.cxt = context;

        Log.e("UmengMessage", uMessage.extra.toString());

        String type = uMessage.extra.get("tongrd_type");
        String value = uMessage.extra.get("tongrd_value");
        String mid = uMessage.extra.get("tongrd_mid");
        String cid = uMessage.extra.get("tongrd_cid");

        String message = uMessage.extra.get("message");

//        String extraData = uMessage.extra.toString();
        Log.e(TAG, "Message: " + message);

        try {
            redirectPage(type, value, mid, cid);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void redirectPage(String type, String value, String mid, String cid) throws JSONException{
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("value", value);

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("tongrd_type", type);
        jsonObject.put("tongrd_value", value);
        jsonObject.put("tongrd_mid", Long.parseLong(mid));
        jsonObject.put("tongrd_cid", Long.parseLong(cid));

        String extraData = jsonObject.toString();

        intent.putExtra("NotificationMessage", extraData);

        if (type.equalsIgnoreCase("url")) {
            intent.setAction(UmengPushMessageReceiver.OPEN_URL);
            cxt.sendBroadcast(intent);
        }
        else {
            intent.setClass(cxt, MainActivity.class);
            cxt.startActivity(intent);
        }
    }
}
