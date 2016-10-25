package com.tongdao.sdk.tools;

import com.tongdao.sdk.config.Constants;

public class TongDaoUrlTool {

    public String getInAppMessageUrl(String userId) {
        return String.format(Constants.API_URL +
                        Constants.API_VERSION +
                        Constants.API_URI_MESSAGES + "?" + Constants.API_URI_MESSAGES_QS,
                userId);
    }

    public String getTrackEventUrlV2() {
        return Constants.API_URL +
                Constants.API_VERSION + Constants.API_URI_EVENTS;
    }
}
