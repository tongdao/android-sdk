package com.tongdao.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.tongdao.sdk.TongDao;

public class DemoPage1 extends AppCompatActivity {
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
        TongDao tongDao = ((TongDaoShowApplication)getApplication()).getTongDao();
        tongDao.displayInAppMessage(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
