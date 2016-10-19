package com.tongdao.newsdk.interfaces;

import com.tongdao.newsdk.beans.TdErrorBean;


/**
 * 错误回调接口
 */
public interface OnErrorListener {

    /**
     * 错误信息回调函数
     *
     * @param errorBean 错误信息
     */
    public void onError(TdErrorBean errorBean);

}
