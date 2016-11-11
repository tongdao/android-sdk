package com.tongdao.sdk.tools;

import android.app.Application;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.MediumTest;
import android.support.test.runner.AndroidJUnit4;

import com.tongdao.sdk.beans.TdOrder;
import com.tongdao.sdk.beans.TdOrderLine;
import com.tongdao.sdk.beans.TdProduct;
import com.tongdao.sdk.beans.TdSource;
import com.tongdao.sdk.enums.TdAppStore;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;

import static org.junit.Assert.*;
import static org.hamcrest.core.IsEqual.*;
import static org.mockito.Mockito.*;

/**
 * Created by agonch on 10/8/16.
 */

@RunWith(AndroidJUnit4.class)
@MediumTest
public class TongDaoDataToolTest{

    TongDaoDataTool dataTool;
    Context mContext;

    @Before
    public void setUp() throws Exception {
        mContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertNotNull(mContext);
        dataTool = new TongDaoDataTool();
        assertNotNull(dataTool);
    }

    @Test
    public void makeInfoProperties() throws Exception {
        //TODO: find a way to test this
        String testGaid = "test-gaid";
        JSONObject result = dataTool.makeInfoProperties(mContext,testGaid);
    }

    @Test
    public void makeRatingProperties() throws Exception {
        int rating = 3;
        JSONObject expected = new JSONObject("{\"!application\":{\"!rating\":3}}");

        JSONObject result = dataTool.makeRatingProperties(rating);

        assertThat("Objects not equal", result.toString(), equalTo(expected.toString()));
    }

    @Test
    public void makeUserPropertiesNull() throws Exception {
        HashMap<String, Object> properties = null;

        JSONObject result = dataTool.makeUserProperties(properties);

        assertNull(result);

        properties = new HashMap<>();

        result = dataTool.makeUserProperties(properties);

        assertNull(result);
    }

    @Test
    public void makeUserPropertiesNotNull() throws Exception {
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("testkey", null);
        properties.put("testkey2", "");
        properties.put("testkey3", new JSONArray("[\"1\",\"2\"]"));
        JSONObject expected = new JSONObject("{\"testkey3\":[\"1\",\"2\"],\"testkey2\":\"\"}");

        JSONObject result = dataTool.makeUserProperties(properties);

        assertThat("Objects not equal", result.toString(), equalTo(expected.toString()));
    }

    @Test
    public void makeSourcePropertiesNull() throws Exception {
        assertNull(dataTool.makeSourceProperties(null));

        TdSource tdSource = new TdSource(null, null, null, null, null);
        assertNull(dataTool.makeSourceProperties(tdSource));

        tdSource = new TdSource(TdAppStore.APP_STORE_91, null, "testgroupid", "testcampaignid", "testsourceid");
        JSONObject result = dataTool.makeSourceProperties(tdSource);
        JSONObject expected = new JSONObject("{\"!source\":{\"!appstore_id\":\"APP_STORE_91\",\"!adgroup_id\":\"testgroupid\",\"!campaign_id\":\"testcampaignid\",\"!source_id\":\"testsourceid\"}}");
        assertThat("Objects not equal", result.toString(), equalTo(expected.toString()));

        tdSource = new TdSource(null, "testadvertisementid", "testgroupid", "testcampaignid", "testsourceid");
        result = dataTool.makeSourceProperties(tdSource);
        expected = new JSONObject("{\"!source\":{\"!ad_id\":\"testadvertisementid\",\"!adgroup_id\":\"testgroupid\",\"!campaign_id\":\"testcampaignid\",\"!source_id\":\"testsourceid\"}}");
        assertThat("Objects not equal", result.toString(), equalTo(expected.toString()));

        tdSource = new TdSource(TdAppStore.APP_STORE_91, "testadvertisementid", null, "testcampaignid", "testsourceid");
        result = dataTool.makeSourceProperties(tdSource);
        expected = new JSONObject("{\"!source\":{\"!appstore_id\":\"APP_STORE_91\",\"!ad_id\":\"testadvertisementid\",\"!campaign_id\":\"testcampaignid\",\"!source_id\":\"testsourceid\"}}");
        assertThat("Objects not equal", result.toString(), equalTo(expected.toString()));

        tdSource = new TdSource(TdAppStore.APP_STORE_91, "testadvertisementid", "testgroupid", null, "testsourceid");
        result = dataTool.makeSourceProperties(tdSource);
        expected = new JSONObject("{\"!source\":{\"!appstore_id\":\"APP_STORE_91\",\"!ad_id\":\"testadvertisementid\",\"!adgroup_id\":\"testgroupid\",\"!source_id\":\"testsourceid\"}}");
        assertThat("Objects not equal", result.toString(), equalTo(expected.toString()));

        tdSource = new TdSource(TdAppStore.APP_STORE_91, "testadvertisementid", null, "testcampaignid", null);
        result = dataTool.makeSourceProperties(tdSource);
        expected = new JSONObject("{\"!source\":{\"!appstore_id\":\"APP_STORE_91\",\"!ad_id\":\"testadvertisementid\",\"!campaign_id\":\"testcampaignid\"}}");
        assertThat("Objects not equal", result.toString(), equalTo(expected.toString()));
    }

