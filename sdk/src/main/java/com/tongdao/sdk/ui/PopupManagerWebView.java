package com.tongdao.sdk.ui;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.tongdao.sdk.R;
import com.tongdao.sdk.beans.TdMessageBean;
import com.tongdao.sdk.interfaces.InAppMessageCallback;
import com.tongdao.sdk.interfaces.TongDaoWebviewInterface;
import com.tongdao.sdk.tools.Log;
import com.tongdao.sdk.tools.TdImageManager;

/**
 * Created by agonch on 11/1/16.
 */

public class PopupManagerWebView{

    private TdMessageBean tdMessageBean;
    private WebView webView;
    private LinearLayout popupView;
    private ViewGroup rootView;
    private InAppMessageCallback callback;

    public PopupManagerWebView(TdMessageBean tdMessageBean, InAppMessageCallback callback) {
        this.tdMessageBean = tdMessageBean;
        this.callback = callback;
    }

    public void displayPopup(Activity activity){
        rootView = (ViewGroup) activity.getWindow().getDecorView();
        popupView = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.td_popup_webview,rootView,false);
        webView = (WebView) popupView.findViewById(R.id.td_popup_view);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            webView.setWebContentsDebuggingEnabled(true);
        }
        webView.setInitialScale(50);
        webView.addJavascriptInterface(new TongDaoWebviewInterface(rootView,popupView,activity,callback),"tongdao");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.i("Popup Manager WebView","page finished loading");
            }
        });
        webView.loadUrl("file:///android_asset/index.html");
        webView.setBackgroundColor(0x00000000);
    }
}
