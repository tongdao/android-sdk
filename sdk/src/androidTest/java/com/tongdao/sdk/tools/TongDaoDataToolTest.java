package com.tongdao.sdk.tools;

import android.app.Application;
import android.support.test.filters.MediumTest;
import android.support.test.runner.AndroidJUnit4;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Created by agonch on 10/8/16.
 */

@RunWith(AndroidJUnit4.class)
@MediumTest
public class TongDaoDataToolTest extends android.test.ApplicationTestCase<Application> {

    public TongDaoDataToolTest() {
        super(Application.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void makeInfoProperties() throws Exception {

    }

    @Test
    public void makeRatingProperties() throws Exception {
        int rating = 3;
        JSONObject result = TongDaoDataTool.makeRatingProperties(rating);
        System.out.println(result.toString());
    }

    @Test
    public void makeUserProperties() throws Exception {

    }

    @Test
    public void makeSourceProperties() throws Exception {

    }

    @Test
    public void makeRegisterProperties() throws Exception {

    }

    @Test
    public void makeProductProperties() throws Exception {

    }

    @Test
    public void makeOrderLinesArrayProperties() throws Exception {

    }

    @Test
    public void makeOrderLinesProperties() throws Exception {

    }

    @Test
    public void makeOrderProperties() throws Exception {

    }

    @Test
    public void setAnonymous() throws Exception {

    }

    @Test
    public void getAnonymous() throws Exception {

    }

}