    @Test
    public void makeSourceProperties() throws Exception {
        TdSource tdSource = new TdSource(TdAppStore.APP_STORE_91, "advertisementtestid", "advertisementgrouptestid", "campaigntestid", "sourcetestid");
        JSONObject expected = new JSONObject("{\"!source\":{\"!appstore_id\":\"APP_STORE_91\",\"!ad_id\":\"advertisementtestid\",\"!adgroup_id\":\"advertisementgrouptestid\",\"!campaign_id\":\"campaigntestid\",\"!source_id\":\"sourcetestid\"}}");

        JSONObject result = dataTool.makeSourceProperties(tdSource);

        assertThat("Objects not equal", result.toString(), equalTo(expected.toString()));
    }

    @Test
    public void makeRegisterProperties() throws Exception {
        when(dataTool.getSystemTime()).thenReturn(1475047297264L);

        JSONObject result = dataTool.makeRegisterProperties(null);
        JSONObject expected = new JSONObject("{\"!register_at\":\"2016-09-28T07:21:37.264Z\"}");
        assertThat("Objects not equal", result.toString(), equalTo(expected.toString()));

        result = dataTool.makeRegisterProperties(new Date(1475047297264L));
        expected = new JSONObject("{\"!register_at\":\"2016-09-28T07:21:37.264Z\"}");
        assertThat("Objects not equal", result.toString(), equalTo(expected.toString()));
    }

    @Test
    public void makeProductPropertiesNull() throws Exception {
        assertNull(dataTool.makeProductProperties(null));

        TdProduct product = new TdProduct();
        assertNull(dataTool.makeProductProperties(product));
    }

    @Test
    public void makeProductPropertiesNotNull() throws Exception {
        TdProduct product = new TdProduct(null, "testsku", "testname", 1f, Currency.getInstance("USD"), "testcategory");
        JSONObject expected = new JSONObject("{\"!sku\":\"testsku\",\"!name\":\"testname\",\"!price\":1,\"!currency\":\"USD\",\"!category\":\"testcategory\"}");
        JSONObject result = dataTool.makeProductProperties(product);
        assertThat("Objects not equal", result.toString(), equalTo(expected.toString()));

        product = new TdProduct("testid", null, "testname", 1f, Currency.getInstance("USD"), "testcategory");
        expected = new JSONObject("{\"!id\":\"testid\",\"!name\":\"testname\",\"!price\":1,\"!currency\":\"USD\",\"!category\":\"testcategory\"}");
        result = dataTool.makeProductProperties(product);
        assertThat("Objects not equal", result.toString(), equalTo(expected.toString()));

        product = new TdProduct("testid", "testsku", null, 1f, Currency.getInstance("USD"), "testcategory");
        result = dataTool.makeProductProperties(product);
        assertNull(result);

        product = new TdProduct("testid", "testsku", "testname", 1f, null, "testcategory");
        result = dataTool.makeProductProperties(product);
        assertNull(result);

        product = new TdProduct("testid", "testsku", "testname", 1f, Currency.getInstance("USD"), null);
        expected = new JSONObject("{\"!id\":\"testid\",\"!sku\":\"testsku\",\"!name\":\"testname\",\"!price\":1,\"!currency\":\"USD\"}");
        result = dataTool.makeProductProperties(product);
        assertThat("Objects not equal", result.toString(), equalTo(expected.toString()));

        product = new TdProduct("testid", "testsku", "testname", 1f, Currency.getInstance("USD"), "testcategory");
        expected = new JSONObject("{\"!id\":\"testid\",\"!sku\":\"testsku\",\"!name\":\"testname\",\"!price\":1,\"!currency\":\"USD\",\"!category\":\"testcategory\"}");
        result = dataTool.makeProductProperties(product);
        assertThat("Objects not equal", result.toString(), equalTo(expected.toString()));
    }

