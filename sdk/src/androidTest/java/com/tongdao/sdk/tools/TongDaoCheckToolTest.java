package com.tongdao.sdk.tools;

import android.support.test.filters.FlakyTest;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

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
        System.out.println("Nonce: " + nonce);
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
}
