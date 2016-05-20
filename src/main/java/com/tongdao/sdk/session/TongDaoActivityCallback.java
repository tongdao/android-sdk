package com.tongdao.sdk.session;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;

import com.tongdao.sdk.TongDao;

import java.util.List;

/**
 * Created by kinjal.patel on 16/05/16.
 */
public class TongDaoActivityCallback implements Application.ActivityLifecycleCallbacks {
    private Context context;
    private boolean isAppBroughtToBackground = false;

    public TongDaoActivityCallback(Context context) {
        this.context = context;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
        try {
            Thread.sleep(500);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if( isApplicationBroughtToBackground() ) {
            TongDao.onAppSessionEnd();
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        try {
            Thread.sleep(500);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if( isAppBroughtToBackground && !isApplicationBroughtToBackground() ) {
            TongDao.onAppSessionStart();
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

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

}