    @Test
    public void makeOrderLinesArrayPropertiesNull() throws Exception {
        assertNull(dataTool.makeOrderLinesArrayProperties(null));
        assertNull(dataTool.makeOrderLinesArrayProperties(new ArrayList<TdOrderLine>()));

        TdProduct productNull = new TdProduct(null, null, null, 1f, null, null);
        TdOrderLine line = new TdOrderLine(productNull,2);
        ArrayList<TdOrderLine> orderLines = new ArrayList<>();
        orderLines.add(line);

        assertNull(dataTool.makeOrderLinesArrayProperties(orderLines));
    }

    @Test
    public void makeOrderLinesArrayPropertiesNotNull() throws Exception {
        TdProduct product = new TdProduct("testid", "testsku", "testname", 1f, Currency.getInstance("USD"), "testcategory");
        TdProduct productNull = new TdProduct(null, null, null, 1f, null, null);
        TdOrderLine line1 = new TdOrderLine(null, 0);
        TdOrderLine line2 = new TdOrderLine(product, 2);
        TdOrderLine line3 = new TdOrderLine(product, -1);
        TdOrderLine line4 = new TdOrderLine(productNull,2);
        ArrayList<TdOrderLine> orderLines = new ArrayList<>();
        orderLines.add(line1);
        orderLines.add(line2);
        orderLines.add(line3);
        orderLines.add(line4);
        JSONArray expected = new JSONArray("[{\"!product\":{\"!id\":\"testid\",\"!sku\":\"testsku\",\"!name\":\"testname\",\"!price\":1,\"!currency\":\"USD\",\"!category\":\"testcategory\"},\"!quantity\":2}]");

        JSONArray result = dataTool.makeOrderLinesArrayProperties(orderLines);
        assertThat("Objects not equal", result.toString(), equalTo(expected.toString()));
    }

    @Test
    public void makeOrderLinesProperties() throws Exception {
        assertNull(dataTool.makeOrderLinesProperties(null));
        assertNull(dataTool.makeOrderLinesProperties(new ArrayList<TdOrderLine>()));

        TdProduct product = new TdProduct("testid", "testsku", "testname", 1f, Currency.getInstance("USD"), "testcategory");
        TdProduct productNull = new TdProduct(null, null, null, 1f, null, null);
        TdOrderLine line1 = new TdOrderLine(null, 0);
        TdOrderLine line2 = new TdOrderLine(product, 2);
        TdOrderLine line3 = new TdOrderLine(product, -1);
        TdOrderLine line4 = new TdOrderLine(productNull,2);
        ArrayList<TdOrderLine> orderLines = new ArrayList<>();
        orderLines.add(line1);
        orderLines.add(line2);
        orderLines.add(line3);
        orderLines.add(line4);
        JSONObject expected = new JSONObject("{\"!order_lines\":[{\"!product\":{\"!id\":\"testid\",\"!sku\":\"testsku\",\"!name\":\"testname\",\"!price\":1,\"!currency\":\"USD\",\"!category\":\"testcategory\"},\"!quantity\":2}]}");

        JSONObject result = dataTool.makeOrderLinesProperties(orderLines);
        assertThat("Objects not equal",result.toString(),equalTo(expected.toString()));
    }

