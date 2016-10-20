package com.tongdao.sdk.tools;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.support.v4.app.NotificationManagerCompat;

/**
 * Created by kinjal.patel on 15/08/16.
 */
public class TongDaoUtils {

    private static Context cxt;

    public TongDaoUtils(Context context) {
        cxt = context;
    }

    /**
     * Method to check whether notifications are enabled on the device. Currently I am trying the
     * new NotificationManagerCompat feature introduced in Android 24.
     * @return
     */
    @SuppressWarnings("unchecked")
    public boolean isNotificationEnabled() {
        return NotificationManagerCompat.from(cxt).areNotificationsEnabled();
    }
}
