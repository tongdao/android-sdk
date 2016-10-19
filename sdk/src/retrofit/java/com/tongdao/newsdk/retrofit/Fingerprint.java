
package com.tongdao.newsdk.retrofit;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Fingerprint {

    @SerializedName("!gaid")
    @Expose
    private String gaid;

    /**
     * 
     * @return
     *     The gaid
     */
    public String getGaid() {
        return gaid;
    }

    /**
     * 
     * @param gaid
     *     The !gaid
     */
    public void setGaid(String gaid) {
        this.gaid = gaid;
    }

}
