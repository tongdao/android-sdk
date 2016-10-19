package com.tongdao.demo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.tongdao.sdk.beans.TdRewardBean;
import com.tongdao.sdk.interfaces.OnRewardUnlockedListener;
import com.tongdao.sdk.TongDao;

import org.json.JSONException;

import java.util.ArrayList;

public class DemoPage3 extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().setIcon(R.drawable.ic_launcher);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.getSupportActionBar().setDisplayShowTitleEnabled(true);
        this.getSupportActionBar().setTitle("Deep linking");
        this.getSupportActionBar().setBackgroundDrawable(this.getResources().getDrawable(R.drawable.bar));
        this.setContentView(R.layout.page);
        ((TextView) this.findViewById(R.id.link_tv)).setText("demo://page3");

        this.registerListeners();
        TongDao.displayAdvertisement(this);
        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, DataTool.BAIDU_API_KEY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        TongDao.onSessionStart(this);
        TongDao.displayInAppMessage(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        TongDao.onSessionEnd(this);
    }

    private void registerListeners() {
        TongDao
                .registerOnRewardUnlockedListener(new OnRewardUnlockedListener() {
                    @Override
                    public void onSuccess(ArrayList<TdRewardBean> rewards) {
                        if (rewards != null && rewards.size() > 0) {
                            try {
                                DataTool.saveTempRewards(DemoPage3.this, rewards);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

}
