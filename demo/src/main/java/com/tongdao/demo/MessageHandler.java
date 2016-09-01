package com.tongdao.demo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

/**
 * Created by kinjal.patel on 23/08/16.
 */
public class MessageHandler extends UmengMessageHandler {

    private Context cxt = null;
    private String TAG = "";

    MessageHandler() {
        super();
    }

    @Override
    public void dealWithNotificationMessage(Context context, UMessage uMessage) {
        super.dealWithNotificationMessage(context, uMessage);

        this.cxt = context;

        Log.e("UmengMessage", uMessage.extra.toString());
//        try {
//            JSONObject jsonObject = new JSONObject(uMessage.extra.toString());

            String type = uMessage.extra.get("tongrd_type"); //jsonObject.getString("tongrd_type");
            String value = uMessage.extra.get("tongrd_value"); //jsonObject.getString("tongrd_value");

            String message = uMessage.extra.get("message"); //jsonObject.getString("message");

            String extraData = uMessage.extra.toString();
            Log.e(TAG, "Message: " + message);

            sendNotification(message, type, value, extraData);

//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    private void sendNotification(String message, String type, String value, String extraData) {

        Intent intent = new Intent();

        PendingIntent pendingIntent;
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("value", value);
        intent.putExtra("NotificationMessage", extraData);

        if (type.equalsIgnoreCase("url")) {
            intent.setAction(UmengPushMessageReceiver.OPEN_URL);
            pendingIntent = PendingIntent.getBroadcast(cxt, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        } else {
            intent.setClass(cxt, MainActivity.class);
            pendingIntent = PendingIntent.getActivity(cxt, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);

        }

        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        android.support.v4.app.NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(cxt)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("GCM Message")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) cxt.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
