package com.tongdao.newsdk.session;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.tongdao.sdk.TongDao;

import java.util.List;

public class TongDaoActivityCallback implements Application.ActivityLifecycleCallbacks {
    private Context context;
    private boolean isAppBroughtToBackground = false;

    public TongDaoActivityCallback(Context context) {
        this.context = context;

        IntentFilter inFilter = new IntentFilter();
        inFilter.addAction(Intent.ACTION_SCREEN_OFF);
        inFilter.addAction(Intent.ACTION_SCREEN_ON);
        context.registerReceiver(new DeviceEventReceiver(), inFilter);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if( isAppBroughtToBackground && !isApplicationBroughtToBackground() ) {
            TongDao.onAppSessionStart();
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        if( isApplicationBroughtToBackground() ) {
            TongDao.onAppSessionEnd();
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        try {
            Thread.sleep(500);
            if( isApplicationBroughtToBackground() ) {
                isAppBroughtToBackground = false;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isApplicationBroughtToBackground() {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                isAppBroughtToBackground = true;
                return true;
            }
        }
        isAppBroughtToBackground = false;
        return false;
    }

    private class DeviceEventReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if( intent.getAction() == Intent.ACTION_SCREEN_OFF && !isApplicationBroughtToBackground()) {
                TongDao.onAppSessionEnd();
            }

            if( intent.getAction() == Intent.ACTION_SCREEN_ON && !isApplicationBroughtToBackground()) {
                TongDao.onAppSessionStart();
            }
        }
    }

}
