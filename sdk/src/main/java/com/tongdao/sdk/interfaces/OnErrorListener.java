package com.tongdao.sdk.interfaces;

import com.tongdao.sdk.beans.TdErrorBean;


/**
 * 错误回调接口
 */
public interface OnErrorListener {

    /**
     * 错误信息回调函数
     *
     * @param errorBean 错误信息
     */
    void onError(TdErrorBean errorBean);

}
