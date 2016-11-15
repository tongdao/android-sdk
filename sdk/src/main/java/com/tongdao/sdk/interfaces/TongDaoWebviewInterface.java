package com.tongdao.sdk.interfaces;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.LinearLayout;

import com.tongdao.sdk.beans.TdMessageBean;
import com.tongdao.sdk.tools.Log;

/**
 * Created by agonch on 11/1/16.
 */

public class TongDaoWebviewInterface {
    ViewGroup container;
    LinearLayout popup;
    LinearLayout.LayoutParams params;
    Activity activity;
    TdMessageBean tdMessageBean;
    InAppMessageCallback callback;
    int screenWidth;
    int screenHeight;
    int width;
    int height;
    int top;
    int left;

    public TongDaoWebviewInterface(ViewGroup container, LinearLayout popup, Activity activity, InAppMessageCallback callback) {
        this.container = container;
        this.popup = popup;
        this.activity = activity;
        this.callback = callback;
    }

    @JavascriptInterface
    public void closePopup(){

    }

    @JavascriptInterface
    public void openPopup(){
        Log.i("Popup Manager WebView","open popup");
        if (params != null && container != null){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    container.addView(popup,params);
                    float[] animation = {-screenHeight,top};
                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(popup,"y",animation);
                }
            });
        }
    }

    @JavascriptInterface
    public void setSize(int width, int height){
        Log.i("Popup Manager WebView","set size: " + width + "," + height);
        params = new LinearLayout.LayoutParams(width,height);
        width = width;
        height = height;
    }

    @JavascriptInterface
    public void positionPopup(int x, int y){
        popup.setTranslationX(x);
        popup.setTranslationY(y);
        top = y;
        left = x;

    }

    @JavascriptInterface
    public int getScreenWidth(){
        Log.i("Popup Manager WebView","get page height");
        return container.getWidth();
    }

    @JavascriptInterface
    public int getScreenHeight(){
        Log.i("Popup Manager WebView","get page width");
        return container.getHeight();
    }

    @JavascriptInterface
    public void trackOpenInAppMessage(){
        callback.callbackTrackOpenInAppMessage(tdMessageBean);
    }

    @JavascriptInterface
    public void trackReceivedInAppMessage(){
        callback.callbackTrackReceivedInAppMessage(tdMessageBean);
    }

    @JavascriptInterface
    public void openMessage(){
        callback.callbackOpenMessage(tdMessageBean);
    }

    @JavascriptInterface
    public float getDensity(){
        return activity.getResources().getDisplayMetrics().density;
    }
}
