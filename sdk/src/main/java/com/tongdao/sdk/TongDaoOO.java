package com.tongdao.sdk;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.tongdao.sdk.beans.TdErrorBean;
import com.tongdao.sdk.beans.TdEventBean;
import com.tongdao.sdk.beans.TdEventBean.ACTION_TYPE;
import com.tongdao.sdk.beans.TdMessageBean;
import com.tongdao.sdk.beans.TdOrder;
import com.tongdao.sdk.beans.TdOrderLine;
import com.tongdao.sdk.beans.TdProduct;
import com.tongdao.sdk.beans.TdSource;
import com.tongdao.sdk.enums.TdGender;
import com.tongdao.sdk.interfaces.InAppMessageCallback;
import com.tongdao.sdk.interfaces.OnDownloadInAppMessageListener;
import com.tongdao.sdk.interfaces.OnDownloadLandingPageListener;
import com.tongdao.sdk.interfaces.OnErrorListener;
import com.tongdao.sdk.interfaces.OnRewardUnlockedListener;
import com.tongdao.sdk.session.TongDaoActivityCallback;
import com.tongdao.sdk.tools.Log;
import com.tongdao.sdk.tools.TongDaoCheckTool;
import com.tongdao.sdk.tools.TongDaoDataTool;
import com.tongdao.sdk.tools.TongDaoDeviceUuidFactory;
import com.tongdao.sdk.tools.TongDaoJsonTool;
import com.tongdao.sdk.tools.TongDaoSavingTool;
import com.tongdao.sdk.ui.InAppDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Main Class for the Tongdao SDK. All calls to the SDK go through here.
 * Deeplink formatting:
 * scheme://any_deeplink?0Qpage=page_id
 */
public class TongDaoOO {
    //constants
    private static final String PAGE_ID = "pageId";
    private static final String MESSAGE = "td_message";
    private static final String ENDING_PAGE_STRING = "0Qpage=";
    private static final String MESSAGE_TAG = "tongrd_mid";
    private static final String CLIENT_TAG = "tongrd_cid";
    private static final String TYPE_TAG = "tongrd_type";
    private static final String VALUE_TAG = "tongrd_value";

    //SDK components, dependencies
    private OnRewardUnlockedListener rewardUnlockedListener;
    private TongDaoBridge tongDaoBridge;
    private TongDaoDataTool dataTool;
    private TongDaoSavingTool savingTool;
    private TongDaoActivityCallback activityCallback;


    public static TongDaoOO getInstance(Application appContext, String appKey){
        TongDaoOO tongDaoOO = new TongDaoOO();
        tongDaoOO.init(appContext,appKey);
        return tongDaoOO;
    }

    public static TongDaoOO getInstance(Application appContext, String appKey, String userId){
        TongDaoOO tongDaoOO = new TongDaoOO();
        tongDaoOO.init(appContext,appKey,userId);
        return tongDaoOO;
    }

