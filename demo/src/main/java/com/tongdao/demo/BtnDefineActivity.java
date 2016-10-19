package com.tongdao.demo;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.tongdao.newsdk.TongDao;

public class BtnDefineActivity extends ActionBarActivity {
    private RadioButton eventRadio;
    private EditText btnNameEt;
    private EditText eventNameEt;
    private LinearLayout valueContainer;
    private LayoutInflater inflater;
    private ArrayList<View> valuesViews = new ArrayList<View>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().setIcon(R.drawable.ic_launcher);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);
        this.getSupportActionBar().setBackgroundDrawable(this.getResources().getDrawable(R.drawable.bar));
        this.setContentView(R.layout.edit_btn);
        this.inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.valueContainer = (LinearLayout) this.findViewById(R.id.value_container);
        this.eventRadio = (RadioButton) this.findViewById(R.id.event_radio);
        this.btnNameEt = (EditText) this.findViewById(R.id.btn_name_et);
        this.eventNameEt = (EditText) this.findViewById(R.id.btn_event_name_et);

        ((RadioGroup) this.findViewById(R.id.type_radio_gp)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (eventRadio.isChecked()) {
                    eventNameEt.setVisibility(View.VISIBLE);
                } else {
                    eventNameEt.setVisibility(View.GONE);
                }
            }
        });

        this.findViewById(R.id.add_value_linear).setOnClickListener(new OnClickListener() {
            @SuppressLint("InflateParams")
            @Override
            public void onClick(View v) {
                final View tempView = inflater.inflate(R.layout.define_item_container, null);
                LinearLayout.LayoutParams tempPara = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                tempPara.bottomMargin = 20;
                valueContainer.addView(tempView, tempPara);
                valuesViews.add(tempView);
                tempView.findViewById(R.id.deleteItemIv).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        valueContainer.removeView(tempView);
                        valuesViews.remove(tempView);
                    }
                });
            }
        });

        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, DataTool.BAIDU_API_KEY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.btn_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save_btn_item) {
            saveDatas();
        }
        return true;
    }

    private void saveDatas() {
        String btnName = this.btnNameEt.getText().toString();
        String eventName = this.eventNameEt.getText().toString();
        Type type = this.eventRadio.isChecked() ? Type.EVENT : Type.ATTRIBUTE;

        if (btnName.trim().equals("")) {
            Toast.makeText(getApplicationContext(), "Button name is empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (type == Type.EVENT) {
            if (eventName.trim().equals("")) {
                Toast.makeText(getApplicationContext(), "Event name is empty!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (type == Type.ATTRIBUTE) {
            if (valuesViews.size() == 0) {
                Toast.makeText(getApplicationContext(), "At least define one attribute!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        HashMap<String, Object> datas = new HashMap<String, Object>();
        for (View eachView : valuesViews) {
            String key = ((EditText) eachView.findViewById(R.id.key)).getText().toString();
            String value = ((EditText) eachView.findViewById(R.id.value)).getText().toString();
            if (key.trim().equals("") || value.trim().equals("")) {
                Toast.makeText(getApplicationContext(), "Please check data's key and value!", Toast.LENGTH_SHORT).show();
                return;
            } else {
                if (DataTool.isNumeric(value)) {
                    datas.put(key, ((int) Double.parseDouble(value)));
                } else {
                    datas.put(key, value);
                }
            }
        }
        //make bean
        DataTool.addNewBean(new TransferBean(type, btnName, eventName, datas));
        this.setResult(RESULT_OK);
        this.finish();
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
