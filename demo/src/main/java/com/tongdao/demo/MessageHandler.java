package com.tongdao.demo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.umeng.message.UHandler;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

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

        String message = uMessage.extra.get("message");

        String extraData = uMessage.extra.toString();
        Log.e(TAG, "Message: " + message);

        redirectPage(message, type, value, extraData);
    }

    private void redirectPage(String message, String type, String value, String extraData) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("value", value);
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
