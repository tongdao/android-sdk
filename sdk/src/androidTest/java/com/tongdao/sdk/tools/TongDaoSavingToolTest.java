package com.tongdao.sdk.tools;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.MediumTest;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;
import android.test.ApplicationTestCase;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.hamcrest.core.IsNull.*;
import static org.hamcrest.core.IsEqual.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by agonch on 9/30/16.
 */

@RunWith(AndroidJUnit4.class)
@MediumTest
public class TongDaoSavingToolTest extends ApplicationTestCase<Application> {

    private static final String USER_INFO_DATA = "LQ_USER_INFO_DATA";
    private static final String USER_ID = "LQ_USER_ID";
    private static final String APP_KEY = "LQ_APP_KEY";
    private static final String PREVIOUS_ID = "LQ_PREVIOUS_ID";
    private static final String ANONYMOUS = "LQ_ANONYMOUS";
    private static final String ANONYMOUS_SET = "IS_ANONYMOUS";
    private static final String APPLICATION_DATA = "APPLICATION_DATA";
    private static final String CONNECTION_DATA = "CONNECTION_DATA";
    private static final String LOCATION_DATA = "LOCATION_DATA";
    private static final String DEVICE_DATA = "DEVICE_DATA";
    private static final String FINGERPRINT_DATA = "FINGERPRINT_DATA";
    private static final String APP_SESSION_DATA = "APP_SESSION";
    private static final String NOTIFICATION_DATA = "NOTIFICATION_DATA";

    Context mContext;
    SharedPreferences sharedPreferences;

    public TongDaoSavingToolTest() {
        super(Application.class);
    }

    @Override @Before
    public void setUp() throws Exception {
        super.setUp();
        mContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        sharedPreferences = mContext.getSharedPreferences(USER_INFO_DATA, Context.MODE_PRIVATE);
        assertNotNull(mContext);
        assertNotNull(sharedPreferences);
    }

    @Override @After
    public void tearDown() throws Exception {
        mContext.getSharedPreferences(USER_INFO_DATA, Context.MODE_PRIVATE).edit().clear().commit();
        super.tearDown();

    }

    public void clearSharedPreferences() {
        mContext.getSharedPreferences(USER_INFO_DATA, Context.MODE_PRIVATE).edit().clear().commit();
    }

    @Test
    public void testSaveAppKeyAndUserIdNull() {
        assertNotNull(mContext);
        clearSharedPreferences();

        String appKey = null;
        String userId = null;
        String expectedResult = "ERR";
        TongDaoSavingTool.saveAppKeyAndUserId(mContext, appKey, userId);
        String resultAppKey = sharedPreferences.getString(APP_KEY, expectedResult);
        String resultUserId = sharedPreferences.getString(USER_ID, expectedResult);
        assertThat("Keys not the same", resultAppKey, equalTo(expectedResult));
        assertThat("Keys not the same", resultUserId, equalTo(expectedResult));
    }

    @Test
    public void testSaveAppKeyAndUserId() {
        assertNotNull(mContext);
        clearSharedPreferences();

        String appKey = "someAppKey";
        String userId = "someUID";
        TongDaoSavingTool.saveAppKeyAndUserId(mContext, appKey, userId);
        String resultAppKey = sharedPreferences.getString(APP_KEY, "ERR");
        String resultUserId = sharedPreferences.getString(USER_ID, "ERR");
        assertThat("Keys not the same", appKey, equalTo(resultAppKey));
        assertThat("Keys not the same", userId, equalTo(resultUserId));
    }

    @Test
    public void testSaveUserInfoDataNull() {
        assertNotNull(mContext);
        clearSharedPreferences();

        String appKey = null;
        String userId = null;
        String previousId = null;
        Boolean anonymous = new Boolean(false);
        String expectedResult = "ERR";

        TongDaoSavingTool.saveUserInfoData(mContext, appKey, userId, previousId, anonymous);
        String resultAppKey = sharedPreferences.getString(APP_KEY, expectedResult);
        String resultUserId = sharedPreferences.getString(USER_ID, expectedResult);
        String resultPreviousId = sharedPreferences.getString(PREVIOUS_ID, expectedResult);

        assertThat("Keys don't match", resultAppKey, equalTo(expectedResult));
        assertThat("Keys don't match", resultUserId, equalTo(expectedResult));
        assertThat("Keys don't match", resultPreviousId, equalTo(expectedResult));

        TongDaoSavingTool.saveUserInfoData(mContext, userId, previousId, anonymous);
        resultUserId = sharedPreferences.getString(USER_ID, expectedResult);
        resultPreviousId = sharedPreferences.getString(PREVIOUS_ID, expectedResult);

        assertThat("Keys don't match", resultUserId, equalTo(expectedResult));
        assertThat("Keys don't match", resultPreviousId, equalTo(expectedResult));
    }

