package com.tongdao.sdk.session;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.tongdao.sdk.TongDao;

/**
 * Created by kinjal.patel on 16/05/16.
 */
public class TongDaoActivityCallback implements Application.ActivityLifecycleCallbacks {

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
        TongDao.onSessionEnd(activity);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        TongDao.onSessionStart(activity);
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
}
