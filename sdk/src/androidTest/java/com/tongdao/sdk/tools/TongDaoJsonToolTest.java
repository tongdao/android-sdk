package com.tongdao.sdk.tools;

import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.hamcrest.core.IsNull.*;
import static org.hamcrest.core.IsEqual.*;
/**
 * Created by agonch on 9/29/16.
 */

@RunWith(AndroidJUnit4.class)
@SmallTest
public class TongDaoJsonToolTest {

    @Test
    public void testOptJsonStringNull(){
        JSONObject nullObject = null;
        String nullKey = null;
        String result = TongDaoJsonTool.optJsonString(nullObject,nullKey);
        assertThat("result should be null",result, nullValue());
    }

    @Test
    public void testOptJsonStringNullKey(){
        JSONObject object = new JSONObject();
        String nullKey = null;
        String result = TongDaoJsonTool.optJsonString(object,nullKey);
        assertThat("result should be null",result, nullValue());
    }

    @Test
    public void testOptJsonString() throws JSONException {
        JSONObject object = new JSONObject("{\"testkey\":\"testvalue\"}");
        String key = "testkey";
        String expectedValue = "testvalue";
        String result = TongDaoJsonTool.optJsonString(object,key);
        assertThat("result is wrong",result, equalTo(expectedValue));
    }
}