    @Test
    public void makeOrderProperties() throws Exception {
        assertNull(dataTool.makeOrderProperties(null));
        assertNull(dataTool.makeOrderProperties(new TdOrder()));

        TdProduct product = new TdProduct("testid", "testsku", "testname", 1f, Currency.getInstance("USD"), "testcategory");
        TdProduct productNull = new TdProduct(null, null, null, 1f, null, null);
        TdOrderLine line1 = new TdOrderLine(null, 0);
        TdOrderLine line2 = new TdOrderLine(product, 2);
        TdOrderLine line3 = new TdOrderLine(product, -1);
        TdOrderLine line4 = new TdOrderLine(productNull,2);
        ArrayList<TdOrderLine> orderLines = new ArrayList<>();
        orderLines.add(line1);
        orderLines.add(line2);
        orderLines.add(line3);
        orderLines.add(line4);
        TdOrder order = new TdOrder("testid",1,1,1,1,1,"testcouponid",Currency.getInstance("USD"),orderLines);
        JSONObject expected = new JSONObject("{\"!order_id\":\"testid\",\"!total\":1,\"!revenue\":1,\"!shipping\":1,\"!tax\":1,\"!discount\":1,\"!coupon_id\":\"testcouponid\",\"!currency\":\"USD\",\"!order_lines\":[{\"!product\":{\"!id\":\"testid\",\"!sku\":\"testsku\",\"!name\":\"testname\",\"!price\":1,\"!currency\":\"USD\",\"!category\":\"testcategory\"},\"!quantity\":2}]}");

        JSONObject result = dataTool.makeOrderProperties(order);
        assertThat("Objects not equal. Result: " + result.toString() + "\nExpected: " + expected.toString(),result.toString().toLowerCase(),equalTo(expected.toString().toLowerCase()));

        order = new TdOrder(null,1,1,1,1,1,"testcouponid",Currency.getInstance("usd"),orderLines);
        expected = new JSONObject("{\"!total\":1,\"!revenue\":1,\"!shipping\":1,\"!tax\":1,\"!discount\":1,\"!coupon_id\":\"testcouponid\",\"!currency\":\"USD\",\"!order_lines\":[{\"!product\":{\"!id\":\"testid\",\"!sku\":\"testsku\",\"!name\":\"testname\",\"!price\":1,\"!currency\":\"USD\",\"!category\":\"testcategory\"},\"!quantity\":2}]}");
        result = dataTool.makeOrderProperties(order);
        assertThat("Objects not equal. Result: " + result.toString() + "\nExpected: " + expected.toString(),result.toString().toLowerCase(),equalTo(expected.toString().toLowerCase()));

        order = new TdOrder("testid",1,1,1,1,1,null,Currency.getInstance("usd"),orderLines);
        expected = new JSONObject("{\"!order_id\":\"testid\",\"!total\":1,\"!revenue\":1,\"!shipping\":1,\"!tax\":1,\"!discount\":1,\"!currency\":\"USD\",\"!order_lines\":[{\"!product\":{\"!id\":\"testid\",\"!sku\":\"testsku\",\"!name\":\"testname\",\"!price\":1,\"!currency\":\"USD\",\"!category\":\"testcategory\"},\"!quantity\":2}]}");
        result = dataTool.makeOrderProperties(order);
        assertThat("Objects not equal. Result: " + result.toString() + "\nExpected: " + expected.toString(),result.toString().toLowerCase(),equalTo(expected.toString().toLowerCase()));

        order = new TdOrder("testid",1,1,1,1,1,"testcouponid",Currency.getInstance("usd"),null);
        expected = new JSONObject("{\"!order_id\":\"testid\",\"!total\":1,\"!revenue\":1,\"!shipping\":1,\"!tax\":1,\"!discount\":1,\"!coupon_id\":\"testcouponid\",\"!currency\":\"USD\"}");
        result = dataTool.makeOrderProperties(order);
        assertThat("Objects not equal. Result: " + result.toString() + "\nExpected: " + expected.toString(),result.toString().toLowerCase(),equalTo(expected.toString().toLowerCase()));

        order = new TdOrder("testid",1,1,1,1,1,null,null,orderLines);
        result = dataTool.makeOrderProperties(order);
        assertNull(result);

        order = new TdOrder("testid",-1,1,1,1,1,null,Currency.getInstance("usd"),orderLines);
        result = dataTool.makeOrderProperties(order);
        assertNull(result);
    }


}