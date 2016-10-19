package com.tongdao.newsdk.tools;

import android.annotation.SuppressLint;
import android.util.Base64;

import com.tongdao.sdk.enums.TdAppStore;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

@SuppressLint("SimpleDateFormat")
public class TongDaoCheckTool {
    public static boolean isEmpty(String text) {
        if (text != null && !text.trim().equals("") && text.length() > 1 && !text.equals("null")) {
            return false;
        }
        return true;
    }

    public static boolean isValidKey(String paramString) {
        if (isEmpty(paramString)) {
            return false;
        }
        return paramString.length() == 32;
    }

    public static boolean checkKeys(String mDeviceId, String mPackageName,
                                    String mAppKey) {
        if (isEmpty(mDeviceId)) {
            return false;
        }

        if (isEmpty(mPackageName)) {
            return false;
        }

        if (!isValidKey(mAppKey)) {
            return false;
        }

        return true;
    }

    public static String getTimeStamp(long timeStamp) {
        Date date = new Date(timeStamp);
        return getTimeStamp(date);
    }

    public static String getTimeStamp(Date date) {
        SimpleDateFormat m_ISO8601Local = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        m_ISO8601Local.setTimeZone(TimeZone.getTimeZone("UTC"));

        return m_ISO8601Local.format(date);
    }

    public static String generateNonce() {
        Random r = new Random();
        byte[] dg = null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            sb.append(r.nextInt(10));
        }
        String num = sb.toString();

        try {
            final MessageDigest mDigest = MessageDigest.getInstance("SHA1");
            mDigest.update(num.getBytes());
            dg = mDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // num = Base64.encodeToString(num.getBytes(),Base64.DEFAULT);
        byte[] newNum = Base64.encode(dg, Base64.NO_WRAP);
        num = new String(newNum);
        return num;
    }

    public static int getAppStoreValue(TdAppStore name) {
        switch (name) {
            case APP_STORE_BAIDU:
                return 1;
            case APP_STORE_TENCENT:
                return 2;
            case APP_STORE_360:
                return 3;
            case APP_STORE_WANDOUJIA:
                return 4;
            case APP_STORE_91:
                return 5;
            case APP_STORE_HIMARKET:
                return 6;
            case APP_STORE_TAOBAO:
                return 7;
            case APP_STORE_XIAOMI:
                return 8;
            case APP_STORE_DCN:
                return 9;
            case APP_STORE_APPCHINA:
                return 10;
            case APP_STORE_CUSTOM1:
                return 1001;
            case APP_STORE_CUSTOM2:
                return 1002;
            case APP_STORE_CUSTOM3:
                return 1003;
            case APP_STORE_CUSTOM4:
                return 1004;
            case APP_STORE_CUSTOM5:
                return 1005;
            case APP_STORE_CUSTOM6:
                return 1006;
            case APP_STORE_CUSTOM7:
                return 1007;
            case APP_STORE_CUSTOM8:
                return 1008;
            case APP_STORE_CUSTOM9:
                return 1009;
            default:
                return 1009;
        }
    }

}
