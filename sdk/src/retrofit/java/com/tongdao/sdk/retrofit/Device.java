
package com.tongdao.sdk.retrofit;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Device {

    @SerializedName("!model")
    @Expose
    private String model;
    @SerializedName("!brand")
    @Expose
    private String brand;
    @SerializedName("!product_id")
    @Expose
    private String productId;
    @SerializedName("!build_id")
    @Expose
    private String buildId;
    @SerializedName("!device_name")
    @Expose
    private String deviceName;
    @SerializedName("!os_name")
    @Expose
    private String osName;
    @SerializedName("!os_version")
    @Expose
    private String osVersion;
    @SerializedName("!language")
    @Expose
    private String language;
    @SerializedName("!width")
    @Expose
    private Integer width;
    @SerializedName("!height")
    @Expose
    private Integer height;

    /**
     * 
     * @return
     *     The model
     */
    public String getModel() {
        return model;
    }

    /**
     * 
     * @param model
     *     The !model
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * 
     * @return
     *     The brand
     */
    public String getBrand() {
        return brand;
    }

    /**
     * 
     * @param brand
     *     The !brand
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * 
     * @return
     *     The productId
     */
    public String getProductId() {
        return productId;
    }

    /**
     * 
     * @param productId
     *     The !product_id
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     * 
     * @return
     *     The buildId
     */
    public String getBuildId() {
        return buildId;
    }

    /**
     * 
     * @param buildId
     *     The !build_id
     */
    public void setBuildId(String buildId) {
        this.buildId = buildId;
    }

    /**
     * 
     * @return
     *     The deviceName
     */
    public String getDeviceName() {
        return deviceName;
    }

    /**
     * 
     * @param deviceName
     *     The !device_name
     */
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    /**
     * 
     * @return
     *     The osName
     */
    public String getOsName() {
        return osName;
    }

    /**
     * 
     * @param osName
     *     The !os_name
     */
    public void setOsName(String osName) {
        this.osName = osName;
    }

    /**
     * 
     * @return
     *     The osVersion
     */
    public String getOsVersion() {
        return osVersion;
    }

    /**
     * 
     * @param osVersion
     *     The !os_version
     */
    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    /**
     * 
     * @return
     *     The language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * 
     * @param language
     *     The !language
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * 
     * @return
     *     The width
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * 
     * @param width
     *     The !width
     */
    public void setWidth(Integer width) {
        this.width = width;
    }

    /**
     * 
     * @return
     *     The height
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * 
     * @param height
     *     The !height
     */
    public void setHeight(Integer height) {
        this.height = height;
    }

}