    @Test
    public void testSaveUserInfoDataNotNull() {
        assertNotNull(mContext);
        clearSharedPreferences();

        String appKey = "testkey";
        String userId = "testid";
        String previousId = "previousid";
        Boolean anonymous = new Boolean(true);
        String errorResult = "ERR";

        TongDaoSavingTool.saveUserInfoData(mContext, appKey, userId, previousId, anonymous);
        String resultAppKey = sharedPreferences.getString(APP_KEY, errorResult);
        String resultUserId = sharedPreferences.getString(USER_ID, errorResult);
        String resultPreviousId = sharedPreferences.getString(PREVIOUS_ID, errorResult);
        Boolean resultAnonymous = sharedPreferences.getBoolean(ANONYMOUS, false);

        assertThat("Keys don't match", resultAppKey, equalTo(appKey));
        assertThat("Keys don't match", resultUserId, equalTo(userId));
        assertThat("Keys don't match", resultPreviousId, equalTo(previousId));
        assertThat("Keys don't match", resultPreviousId, equalTo(previousId));
        assertThat("Keys don't match", resultAnonymous, equalTo(anonymous));

        TongDaoSavingTool.saveUserInfoData(mContext, userId, previousId, anonymous);
        resultUserId = sharedPreferences.getString(USER_ID, errorResult);
        resultPreviousId = sharedPreferences.getString(PREVIOUS_ID, errorResult);
        resultAnonymous = sharedPreferences.getBoolean(ANONYMOUS, false);

        assertThat("Keys don't match", resultUserId, equalTo(userId));
        assertThat("Keys don't match", resultPreviousId, equalTo(previousId));
        assertThat("Keys don't match", resultAnonymous, equalTo(anonymous));
    }

    @Test
    public void testIsAnonymousSet() {
        assertNotNull(mContext);
        clearSharedPreferences();

        TongDaoSavingTool.isAnonymousSet(mContext);

        Boolean isAnonymous = sharedPreferences.getBoolean(ANONYMOUS_SET, false);

        assertThat("Should be true", isAnonymous, equalTo(true));
    }

    @Test
    public void testGetAnonymousSet() {
        assertNotNull(mContext);
        clearSharedPreferences();
        sharedPreferences.edit().putBoolean(ANONYMOUS_SET, true).commit();

        Boolean anonymousSet = TongDaoSavingTool.getAnonymousSet(mContext);

        assertThat("Should be true", anonymousSet, equalTo(true));
    }

    @Test
    public void testGetAnonymous() {
        assertNotNull(mContext);
        clearSharedPreferences();

        Boolean anonymous = TongDaoSavingTool.getAnonymous(mContext);

        assertThat("Should be true", anonymous, equalTo(true));
    }

    @Test
    public void testSetPermissionDenied() {
        assertNotNull(mContext);
        clearSharedPreferences();
        String permission = "testPermission";

        TongDaoSavingTool.setPermissionDenied(mContext, permission);

        Boolean permissionResult = sharedPreferences.getBoolean(permission, false);

        assertThat("Should be true", permissionResult, equalTo(true));
    }

    @Test
    public void testGetPermissionDenied() {
        assertNotNull(mContext);
        clearSharedPreferences();
        String permission = "testPermission";

        sharedPreferences.edit().putBoolean(permission,true).commit();
        Boolean permissionResult = TongDaoSavingTool.getPermissionDenied(mContext, permission);

        assertThat("Should be true", permissionResult, equalTo(true));
    }

    @Test
    public void testSetApplicationInfoData(){
        assertNotNull(mContext);
        clearSharedPreferences();
        String applicationData = "testdata";

        TongDaoSavingTool.setApplicationInfoData(mContext,applicationData);
        String resultData = sharedPreferences.getString(APPLICATION_DATA,"ERR");

        assertThat("Wrong result",resultData,equalTo(applicationData));
    }

    @Test
    public void testGetApplicationInfoData(){
        assertNotNull(mContext);
        clearSharedPreferences();
        String applicationData = "testdata";

        sharedPreferences.edit().putString(APPLICATION_DATA,applicationData).commit();
        String resultData = TongDaoSavingTool.getApplicationInfoData(mContext);

        assertThat("Wrong result",resultData,equalTo(applicationData));
    }

