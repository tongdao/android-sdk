package com.tongdao.sdk.tools;

import android.content.Context;
import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.MediumTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Locale;

import static org.junit.Assert.*;

/**
 * Created by agonch on 10/9/16.
 */
@RunWith(AndroidJUnit4.class)
@MediumTest
public class TongDaoAppInfoToolTest {
    Context mContext;

    @Before
    public void setUp(){
        mContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertNotNull(mContext);
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

    }

    @Test
    public void getImeiInfos() throws Exception {

    }

    @Test
    public void getMacInfos() throws Exception {

    }

    @Test
    public void getUdidInfos() throws Exception {

    }

    @Test
    public void getGaid() throws Exception {

    }

}