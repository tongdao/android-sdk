
package com.tongdao.newsdk.retrofit;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Application {

    @SerializedName("!version_code")
    @Expose
    private Integer versionCode;
    @SerializedName("!version_name")
    @Expose
    private String versionName;
    @SerializedName("!rating")
    @Expose
    private Integer rating;

    /**
     * 
     * @return
     *     The versionCode
     */
    public Integer getVersionCode() {
        return versionCode;
    }

    /**
     * 
     * @param versionCode
     *     The !version_code
     */
    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    /**
     * 
     * @return
     *     The versionName
     */
    public String getVersionName() {
        return versionName;
    }

    /**
     * 
     * @param versionName
     *     The !version_name
     */
    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    /**
     * 
     * @return
     *     The rating
     */
    public Integer getRating() {
        return rating;
    }

    /**
     * 
     * @param rating
     *     The !rating
     */
    public void setRating(Integer rating) {
        this.rating = rating;
    }

}
