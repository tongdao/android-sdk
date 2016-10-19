package com.tongdao.newsdk.tools;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.support.v4.app.NotificationManagerCompat;

/**
 * Created by kinjal.patel on 15/08/16.
 */
public class TongDaoUtils {

    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";
    private static Context cxt;
    private static ApplicationInfo appInfo;
    private static String pkg;

    public static void init(Context context) {
        cxt = context;
        appInfo = context.getApplicationInfo();
        pkg = context.getApplicationContext().getPackageName();
    }

    /**
     * Method to check whether notifications are enabled on the device. Currently I am trying the
     * new NotificationManagerCompat feature introduced in Android 24.
     * @return
     */
    @SuppressWarnings("unchecked")
    public static boolean isNotificationEnabled() {
        return NotificationManagerCompat.from(cxt).areNotificationsEnabled();
    }
}
