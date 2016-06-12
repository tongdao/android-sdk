package com.tongdao.sdk.interfaces;

import com.tongdao.sdk.beans.TdMessageBean;

import java.util.ArrayList;


public interface OnDownloadInAppMessageListener {

    public void onSuccess(ArrayList<TdMessageBean> tdMessageBeanList);

}
