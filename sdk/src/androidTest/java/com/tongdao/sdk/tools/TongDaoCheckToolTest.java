package com.tongdao.sdk.tools;

import android.support.test.filters.FlakyTest;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import com.tongdao.sdk.enums.TdAppStore;

import junit.extensions.RepeatedTest;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

import static org.hamcrest.core.IsEqual.*;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

/**
 * Created by agonch on 9/28/16.
 */

@RunWith(AndroidJUnit4.class)
@SmallTest
public class TongDaoCheckToolTest {

    @Test
    public void testIsEmpty(){
        assertThat("Null not supported",TongDaoCheckTool.isEmpty(null),is(true));
        assertThat("Empty not supported",TongDaoCheckTool.isEmpty(""),is(true));
        assertThat("Spaced strings not supported",TongDaoCheckTool.isEmpty(" "),is(true));
        assertThat("Single character text not supported",TongDaoCheckTool.isEmpty("."),is(true));
        assertThat("Text null not supported",TongDaoCheckTool.isEmpty("null"),is(true));
        assertThat("Not empty text should pass",TongDaoCheckTool.isEmpty("sample"),is(false));
    }

    @Test
    public void testGetTimestampLong(){
        long timestamp = 1475047297264L;
        String result = "2016-09-28T07:21:37.264Z";
        assertThat("Timestamps don't match",TongDaoCheckTool.getTimeStamp(timestamp), equalTo(result));
    }

    @Test
    public void testGetTimestampDate(){
        Date timestamp = new Date(1475047297264L);
        String result = "2016-09-28T07:21:37.264Z";
        assertThat("Timestamps don't match",TongDaoCheckTool.getTimeStamp(timestamp), equalTo(result));
    }

    @Test
    public void testGetNonce(){
        String nonce = TongDaoCheckTool.generateNonce();
        assertThat("Nonce should be of length 28",nonce.length(),equalTo(28));
    }

    @Test
    public void testIsValidKeyEmpty(){
        String emptyKey = "";
        boolean isValid = TongDaoCheckTool.isValidKey(emptyKey);
        assertThat("Key shouldn't be valid",isValid,is(false));
    }

    @Test
    public void testIsValidKeyShort(){
        String shortKey = "1234567890";
        boolean isValid = TongDaoCheckTool.isValidKey(shortKey);
        assertThat("Key shouldn't be valid",isValid,is(false));
    }

    @Test
    public void testIsValidKeyFull(){
        String nonEmptyKey = "12345678901234567890123456789012";
        boolean isValid = TongDaoCheckTool.isValidKey(nonEmptyKey);
        assertThat("Key should be valid",isValid,is(true));
    }

    @Test
    public void testCheckKeysEmpty(){
        String deviceId = null;
        String packageName = null;
        String appKey = null;
        boolean isValid = TongDaoCheckTool.checkKeys(deviceId,packageName,appKey);
        assertThat("Should not be valid",isValid,is(false));

        deviceId = "testid";
        packageName = null;
        appKey = null;
        isValid = TongDaoCheckTool.checkKeys(deviceId,packageName,appKey);
        assertThat("Should not be valid",isValid,is(false));
    }

    @Test
    public void testCheckKeysWrongKey(){
        String deviceId = "123123r";
        String packageName = "com.package.name";
        String appKey = "1234567890";
        boolean isValid = TongDaoCheckTool.checkKeys(deviceId,packageName,appKey);
        assertThat("Should not be valid",isValid,is(false));
    }

    @Test
    public void testCheckKeys(){
        String deviceId = "123123r";
        String packageName = "com.package.name";
        String appKey = "12345678901234567890123456789012";
        boolean isValid = TongDaoCheckTool.checkKeys(deviceId,packageName,appKey);
        assertThat("Should be valid",isValid,is(true));
    }

    @Test
    public void testGetAppStoreValue(){
        assertThat("Value is wrong",TongDaoCheckTool.getAppStoreValue(TdAppStore.APP_STORE_BAIDU),equalTo(1));
        assertThat("Value is wrong",TongDaoCheckTool.getAppStoreValue(TdAppStore.APP_STORE_TENCENT),equalTo(2));
        assertThat("Value is wrong",TongDaoCheckTool.getAppStoreValue(TdAppStore.APP_STORE_360),equalTo(3));
        assertThat("Value is wrong",TongDaoCheckTool.getAppStoreValue(TdAppStore.APP_STORE_WANDOUJIA),equalTo(4));
        assertThat("Value is wrong",TongDaoCheckTool.getAppStoreValue(TdAppStore.APP_STORE_91),equalTo(5));
        assertThat("Value is wrong",TongDaoCheckTool.getAppStoreValue(TdAppStore.APP_STORE_HIMARKET),equalTo(6));
        assertThat("Value is wrong",TongDaoCheckTool.getAppStoreValue(TdAppStore.APP_STORE_TAOBAO),equalTo(7));
        assertThat("Value is wrong",TongDaoCheckTool.getAppStoreValue(TdAppStore.APP_STORE_XIAOMI),equalTo(8));
        assertThat("Value is wrong",TongDaoCheckTool.getAppStoreValue(TdAppStore.APP_STORE_DCN),equalTo(9));
        assertThat("Value is wrong",TongDaoCheckTool.getAppStoreValue(TdAppStore.APP_STORE_APPCHINA),equalTo(10));
        assertThat("Value is wrong",TongDaoCheckTool.getAppStoreValue(TdAppStore.APP_STORE_CUSTOM1),equalTo(1001));
        assertThat("Value is wrong",TongDaoCheckTool.getAppStoreValue(TdAppStore.APP_STORE_CUSTOM2),equalTo(1002));
        assertThat("Value is wrong",TongDaoCheckTool.getAppStoreValue(TdAppStore.APP_STORE_CUSTOM3),equalTo(1003));
        assertThat("Value is wrong",TongDaoCheckTool.getAppStoreValue(TdAppStore.APP_STORE_CUSTOM4),equalTo(1004));
        assertThat("Value is wrong",TongDaoCheckTool.getAppStoreValue(TdAppStore.APP_STORE_CUSTOM5),equalTo(1005));
        assertThat("Value is wrong",TongDaoCheckTool.getAppStoreValue(TdAppStore.APP_STORE_CUSTOM6),equalTo(1006));
        assertThat("Value is wrong",TongDaoCheckTool.getAppStoreValue(TdAppStore.APP_STORE_CUSTOM7),equalTo(1007));
        assertThat("Value is wrong",TongDaoCheckTool.getAppStoreValue(TdAppStore.APP_STORE_CUSTOM8),equalTo(1008));
        assertThat("Value is wrong",TongDaoCheckTool.getAppStoreValue(TdAppStore.APP_STORE_CUSTOM9),equalTo(1009));
    }
}
