
package com.tongdao.sdk.retrofit;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Source {

    @SerializedName("!appstore_id")
    @Expose
    private String appstoreId;
    @SerializedName("!ad_id")
    @Expose
    private String adId;
    @SerializedName("!adgroup_id")
    @Expose
    private String adgroupId;
    @SerializedName("!campaign_id")
    @Expose
    private String campaignId;
    @SerializedName("!source_id")
    @Expose
    private String sourceId;

    /**
     * 
     * @return
     *     The appstoreId
     */
    public String getAppstoreId() {
        return appstoreId;
    }

    /**
     * 
     * @param appstoreId
     *     The !appstore_id
     */
    public void setAppstoreId(String appstoreId) {
        this.appstoreId = appstoreId;
    }

    /**
     * 
     * @return
     *     The adId
     */
    public String getAdId() {
        return adId;
    }

    /**
     * 
     * @param adId
     *     The !ad_id
     */
    public void setAdId(String adId) {
        this.adId = adId;
    }

    /**
     * 
     * @return
     *     The adgroupId
     */
    public String getAdgroupId() {
        return adgroupId;
    }

    /**
     * 
     * @param adgroupId
     *     The !adgroup_id
     */
    public void setAdgroupId(String adgroupId) {
        this.adgroupId = adgroupId;
    }

    /**
     * 
     * @return
     *     The campaignId
     */
    public String getCampaignId() {
        return campaignId;
    }

    /**
     * 
     * @param campaignId
     *     The !campaign_id
     */
    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    /**
     * 
     * @return
     *     The sourceId
     */
    public String getSourceId() {
        return sourceId;
    }

    /**
     * 
     * @param sourceId
     *     The !source_id
     */
    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

}
