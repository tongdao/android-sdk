package com.tongdao.sdk.test;

import android.app.Activity;
import android.content.Context;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.tongdao.sdk.tools.TongDaoAppInfoTool;
import com.tongdao.sdk.tools.TongDaoDataTool;

import org.json.JSONObject;

/**
 * Created by kinjal.patel on 13/05/16.
 */
public class ApplicationTest extends InstrumentationTestCase {

    private final String APP_KEY = "de89454e930e2257ddd96d6b4d0f48b5";
    Context appContext = null;
    Activity activityContext;

    public void testIdentify() throws Exception {
        appContext = getInstrumentation().getContext();

        assertNotNull(appContext);
        String gaid = TongDaoAppInfoTool.getGaid(appContext);
        try {
            JSONObject properties = TongDaoDataTool.makeInfoProperties(appContext, gaid);
            Log.e("testIdentify", properties.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

//    public void testIdentifyWhenApplicationDataChanged() {
//        String userId = generateUserId();
//
//        assertNotNull(userId);
//
//        TongDaoSavingTool.setApplicationInfoData(activityContext, "DummyApplicationData");
//
//        TongDao.init(activityContext, APP_KEY);
//        TongDao.setUserId(activityContext, userId);
//    }
//
//    public void testIdentifyWhenConnectionDataChanged() {
//        String userId = generateUserId();
//
//        assertNotNull(userId);
//
//        TongDaoSavingTool.setConnectionInfoData(activityContext, "DummyConnectionData");
//
//        TongDao.init(activityContext, APP_KEY);
//        TongDao.setUserId(activityContext, userId);
//    }
//
//    public void testIdentifyWhenLocationDataChanged() {
//        String userId = generateUserId();
//
//        assertNotNull(userId);
//
//        TongDaoSavingTool.setLocationInfoData(activityContext, "DummyLocationData");
//
//        TongDao.init(activityContext, APP_KEY);
//        TongDao.setUserId(activityContext, userId);
//    }
//
//    public void testIdentifyWhenFingerPrintDataChanged() {
//        String userId = generateUserId();
//
//        assertNotNull(userId);
//
//        TongDaoSavingTool.setFingerprintInfoData(activityContext, "DummyFingerPrintData");
//
//        TongDao.init(activityContext, APP_KEY);
//        TongDao.setUserId(activityContext, userId);
//    }
//
//    public void testIdentifyWhenDeviceDataChanged() {
//        String userId = generateUserId();
//
//        assertNotNull(userId);
//
//        TongDaoSavingTool.setDeviceInfoData(activityContext, "DummyDeviceData");
//
//        TongDao.init(activityContext, APP_KEY);
//        TongDao.setUserId(activityContext, userId);
//    }
//
//    private String generateUserId() {
//        return String.valueOf(new Random().nextInt(10));
//    }

}
