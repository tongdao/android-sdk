package com.tongdao.sdk.tools;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

import com.tongdao.sdk.beans.TdOrder;
import com.tongdao.sdk.beans.TdOrderLine;
import com.tongdao.sdk.beans.TdProduct;
import com.tongdao.sdk.beans.TdSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class TongDaoDataTool {

    public JSONObject makeInfoProperties(@NonNull Context appContext, String gaid) throws JSONException {
        TongDaoAppInfoTool appInfoTool = new TongDaoAppInfoTool();
        TongDaoSavingTool savingTool = new TongDaoSavingTool();
        JSONObject propertiesObj = new JSONObject();
        //application
        Object[] versioObjs = appInfoTool.getVersionCodeOsName(appContext);
        if (versioObjs != null) {
            JSONObject applicationObj = new JSONObject();
            applicationObj.put("!version_code", (int) versioObjs[0]);
            applicationObj.put("!version_name", versioObjs[1] == null ? "" : (String) (versioObjs[1]));

            String appData = savingTool.getApplicationInfoData(appContext);
            if (appData == null || !appData.equalsIgnoreCase(applicationObj.toString())) {
                propertiesObj.put("!application", applicationObj);
                savingTool.setApplicationInfoData(appContext, applicationObj.toString());
            }
        }

        //connection
        Object[] networkObjs = appInfoTool.getNetworkInfo(appContext);
        JSONObject connectionObj = new JSONObject();
        connectionObj.put("!carrier_name", networkObjs[2]);
        if (networkObjs[3] instanceof Integer) {
            connectionObj.put("!carrier_code", (int) networkObjs[3]);
        } else {
            connectionObj.put("!carrier_code", networkObjs[3]);
        }

        connectionObj.put("!connection_type", networkObjs[0]);
        connectionObj.put("!connection_quality", networkObjs[1]);

        String connectionData = savingTool.getConnectionInfoData(appContext);
        if (connectionData == null || !connectionData.equalsIgnoreCase(connectionObj.toString())) {
            propertiesObj.put("!connection", connectionObj);
            savingTool.setConnectionInfoData(appContext, connectionObj.toString());
        }

        //location

        Object[] locationObjs = appInfoTool.getCurrentLocation(appContext);
        JSONObject locationObj = new JSONObject();
        locationObj.put("!latitude", (double) locationObjs[0]);
        locationObj.put("!longitude", (double) locationObjs[1]);
        locationObj.put("!source", locationObjs[2]);

        String locationData = savingTool.getLocationInfoData(appContext);
        if (locationData == null || !locationData.equalsIgnoreCase(locationObj.toString())) {
            propertiesObj.put("!location", locationObj);
            savingTool.setLocationInfoData(appContext, locationObj.toString());
        }

        //device
        Object[] devicesObjs = appInfoTool.getDeviceInfo();
        JSONObject deviceObj = new JSONObject();
        deviceObj.put("!model", devicesObjs[0] == null ? "" : (String) (devicesObjs[0]));
        deviceObj.put("!brand", devicesObjs[1] == null ? "" : (String) (devicesObjs[1]));
        deviceObj.put("!product_id", devicesObjs[2] == null ? "" : (String) (devicesObjs[2]));
        deviceObj.put("!build_id", devicesObjs[3] == null ? "" : (String) (devicesObjs[3]));
        deviceObj.put("!device_name", devicesObjs[4] == null ? "" : (String) (devicesObjs[4]));
        deviceObj.put("!os_name", devicesObjs[5] == null ? "" : (String) (devicesObjs[5]));
        deviceObj.put("!os_version", devicesObjs[6]);
        deviceObj.put("!language", devicesObjs[7]);
        //w and h
        DisplayMetrics dm = appContext.getResources().getDisplayMetrics();
        int resolution_width = dm.widthPixels;
        int resolution_height = dm.heightPixels;
        deviceObj.put("!width", resolution_width);
        deviceObj.put("!height", resolution_height);

        String deviceData = savingTool.getDeviceInfoData(appContext);
        if (deviceData == null || !deviceData.equalsIgnoreCase(deviceObj.toString())) {
            propertiesObj.put("!device", deviceObj);
            savingTool.setDeviceInfoData(appContext, deviceObj.toString());
        }

        //fingerprint
        JSONObject fingerprintObj = new JSONObject();
        appInfoTool.getImeiInfos(appContext, fingerprintObj);
        String[] macInfos = appInfoTool.getMacInfos();
        fingerprintObj.put("!mac", macInfos[0]);
        fingerprintObj.put("!mac_md5", macInfos[1]);
        fingerprintObj.put("!mac_sha1", macInfos[2]);

        String[] udidInfos = appInfoTool.getUdidInfos(appContext);
        fingerprintObj.put("!udid", udidInfos[0]);
        fingerprintObj.put("!udid_md5", udidInfos[1]);
        fingerprintObj.put("!udid_sha1", udidInfos[2]);

        if (gaid != null) {
            fingerprintObj.put("!gaid", gaid);
        }

        String fingerPrintInfoData = savingTool.getFingerprintInfoData(appContext);
        if (fingerPrintInfoData == null || !fingerPrintInfoData.equalsIgnoreCase(fingerprintObj.toString())) {
            propertiesObj.put("!fingerprint", fingerprintObj);
            savingTool.setFingerprintInfoData(appContext, fingerprintObj.toString());
        }

        boolean anonymousSet = savingTool.getAnonymousSet(appContext);
        boolean mAnonymous = savingTool.getAnonymous(appContext);
        if (!anonymousSet) {
            propertiesObj.put("!anonymous", mAnonymous);
            savingTool.isAnonymousSet(appContext);
            savingTool.setAnonymous(appContext, mAnonymous);
        } else {
            boolean anonymousData = savingTool.getAnonymous(appContext);
            if (mAnonymous != anonymousData) {
                propertiesObj.put("!anonymous", mAnonymous);
                savingTool.setAnonymous(appContext, mAnonymous);
            }
        }

        System.out.println("Device object info->" + propertiesObj.toString());
        return propertiesObj;
    }

    public JSONObject makeRatingProperties(int rating) throws JSONException {
        JSONObject appObj = new JSONObject();
        appObj.put("!rating", rating);
        JSONObject propertiesObj = new JSONObject();
        propertiesObj.put("!application", appObj);
        return propertiesObj;
    }

    public JSONObject makeUserProperties(HashMap<String, Object> values) throws JSONException {
        if (values == null || values.isEmpty()) {
            return null;
        }

        JSONObject propertiesObj = new JSONObject();
        Iterator<Entry<String, Object>> keydatasIter = values.entrySet().iterator();
        while (keydatasIter.hasNext()) {
            Entry<String, Object> tempKeyValueEntry = keydatasIter.next();
            propertiesObj.put(tempKeyValueEntry.getKey(), tempKeyValueEntry.getValue());
        }

        return propertiesObj;
    }

    public JSONObject makeSourceProperties(TdSource lqSource) throws JSONException {
        if (lqSource == null) {
            return null;
        }

        if (lqSource.getAppStore() == null && lqSource.getAdvertisementId() == null && lqSource.getAdvertisementGroupId() == null && lqSource.getCampaignId() == null && lqSource.getSourceId() == null) {
            return null;
        }

        JSONObject sourceObj = new JSONObject();
        if (lqSource.getAppStore() != null) {
            sourceObj.put("!appstore_id", lqSource.getAppStore().name());
        }

        if (lqSource.getAdvertisementId() != null) {
            sourceObj.put("!ad_id", lqSource.getAdvertisementId());
        }

        if (lqSource.getAdvertisementGroupId() != null) {
            sourceObj.put("!adgroup_id", lqSource.getAdvertisementGroupId());
        }

        if (lqSource.getCampaignId() != null) {
            sourceObj.put("!campaign_id", lqSource.getCampaignId());
        }

        if (lqSource.getSourceId() != null) {
            sourceObj.put("!source_id", lqSource.getSourceId());
        }

        JSONObject propertiesObj = new JSONObject();

        propertiesObj.put("!source", sourceObj);

        return propertiesObj;
    }

    public JSONObject makeRegisterProperties(Date date) throws JSONException {
        String timeString;
        if (date == null) {
            timeString = TongDaoCheckTool.getTimeStamp(TongDaoClockTool.currentTimeMillis());
        } else {
            timeString = TongDaoCheckTool.getTimeStamp(date);
        }

        JSONObject propertiesObj = new JSONObject();
        propertiesObj.put("!register_at", timeString);
        return propertiesObj;
    }


    public JSONObject makeProductProperties(TdProduct product) throws JSONException {
        if (product == null) {
            return null;
        }

        JSONObject productObj = null;

        if (product.getName() != null && product.getCurrency() != null) {
            productObj = new JSONObject();

            if (product.getId() != null) {
                productObj.put("!id", product.getId());
            }

            if (product.getSku() != null) {
                productObj.put("!sku", product.getSku());
            }

            if (product.getName() != null) {
                productObj.put("!name", product.getName());
            }

            productObj.put("!price", Float.valueOf(product.getPrice()));

            if (product.getCurrency() != null) {
                productObj.put("!currency", product.getCurrency().getCurrencyCode());
            }

            if (product.getCategory() != null) {
                productObj.put("!category", product.getCategory());
            }
        }

        return productObj;
    }

    public JSONArray makeOrderLinesArrayProperties(ArrayList<TdOrderLine> orderLines) throws JSONException {
        if (orderLines == null || orderLines.isEmpty()) {
            return null;
        }

        JSONArray tempOrderLinesArray = new JSONArray();
        for (TdOrderLine eachLqOrderLine : orderLines) {
            TdProduct tempProduct = eachLqOrderLine.getProduct();
            int quantity = eachLqOrderLine.getQuantity();
            if (tempProduct != null && quantity > 0) {
                JSONObject tempProductObj = makeProductProperties(tempProduct);
                if (tempProductObj != null) {
                    JSONObject tempOrderLine = new JSONObject();
                    tempOrderLine.put("!product", tempProductObj);
                    tempOrderLine.put("!quantity", quantity);
                    //add to jsonArray
                    tempOrderLinesArray.put(tempOrderLine);
                }
            }
        }

        if (tempOrderLinesArray.length() == 0) {
            return null;
        }

        return tempOrderLinesArray;
    }

    public JSONObject makeOrderLinesProperties(ArrayList<TdOrderLine> orderLines) throws JSONException {
        JSONArray tempOrderLinesArray = makeOrderLinesArrayProperties(orderLines);
        JSONObject propertiesObj = null;
        if (tempOrderLinesArray != null) {
            propertiesObj = new JSONObject();
            propertiesObj.put("!order_lines", tempOrderLinesArray);
        }

        return propertiesObj;
    }


    public JSONObject makeOrderProperties(TdOrder order) throws JSONException {
        if (order == null) {
            return null;
        }

        String orderId = order.getOrderId();
        String couponId = order.getCouponId();
        Currency currency = order.getCurrency();
        ArrayList<TdOrderLine> orderlines = order.getOrderLines();

        if (currency == null || order.getTotal() <= 0) {
            return null;
        }


        JSONObject propertiesObj = new JSONObject();

        if (orderId != null) {
            propertiesObj.put("!order_id", orderId);
        }

        propertiesObj.put("!total", Float.valueOf(order.getTotal()));
        propertiesObj.put("!revenue", order.getRevenue());
        propertiesObj.put("!shipping", order.getShipping());
        propertiesObj.put("!tax", order.getTax());
        propertiesObj.put("!discount", order.getDiscount());

        if (couponId != null) {
            propertiesObj.put("!coupon_id", couponId);
        }

        if (currency != null) {
            propertiesObj.put("!currency", currency.getCurrencyCode());
        }

        JSONArray orderlinesArray = makeOrderLinesArrayProperties(orderlines);

        if (orderlinesArray != null) {
            propertiesObj.put("!order_lines", orderlinesArray);
        }

        Log.i("PlaceOrder from JSON->", "" + propertiesObj.get("!total"));

        return propertiesObj;
    }
}
