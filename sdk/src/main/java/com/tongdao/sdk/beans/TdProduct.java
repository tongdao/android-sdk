package com.tongdao.sdk.beans;

import java.util.Currency;

public class TdProduct {

    private String id;

    private String sku;

    private String name;

    private float price;

    private Currency currency;

    private String category;

    /**
     * 同道内部调用,不建议使用
     */
    public String getId() {
        return id;
    }

    /**
     * 设置产品的Id
     *
     * @param id 产品的Id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 同道内部调用,不建议使用
     */
    public String getSku() {
        return sku;
    }

    /**
     * 设置产品的SKU
     *
     * @param sku 产品的SKU
     */
    public void setSku(String sku) {
        this.sku = sku;
    }

    /**
     * 同道内部调用,不建议使用
     */
    public String getName() {
        return name;
    }

    /**
     * 设置产品的名称
     *
     * @param name 产品的名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 同道内部调用,不建议使用
     */
    public float getPrice() {
        return price;
    }

    /**
     * 设置产品的价格
     *
     * @param price 产品的价格
     */
    public void setPrice(float price) {
        this.price = price;
    }

    /**
     * 同道内部调用,不建议使用
     */
    public Currency getCurrency() {
        return currency;
    }

    /**
     * 设置产品的货币单位
     *
     * @param currency 产品的货币单位(安卓Currency对象)
     */
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    /**
     * 同道内部调用,不建议使用
     */
    public String getCategory() {
        return category;
    }

    /**
     * 设置产品的类别
     *
     * @param category 产品的类别
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * 无参构造函数，所有值都为空，可以使用set方法设置值
     */
    public TdProduct() {
    }

    /**
     * 构造函数
     *
     * @param id       产品的Id
     * @param sku      产品的SKU
     * @param name     产品的名称
     * @param price    产品的价格
     * @param currency 产品的货币单位
     * @param category 产品的类别
     */
    public TdProduct(String id, String sku, String name, float price, Currency currency, String category) {
        this.id = id;
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.currency = currency;
        this.category = category;
    }
}
