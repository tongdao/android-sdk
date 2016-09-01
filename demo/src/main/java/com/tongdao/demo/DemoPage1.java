package com.tongdao.demo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.tongdao.sdk.beans.TdRewardBean;
import com.tongdao.sdk.interfaces.ui.OnRewardUnlockedListener;
import com.tongdao.sdk.ui.TongDaoUiCore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DemoPage1 extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().setIcon(R.drawable.ic_launcher);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.getSupportActionBar().setDisplayShowTitleEnabled(true);
        this.getSupportActionBar().setTitle("Deep linking");
        this.getSupportActionBar().setBackgroundDrawable(this.getResources().getDrawable(R.drawable.bar));
        this.setContentView(R.layout.page);
        ((TextView) this.findViewById(R.id.link_tv)).setText("demo://page1");

        this.registerListeners();
        TongDaoUiCore.displayAdvertisement(this);
        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, DataTool.BAIDU_API_KEY);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                MessageHandler receiver = new MessageHandler();
//                try {
//                    receiver.redirectPage(DemoPage1.this, "app", "http://www.baidu.com", "72837", "8263762");
//                }
//                catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }, 3000);

    }

    @Override
    protected void onResume() {
        super.onResume();
        TongDaoUiCore.onSessionStart(this);
        TongDaoUiCore.displayInAppMessage(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        TongDaoUiCore.onSessionEnd(this);
    }

    private void registerListeners() {
        TongDaoUiCore
                .registerOnRewardUnlockedListener(new OnRewardUnlockedListener() {
                    @Override
                    public void onSuccess(ArrayList<TdRewardBean> rewards) {
                        if (rewards != null && rewards.size() > 0) {
                            try {
                                DataTool.saveTempRewards(DemoPage1.this, rewards);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }
}
