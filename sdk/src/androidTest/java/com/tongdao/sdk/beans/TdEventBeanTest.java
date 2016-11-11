package com.tongdao.sdk.beans;



import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by agonch on 9/27/16.
 * Instrumentation testing because JSON is part of Android, not Java
 */

@RunWith(AndroidJUnit4.class)
@SmallTest
public class TdEventBeanTest {

    @Test(expected = NullPointerException.class)
    public void testGetJsonObjectNull(){
        //set up a TdEventBean with null properties
        TdEventBean emptyBean = new TdEventBean(null,null,null);
        try {
            JSONObject emptyObject = emptyBean.getJsonObject();
        } catch (JSONException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testGetJsonObjectNullExceptAction(){
        //IDENTIFY ACTION - 3 PARAMETERS CONSTRUCTOR
        TdEventBean emptyBean = new TdEventBean(TdEventBean.ACTION_TYPE.identify,null,null);
        try{
            JSONObject result = emptyBean.getJsonObject();
            assertThat("Key action is missing",result.has(TdEventBean.KEY_ACTION),is(true));
            assertThat("Key user_id shouldn't be here",result.has(TdEventBean.KEY_USER_ID),is(false));
            assertThat("Key timestamp is missing",result.has(TdEventBean.KEY_TIMESTAMP),is(true));
            assertThat("Key properties shouldn't be here",result.has(TdEventBean.KEY_PROPERTIES),is(false));
            assertThat("Key previous_id shouldn't be here",result.has(TdEventBean.KEY_PREVIOUS_ID),is(false));
        }catch (JSONException e){
            System.out.println(e.getMessage());
        }

        //IDENTIFY ACTION - 4 PARAMETERS CONSTRUCTOR
        emptyBean = new TdEventBean(TdEventBean.ACTION_TYPE.identify,null,null,null);
        try{
            JSONObject result = emptyBean.getJsonObject();
            assertThat("Key action is missing",result.has(TdEventBean.KEY_ACTION),is(true));
            assertThat("Key user_id shouldn't be here",result.has(TdEventBean.KEY_USER_ID),is(false));
            assertThat("Key timestamp is missing",result.has(TdEventBean.KEY_TIMESTAMP),is(true));
            assertThat("Key properties shouldn't be here",result.has(TdEventBean.KEY_PROPERTIES),is(false));
            assertThat("Key previous_id shouldn't be here",result.has(TdEventBean.KEY_PREVIOUS_ID),is(false));
        }catch (JSONException e){
            System.out.println(e.getMessage());
        }

        //IDENTIFY ACTION - 5 PARAMETERS CONSTRUCTOR
        emptyBean = new TdEventBean(TdEventBean.ACTION_TYPE.identify,null,null,null,null);
        try{
            JSONObject result = emptyBean.getJsonObject();
            assertThat("Key action is missing",result.has(TdEventBean.KEY_ACTION),is(true));
            assertThat("Key user_id shouldn't be here",result.has(TdEventBean.KEY_USER_ID),is(false));
            assertThat("Key timestamp is missing",result.has(TdEventBean.KEY_TIMESTAMP),is(true));
            assertThat("Key properties shouldn't be here",result.has(TdEventBean.KEY_PROPERTIES),is(false));
            assertThat("Key previous_id shouldn't be here",result.has(TdEventBean.KEY_PREVIOUS_ID),is(false));
        }catch (JSONException e){
            System.out.println(e.getMessage());
        }

        //TRACK ACTION - 3 PARAMETERS CONSTRUCTOR
        emptyBean = new TdEventBean(TdEventBean.ACTION_TYPE.track,null,null);
        try{
            JSONObject result = emptyBean.getJsonObject();
            assertThat("Key action is missing",result.has(TdEventBean.KEY_ACTION),is(true));
            assertThat("Key user_id shouldn't be here",result.has(TdEventBean.KEY_USER_ID),is(false));
            assertThat("Key timestamp is missing",result.has(TdEventBean.KEY_TIMESTAMP),is(true));
            assertThat("Key properties shouldn't be here",result.has(TdEventBean.KEY_PROPERTIES),is(false));
            assertThat("Key previous_id shouldn't be here",result.has(TdEventBean.KEY_PREVIOUS_ID),is(false));
        }catch (JSONException e){
            System.out.println(e.getMessage());
        }

        //TRACK ACTION - 4 PARAMETERS CONSTRUCTOR
        emptyBean = new TdEventBean(TdEventBean.ACTION_TYPE.track,null,null,null);
        try{
            JSONObject result = emptyBean.getJsonObject();
            assertThat("Key action is missing",result.has(TdEventBean.KEY_ACTION),is(true));
            assertThat("Key user_id shouldn't be here",result.has(TdEventBean.KEY_USER_ID),is(false));
            assertThat("Key timestamp is missing",result.has(TdEventBean.KEY_TIMESTAMP),is(true));
            assertThat("Key properties shouldn't be here",result.has(TdEventBean.KEY_PROPERTIES),is(false));
            assertThat("Key previous_id shouldn't be here",result.has(TdEventBean.KEY_PREVIOUS_ID),is(false));
        }catch (JSONException e){
            System.out.println(e.getMessage());
        }

        //TRACK ACTION - 5 PARAMETERS CONSTRUCTOR
        emptyBean = new TdEventBean(TdEventBean.ACTION_TYPE.track,null,null,null,null);
        try{
            JSONObject result = emptyBean.getJsonObject();
            assertThat("Key action is missing",result.has(TdEventBean.KEY_ACTION),is(true));
            assertThat("Key user_id shouldn't be here",result.has(TdEventBean.KEY_USER_ID),is(false));
            assertThat("Key timestamp is missing",result.has(TdEventBean.KEY_TIMESTAMP),is(true));
            assertThat("Key properties shouldn't be here",result.has(TdEventBean.KEY_PROPERTIES),is(false));
            assertThat("Key previous_id shouldn't be here",result.has(TdEventBean.KEY_PREVIOUS_ID),is(false));
        }catch (JSONException e){
            System.out.println(e.getMessage());
        }

        //MERGE ACTION - 3 PARAMETERS CONSTRUCTOR
        emptyBean = new TdEventBean(TdEventBean.ACTION_TYPE.merge,null,null);
        try{
            JSONObject result = emptyBean.getJsonObject();
            assertThat("Key action is missing",result.has(TdEventBean.KEY_ACTION),is(true));
            assertThat("Key user_id shouldn't be here",result.has(TdEventBean.KEY_USER_ID),is(false));
            assertThat("Key timestamp is missing",result.has(TdEventBean.KEY_TIMESTAMP),is(true));
            assertThat("Key properties shouldn't be here",result.has(TdEventBean.KEY_PROPERTIES),is(false));
            assertThat("Key previous_id shouldn't be here",result.has(TdEventBean.KEY_PREVIOUS_ID),is(false));
        }catch (JSONException e){
            System.out.println(e.getMessage());
        }

        //MERGE ACTION - 4 PARAMETERS CONSTRUCTOR
        emptyBean = new TdEventBean(TdEventBean.ACTION_TYPE.merge,null,null,null);
        try{
            JSONObject result = emptyBean.getJsonObject();
            assertThat("Key action is missing",result.has(TdEventBean.KEY_ACTION),is(true));
            assertThat("Key user_id shouldn't be here",result.has(TdEventBean.KEY_USER_ID),is(false));
            assertThat("Key timestamp is missing",result.has(TdEventBean.KEY_TIMESTAMP),is(true));
            assertThat("Key properties shouldn't be here",result.has(TdEventBean.KEY_PROPERTIES),is(false));
            assertThat("Key previous_id shouldn't be here",result.has(TdEventBean.KEY_PREVIOUS_ID),is(false));
        }catch (JSONException e){
            System.out.println(e.getMessage());
        }

        //MERGE ACTION - 5 PARAMETERS CONSTRUCTOR
        emptyBean = new TdEventBean(TdEventBean.ACTION_TYPE.merge,null,null,null,null);
        try{
            JSONObject result = emptyBean.getJsonObject();
            assertThat("Key action is missing",result.has(TdEventBean.KEY_ACTION),is(true));
            assertThat("Key user_id shouldn't be here",result.has(TdEventBean.KEY_USER_ID),is(false));
            assertThat("Key timestamp is missing",result.has(TdEventBean.KEY_TIMESTAMP),is(true));
            assertThat("Key properties shouldn't be here",result.has(TdEventBean.KEY_PROPERTIES),is(false));
            assertThat("Key previous_id shouldn't be here",result.has(TdEventBean.KEY_PREVIOUS_ID),is(false));
        }catch (JSONException e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testGetJsonObjectEmptyActionIdentify(){
        //3 PARAMS
        TdEventBean emptyBean = new TdEventBean(TdEventBean.ACTION_TYPE.identify,"","");
        try {
            JSONObject result = emptyBean.getJsonObject();
            assertThat("Key action is missing",result.has(TdEventBean.KEY_ACTION),is(true));
            assertThat("Key user_id is missing",result.has(TdEventBean.KEY_USER_ID),is(true));
            assertThat("Key event shouldn't be here",result.has(TdEventBean.KEY_EVENT),is(false));
            assertThat("Key timestamp is missing",result.has(TdEventBean.KEY_TIMESTAMP),is(true));
            assertThat("Key properties shouldn't be here",result.has(TdEventBean.KEY_PROPERTIES),is(false));
            assertThat("Key previous_id shouldn't be here",result.has(TdEventBean.KEY_PREVIOUS_ID),is(false));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //4 PARAMS
        emptyBean = new TdEventBean(TdEventBean.ACTION_TYPE.identify,"","",new JSONObject());
        try {
            JSONObject result = emptyBean.getJsonObject();
            assertThat("Key action is missing",result.has(TdEventBean.KEY_ACTION),is(true));
            assertThat("Key user_id is missing",result.has(TdEventBean.KEY_USER_ID),is(true));
            assertThat("Key event shouldn't be here",result.has(TdEventBean.KEY_EVENT),is(false));
            assertThat("Key timestamp is missing",result.has(TdEventBean.KEY_TIMESTAMP),is(true));
            assertThat("Key properties is missing",result.has(TdEventBean.KEY_PROPERTIES),is(true));
            assertThat("Key previous_id shouldn't be here",result.has(TdEventBean.KEY_PREVIOUS_ID),is(false));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //5 PARAMS
        emptyBean = new TdEventBean(TdEventBean.ACTION_TYPE.identify,"","","",new JSONObject());
        try {
            JSONObject result = emptyBean.getJsonObject();
            assertThat("Key action is missing",result.has(TdEventBean.KEY_ACTION),is(true));
            assertThat("Key user_id is missing",result.has(TdEventBean.KEY_USER_ID),is(true));
            assertThat("Key event shouldn't be here",result.has(TdEventBean.KEY_EVENT),is(false));
            assertThat("Key timestamp is missing",result.has(TdEventBean.KEY_TIMESTAMP),is(true));
            assertThat("Key properties is missing",result.has(TdEventBean.KEY_PROPERTIES),is(true));
            assertThat("Key previous_id shouldn't be here",result.has(TdEventBean.KEY_PREVIOUS_ID),is(false));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetJsonObjectEmptyActionTrack(){
        //3 PARAMS
        TdEventBean emptyBean = new TdEventBean(TdEventBean.ACTION_TYPE.track,"","");
        try {
            JSONObject result = emptyBean.getJsonObject();
            assertThat("Key action is missing",result.has(TdEventBean.KEY_ACTION),is(true));
            assertThat("Key user_id is missing",result.has(TdEventBean.KEY_USER_ID),is(true));
            assertThat("Key event is missing",result.has(TdEventBean.KEY_EVENT),is(true));
            assertThat("Key timestamp is missing",result.has(TdEventBean.KEY_TIMESTAMP),is(true));
            assertThat("Key properties shouldn't be here",result.has(TdEventBean.KEY_PROPERTIES),is(false));
            assertThat("Key previous_id shouldn't be here",result.has(TdEventBean.KEY_PREVIOUS_ID),is(false));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //4 PARAMS
        emptyBean = new TdEventBean(TdEventBean.ACTION_TYPE.track,"","",new JSONObject());
        try {
            JSONObject result = emptyBean.getJsonObject();
            assertThat("Key action is missing",result.has(TdEventBean.KEY_ACTION),is(true));
            assertThat("Key user_id is missing",result.has(TdEventBean.KEY_USER_ID),is(true));
            assertThat("Key event is missing",result.has(TdEventBean.KEY_EVENT),is(true));
            assertThat("Key timestamp is missing",result.has(TdEventBean.KEY_TIMESTAMP),is(true));
            assertThat("Key properties is missing",result.has(TdEventBean.KEY_PROPERTIES),is(true));
            assertThat("Key previous_id shouldn't be here",result.has(TdEventBean.KEY_PREVIOUS_ID),is(false));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //5 PARAMS
        emptyBean = new TdEventBean(TdEventBean.ACTION_TYPE.track,"","","",new JSONObject());
        try {
            JSONObject result = emptyBean.getJsonObject();
            assertThat("Key action is missing",result.has(TdEventBean.KEY_ACTION),is(true));
            assertThat("Key user_id is missing",result.has(TdEventBean.KEY_USER_ID),is(true));
            assertThat("Key event is missing",result.has(TdEventBean.KEY_EVENT),is(true));
            assertThat("Key timestamp is missing",result.has(TdEventBean.KEY_TIMESTAMP),is(true));
            assertThat("Key properties is missing",result.has(TdEventBean.KEY_PROPERTIES),is(true));
            assertThat("Key previous_id shouldn't be here",result.has(TdEventBean.KEY_PREVIOUS_ID),is(false));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetJsonObjectEmptyActionMerge(){
        //3 PARAMS
        TdEventBean emptyBean = new TdEventBean(TdEventBean.ACTION_TYPE.merge,"","");
        try {
            JSONObject result = emptyBean.getJsonObject();
            assertThat("Key action is missing",result.has(TdEventBean.KEY_ACTION),is(true));
            assertThat("Key user_id is missing",result.has(TdEventBean.KEY_USER_ID),is(true));
            assertThat("Key timestamp is missing",result.has(TdEventBean.KEY_TIMESTAMP),is(true));
            assertThat("Key properties shouldn't be here",result.has(TdEventBean.KEY_PROPERTIES),is(false));
            assertThat("Key previous_id shouldn't be here",result.has(TdEventBean.KEY_PREVIOUS_ID),is(false));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //4 PARAMS
        emptyBean = new TdEventBean(TdEventBean.ACTION_TYPE.merge,"","",new JSONObject());
        try {
            JSONObject result = emptyBean.getJsonObject();
            assertThat("Key action is missing",result.has(TdEventBean.KEY_ACTION),is(true));
            assertThat("Key user_id is missing",result.has(TdEventBean.KEY_USER_ID),is(true));
            assertThat("Key timestamp is missing",result.has(TdEventBean.KEY_TIMESTAMP),is(true));
            assertThat("Key properties is missing",result.has(TdEventBean.KEY_PROPERTIES),is(true));
            assertThat("Key previous_id shouldn't be here",result.has(TdEventBean.KEY_PREVIOUS_ID),is(false));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //5 PARAMS
        emptyBean = new TdEventBean(TdEventBean.ACTION_TYPE.merge,"","","",new JSONObject());
        try {
            JSONObject result = emptyBean.getJsonObject();
            assertThat("Key action is missing",result.has(TdEventBean.KEY_ACTION),is(true));
            assertThat("Key user_id is missing",result.has(TdEventBean.KEY_USER_ID),is(true));
            assertThat("Key timestamp is missing",result.has(TdEventBean.KEY_TIMESTAMP),is(true));
            assertThat("Key properties is missing",result.has(TdEventBean.KEY_PROPERTIES),is(true));
            assertThat("Key previous_id is missing",result.has(TdEventBean.KEY_PREVIOUS_ID),is(true));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
