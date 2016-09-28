package com.tongdao.sdk;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.tongdao.sdk.tools.TongDaoAppInfoTool;
import com.tongdao.sdk.tools.TongDaoDataTool;
import com.tongdao.sdk.tools.TongDaoSavingTool;

import org.json.JSONObject;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Collections;
import java.util.Random;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
@Ignore
public class ApplicationTest extends ApplicationTestCase<Application> {

    private final String APP_KEY = "de89454e930e2257ddd96d6b4d0f48b5";
    Context appContext = null;

    Activity activityContext;
    PackageManager pm;
    String packageName;

    int accessCoarseLocation;
    int accessFineLocation;
    int telephonyPermission;

    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        appContext = this.getContext();

        assertNotNull(appContext);

        pm = appContext.getPackageManager();
        packageName = appContext.getPackageName();

        accessCoarseLocation = pm.checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, packageName);
        accessFineLocation = pm.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, packageName);

        assertEquals("Location permission is required", true, accessCoarseLocation == 0 || accessFineLocation == 0);

        telephonyPermission = pm.checkPermission(Manifest.permission.READ_PHONE_STATE, packageName);
        assertEquals("Telephony permission is required", true, telephonyPermission == 0);

    }

    @Test
    public void testIdentify() throws Exception {
        assertNotNull(appContext);

        assertEquals("Location permission is required", true, accessCoarseLocation == 0 || accessFineLocation == 0);
        assertEquals("Telephony permission is required", true, telephonyPermission == 0);

        TongDao.init(appContext, APP_KEY);
    }

    @Test
    public void testIdentifyWhenApplicationDataChanged() {
        String userId = generateUserId();

        assertNotNull(userId);

        assertEquals("Location permission is required", true, accessCoarseLocation == 0 || accessFineLocation == 0);
        assertEquals("Telephony permission is required", true, telephonyPermission == 0);

        TongDaoSavingTool.setApplicationInfoData(appContext, "DummyApplicationData");
        TongDao.init(appContext, APP_KEY);
    }

    public void testIdentifyWhenConnectionDataChanged() {
        String userId = generateUserId();

        assertNotNull(userId);

        assertEquals("Location permission is required", true, accessCoarseLocation == 0 || accessFineLocation == 0);
        assertEquals("Telephony permission is required", true, telephonyPermission == 0);

        TongDaoSavingTool.setConnectionInfoData(appContext, "DummyConnectionData");
        TongDao.init(appContext, APP_KEY);
    }

    public void testIdentifyWhenLocationDataChanged() {
        String userId = generateUserId();

        assertNotNull(userId);

        assertEquals("Location permission is required", true, accessCoarseLocation == 0 || accessFineLocation == 0);
        assertEquals("Telephony permission is required", true, telephonyPermission == 0);

        TongDaoSavingTool.setLocationInfoData(appContext, "DummyLocationData");

        TongDao.init(appContext, APP_KEY);
    }

    public void testIdentifyWhenFingerPrintDataChanged() {
        String userId = generateUserId();

        assertNotNull(userId);

        assertEquals("Location permission is required", true, accessCoarseLocation == 0 || accessFineLocation == 0);
        assertEquals("Telephony permission is required", true, telephonyPermission == 0);

        TongDaoSavingTool.setFingerprintInfoData(appContext, "DummyFingerPrintData");

        TongDao.init(appContext, APP_KEY);
    }

    public void testIdentifyWhenDeviceDataChanged() {
        String userId = generateUserId();

        assertNotNull(userId);

        assertEquals("Location permission is required", true, accessCoarseLocation == 0 || accessFineLocation == 0);
        assertEquals("Telephony permission is required", true, telephonyPermission == 0);

        TongDaoSavingTool.setDeviceInfoData(appContext, "DummyDeviceData");

        TongDao.init(appContext, APP_KEY);
    }

    private String generateUserId() {
        return String.valueOf(new Random().nextInt(10));
    }

}