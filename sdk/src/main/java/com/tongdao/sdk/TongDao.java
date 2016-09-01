package com.tongdao.sdk;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import com.tongdao.sdk.beans.TdEventBean;
import com.tongdao.sdk.beans.TdEventBean.ACTION_TYPE;
import com.tongdao.sdk.beans.TdMessageBean;
import com.tongdao.sdk.beans.TdOrder;
import com.tongdao.sdk.beans.TdOrderLine;
import com.tongdao.sdk.beans.TdProduct;
import com.tongdao.sdk.beans.TdSource;
import com.tongdao.sdk.enums.TdGender;
import com.tongdao.sdk.interfaces.OnDownloadInAppMessageListener;
import com.tongdao.sdk.interfaces.OnDownloadLandingPageListener;
import com.tongdao.sdk.interfaces.OnErrorListener;
import com.tongdao.sdk.session.TongDaoActivityCallback;
import com.tongdao.sdk.tools.TongDaoCheckTool;
import com.tongdao.sdk.tools.TongDaoDataTool;
import com.tongdao.sdk.tools.TongDaoDeviceUuidFactory;
import com.tongdao.sdk.tools.TongDaoJsonTool;
import com.tongdao.sdk.tools.TongDaoSavingTool;
import com.tongdao.sdk.tools.TongDaoUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TongDao {

    private static TongDaoBridge lingQianBridge;
    //	scheme://any_deeplink?0Qpage=page_id
    private static final String ENDING_PAGE_STRING = "0Qpage=";

    private static final String MESSAGE_TAG = "tongrd_mid";
    private static final String CLIENT_TAG = "tongrd_cid";
    private static final String TYPE_TAG = "tongrd_type";
    private static final String VALUE_TAG = "tongrd_value";


    /**
     * 初始化同道服务,请在onCreate方法中调用
     *
     * @param appContext 应用程序的上下文
     * @param appKey     开发者从同道平台获得的AppKey
     * @return boolean 同道服务的初始化结果
     */
    public static boolean init(Context appContext, String appKey) {
        String deviceId = TongDao.generateDeviceId(appContext);
        TongDaoUtils.init(appContext);
        if (TongDaoCheckTool.isValidKey(appKey) && !TongDaoCheckTool.isEmpty(deviceId)) {
            lingQianBridge = TongDaoBridge.getInstance(appContext, appKey, deviceId, deviceId);
            lingQianBridge.init();
            TongDao.onAppSessionStart();
            return true;
        } else {
            return false;
        }
    }

    /**
     * 初始化同道服务,请在onCreate方法中调用
     *
     * @param appContext 应用程序的上下文
     * @param appKey     开发者从同道平台获得的AppKey
     * @param userId     用户自定义
     * @return boolean 同道服务的初始化结果
     */
    public static boolean init(Context appContext, String appKey, String userId){
        String deviceId = TongDao.generateDeviceId(appContext);
        if(null == userId){
            if (TongDaoCheckTool.isValidKey(appKey) && !TongDaoCheckTool.isEmpty(userId)) {
                lingQianBridge = TongDaoBridge.getInstance(appContext, appKey, deviceId, deviceId);
                TongDaoSavingTool.setAnonymous(appContext, true);
                lingQianBridge.init();
                TongDao.onAppSessionStart();
                return true;
            } else {
                return false;
            }
        }else {
            if (TongDaoCheckTool.isValidKey(appKey) && !TongDaoCheckTool.isEmpty(userId)) {
                lingQianBridge = TongDaoBridge.getInstance(appContext, appKey, deviceId, userId, null, false);
                TongDaoSavingTool.setAnonymous(appContext, false);
                lingQianBridge.init();
                TongDao.onAppSessionStart();
                return true;
            } else {
                return false;
            }
        }
    }

    public static void trackEvent() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if( lingQianBridge != null ) {
                    lingQianBridge.trackIdentify();
                }
            }
        }).start();
    }
    /**
     * 使用用户自定义userid
     *
     * @param userId 用户自定义的userid
     * @return void 没有返回值
     */
    public static void setUserId(Context appContext, String userId){
        if(null == userId){
            if(!TongDaoSavingTool.getAnonymous(appContext)){
                TongDaoSavingTool.saveUserInfoData(appContext, TongDao.generateDeviceId(appContext), TongDao.generateDeviceId(appContext), true);
                lingQianBridge.changePropertiesAndUserId(ACTION_TYPE.identify, null, TongDao.generateDeviceId(appContext));
            }
        }else{
            if(TongDaoSavingTool.getAnonymous(appContext)){
                TongDaoSavingTool.saveUserInfoData(appContext, userId, TongDao.generateDeviceId(appContext), false);
                lingQianBridge.changePropertiesAndUserId(ACTION_TYPE.merge, TongDao.generateDeviceId(appContext), userId);
            }else{
                TongDaoSavingTool.saveUserInfoData(appContext, userId, TongDao.generateDeviceId(appContext), false);
                lingQianBridge.changePropertiesAndUserId(ACTION_TYPE.identify, TongDao.generateDeviceId(appContext), userId);
            }
        }
    }
    /**
     * 使用同道SDK生成userId
     *
     * @param appContext 应用程序的上下文
     * @return String 生成userId
     */
    public static String generateDeviceId(Context appContext) {
        try {
            return TongDaoDeviceUuidFactory.getDeviceUuid(appContext).toString();
        } catch (UnsupportedEncodingException e) {
            Log.e("TongRd SDK", "UnsupportedEncodingException");
        }
        return null;
    }

    /**
     * 跟踪用户自定义事件
     *
     * @param eventName 用户自定义事件名称(不能以!打头)
     */
    public static void track(String eventName) {
        if (eventName == null || eventName.trim().equals("") || eventName.startsWith("!")) {
            Log.e("TongRd SDK", "event starting with ! are reserved");
        } else {
            sendEvent(ACTION_TYPE.track, eventName, null);
        }
    }

    /**
     * 跟踪用户自定义事件
     *
     * @param eventName 用户自定义事件名称(不能以!打头)
     * @param values    跟踪事件附带的键值对(值支持字符串和数字)
     */
    public static void track(String eventName, HashMap<String, Object> values) {
        if (eventName == null || eventName.trim().equals("") || eventName.startsWith("!")) {
            Log.e("TongRd SDK", "event starting with ! are reserved");
        } else {
            try {
                sendEvent(ACTION_TYPE.track, eventName, TongDaoDataTool.makeUserProperties(values));
            } catch (JSONException e) {
                Log.e("track", "JSONException");
            }
        }
    }

    /**
     * 开始记录用户的使用时长
     *
     * @param activity 当前应用程序的Activity
     */
    public static void onSessionStart(Activity activity) {
        if (lingQianBridge != null && lingQianBridge.getUserId() != null && activity != null) {
            lingQianBridge.onSessionStart(activity);
        }
    }

    /**
     * 开始记录用户的使用时长
     *
     * @param pageName 用户定义的页面名称
     */
    public static void onSessionStart(String pageName) {
        if (lingQianBridge != null && lingQianBridge.getUserId() != null && pageName != null) {
            lingQianBridge.onSessionStart(pageName);
        }
    }



    /**
     * 终止记录用户的使用时长
     *
     * @param activity 当前应用程序的Activity
     */
    public static void onSessionEnd(Activity activity) {
        if (lingQianBridge != null && lingQianBridge.getUserId() != null && activity != null) {
            lingQianBridge.onSessionEnd(activity);
        }
    }

    /**
     * 终止记录用户的使用时长
     *
     * @param pageName 用户定义的页面名称
     */
    public static void onSessionEnd(String pageName) {
        if (lingQianBridge != null && lingQianBridge.getUserId() != null && pageName != null) {
            lingQianBridge.onSessionEnd(pageName);
        }
    }

    public static void onAppSessionStart() {
        Log.e("session event track bf", "Start" + 0);
        if (lingQianBridge != null && lingQianBridge.getUserId() != null) {
            Log.e("session event track", "Start" + 0);
            lingQianBridge.onAppSessionStart();
        }
    }

    public static void onAppSessionEnd() {
        Log.e("session event track bf", "Emd" + 0);
        if (lingQianBridge != null && lingQianBridge.getUserId() != null) {
            Log.e("session event track", "Emd" + 0);
            lingQianBridge.onAppSessionEnd();
        }
    }

    /**
     * 保存用户多个自定义属性
     *
     * @param values 用户的属性键值对(值支持字符串和数字)
     */
    public static void identify(HashMap<String, Object> values) {
        if (values != null && !values.isEmpty()) {
            try {
                sendEvent(ACTION_TYPE.identify, null, TongDaoDataTool.makeUserProperties(values));
            } catch (JSONException e) {
                Log.e("identify", "JSONException");
            }
        }
    }

    /**
     * 保存用户单个自定义属性
     *
     * @param name  属性名
     * @param value 属性值(值支持字符串和数字)
     */
    public static void identify(String name, Object value) {
        if (name != null && !name.trim().equals("") && value != null) {
            HashMap<String, Object> values = new HashMap<String, Object>();
            values.put(name, value);
            try {
                sendEvent(ACTION_TYPE.identify, null, TongDaoDataTool.makeUserProperties(values));
            } catch (JSONException e) {
                Log.e("identify", "JSONException");
            }
        }
    }

    /**
     * 保存用户名字属性
     *
     * @param firstName 名
     * @param lastName  姓
     */
    public static void identifyFullName(String firstName, String lastName) {
        if (firstName == null && lastName == null) {
            return;
        } else {
            HashMap<String, Object> values = new HashMap<String, Object>();
            if (firstName != null && lastName == null) {
                values.put("!first_name", firstName);
            } else if (firstName == null && lastName != null) {
                values.put("!last_name", lastName);
            } else {
                values.put("!first_name", firstName);
                values.put("!last_name", lastName);
            }

            try {
                sendEvent(ACTION_TYPE.identify, null, TongDaoDataTool.makeUserProperties(values));
            } catch (JSONException e) {
                Log.e("identifyFullName", "JSONException");
            }
        }
    }

    /**
     * 保存用户的推送Token到同道平台，同道将根据用户Token推送信息
     *
     * @param push_token 用户的推送Token
     *                   (百度:onBind返回的channelId,
     *                   JPUSH:JPushInterface.EXTRA_REGISTRATION_ID,
     *                   GETUI:bundle.getString("clientid"))
     */
    public static void identifyPushToken(String push_token) {
        if (push_token == null || push_token.trim().equals("")) {
            return;
        } else {
            HashMap<String, Object> values = new HashMap<String, Object>();
            values.put("!push_token", push_token);
            try {
                sendEvent(ACTION_TYPE.identify, null, TongDaoDataTool.makeUserProperties(values));
            } catch (JSONException e) {
                Log.e("identifyPushToken", "JSONException");
            }
        }
    }

    /**
     * 保存用户全名
     *
     * @param fullName 用户全名
     */
    public static void identifyFullName(String fullName) {
        if (fullName == null || fullName.trim().equals("")) {
            return;
        } else {
            HashMap<String, Object> values = new HashMap<String, Object>();
            values.put("!name", fullName);
            try {
                sendEvent(ACTION_TYPE.identify, null, TongDaoDataTool.makeUserProperties(values));
            } catch (JSONException e) {
                Log.e("identifyFullName", "JSONException");
            }
        }
    }

    /**
     * 保存用户应用中的名字
     *
     * @param userName 用户应用中的名字
     */
    public static void identifyUserName(String userName) {
        if (userName == null || userName.trim().equals("")) {
            return;
        } else {
            HashMap<String, Object> values = new HashMap<String, Object>();
            values.put("!username", userName);
            try {
                sendEvent(ACTION_TYPE.identify, null, TongDaoDataTool.makeUserProperties(values));
            } catch (JSONException e) {
                Log.e("identifyUserName", "JSONException");
            }
        }
    }

    /**
     * 保存用户邮件地址
     *
     * @param email 用户邮件地址
     */
    public static void identifyEmail(String email) {
        if (email == null || email.trim().equals("")) {
            return;
        } else {
            HashMap<String, Object> values = new HashMap<String, Object>();
            values.put("!email", email);
            try {
                sendEvent(ACTION_TYPE.identify, null, TongDaoDataTool.makeUserProperties(values));
            } catch (JSONException e) {
                Log.e("identifyEmail", "JSONException");
            }
        }
    }

    /**
     * 保存用户电话号码
     *
     * @param phoneNumber 用户电话号码
     */
    public static void identifyPhone(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().equals("")) {
            return;
        } else {
            HashMap<String, Object> values = new HashMap<String, Object>();
            values.put("!phone", phoneNumber);
            try {
                sendEvent(ACTION_TYPE.identify, null, TongDaoDataTool.makeUserProperties(values));
            } catch (JSONException e) {
                Log.e("identifyPhone", "JSONException");
            }
        }
    }

    /**
     * 保存用户性别
     *
     * @param gender 用户性别(枚举类型)
     */
    public static void identifyGender(TdGender gender) {
        if (gender == null) {
            return;
        } else {
            HashMap<String, Object> values = new HashMap<String, Object>();
            values.put("!gender", gender.toString());
            try {
                sendEvent(ACTION_TYPE.identify, null, TongDaoDataTool.makeUserProperties(values));
            } catch (JSONException e) {
                Log.e("identifyGender", "JSONException");
            }
        }
    }

    /**
     * 保存用户年龄
     *
     * @param age 用户年龄
     */
    public static void identifyAge(int age) {
        if (age > 0) {
            HashMap<String, Object> values = new HashMap<String, Object>();
            values.put("!age", age);
            try {
                sendEvent(ACTION_TYPE.identify, null, TongDaoDataTool.makeUserProperties(values));
            } catch (JSONException e) {
                Log.e("identifyAge", "JSONException");
            }
        }
    }

    /**
     * 保存用户头像链接地址
     *
     * @param url 用户头像链接地址
     */
    public static void identifyAvatar(String url) {
        if (url == null || url.trim().equals("")) {
            return;
        } else {
            HashMap<String, Object> values = new HashMap<String, Object>();
            values.put("!avatar", url);
            try {
                sendEvent(ACTION_TYPE.identify, null, TongDaoDataTool.makeUserProperties(values));
            } catch (JSONException e) {
                Log.e("identifyAvatar", "JSONException");
            }
        }
    }

    /**
     * 保存用户联系地址
     *
     * @param address 用户联系地址
     */
    public static void identifyAddress(String address) {
        if (address == null || address.trim().equals("")) {
            return;
        } else {
            HashMap<String, Object> values = new HashMap<String, Object>();
            values.put("!address", address);
            try {
                sendEvent(ACTION_TYPE.identify, null, TongDaoDataTool.makeUserProperties(values));
            } catch (JSONException e) {
                Log.e("identifyAddress", "JSONException");
            }
        }
    }

    /**
     * 保存用户出生日期
     *
     * @param date 用户出生日期
     */
    public static void identifyBirthday(Date date) {
        if (date != null) {
            HashMap<String, Object> values = new HashMap<String, Object>();
            values.put("!birthday", TongDaoCheckTool.getTimeStamp(date));
            try {
                sendEvent(ACTION_TYPE.identify, null, TongDaoDataTool.makeUserProperties(values));
            } catch (JSONException e) {
                Log.e("identifyBirthday", "JSONException");
            }
        }
    }

    /**
     * 保存用户应用源信息
     *
     * @param lqSource 用户应用源信息对象
     */
    public static void identifySource(TdSource lqSource) {
        try {
            JSONObject dataObj = TongDaoDataTool.makeSourceProperties(lqSource);
            if (dataObj != null) {
                sendEvent(ACTION_TYPE.identify, null, dataObj);
            }
        } catch (JSONException e) {
            Log.e("identifySource", "JSONException");
        }
    }

    /**
     * 跟踪应用注册日期
     *
     * @param date 用户设置的日期对象
     */
    public static void trackRegistration(Date date) {
        try {
            sendEvent(ACTION_TYPE.track, "!registration", TongDaoDataTool.makeRegisterProperties(date));
        } catch (JSONException e) {
            Log.e("trackRegistration", "JSONException");
        }
    }

    /**
     * 跟踪应用注册日期(使用当前系统时间,无日期参数)
     */
    public static void trackRegistration() {
        trackRegistration(null);
    }

    /**
     * 跟踪应用评分
     *
     * @param rating 应用评分
     */
    public static void identifyRating(int rating) {
        try {
            sendEvent(ACTION_TYPE.identify, null, TongDaoDataTool.makeRatingProperties(rating));
        } catch (JSONException e) {
            Log.e("trackRate", "JSONException");
        }
    }

    /**
     * 跟踪浏览商品类别
     *
     * @param category 商品类别
     */
    public static void trackViewProductCategory(String category) {
        if (category != null && !category.trim().equals("")) {
            HashMap<String, Object> values = new HashMap<String, Object>();
            values.put("!category", category);
            try {
                sendEvent(ACTION_TYPE.track, "!view_product_category", TongDaoDataTool.makeUserProperties(values));
            } catch (JSONException e) {
                Log.e("trackViewProductCategory", "JSONException");
            }
        }
    }

    /**
     * 跟踪浏览商品信息
     *
     * @param product 商品信息
     */
    public static void trackViewProduct(TdProduct product) {
        try {
            JSONObject dataObj = TongDaoDataTool.makeProductProperties(product);
            if (dataObj != null) {
                sendEvent(ACTION_TYPE.track, "!view_product", dataObj);
            }
        } catch (JSONException e) {
            Log.e("trackViewProducts", "JSONException");
        }
    }

    /**
     * 跟踪添加多个订单到购物车
     *
     * @param orderLines 订单列表
     */
    public static void trackAddCart(ArrayList<TdOrderLine> orderLines) {
        try {
            JSONObject dataObj = TongDaoDataTool.makeOrderLinesProperties(orderLines);
            if (dataObj != null) {
                sendEvent(ACTION_TYPE.track, "!add_cart", dataObj);
            }
        } catch (JSONException e) {
            Log.e("trackAddCart", "JSONException");
        }
    }

    /**
     * 跟踪添加单个订单到购物车
     *
     * @param orderLine 单个订单
     */
    public static void trackAddCart(TdOrderLine orderLine) {
        if (orderLine != null) {
            ArrayList<TdOrderLine> orderLines = new ArrayList<TdOrderLine>();
            orderLines.add(orderLine);
            trackAddCart(orderLines);
        }
    }

    /**
     * 跟踪从购物车删除多个订单
     *
     * @param orderLines 订单列表
     */
    public static void trackRemoveCart(ArrayList<TdOrderLine> orderLines) {
        try {
            JSONObject dataObj = TongDaoDataTool.makeOrderLinesProperties(orderLines);
            if (dataObj != null) {
                sendEvent(ACTION_TYPE.track, "!remove_cart", dataObj);
            }
        } catch (JSONException e) {
            Log.e("trackRemoveCart", "JSONException");
        }
    }

    /**
     * 跟踪从购物车删除单个订单
     *
     * @param orderLine 单个订单
     */
    public static void trackRemoveCart(TdOrderLine orderLine) {
        if (orderLine != null) {
            ArrayList<TdOrderLine> orderLines = new ArrayList<TdOrderLine>();
            orderLines.add(orderLine);
            trackRemoveCart(orderLines);
        }
    }

    /**
     * 跟踪提交的交易信息
     *
     * @param order 交易信息
     */
    public static void trackPlaceOrder(TdOrder order) {
        try {
            JSONObject dataObj = TongDaoDataTool.makeOrderProperties(order);
            if (dataObj != null) {
                sendEvent(ACTION_TYPE.track, "!place_order", dataObj);
            }
        } catch (JSONException e) {
            Log.e("trackPlaceOrder", "JSONException");
        }
    }


    /**
     * 跟踪提交的交易信息
     *
     * @param name     产品名称
     * @param price    产品单价
     * @param currency 产品货币
     * @param quantity 产品个数
     */
    public static void trackPlaceOrder(String name, float price, Currency currency, int quantity) {
        if (name == null || name.trim().equals("") || currency == null || price <= 0 || quantity <= 0) {
            return;
        }

        try {
            TdProduct tempProduct = new TdProduct(null, null, name, price, currency, null);
            TdOrderLine tempTdOrderLine = new TdOrderLine(tempProduct, quantity);
            ArrayList<TdOrderLine> orderLines = new ArrayList<TdOrderLine>();
            orderLines.add(tempTdOrderLine);

            TdOrder order = new TdOrder();
            order.setTotal(Float.valueOf(price * quantity));
            order.setCurrency(currency);
            order.setOrderLines(orderLines);

            Log.i("PlaceOrder", "" + order.getTotal());

            JSONObject dataObj = TongDaoDataTool.makeOrderProperties(order);
            if (dataObj != null) {
                sendEvent(ACTION_TYPE.track, "!place_order", dataObj);
            }
        } catch (JSONException e) {
            Log.e("trackPlaceOrder", "JSONException");
        }
    }

    /**
     * 跟踪提交的交易信息，产品个数默认为1
     *
     * @param name     产品名称
     * @param price    产品单价
     * @param currency 产品货币
     */
    public static void trackPlaceOrder(String name, float price, Currency currency) {
        trackPlaceOrder(name, price, currency, 1);
    }

    private static void sendEvent(ACTION_TYPE action, String event, JSONObject properties) {
        String userId = null;
        if (lingQianBridge != null && (userId = lingQianBridge.getUserId()) != null) {
            TdEventBean tempEb = new TdEventBean(action, userId, event, properties);
            lingQianBridge.startTrackEvents(tempEb);
        }
    }

    /**
     * 下载应用广告页面信息
     *
     * @param pageId                        广告页面的pageId
     * @param onDownloadLandingPageListener 下载成功的回调接口函数，可以得到广告页面信息
     * @param onErrorListener               下载失败的回调接口函数
     */
    public static void downloadLandingPage(String pageId, OnDownloadLandingPageListener onDownloadLandingPageListener, OnErrorListener onErrorListener) {
        if (lingQianBridge != null) {
            lingQianBridge.startDownloadLandingPage(pageId, onDownloadLandingPageListener, onErrorListener);
        }
    }

    /**
     * 下载应用In App Message页面信息
     *
     * @param onDownloadInAppMessageListener 下载成功的回调接口函数，可以得到In App Message列表
     * @param onErrorListener                下载失败的回调接口函数
     */
    public static void downloadInAppMessages(OnDownloadInAppMessageListener onDownloadInAppMessageListener, OnErrorListener onErrorListener) {
        if (lingQianBridge != null) {
            lingQianBridge.startDownloadInAppMessages(onDownloadInAppMessageListener, onErrorListener);
        }
    }

    /**
     * 通过包含Deeplink的Intent取得广告的PageId
     *
     * @param deepLinkIntent 包含Deeplink的Intent
     * @return String 广告的PageId
     */
    public static String getPageId(Intent deepLinkIntent) {
        if (deepLinkIntent != null && deepLinkIntent.getDataString() != null) {
            String deeplink = deepLinkIntent.getDataString();
            int indexOfPage = deeplink.indexOf(ENDING_PAGE_STRING);
            if (indexOfPage != -1) {
                return getDataId(deeplink);
            }
        }
        return null;
    }

    private static String getDataId(String deeplink) {
        int lastIndexS = deeplink.lastIndexOf("=");
        String id = deeplink.substring(lastIndexS + 1);
        return id;
    }

    /**
     * 跟踪用户打开了推送消息(已过时，请使用trackOpenPushMessage)
     *
     * @param extraData 推送消息的附加信息
     */
    public static void trackOpenMessage(String extraData) {
        if (extraData == null) {
            return;
        }

        try {
            JSONObject messageObj = new JSONObject(extraData);
            long messageId = messageObj.optLong(MESSAGE_TAG);
            long clientId = messageObj.optLong(CLIENT_TAG);
            if (messageId != 0 && clientId != 0) {
                sendOpenMessage("!open_message", messageId, clientId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("trackOpenMessage", "JSONException");
        }
    }

    /**
     * 跟踪用户打开了推送消息
     *
     * @param extraData 推送消息的附加信息
     */
    public static void trackOpenPushMessage(String extraData) {
        trackOpenMessage(extraData);
    }

    /**
     * 使用推送消息的附加信息打开应用，链接或网页
     *
     * @param extraData 推送消息的附加信息
     */
    public static void openPage(Context appcontext, String extraData) {
        if (extraData == null) {
            return;
        }

        try {
            JSONObject messageObj = new JSONObject(extraData);

            String type = TongDaoJsonTool.optJsonString(messageObj, TYPE_TAG);
            String value = TongDaoJsonTool.optJsonString(messageObj, VALUE_TAG);
            if (type == null || value == null) {
                return;
            }

            if (type.equalsIgnoreCase("deeplink")) {
                Intent deepLinkIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(value));
                deepLinkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (isIntentCallable(appcontext, deepLinkIntent)) {
                    appcontext.startActivity(deepLinkIntent);
                }
            } else if (type.equalsIgnoreCase("url")) {
                Intent viewIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(value));
                viewIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                appcontext.startActivity(viewIntent);
            } else if (type.equalsIgnoreCase("app")) {
                boolean isExist = isAppExist(appcontext, value);
                if (isExist) {
                    startApkByPackageName(appcontext, value);
                }
            }
        } catch (JSONException e) {
            Log.e("openPage", "JSONException");
        }
    }

    private static boolean isIntentCallable(Context context, Intent intent) {
        return context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }

    private static boolean isAppExist(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (PackageInfo eachPackageInfo : pinfo) {
                if (eachPackageInfo.packageName != null && eachPackageInfo.packageName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void startApkByPackageName(Context context, String packageName) {
        Intent appIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (appIntent != null) {
            appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(appIntent);
        }
    }

    private static void sendOpenMessage(String eventName, long mid, long cid) throws JSONException {
        HashMap<String, Object> values = new HashMap<String, Object>();
        values.put("!message_id", mid);
        values.put("!campaign_id", cid);
        sendEvent(ACTION_TYPE.track, eventName, TongDaoDataTool.makeUserProperties(values));
    }

    /**
     * 跟踪用户点击了应用内消息
     *
     * @param tdMessageBean 同道返回的TdMessageBean
     */
    public static void trackOpenInAppMessage(TdMessageBean tdMessageBean) {
        if (tdMessageBean == null) {
            return;
        }

        try {
            long messageId = tdMessageBean.getMid();
            long clientId = tdMessageBean.getCid();
            if (messageId != 0 && clientId != 0) {
                sendOpenMessage("!open_message", messageId, clientId);
            }
        } catch (JSONException e) {
            Log.e("trackOpenInAppMessage", "JSONException");
        }
    }


    /**
     * 跟踪用户收到了应用内消息
     *
     * @param tdMessageBean 同道返回的TdMessageBean
     */
    public static void trackReceivedInAppMessage(TdMessageBean tdMessageBean) {
        if (tdMessageBean == null) {
            return;
        }

        try {
            long messageId = tdMessageBean.getMid();
            long clientId = tdMessageBean.getCid();
            if (messageId != 0 && clientId != 0) {
                sendOpenMessage("!receive_message", messageId, clientId);
            }
        } catch (JSONException e) {
            Log.e("trackReceiovedInAppMsg", "JSONException");
        }
    }

    public static void registerApplication(Application application) {
        application.registerActivityLifecycleCallbacks(new TongDaoActivityCallback(application.getApplicationContext()));
    }
}
