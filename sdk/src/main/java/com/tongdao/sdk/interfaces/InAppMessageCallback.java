package com.tongdao.sdk.interfaces;

import com.tongdao.sdk.beans.TdMessageBean;

/**
 * Created by agonch on 10/24/16.
 */

public interface InAppMessageCallback {

    void callbackTrackOpenInAppMessage(TdMessageBean tdMessageBean);
    void callbackTrackReceivedInAppMessage(TdMessageBean tdMessageBean);
}