    @Test
    public void testSetConnectionInfoData() throws Exception {
        assertNotNull(mContext);
        clearSharedPreferences();
        String data = "testdata";

        TongDaoSavingTool.setConnectionInfoData(mContext,data);
        String result = sharedPreferences.getString(CONNECTION_DATA,"ERR");

        assertThat("Wrong result",result,equalTo(data));
    }

    @Test
    public void testGetConnectionInfoData() throws Exception {
        assertNotNull(mContext);
        clearSharedPreferences();
        String data = "testdata";

        sharedPreferences.edit().putString(CONNECTION_DATA,data).commit();
        String result = TongDaoSavingTool.getConnectionInfoData(mContext);

        assertThat("Wrong result",result,equalTo(data));
    }

    @Test
    public void testSetLocationInfoData() throws Exception {
        assertNotNull(mContext);
        clearSharedPreferences();
        String data = "testdata";

        TongDaoSavingTool.setLocationInfoData(mContext,data);
        String result = sharedPreferences.getString(LOCATION_DATA,"ERR");

        assertThat("Wrong result",result,equalTo(data));
    }

    @Test
    public void testGetLocationInfoData() throws Exception {
        assertNotNull(mContext);
        clearSharedPreferences();
        String data = "testdata";

        sharedPreferences.edit().putString(LOCATION_DATA,data).commit();
        String result = TongDaoSavingTool.getLocationInfoData(mContext);

        assertThat("Wrong result",result,equalTo(data));
    }

    @Test
    public void testSetDeviceInfoData() throws Exception {
        assertNotNull(mContext);
        clearSharedPreferences();
        String data = "testdata";

        TongDaoSavingTool.setDeviceInfoData(mContext,data);
        String result = sharedPreferences.getString(DEVICE_DATA,"ERR");

        assertThat("Wrong result",result,equalTo(data));
    }

    @Test
    public void testGetDeviceInfoData() throws Exception {
        assertNotNull(mContext);
        clearSharedPreferences();
        String data = "testdata";

        sharedPreferences.edit().putString(DEVICE_DATA,data).commit();
        String result = TongDaoSavingTool.getDeviceInfoData(mContext);

        assertThat("Wrong result",result,equalTo(data));
    }

    @Test
    public void testSetFingerprintInfoData() throws Exception {
        assertNotNull(mContext);
        clearSharedPreferences();
        String data = "testdata";

        TongDaoSavingTool.setFingerprintInfoData(mContext,data);
        String result = sharedPreferences.getString(FINGERPRINT_DATA,"ERR");

        assertThat("Wrong result",result,equalTo(data));
    }

    @Test
    public void testGetFingerprintInfoData() throws Exception {
        assertNotNull(mContext);
        clearSharedPreferences();
        String data = "testdata";

        sharedPreferences.edit().putString(FINGERPRINT_DATA,data).commit();
        String result = TongDaoSavingTool.getFingerprintInfoData(mContext);

        assertThat("Wrong result",result,equalTo(data));
    }

    @Test
    public void testSetAppSessionData() throws Exception {
        assertNotNull(mContext);
        clearSharedPreferences();
        String data = "testdata";

        TongDaoSavingTool.setAppSessionData(mContext,data);
        String result = sharedPreferences.getString(APP_SESSION_DATA,"ERR");

        assertThat("Wrong result",result,equalTo(data));
    }

    @Test
    public void testGetAppSessionData() throws Exception {
        assertNotNull(mContext);
        clearSharedPreferences();
        String data = "testdata";

        sharedPreferences.edit().putString(APP_SESSION_DATA,data).commit();
        String result = TongDaoSavingTool.getAppSessionData(mContext);

        assertThat("Wrong result",result,equalTo(data));
    }

    @Test
    public void testSetNotificationData() throws Exception {
        assertNotNull(mContext);
        clearSharedPreferences();
        int data = 2343;

        TongDaoSavingTool.setNotificationData(mContext,data);
        int result = sharedPreferences.getInt(NOTIFICATION_DATA,-1);

        assertThat("Wrong result",result,equalTo(data));
    }

    @Test
    public void testGetNotificationData() throws Exception {
        assertNotNull(mContext);
        clearSharedPreferences();
        int data = 2343;

        sharedPreferences.edit().putInt(NOTIFICATION_DATA,data).commit();
        int result = TongDaoSavingTool.getNotificationData(mContext);

        assertThat("Wrong result",result,equalTo(data));
    }


}
