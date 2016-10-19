package com.tongdao.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tongdao.sdk.beans.TdRewardBean;

import android.content.Context;
import android.net.Uri;

public class DataTool {
//    public static final String APP_KEY = "de89454e930e2257ddd96d6b4d0f48b5";
//public static final String APP_KEY = "2cc1c88798b3a350d16b6800dc6d3612";
    public static final String APP_KEY = "ee7b9cf0a11d3cc6762097a28f1a6886";
//    public static final String APP_KEY = "bc4d604fdc143433683af34b5693f444";
    public static final String BAIDU_API_KEY = "QGLHVuaavhBgygVCNVlDyRYB";
    private static ArrayList<TransferBean> transferBeanList;
    private static ArrayList<TransferRewardBean> transferRewardBeanList;

    public static void addNewBean(TransferBean newTransferBean) {
        transferBeanList.add(newTransferBean);
    }

    public static void deleteBean(int index) {
        transferBeanList.remove(index);
    }

    public static ArrayList<TransferBean> getAllBeans() {
        return transferBeanList;
    }


    public static void addNewRewardBean(TransferRewardBean newTransferRewardBean) {
        transferRewardBeanList.add(newTransferRewardBean);
    }

    public static void deleteRewardBean(int index) {
        transferRewardBeanList.remove(index);
    }

    public static ArrayList<TransferRewardBean> getAllRewardBeans() {
        return transferRewardBeanList;
    }

    public static String makeBtnsString() throws JSONException {
        if (transferBeanList.size() > 0) {
            JSONArray jsonArray = new JSONArray();
            for (TransferBean eachTransferBean : transferBeanList) {
                JSONObject outJsonObj = new JSONObject();
                outJsonObj.put("type", eachTransferBean.getType().name());
                outJsonObj.put("btn_name", eachTransferBean.getButtonName());
                outJsonObj.put("event_name", eachTransferBean.getEventName());

                JSONObject innerJsonObj = new JSONObject(eachTransferBean.getDatas());
                outJsonObj.put("datas", innerJsonObj);
                jsonArray.put(outJsonObj);
            }

            return jsonArray.toString();
        } else {
            return null;
        }
    }

    public static String makeRewardsString(ArrayList<TransferRewardBean> rewards) throws JSONException {
        if (rewards.size() > 0) {
            JSONArray jsonArray = new JSONArray();
            for (TransferRewardBean eachTransferRewardBean : rewards) {
                JSONObject outJsonObj = new JSONObject();
                outJsonObj.put("url", eachTransferRewardBean.getPicUri() == null ? null : eachTransferRewardBean.getPicUri().toString());
                outJsonObj.put("name", eachTransferRewardBean.getRewardName());
                outJsonObj.put("sku", eachTransferRewardBean.getRewardSku());
                outJsonObj.put("num", eachTransferRewardBean.getNum());
                jsonArray.put(outJsonObj);
            }

            return jsonArray.toString();
        } else {
            return null;
        }
    }

    public static void initialBtnDatas(String btnJsonString) throws JSONException {
        transferBeanList = new ArrayList<TransferBean>();
        if (btnJsonString != null && !btnJsonString.equals("")) {
            JSONArray tempBtnsJsonArray = new JSONArray(btnJsonString);
            for (int i = 0; i < tempBtnsJsonArray.length(); i++) {
                JSONObject tempJsonObj = (JSONObject) tempBtnsJsonArray.get(i);
                Type type = Enum.valueOf(Type.class, String.valueOf(tempJsonObj.get("type")));
                String buttonName = String.valueOf(tempJsonObj.get("btn_name"));
                String eventName = String.valueOf(tempJsonObj.get("event_name"));

                HashMap<String, Object> datas = new HashMap<String, Object>();
                JSONObject datasJson = tempJsonObj.getJSONObject("datas");
                if (datasJson.length() > 0) {
                    Iterator<String> keys = datasJson.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        datas.put(key, datasJson.get(key));
                    }
                }

                transferBeanList.add(new TransferBean(type, buttonName, eventName, datas));
            }
        }
    }


    public static void initialRewardDatas(String rewardJsonString) throws JSONException {
        transferRewardBeanList = new ArrayList<TransferRewardBean>();
        addTransferRewardBeans(transferRewardBeanList, rewardJsonString);
    }

    private static void addTransferRewardBeans(ArrayList<TransferRewardBean> container, String rewardJsonString) throws JSONException {
        if (rewardJsonString != null && !rewardJsonString.equals("")) {
            JSONArray tempBtnsJsonArray = new JSONArray(rewardJsonString);
            for (int i = 0; i < tempBtnsJsonArray.length(); i++) {
                JSONObject tempJsonObj = (JSONObject) tempBtnsJsonArray.get(i);
                String url = optJsonString(tempJsonObj, "url");
                String name = optJsonString(tempJsonObj, "name");
                String sku = optJsonString(tempJsonObj, "sku");
                int num = tempJsonObj.getInt("num");
                container.add(new TransferRewardBean(url == null ? null : Uri.parse(url), name, sku, num));
            }
        }
    }

    public static void saveTempRewards(Context context, ArrayList<TdRewardBean> rewards) throws JSONException {
        ArrayList<TransferRewardBean> tempList = new ArrayList<TransferRewardBean>();

        addTransferRewardBeans(tempList, TempDataPreference.getRewardJsonString(context));

        for (TdRewardBean eachRewardBean : rewards) {
            boolean isExist = false;
            for (TransferRewardBean eachTransferRewardBean : tempList) {
                if (eachTransferRewardBean.getRewardSku().equals(eachRewardBean.getSku())) {
                    int newNum = eachTransferRewardBean.getNum() + eachRewardBean.getQuantity();
                    eachTransferRewardBean.setRewardName(eachRewardBean.getName());
                    eachTransferRewardBean.setNum(newNum);
                    isExist = true;
                    break;
                }
            }

            if (!isExist) {
                tempList.add(new TransferRewardBean(null, eachRewardBean.getName(), eachRewardBean.getSku(), eachRewardBean.getQuantity()));
            }
        }

        String tempJsonString = makeRewardsString(tempList);
        TempDataPreference.setRewardJsonString(context, tempJsonString);
    }

    public static ArrayList<TransferRewardBean> recoverTempRewards(Context context) throws JSONException {
        ArrayList<TransferRewardBean> tempList = new ArrayList<TransferRewardBean>();
        addTransferRewardBeans(tempList, TempDataPreference.getRewardJsonString(context));
        return tempList;
    }

    private static String optJsonString(JSONObject json, String key) {
        if (json.isNull(key))
            return null;
        else
            return json.optString(key, null);
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

}
