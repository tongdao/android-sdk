package com.tongdao.sdk.tools;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.MediumTest;
import android.support.test.runner.AndroidJUnit4;
import android.telephony.TelephonyManager;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Locale;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by agonch on 10/9/16.
 */
@RunWith(AndroidJUnit4.class)
@MediumTest
public class TongDaoAppInfoToolTest {
    Context mContext;
    TongDaoAppInfoTool appInfoTool;

    @Before
    public void setUp(){
        mContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertNotNull(mContext);
        appInfoTool = new TongDaoAppInfoTool();
        assertNotNull(appInfoTool);
    }
    @Test
    public void getDeviceInfo() throws Exception {
        TongDaoAppInfoTool appInfoTool = new TongDaoAppInfoTool();
        Object[] expected = {Build.MODEL, Build.MANUFACTURER, Build.PRODUCT, Build.DISPLAY, Build.DEVICE, "android", Build.VERSION.RELEASE, Locale.getDefault().toString()};
        Object[] result = appInfoTool.getDeviceInfo();
        assertArrayEquals(result,expected);
    }

    @Test
    public void getVersionCodeOsName() throws Exception {
        TongDaoAppInfoTool appInfoTool = new TongDaoAppInfoTool();
        Object[] result = appInfoTool.getVersionCodeOsName(mContext);
        assertNotNull(result);
    }

    @Test
    public void getNetworkInfo() throws Exception {
        TongDaoAppInfoTool appInfoTool = new TongDaoAppInfoTool();
        Object[] result = appInfoTool.getNetworkInfo(mContext);
        assertNotNull(result);
    }

    @Test
    public void getCurrentLocation() throws Exception {
        Context mockContext = mock(Context.class);
        PackageManager mockPackageManager = mock(PackageManager.class);
        String mockPackageName = "com.tongdao.test";
        LocationManager mockLocationManager = mock(LocationManager.class);
        Location mockLocation = mock(Location.class);
        double longitude = 25.0;
        double latitude = 50.0;
        when(mockContext.getPackageManager()).thenReturn(mockPackageManager);
        when(mockContext.getPackageName()).thenReturn(mockPackageName);
        when(mockPackageManager.checkPermission(anyString(),anyString())).thenReturn(0);
        when(mockContext.getSystemService(anyString())).thenReturn(mockLocationManager);
        when(mockLocationManager.getLastKnownLocation(anyString())).thenReturn(mockLocation);
        when(mockLocation.getLatitude()).thenReturn(latitude);
        when(mockLocation.getLongitude()).thenReturn(longitude);
        Object[] result = appInfoTool.getCurrentLocation(mockContext);
        Object[] expectedResult = {50.0d,25.0d,"fine"};
        assertThat("objects not equal",result,equalTo(expectedResult));
    }

    @Test
    public void getImeiInfos() throws Exception {
        String testPackageName = "com.tongdao.test";
        String testImei = "353490061927915";
        String expectedResult = "{\"!imei\":\"353490061927915\",\"!imei_md5\":\"7696de438d2de019506ff7346c57d010\",\"!imei_sha1\":\"4e3b63326bcfe15318c8118e70c7e664885462b4\"}";
        JSONObject expectedResultJson = new JSONObject(expectedResult);
        Context mockContext = mock(Context.class);
        TelephonyManager mockTelephonyManager = mock(TelephonyManager.class);
        PackageManager mockPackageManager = mock(PackageManager.class);
        when(mockContext.getPackageManager()).thenReturn(mockPackageManager);
        when(mockContext.getPackageName()).thenReturn(testPackageName);
        when(mockPackageManager.checkPermission(Manifest.permission.READ_PHONE_STATE,testPackageName)).thenReturn(0);
        when(mockContext.getSystemService(Context.TELEPHONY_SERVICE)).thenReturn(mockTelephonyManager);
        when(mockTelephonyManager.getDeviceId()).thenReturn(testImei);
        JSONObject emptyObject = new JSONObject();
        appInfoTool.getImeiInfos(mockContext,emptyObject);
        assertThat("Objects not equal. Result: " + emptyObject.toString(),expectedResultJson.toString(),equalTo(emptyObject.toString()));
    }

    @Test
    public void getMacInfos() throws Exception {
        assertThat("Should be of length 3", appInfoTool.getMacInfos().length,equalTo(3));
    }

    @Test
    public void getUdidInfos() throws Exception {
        assertThat("Should be of length 3", appInfoTool.getUdidInfos(mContext).length,equalTo(3));
    }

    @Test
    public void getGaid() throws Exception {
        //not sure how to test this method
    }

}