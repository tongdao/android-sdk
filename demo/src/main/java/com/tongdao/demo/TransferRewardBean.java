package com.tongdao.demo;

import android.net.Uri;

public class TransferRewardBean {

    private Uri picUri;
    private String rewardName;
    private String rewardSku;
    private int num;

    public TransferRewardBean(Uri picUri, String rewardName, String rewardSku, int num) {
        this.picUri = picUri;
        this.rewardName = rewardName;
        this.rewardSku = rewardSku;
        this.num = num;
    }

    public Uri getPicUri() {
        return picUri;
    }

    public String getRewardName() {
        return rewardName;
    }

    public String getRewardSku() {
        return rewardSku;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }


}
