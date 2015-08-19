package com.tongdao.sdk.interfaces;

import java.util.ArrayList;
import com.tongdao.sdk.beans.TdMessageBean;


public interface OnDownloadInAppMessageListener {
	
	 public void onSuccess(ArrayList<TdMessageBean> tdMessageBeanList);

}
