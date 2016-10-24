package com.tongdao.sdk.interfaces;

import com.tongdao.sdk.beans.TdRewardBean;

import java.util.ArrayList;

public interface OnRewardUnlockedListener {

    void onSuccess(ArrayList<TdRewardBean> rewards);

}
