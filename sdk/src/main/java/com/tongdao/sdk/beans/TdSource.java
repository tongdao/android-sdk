package com.tongdao.sdk.beans;

import com.tongdao.sdk.enums.TdAppStore;

public class TdSource {
    private TdAppStore appStore;
    private String advertisementId;
    private String advertisementGroupId;
    private String campaignId;
    private String sourceId;

    /**
     * 同道内部调用,不建议使用
     */
    public String getAdvertisementId() {
        return advertisementId;
    }

    /**
     * 设置广告Id
     *
     * @param advertisementId 广告Id
     */
    public void setAdvertisementId(String advertisementId) {
        this.advertisementId = advertisementId;
    }

    /**
     * 同道内部调用,不建议使用
     */
    public String getAdvertisementGroupId() {
        return advertisementGroupId;
    }

    /**
     * 设置广告群组Id
     *
     * @param advertisementGroupId 广告群组Id
     */
    public void setAdvertisementGroupId(String advertisementGroupId) {
        this.advertisementGroupId = advertisementGroupId;
    }

    /**
     * 同道内部调用,不建议使用
     */
    public String getCampaignId() {
        return campaignId;
    }

    /**
     * 设置活动Id
     *
     * @param campaignId 活动Id
     */
    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    /**
     * 同道内部调用,不建议使用
     */
    public String getSourceId() {
        return sourceId;
    }

    /**
     * 设置来源Id
     *
     * @param sourceId 来源Id
     */
    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    /**
     * 同道内部调用,不建议使用
     */
    public TdAppStore getAppStore() {
        return appStore;
    }

    /**
     * 设置应用商店
     *
     * @param appStore 应用商店(枚举类型)
     */
    public void setAppStore(TdAppStore appStore) {
        this.appStore = appStore;
    }

    /**
     * 无参构造函数，所有值都为空，可以使用set方法设置值
     */
    public TdSource() {
    }

    /**
     * 构造函数
     *
     * @param appStore             应用商店(枚举类型)
     * @param advertisementId      广告Id
     * @param advertisementGroupId 广告群组Id
     * @param campaignId           活动Id
     * @param sourceId             来源Id
     */
    public TdSource(TdAppStore appStore, String advertisementId, String advertisementGroupId, String campaignId, String sourceId) {
        this.appStore = appStore;
        this.advertisementId = advertisementId;
        this.advertisementGroupId = advertisementGroupId;
        this.campaignId = campaignId;
        this.sourceId = sourceId;
    }
}
