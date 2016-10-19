package com.tongdao.newsdk.interfaces;

import com.tongdao.newsdk.beans.TdMessageBean;

import java.util.ArrayList;


public interface OnDownloadInAppMessageListener {

    public void onSuccess(ArrayList<TdMessageBean> tdMessageBeanList);

}
