package com.tongdao.demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.tongdao.newsdk.TongDao;

/**
 * Created by kinjal.patel on 23/08/16.
 */
public class UmengPushMessageReceiver extends BroadcastReceiver {
    public static String OPEN_URL = "com.umeng.message.OPEN_URL";
    private static String TAG = UmengPushMessageReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase(OPEN_URL)) {
            Intent newIntent = new Intent();
            newIntent.setAction(Intent.ACTION_VIEW);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            newIntent.setData(Uri.parse(intent.getStringExtra("value")));

            context.startActivity(newIntent);

            if (intent.getStringExtra("NotificationMessage") != null) {
                // extract the extra-data in the Notification
                String msg = intent.getStringExtra("NotificationMessage");
                Log.i(TAG, "NotificationMessage - " + msg);
                TongDao.trackOpenPushMessage(msg);
                TongDao.openPage(context, msg);
            }
        }
    }
}
