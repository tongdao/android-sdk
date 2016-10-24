package com.tongdao.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.tongdao.sdk.TongDaoOO;
import com.tongdao.sdk.beans.TdRewardBean;
import com.tongdao.sdk.interfaces.OnRewardUnlockedListener;

import org.json.JSONException;

import java.util.ArrayList;

public class DemoPage4 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().setIcon(R.drawable.ic_launcher);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.getSupportActionBar().setDisplayShowTitleEnabled(true);
        this.getSupportActionBar().setTitle("Deep linking");
        this.getSupportActionBar().setBackgroundDrawable(this.getResources().getDrawable(R.drawable.bar));
        this.setContentView(R.layout.page);
        ((TextView) this.findViewById(R.id.link_tv)).setText("demo://page4");

        this.registerListeners();
        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, DataTool.BAIDU_API_KEY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        TongDaoOO tongDao = ((TongDaoShowApplication)getApplication()).getTongDao();
        tongDao.displayInAppMessage(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void registerListeners() {
        TongDaoOO tongDao = ((TongDaoShowApplication)getApplication()).getTongDao();
        tongDao
                .registerOnRewardUnlockedListener(new OnRewardUnlockedListener() {
                    @Override
                    public void onSuccess(ArrayList<TdRewardBean> rewards) {
                        if (rewards != null && rewards.size() > 0) {
                            try {
                                DataTool.saveTempRewards(DemoPage4.this, rewards);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }
}
