package com.tongdao.demo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.tongdao.sdk.TongDaoOO;
import com.tongdao.sdk.beans.TdRewardBean;
import com.tongdao.sdk.interfaces.OnRewardUnlockedListener;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private LinearLayout btnContainer;
    private LinearLayout rewardsContainer;
    private ImageView mainCn;
    private Bitmap bm;
    private LayoutInflater inflater;
    private static final int SAMPLE_SIZE = 4;
    private ArrayList<Bitmap> rewardBitmaps = new ArrayList<Bitmap>();
    private Uri bkUri;

    private PushAgent mPushAgent;
    public Handler handler = new Handler();

    PackageManager pm;
    String packageName;
    TongDaoOO tongDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tongDao = ((TongDaoShowApplication)getApplication()).getTongDao();
        this.getSupportActionBar().setIcon(R.drawable.ic_launcher);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.getSupportActionBar().setDisplayShowTitleEnabled(true);
        this.getSupportActionBar().setTitle("Fellow Demo");
        this.getSupportActionBar().setBackgroundDrawable(this.getResources().getDrawable(R.drawable.bar));
        setContentView(R.layout.activity_main);


        this.inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mainCn = (ImageView) this.findViewById(R.id.main_cn);
        this.btnContainer = (LinearLayout) this.findViewById(R.id.btn_container);
        this.rewardsContainer = (LinearLayout) this.findViewById(R.id.rewards_container);
        this.findViewById(R.id.page1_tv).setOnClickListener(this);
        this.findViewById(R.id.page2_tv).setOnClickListener(this);
        this.findViewById(R.id.page3_tv).setOnClickListener(this);
        this.findViewById(R.id.page4_tv).setOnClickListener(this);
        this.findViewById(R.id.page5_tv).setOnClickListener(this);
        this.findViewById(R.id.page6_tv).setOnClickListener(this);
        this.findViewById(R.id.identify_tv).setOnClickListener(this);
        this.findViewById(R.id.track_tv).setOnClickListener(this);

        this.loadBtns();

        this.registerListeners();


        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, DataTool.BAIDU_API_KEY);

        // Umeng Push notification registration

        mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);
        mPushAgent.onAppStart();
        PushAgent.getInstance(this).setNotificationClickHandler(umengMessageHandler);

        if (!mPushAgent.isRegistered()) {
            Log.e("Push", "Not registered...");
            mPushAgent.enable(mRegisterCallback);
        }
        else {
            String device_token = UmengRegistrar.getRegistrationId(this);
            tongDao.identifyPushToken(device_token);
            Log.e("Push", device_token);
        }

        pm = this.getPackageManager();
        packageName = this.getPackageName();

        int phoneStatePermission = pm.checkPermission(Manifest.permission.READ_PHONE_STATE, packageName);

        if (phoneStatePermission != 0) {
            this.requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }

        onNewIntent(getIntent());
    }

    @Override
    public void onNewIntent(Intent intent) {

        Log.i("Umeng", "NotificationMessage - ");
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("NotificationMessage")) {
                // extract the extra-data in the Notification
                String msg = extras.getString("NotificationMessage");
                Log.i("Umeng", "NotificationMessage - " + msg);
                tongDao.trackOpenPushMessage(msg);
                tongDao.openPage(this, msg);

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.registerListeners();
        try {
            refreshReward();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tongDao.displayInAppMessage(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == -1 && grantResults.length == 0) {
//            finish();
            return;
        }

        for (int i : grantResults) {
            if (i == PackageManager.PERMISSION_GRANTED) {
                tongDao.trackEvent();
            }

            if( requestCode == 1 && pm.checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, packageName) != 0 ) {
                this.requestPermissions(new String[]{ Manifest.permission.ACCESS_COARSE_LOCATION }, 2);
            }

            if( requestCode == 2 && pm.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, packageName) != 0 ) {
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 3);
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        tongDao.onSessionEnd(this);
    }

    private void refreshReward() throws JSONException {
        if (!this.isFinishing()) {
            ArrayList<TransferRewardBean> tempRewards = DataTool.recoverTempRewards(this);
            ArrayList<TransferRewardBean> allOldRewards = DataTool.getAllRewardBeans();
//
            ArrayList<TransferRewardBean> newRewards = new ArrayList<TransferRewardBean>();
            for (TransferRewardBean eachNewTransferRewardBean : tempRewards) {
                boolean isExist = false;
                for (TransferRewardBean eachOldTransferRewardBean : allOldRewards) {
                    if (eachOldTransferRewardBean.getRewardSku().equals(eachNewTransferRewardBean.getRewardSku())) {
                        int newNum = eachOldTransferRewardBean.getNum() + eachNewTransferRewardBean.getNum();
                        eachOldTransferRewardBean.setNum(newNum);
                        eachOldTransferRewardBean.setRewardName(eachNewTransferRewardBean.getRewardName());
                        //update ui
                        updateRewardNum(eachNewTransferRewardBean.getRewardName(), eachNewTransferRewardBean.getRewardSku(), eachNewTransferRewardBean.getNum());
                        isExist = true;
                        break;
                    }
                }

                if (!isExist) {
                    //add new to ui
                    addRewardItem(eachNewTransferRewardBean);
                    newRewards.add(eachNewTransferRewardBean);
                }
            }

            allOldRewards.addAll(newRewards);
        }
    }

    private void registerListeners() {
        tongDao.registerOnRewardUnlockedListener(new OnRewardUnlockedListener() {
            @Override
            public void onSuccess(ArrayList<TdRewardBean> rewards) {
                if (rewards != null && rewards.size() > 0) {
                    try {
                        DataTool.saveTempRewards(MainActivity.this, rewards);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.setting_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.setting_bk_item) {
            startChangeBk();
        } else if (item.getItemId() == R.id.setting_jp_item) {
            startDefineReward();
        } else if (item.getItemId() == R.id.setting_add_item) {
            startDefineBtn();
        } else if(item.getItemId() == R.id.test_userid_item){
            Intent in = new Intent(this, LogInActivity.class);
            this.startActivity(in);

        }
        return true;
    }

    private void startChangeBk() {
        Intent bkIntent = new Intent(this, ChangeBkActivity.class);
        this.startActivityForResult(bkIntent, 998);
    }

    private void startDefineBtn() {
        Intent defineIntent = new Intent(this, BtnDefineActivity.class);
        this.startActivityForResult(defineIntent, 999);
    }

    private void startDefineReward() {
        Intent defineRewardIntent = new Intent(this, RewardDefineActivity.class);
        this.startActivityForResult(defineRewardIntent, 997);
    }

    @SuppressWarnings("deprecation")
    private void addBtn(TransferBean tempTransferBean, int index) {
        Button tempButton = new Button(this);
        tempButton.setTransformationMethod(null);
        tempButton.setText(tempTransferBean.getButtonName());
        tempButton.setTag(String.valueOf(index));
        tempButton.setTextSize(20);
        tempButton.setTextColor(Color.WHITE);
        tempButton.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.button_selector));
        LinearLayout.LayoutParams tempLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 160);
        tempLayoutParams.bottomMargin = 15;
        this.btnContainer.addView(tempButton, tempLayoutParams);

        tempButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = Integer.parseInt(v.getTag().toString());
                TransferBean sendTransferBean = DataTool.getAllBeans().get(index);
                if (sendTransferBean.getType() == Type.EVENT) {
                    if (sendTransferBean.getDatas().isEmpty()) {
                        tongDao.track(sendTransferBean.getEventName());
                    } else {
                        tongDao.track(sendTransferBean.getEventName(), sendTransferBean.getDatas());
                    }
                } else if (sendTransferBean.getType() == Type.ATTRIBUTE) {
                    if (!sendTransferBean.getDatas().isEmpty()) {
                        tongDao.identify(sendTransferBean.getDatas());
                    }
                }
            }
        });
    }

    private void loadAllButtons() {
        ArrayList<TransferBean> buttonDatas = DataTool.getAllBeans();
        int i = 0;
        for (TransferBean eachTransferBean : buttonDatas) {
            addBtn(eachTransferBean, i);
            i++;
        }
    }

    private void loadAllRewards() {
        ArrayList<TransferRewardBean> rewardsDatas = DataTool.getAllRewardBeans();
        for (TransferRewardBean eachTransferRewardBean : rewardsDatas) {
            addRewardItem(eachTransferRewardBean);
        }
    }

    private void updateRewardNum(String name, String sku, int num) {
        View tempView = this.rewardsContainer.findViewWithTag(sku);
        if (tempView != null) {
            TextView numTextView = (TextView) tempView.findViewById(R.id.reward_num_tv);
            TextView nameTextView = (TextView) tempView.findViewById(R.id.reward_item_name_tv);
            int currentNum = Integer.parseInt(numTextView.getText().toString()) + num;
            numTextView.setText(String.valueOf(currentNum));
            nameTextView.setText(name);
        }
    }

    @SuppressWarnings("deprecation")
    private void setBk(Uri bkUri) {
        ContentResolver resolver = getContentResolver();
        try {
            this.bkUri = bkUri;

            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inSampleSize = SAMPLE_SIZE;
            bm = BitmapFactory.decodeStream(resolver.openInputStream(this.bkUri), null, opts);//MediaStore.Images.Media.getBitmap(resolver, originalUri);
            this.mainCn.setBackgroundDrawable(new BitmapDrawable(bm));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 999 && resultCode == RESULT_OK) {
            ArrayList<TransferBean> datas = DataTool.getAllBeans();
            TransferBean tempTransferBean = datas.get(datas.size() - 1);
            addBtn(tempTransferBean, datas.size() - 1);
        } else if (requestCode == 998 && resultCode == RESULT_OK) {
            setBk(data.getData());
        } else if (requestCode == 997 && resultCode == RESULT_OK) {
            ArrayList<TransferRewardBean> datas = DataTool.getAllRewardBeans();
            TransferRewardBean tempTransferRewardBean = datas.get(datas.size() - 1);
            addRewardItem(tempTransferRewardBean);
        }
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("InflateParams")
    private void addRewardItem(TransferRewardBean tempTransferRewardBean) {
        View rewardItem = this.inflater.inflate(R.layout.reward_item, null);
        ImageView rewardIv = (ImageView) rewardItem.findViewById(R.id.reward_item_iv);
        TextView rewardNameTv = (TextView) rewardItem.findViewById(R.id.reward_item_name_tv);
        TextView rewardSkuTv = (TextView) rewardItem.findViewById(R.id.reward_item_sku_tv);
        TextView rewardNumTv = (TextView) rewardItem.findViewById(R.id.reward_num_tv);

        rewardItem.setTag(tempTransferRewardBean.getRewardSku());
        rewardNameTv.setText(tempTransferRewardBean.getRewardName());
        rewardSkuTv.setText(tempTransferRewardBean.getRewardSku());
        rewardNumTv.setText(String.valueOf(tempTransferRewardBean.getNum()));

        if (tempTransferRewardBean.getPicUri() != null && !tempTransferRewardBean.getPicUri().toString().equals("")) {
            ContentResolver resolver = getContentResolver();
            try {
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inSampleSize = SAMPLE_SIZE;
                Bitmap rewardBitmap = BitmapFactory.decodeStream(resolver.openInputStream(tempTransferRewardBean.getPicUri()), null, opts);//MediaStore.Images.Media.getBitmap(resolver, originalUri);
                rewardIv.setBackgroundDrawable(new BitmapDrawable(rewardBitmap));
                rewardBitmaps.add(rewardBitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        LinearLayout.LayoutParams tempLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        this.rewardsContainer.addView(rewardItem, tempLayoutParams);
    }

    private void saveBtnsData() {
        try {
            String btns = DataTool.makeBtnsString();
            String rewards = DataTool.makeRewardsString(DataTool.getAllRewardBeans());
            if (btns != null || rewards != null || this.bkUri != null) {
                if (btns != null) {
                    DataPreference.setBtnJsonString(MainActivity.this, btns);
                }

                if (rewards != null) {
                    DataPreference.setRewardJsonString(MainActivity.this, rewards);
                }

                if (this.bkUri != null) {
                    DataPreference.setBkString(MainActivity.this, this.bkUri.toString());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
        }
    }

    private void loadBtns() {
        String oldBtnJsonString = null;
        String oldRewardString = null;
        String bkUriString = null;

        oldBtnJsonString = DataPreference.getBtnJsonString(MainActivity.this);
        oldRewardString = DataPreference.getRewardJsonString(MainActivity.this);
        bkUriString = DataPreference.getBkString(MainActivity.this);

        try {
            DataTool.initialBtnDatas(oldBtnJsonString);
            DataTool.initialRewardDatas(oldRewardString);
            loadAllButtons();
            loadAllRewards();

            if (bkUriString != null && !bkUriString.equals("")) {
                setBk(Uri.parse(bkUriString));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        saveBtnsData();
        super.onDestroy();
        if (this.bm != null && !this.bm.isRecycled()) {
            this.bm.recycle();
            this.bm = null;
        }

        for (Bitmap eachBitmap : this.rewardBitmaps) {
            if (eachBitmap != null && !eachBitmap.isRecycled()) {
                eachBitmap.recycle();
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent linkIntent = null;
        switch (v.getId()) {
            case R.id.page1_tv:
                linkIntent = new Intent(this, DemoPage1.class);
                break;
            case R.id.page2_tv:
                linkIntent = new Intent(this, DemoPage2.class);
                break;
            case R.id.page3_tv:
                linkIntent = new Intent(this, DemoPage3.class);
                break;
            case R.id.page4_tv:
                linkIntent = new Intent(this, DemoPage4.class);
                break;
            case R.id.page5_tv:
                linkIntent = new Intent(this, DemoPage5.class);
                break;
            case R.id.page6_tv:
                Bundle args = new Bundle();
                args.putString("price", "100");
                args.putString("quality", "200");
                FragmentManager fm = getSupportFragmentManager();
                PaymentDialog paymentDialog = PaymentDialog.newInstance(getApplication());
                paymentDialog.setArguments(args);
                paymentDialog.show(fm, "frg_payment");
                return;
            case R.id.identify_tv:
                tongDao = TongDaoOO.getInstance(getApplication(),DataTool.APP_KEY);
                return;
            case R.id.track_tv:
                tongDao.track("trackTestingName");
                return;
        }

        this.startActivity(linkIntent);
    }

    public IUmengRegisterCallback mRegisterCallback = new IUmengRegisterCallback() {

        @Override
        public void onRegistered(final String registrationId) {
            Log.e("Push", registrationId);
            handler.post(new Runnable() {

                @Override
                public void run() {
                    tongDao.identifyPushToken(registrationId);
                }
            });
        }
    };

    public MessageHandler umengMessageHandler = new MessageHandler();

}
