package com.tongdao.demo;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.tongdao.sdk.TongDaoOO;

import java.io.FileNotFoundException;

public class RewardDefineActivity extends ActionBarActivity {
    private static final String IMAGE_TYPE = "image/*";
    private static final int IMAGE_CODE = 0;
    private Uri originalUri;
    private ImageView bkIv;
    private Bitmap bm = null;
    private EditText rewardNameEt;
    private EditText rewardSkuEt;
    private static final int SAMPLE_SIZE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().setIcon(R.drawable.ic_launcher);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.getSupportActionBar().setDisplayShowTitleEnabled(true);
        this.getSupportActionBar().setTitle(" Setting prizes");
        this.getSupportActionBar().setBackgroundDrawable(this.getResources().getDrawable(R.drawable.bar));

        this.setContentView(R.layout.define_reward);

        this.bkIv = (ImageView) this.findViewById(R.id.reward_iv);
        this.rewardNameEt = (EditText) this.findViewById(R.id.reward_name_et);
        this.rewardSkuEt = (EditText) this.findViewById(R.id.reward_sku_et);

        this.findViewById(R.id.take_reward_pic).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
                getAlbum.setType(IMAGE_TYPE);
                startActivityForResult(getAlbum, IMAGE_CODE);
            }
        });

        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, DataTool.BAIDU_API_KEY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == IMAGE_CODE) {
            originalUri = data.getData();
            try {
                ContentResolver resolver = getContentResolver();
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inSampleSize = SAMPLE_SIZE;
                bm = BitmapFactory.decodeStream(resolver.openInputStream(originalUri), null, opts);//MediaStore.Images.Media.getBitmap(resolver, originalUri);
                this.bkIv.setImageBitmap(bm);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.btn_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save_btn_item) {
            saveData();
        }
        return true;
    }

    private void saveData() {
        if (this.originalUri == null) {
            Toast.makeText(getApplicationContext(), "Please set reward pic!", Toast.LENGTH_SHORT).show();
            return;
        }

        String rewardName = this.rewardNameEt.getText().toString();
        if (rewardName.trim().equals("")) {
            Toast.makeText(getApplicationContext(), "Please set reward name!", Toast.LENGTH_SHORT).show();
            return;
        }

        String rewardSku = this.rewardSkuEt.getText().toString();
        if (rewardSku.trim().equals("")) {
            Toast.makeText(getApplicationContext(), "Please set reward sku!", Toast.LENGTH_SHORT).show();
            return;
        }

        DataTool.addNewRewardBean(new TransferRewardBean(originalUri, rewardName, rewardSku, 0));
        this.setResult(RESULT_OK);
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.bm != null && !this.bm.isRecycled()) {
            this.bm.recycle();
            this.bm = null;
        }
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
}