    /**
     * Initialize the Tongdao SDK. Call this from your application's onCreate method.
     *
     * @param appContext Your application object
     * @param appKey     Your AppKey
     * @return boolean   true if the SDK initialized correctly, false otherwise
     */
    private boolean init(Application appContext, String appKey) {
        String deviceId = generateDeviceId(appContext);
        dataTool = new TongDaoDataTool();
        savingTool = new TongDaoSavingTool();
        activityCallback = new TongDaoActivityCallback(appContext,this);
        registerApplication(appContext);
        if (TongDaoCheckTool.isValidKey(appKey) && !TongDaoCheckTool.isEmpty(deviceId)) {
            tongDaoBridge = new TongDaoBridge(appContext, appKey, deviceId, deviceId);
            tongDaoBridge.init();
            onAppSessionStart();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Initialize the Tongdao SDK. Call this from your application's onCreate method.
     *
     * @param appContext Your application object
     * @param appKey     Your AppKey
     * @param userId     The ID of the User
     * @return boolean   true if the SDK initialized correctly, false otherwise
     */
    private boolean init(Application appContext, String appKey, String userId) {
        String deviceId = generateDeviceId(appContext);
        dataTool = new TongDaoDataTool();
        savingTool = new TongDaoSavingTool();
        activityCallback = new TongDaoActivityCallback(appContext,this);
        registerApplication(appContext);
        if(null == userId){
            if (TongDaoCheckTool.isValidKey(appKey) && !TongDaoCheckTool.isEmpty(userId)) {
                tongDaoBridge = new TongDaoBridge(appContext, appKey, deviceId, deviceId);
                savingTool.setAnonymous(appContext, true);
                tongDaoBridge.init();
                onAppSessionStart();
                return true;
            } else {
                return false;
            }
        }else {
            if (TongDaoCheckTool.isValidKey(appKey) && !TongDaoCheckTool.isEmpty(userId)) {
                tongDaoBridge = new TongDaoBridge(appContext, appKey, deviceId, userId, null, false);
                savingTool.setAnonymous(appContext, false);
                tongDaoBridge.init();
                onAppSessionStart();
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Set the user ID
     *
     * @param userId The User ID to be set
     * @return void
     */
    public void setUserId(Context appContext, String userId){
        if(null == userId){
            if(!savingTool.getAnonymous(appContext)){
                savingTool.saveUserInfoData(appContext, generateDeviceId(appContext), generateDeviceId(appContext), true);
                tongDaoBridge.changePropertiesAndUserId(ACTION_TYPE.identify, null, generateDeviceId(appContext));
            }
        }else{
            if(savingTool.getAnonymous(appContext)){
                savingTool.saveUserInfoData(appContext, userId, generateDeviceId(appContext), false);
                tongDaoBridge.changePropertiesAndUserId(ACTION_TYPE.merge, generateDeviceId(appContext), userId);
            }else{
                savingTool.saveUserInfoData(appContext, userId, generateDeviceId(appContext), false);
                tongDaoBridge.changePropertiesAndUserId(ACTION_TYPE.identify, generateDeviceId(appContext), userId);
            }
        }
    }

    /**
     * Set the RewardUnlockedListener for the SDK
     *
     * @param onRewardUnlockedListener Your RewardUnlockedListener
     */
    public void registerOnRewardUnlockedListener(OnRewardUnlockedListener onRewardUnlockedListener) {
        rewardUnlockedListener = onRewardUnlockedListener;
    }

    /**
     * Track custom events
     *
     * @param eventName User-defined event name (cannot start with !)
     * @param values    Hash-Map of values to track (supports Strings and numbers)
     */
    public void track(String eventName, HashMap<String, Object> values) {
        if (eventName == null || eventName.trim().equals("") || eventName.startsWith("!")) {
            Log.e("TongRd SDK", "event starting with ! are reserved");
        } else {
            try {
                sendEvent(ACTION_TYPE.track, eventName, dataTool.makeUserProperties(values));
            } catch (JSONException e) {
                Log.e("track", "JSONException");
            }
        }
    }

    /**
     * Start recording a user session for the Activity.
     *
     * @param activity Activity for which to record the session
     */
    public void onSessionStart(Activity activity) {
        if (tongDaoBridge != null && tongDaoBridge.getUserId() != null && activity != null) {
            tongDaoBridge.onSessionStart(activity);
        }
    }

    /**
     * Start recording a user session for the page with the page name.
     *
     * @param pageName Custom page name for which to record the session
     */
    public void onSessionStart(String pageName) {
        if (tongDaoBridge != null && tongDaoBridge.getUserId() != null && pageName != null) {
            tongDaoBridge.onSessionStart(pageName);
        }
    }

    /**
     * Stop recording the user session for the Activity.
     *
     * @param activity Activity for which to stop recording the session
     */
    public void onSessionEnd(Activity activity) {
        if (tongDaoBridge != null && tongDaoBridge.getUserId() != null && activity != null) {
            tongDaoBridge.onSessionEnd(activity);
        }
    }

    /**
     * Stop recording the user session for the page.
     *
     * @param pageName Custom page name for which to stop recording the session
     */
    public void onSessionEnd(String pageName) {
        if (tongDaoBridge != null && tongDaoBridge.getUserId() != null && pageName != null) {
            tongDaoBridge.onSessionEnd(pageName);
        }
    }

    /**
     * Identify custom user values
     *
     * @param values Hash-Map of custom values (support strings and numbers)
     */
    public void identify(HashMap<String, Object> values) {
        if (values != null && !values.isEmpty()) {
            try {
                sendEvent(ACTION_TYPE.identify, null, dataTool.makeUserProperties(values));
            } catch (JSONException e) {
                Log.e("identify", "JSONException");
            }
        }
    }

    /**
     * Identify a single custom attribute for the user
     *
     * @param name  Custom property name
     * @param value Custom property value (support strings and numbers)
     */
    public void identify(String name, Object value) {
        if (name != null && !name.trim().equals("") && value != null) {
            HashMap<String, Object> values = new HashMap<String, Object>();
            values.put(name, value);
            try {
                sendEvent(ACTION_TYPE.identify, null, dataTool.makeUserProperties(values));
            } catch (JSONException e) {
                Log.e("identify", "JSONException");
            }
        }
    }

    /**
     * Save the user's push token to Tongdao. This allows Tongdao to send push messages based on
     * the token.
     *
     * @param push_token The user's push token
     *                   (Baidu:onBind's method's variable channelId,
     *                   JPUSH:JPushInterface.EXTRA_REGISTRATION_ID,
     *                   GETUI:bundle.getString("clientid",
     *                   etc.))
     */
    public void identifyPushToken(String push_token) {
        if (push_token == null || push_token.trim().equals("")) {
            return;
        } else {
            HashMap<String, Object> values = new HashMap<String, Object>();
            values.put("!push_token", push_token);
            try {
                sendEvent(ACTION_TYPE.identify, null, dataTool.makeUserProperties(values));
            } catch (JSONException e) {
                Log.e("identifyPushToken", "JSONException");
            }
        }
    }

    /**
     * Track user-defined events
     *
     * @param eventName User-defined event name (cannot start with !)
     */
    public void track(String eventName) {
        if (eventName == null || eventName.trim().equals("") || eventName.startsWith("!")) {
            Log.e("TongRd SDK", "event starting with ! are reserved");
        } else {
            sendEvent(ACTION_TYPE.track, eventName, null);
        }
    }

    /**
     * Save the user's full name
     *
     * @param firstName First name
     * @param lastName  Last Name
     */
    public void identifyFullName(String firstName, String lastName) {
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
                sendEvent(ACTION_TYPE.identify, null, dataTool.makeUserProperties(values));
            } catch (JSONException e) {
                Log.e("identifyFullName", "JSONException");
            }
        }
    }

    /**
     * Save the user's full name
     *
     * @param fullName Full name
     */
    public void identifyFullName(String fullName) {
        if (fullName == null || fullName.trim().equals("")) {
            return;
        } else {
            HashMap<String, Object> values = new HashMap<String, Object>();
            values.put("!name", fullName);
            try {
                sendEvent(ACTION_TYPE.identify, null, dataTool.makeUserProperties(values));
            } catch (JSONException e) {
                Log.e("identifyFullName", "JSONException");
            }
        }
    }

    /**
     * Save the user's user name
     *
     * @param userName User name
     */
    public void identifyUserName(String userName) {
        if (userName == null || userName.trim().equals("")) {
            return;
        } else {
            HashMap<String, Object> values = new HashMap<String, Object>();
            values.put("!username", userName);
            try {
                sendEvent(ACTION_TYPE.identify, null, dataTool.makeUserProperties(values));
            } catch (JSONException e) {
                Log.e("identifyUserName", "JSONException");
            }
        }
    }

    /**
     * Save the users e-mail address
     *
     * @param email E-mail address
     */
    public void identifyEmail(String email) {
        if (email == null || email.trim().equals("")) {
            return;
        } else {
            HashMap<String, Object> values = new HashMap<String, Object>();
            values.put("!email", email);
            try {
                sendEvent(ACTION_TYPE.identify, null, dataTool.makeUserProperties(values));
            } catch (JSONException e) {
                Log.e("identifyEmail", "JSONException");
            }
        }
    }

    /**
     * Save the user's phone
     *
     * @param phoneNumber Phone number
     */
    public void identifyPhone(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().equals("")) {
            return;
        } else {
            HashMap<String, Object> values = new HashMap<String, Object>();
            values.put("!phone", phoneNumber);
            try {
                sendEvent(ACTION_TYPE.identify, null, dataTool.makeUserProperties(values));
            } catch (JSONException e) {
                Log.e("identifyPhone", "JSONException");
            }
        }
    }

    /**
     * Save the user's gender
     *
     * @param gender User gender (enum)
     */
    public void identifyGender(TdGender gender) {
        if (gender == null) {
            return;
        } else {
            HashMap<String, Object> values = new HashMap<String, Object>();
            values.put("!gender", gender.toString());
            try {
                sendEvent(ACTION_TYPE.identify, null, dataTool.makeUserProperties(values));
            } catch (JSONException e) {
                Log.e("identifyGender", "JSONException");
            }
        }
    }

    /**
     * Save the user's age
     *
     * @param age Age
     */
    public void identifyAge(int age) {
        if (age > 0) {
            HashMap<String, Object> values = new HashMap<String, Object>();
            values.put("!age", age);
            try {
                sendEvent(ACTION_TYPE.identify, null, dataTool.makeUserProperties(values));
            } catch (JSONException e) {
                Log.e("identifyAge", "JSONException");
            }
        }
    }

    /**
     * Save the user's image link
     *
     * @param url Image URL
     */
    public void identifyAvatar(String url) {
        if (url == null || url.trim().equals("")) {
            return;
        } else {
            HashMap<String, Object> values = new HashMap<String, Object>();
            values.put("!avatar", url);
            try {
                sendEvent(ACTION_TYPE.identify, null, dataTool.makeUserProperties(values));
            } catch (JSONException e) {
                Log.e("identifyAvatar", "JSONException");
            }
        }
    }

    /**
     * Save the user's contact address
     *
     * @param address User's address
     */
    public void identifyAddress(String address) {
        if (address == null || address.trim().equals("")) {
            return;
        } else {
            HashMap<String, Object> values = new HashMap<String, Object>();
            values.put("!address", address);
            try {
                sendEvent(ACTION_TYPE.identify, null, dataTool.makeUserProperties(values));
            } catch (JSONException e) {
                Log.e("identifyAddress", "JSONException");
            }
        }
    }

    /**
     * Save the user's birthday
     *
     * @param date User's birthday
     */
    public void identifyBirthday(Date date) {
        if (date != null) {
            HashMap<String, Object> values = new HashMap<String, Object>();
            values.put("!birthday", TongDaoCheckTool.getTimeStamp(date));
            try {
                sendEvent(ACTION_TYPE.identify, null, dataTool.makeUserProperties(values));
            } catch (JSONException e) {
                Log.e("identifyBirthday", "JSONException");
            }
        }
    }

    /**
     * Save the user's application's information
     *
     * @param source User application information object
     */
    public void identifySource(TdSource source) {
        try {
            JSONObject dataObj = dataTool.makeSourceProperties(source);
            if (dataObj != null) {
                sendEvent(ACTION_TYPE.identify, null, dataObj);
            }
        } catch (JSONException e) {
            Log.e("identifySource", "JSONException");
        }
    }

    /**
     * Track user registration in the app (current date and time)
     */
    public void trackRegistration() {
        trackRegistration(null);
    }

    /**
     * Track user registration in the app
     *
     * @param date Registration date and time
     */
    public void trackRegistration(Date date) {
        try {
            sendEvent(ACTION_TYPE.track, "!registration", dataTool.makeRegisterProperties(date));
        } catch (JSONException e) {
            Log.e("trackRegistration", "JSONException");
        }
    }

    /**
     * Track application rating
     *
     * @param rating Application rating
     */
    public void identifyRating(int rating) {
        try {
            sendEvent(ACTION_TYPE.identify, null, dataTool.makeRatingProperties(rating));
        } catch (JSONException e) {
            Log.e("trackRate", "JSONException");
        }
    }

    /**
     * Track the user viewing a product category
     *
     * @param category Product category
     */
    public void trackViewProductCategory(String category) {
        if (category != null && !category.trim().equals("")) {
            HashMap<String, Object> values = new HashMap<String, Object>();
            values.put("!category", category);
            try {
                sendEvent(ACTION_TYPE.track, "!view_product_category", dataTool.makeUserProperties(values));
            } catch (JSONException e) {
                Log.e("trackViewProductCategory", "JSONException");
            }
        }
    }

    /**
     * Track the user viewing a product
     *
     * @param product Product
     */
    public void trackViewProduct(TdProduct product) {
        try {
            JSONObject dataObj = dataTool.makeProductProperties(product);
            if (dataObj != null) {
                sendEvent(ACTION_TYPE.track, "!view_product", dataObj);
            }
        } catch (JSONException e) {
            Log.e("trackViewProducts", "JSONException");
        }
    }

    /**
     * Track the user adding products to the cart
     *
     * @param orderLines List of products added to the cart
     */
    public void trackAddCart(ArrayList<TdOrderLine> orderLines) {
        try {
            JSONObject dataObj = dataTool.makeOrderLinesProperties(orderLines);
            if (dataObj != null) {
                sendEvent(ACTION_TYPE.track, "!add_cart", dataObj);
            }
        } catch (JSONException e) {
            Log.e("trackAddCart", "JSONException");
        }
    }

    /**
     * Track the user adding one product to the cart
     *
     * @param orderLine Product added to the cart
     */
    public void trackAddCart(TdOrderLine orderLine) {
        if (orderLine != null) {
            ArrayList<TdOrderLine> orderLines = new ArrayList<TdOrderLine>();
            orderLines.add(orderLine);
            trackAddCart(orderLines);
        }
    }

    /**
     * Track the user removing products from the cart
     *
     * @param orderLines List of products removed from cart
     */
    public void trackRemoveCart(ArrayList<TdOrderLine> orderLines) {
        try {
            JSONObject dataObj = dataTool.makeOrderLinesProperties(orderLines);
            if (dataObj != null) {
                sendEvent(ACTION_TYPE.track, "!remove_cart", dataObj);
            }
        } catch (JSONException e) {
            Log.e("trackRemoveCart", "JSONException");
        }
    }

    /**
     * Track the user removing one product from the cart
     *
     * @param orderLine Product removed from the cart
     */
    public void trackRemoveCart(TdOrderLine orderLine) {
        if (orderLine != null) {
            ArrayList<TdOrderLine> orderLines = new ArrayList<TdOrderLine>();
            orderLines.add(orderLine);
            trackRemoveCart(orderLines);
        }
    }

    /**
     * Track the user placing an order
     *
     * @param order Order information
     */
    public void trackPlaceOrder(TdOrder order) {
        try {
            JSONObject dataObj = dataTool.makeOrderProperties(order);
            if (dataObj != null) {
                sendEvent(ACTION_TYPE.track, "!place_order", dataObj);
            }
        } catch (JSONException e) {
            Log.e("trackPlaceOrder", "JSONException");
        }
    }

    /**
     * Track the user placing an order
     *
     * @param name     Product name
     * @param price    Product price
     * @param currency Product currency
     * @param quantity Product quantity
     */
    public void trackPlaceOrder(String name, float price, Currency currency, int quantity) {
        if (name == null || name.trim().equals("") || currency == null || price<=0 || quantity <= 0) {
            return;
        }

        try {
            TdProduct tempProduct = new TdProduct(null, null, name, price, currency, null);
            TdOrderLine tempTdOrderLine = new TdOrderLine(tempProduct, quantity);
            ArrayList<TdOrderLine> orderLines = new ArrayList<TdOrderLine>();
            orderLines.add(tempTdOrderLine);

            float total = new BigDecimal(String.valueOf(price)).multiply(new BigDecimal(String.valueOf(quantity))).floatValue();

            TdOrder order = new TdOrder();
            order.setTotal(total);
            order.setCurrency(currency);
            order.setOrderLines(orderLines);

            Log.i("PlaceOrder", "" + order.getTotal());

            JSONObject dataObj = dataTool.makeOrderProperties(order);
            if (dataObj != null) {
                sendEvent(ACTION_TYPE.track, "!place_order", dataObj);
            }
        } catch (JSONException e) {
            Log.e("trackPlaceOrder", "JSONException");
        }
    }

    /**
     * Track the user placing an order for quantity of 1
     *
     * @param name     Product name
     * @param price    Product price
     * @param currency Product currency
     */
    public void trackPlaceOrder(String name, float price, Currency currency) {
        trackPlaceOrder(name, price, currency, 1);
    }


    /**
     * Fetch and display any pending in-app message
     *
     * @param activity The Activity in which to display the message
     */
    public void displayInAppMessage(final AppCompatActivity activity) {
        downloadInAppMessages(new OnDownloadInAppMessageListener() {
            @Override
            public void onSuccess(final ArrayList<TdMessageBean> tdMessageBeanList) {
                if (tdMessageBeanList.size() > 0 && activity != null && !activity.isFinishing()) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            InAppDialog inAppDialog = InAppDialog.newInstance(tdMessageBeanList.get(0), new InAppMessageCallback() {
                                @Override
                                public void callbackTrackOpenInAppMessage(TdMessageBean tdMessageBean) {
                                    trackOpenInAppMessage(tdMessageBean);
                                }

                                @Override
                                public void callbackTrackReceivedInAppMessage(TdMessageBean tdMessageBean) {
                                    trackReceivedInAppMessage(tdMessageBean);
                                }
                            });
                            inAppDialog.show(activity.getSupportFragmentManager(),"frg_inapp");
                        }
                    });
                }
            }
        }, new OnErrorListener() {
            @Override
            public void onError(final TdErrorBean errorBean) {
                if (activity != null && !activity.isFinishing()) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity.getApplicationContext(), "" + errorBean.getErrorCode() + ":" + errorBean.getErrorMsg(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    /**
     * Track the opening of a push message
     *
     * @param extraData Push message additional data (sent through Tongdao SDK Push integration)
     */
    public void trackOpenPushMessage(String extraData) {
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
     * Handle opening an app, deeplink or URL from the push message extra
     *
     * @param extraData Extra push message information (sent through Tongdao SDK Push integration)
     */
    public void openPage(Context appcontext, String extraData) {
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

    /**
     * Track the opening of an in-app message
     *
     * @param tdMessageBean TdMessageBean received from Tongdao
     */
    public void trackOpenInAppMessage(TdMessageBean tdMessageBean) {
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
     * Track that the user received an in-app message
     *
     * @param tdMessageBean TdMessageBean received from Tongdao
     */
    public void trackReceivedInAppMessage(TdMessageBean tdMessageBean) {
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

    /**
     * Use the Tongdao SDK to generate a User ID linked to the device
     *
     * @param appContext Application Context
     * @return String Generated User ID
     */
    public String generateDeviceId(Context appContext) {
        try {
            return TongDaoDeviceUuidFactory.getDeviceUuid(appContext).toString();
        } catch (UnsupportedEncodingException e) {
            Log.e("TongRd SDK", "UnsupportedEncodingException");
        }
        return null;
    }

    public void onAppSessionStart() {
        Log.e("session event track bf", "Start" + 0);
        if (tongDaoBridge != null && tongDaoBridge.getUserId() != null) {
            Log.e("session event track", "Start" + 0);
            tongDaoBridge.onAppSessionStart();
        }
    }

    public void onAppSessionEnd() {
        Log.e("session event track bf", "Emd" + 0);
        if (tongDaoBridge != null && tongDaoBridge.getUserId() != null) {
            Log.e("session event track", "Emd" + 0);
            tongDaoBridge.onAppSessionEnd();
        }
    }

    private void sendEvent(ACTION_TYPE action, String event, JSONObject properties) {
        String userId = null;
        if (tongDaoBridge != null && (userId = tongDaoBridge.getUserId()) != null) {
            TdEventBean tempEb = new TdEventBean(action, userId, event, properties);
            tongDaoBridge.startTrackEvents(tempEb);
        }
    }


    private String getDataId(String deeplink) {
        int lastIndexS = deeplink.lastIndexOf("=");
        String id = deeplink.substring(lastIndexS + 1);
        return id;
    }



    /**
     * 下载应用In App Message页面信息
     *
     * @param onDownloadInAppMessageListener 下载成功的回调接口函数，可以得到In App Message列表
     * @param onErrorListener                下载失败的回调接口函数
     */
    public void downloadInAppMessages(OnDownloadInAppMessageListener onDownloadInAppMessageListener, OnErrorListener onErrorListener) {
        if (tongDaoBridge != null) {
            tongDaoBridge.startDownloadInAppMessages(onDownloadInAppMessageListener, onErrorListener);
        }
    }

    private void sendOpenMessage(String eventName, long mid, long cid) throws JSONException {
        HashMap<String, Object> values = new HashMap<String, Object>();
        values.put("!message_id", mid);
        values.put("!campaign_id", cid);
        sendEvent(ACTION_TYPE.track, eventName, dataTool.makeUserProperties(values));
    }

    private boolean isIntentCallable(Context context, Intent intent) {
        return context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }

    private boolean isAppExist(Context context, String packageName) {
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

    private void startApkByPackageName(Context context, String packageName) {
        Intent appIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (appIntent != null) {
            appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(appIntent);
        }
    }

    public void registerApplication(Application application) {
        application.registerActivityLifecycleCallbacks(activityCallback);
    }

    public void trackEvent() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if( tongDaoBridge != null ) {
                    tongDaoBridge.trackIdentify();
                }
            }
        }).start();
    }

    /**
     * 下载应用广告页面信息
     *
     * @param pageId                        广告页面的pageId
     * @param onDownloadLandingPageListener 下载成功的回调接口函数，可以得到广告页面信息
     * @param onErrorListener               下载失败的回调接口函数
     */
    public void downloadLandingPage(String pageId, OnDownloadLandingPageListener onDownloadLandingPageListener, OnErrorListener onErrorListener) {
        if (tongDaoBridge != null) {
            tongDaoBridge.startDownloadLandingPage(pageId, onDownloadLandingPageListener, onErrorListener);
        }
    }

    /**
     * Enable tracking for activity sessions
     */
    public void startTrackingActivitySessions(){
        activityCallback.setActivityTracking(true);
    }

    /**
     * Disable tracking for activity sessions
     */
    public void stopTrackingActivitySessions(){
        activityCallback.setActivityTracking(false);
    }
}
