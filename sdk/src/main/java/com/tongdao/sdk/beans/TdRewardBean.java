package com.tongdao.sdk.beans;


public class TdRewardBean {

    private int id;
    private String name;
    private int quantity;
    private String sku;

    /**
     * 与在同道平台设置奖品界面的奖品编号一致,可用于确定哪个奖品被成功领取
     *
     * @return String 奖品界面的奖品编号
     */
    public String getSku() {
        return sku;
    }

    /**
     * 同道SDK内部调用,不建议使用
     *
     * @param sku
     */
    public void setSku(String sku) {
        this.sku = sku;
    }

    /**
     * 获得该奖品个数
     *
     * @return int 个数
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * 同道SDK内部调用,不建议使用
     *
     * @param quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * 获得奖品Id
     *
     * @return int 奖品Id
     */
    public int getId() {
        return id;
    }

    /**
     * 同道SDK内部调用,不建议使用
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * 获得该奖品名称
     *
     * @return String 奖品名称
     */
    public String getName() {
        return name;
    }

    /**
     * 同道SDK内部调用,不建议使用
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
}
