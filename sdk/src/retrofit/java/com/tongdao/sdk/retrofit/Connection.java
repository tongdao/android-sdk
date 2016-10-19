
package com.tongdao.sdk.retrofit;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Connection {

    @SerializedName("!carrier_name")
    @Expose
    private String carrierName;
    @SerializedName("!carrier_code")
    @Expose
    private Integer carrierCode;
    @SerializedName("!connection_type")
    @Expose
    private String connectionType;
    @SerializedName("!connection_quality")
    @Expose
    private Integer connectionQuality;

    /**
     * 
     * @return
     *     The carrierName
     */
    public String getCarrierName() {
        return carrierName;
    }

    /**
     * 
     * @param carrierName
     *     The !carrier_name
     */
    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }

    /**
     * 
     * @return
     *     The carrierCode
     */
    public Integer getCarrierCode() {
        return carrierCode;
    }

    /**
     * 
     * @param carrierCode
     *     The !carrier_code
     */
    public void setCarrierCode(Integer carrierCode) {
        this.carrierCode = carrierCode;
    }

    /**
     * 
     * @return
     *     The connectionType
     */
    public String getConnectionType() {
        return connectionType;
    }

    /**
     * 
     * @param connectionType
     *     The !connection_type
     */
    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    /**
     * 
     * @return
     *     The connectionQuality
     */
    public Integer getConnectionQuality() {
        return connectionQuality;
    }

    /**
     * 
     * @param connectionQuality
     *     The !connection_quality
     */
    public void setConnectionQuality(Integer connectionQuality) {
        this.connectionQuality = connectionQuality;
    }

}
