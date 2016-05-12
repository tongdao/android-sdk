package com.tongdao.sdk.tools;

import com.tongdao.sdk.config.Constants;

public class TongDaoUrlTool {

    public static String getLandingPageUrl(String landingPageId, String userId) {
        return String.format(Constants.API_URL +
                        Constants.API_VERSION +
                        Constants.API_URI_PAGE + "?" + Constants.API_URI_PAGE_QS,
                new Object[]{landingPageId, userId});
    }

    public static String getInAppMessageUrl(String userId) {
        return String.format(Constants.API_URL +
                        Constants.API_VERSION +
                        Constants.API_URI_MESSAGES + "?" + Constants.API_URI_MESSAGES_QS,
                new Object[]{userId});
    }

    public static String getTrackEventUrlV2() {
        return Constants.API_URL +
                Constants.API_VERSION + Constants.API_URI_EVENTS;
    }
}
