package com.tongdao.sdk.interfaces;

import com.tongdao.sdk.beans.TdPageBean;

/**
 * 
 *着陆页下载成功的回调接口
 *
 */
public interface OnDownloadLandingPageListener {
	
	/**
	 * 
	 *着陆页信息回调函数
	 *@param pageBean 着陆页信息
	 */
	 public void onSuccess(TdPageBean pageBean);

}
