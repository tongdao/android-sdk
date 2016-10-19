package com.tongdao.demo;

import java.io.FileNotFoundException;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.tongdao.newsdk.TongDao;
import com.tongdao.demo.R;

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
import android.widget.ImageView;
import android.widget.Toast;

public class ChangeBkActivity extends ActionBarActivity {

    private static final String IMAGE_TYPE = "image/*";
    private static final int IMAGE_CODE = 0;
    private Uri originalUri;
    private ImageView bkIv;
    private Bitmap bm = null;
    private static final int SAMPLE_SIZE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().setIcon(R.drawable.ic_launcher);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.getSupportActionBar().setDisplayShowTitleEnabled(true);
        this.getSupportActionBar().setTitle("Home");
        this.getSupportActionBar().setBackgroundDrawable(
                this.getResources().getDrawable(R.drawable.bar));
        this.setContentView(R.layout.change_bk);

        this.bkIv = (ImageView) this.findViewById(R.id.bk_iv);
        this.findViewById(R.id.take_pic).setOnClickListener(
                new OnClickListener() {
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
            if (originalUri == null) {
                Toast.makeText(getApplicationContext(), "Please take the pics!", Toast.LENGTH_SHORT).show();
            } else {
                Intent uriIntent = new Intent();
                uriIntent.setData(originalUri);
                this.setResult(RESULT_OK, uriIntent);
                this.finish();
            }
        }
        return true;
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
        TongDao.displayInAppMessage(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
