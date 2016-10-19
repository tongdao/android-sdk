package com.tongdao.newsdk.interfaces;

import com.tongdao.newsdk.beans.TdRewardBean;

import java.util.ArrayList;

public interface OnRewardUnlockedListener {

    public void onSuccess(ArrayList<TdRewardBean> rewards);

}
