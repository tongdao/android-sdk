package com.tongdao.demo;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.umeng.message.UmengBaseIntentService;
import com.umeng.message.entity.UMessage;

import org.android.agoo.client.BaseConstants;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kinjal.patel on 17/05/16.
 */
public class MyPushIntentService extends UmengBaseIntentService {
    private static final String TAG = MyPushIntentService.class.getName();

    @Override
    protected void onMessage(Context context, Intent intent) {
        super.onMessage(context, intent);

        try {
            String message = intent.getStringExtra(BaseConstants.MESSAGE_BODY);
            UMessage msg = new UMessage(new JSONObject(message));
            Log.d(TAG, "message=" + message);    //消息体
            Log.d(TAG, "custom=" + msg.custom);    //自定义消息的内容
            Log.d(TAG, "title=" + msg.title);    //通知标题
            Log.d(TAG, "text=" + msg.text);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
