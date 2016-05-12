package com.tongdao.sdk.beans;

import java.util.ArrayList;
import java.util.Currency;

public class TdOrder {

    private String orderId;

    private float total;

    private float revenue;

    private float shipping;

    private float tax;

    private float discount;

    private String couponId;

    private Currency currency;

    private ArrayList<TdOrderLine> orderLines;

    /**
     * 同道内部调用,不建议使用
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * 设置交易Id
     *
     * @param orderId 交易Id
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * 同道内部调用,不建议使用
     */
    public float getTotal() {
        return total;
    }

    /**
     * 设置交易总价
     *
     * @param total 交易总价
     */
    public void setTotal(float total) {
        this.total = total;
    }

    /**
     * 同道内部调用,不建议使用
     */
    public float getRevenue() {
        return revenue;
    }

    /**
     * 设置交易收益
     *
     * @param revenue 交易收益
     */
    public void setRevenue(float revenue) {
        this.revenue = revenue;
    }

    /**
     * 同道内部调用,不建议使用
     */
    public float getShipping() {
        return shipping;
    }

    /**
     * 设置运输费用
     *
     * @param shipping 运输费用
     */
    public void setShipping(float shipping) {
        this.shipping = shipping;
    }

    /**
     * 同道内部调用,不建议使用
     */
    public float getTax() {
        return tax;
    }

    /**
     * 设置税费
     *
     * @param tax 税费
     */
    public void setTax(float tax) {
        this.tax = tax;
    }

    /**
     * 同道内部调用,不建议使用
     */
    public float getDiscount() {
        return discount;
    }

    /**
     * 设置折扣具体金额
     *
     * @param discount 折扣具体金额
     */
    public void setDiscount(float discount) {
        this.discount = discount;
    }

    /**
     * 同道内部调用,不建议使用
     */
    public String getCouponId() {
        return couponId;
    }

    /**
     * 设置订货单Id
     *
     * @param couponId 订货单Id
     */
    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    /**
     * 同道内部调用,不建议使用
     */
    public Currency getCurrency() {
        return currency;
    }

    /**
     * 设置交易的货币单位
     *
     * @param currency 交易的货币单位(安卓Currency对象)
     */
    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    /**
     * 同道内部调用,不建议使用
     */
    public ArrayList<TdOrderLine> getOrderLines() {
        return orderLines;
    }

    /**
     * 设置交易的订单列表
     *
     * @param orderLines 交易的订单列表
     */
    public void setOrderLines(ArrayList<TdOrderLine> orderLines) {
        this.orderLines = orderLines;
    }

    /**
     * 无参构造函数，所有值都为空，可以使用set方法设置值
     */
    public TdOrder() {
    }

    /**
     * 构造函数
     *
     * @param orderId    设置交易Id
     * @param total      交易总价
     * @param revenue    交易收益
     * @param shipping   运输费用
     * @param tax        税费
     * @param discount   折扣具体金额
     * @param couponId   订货单Id
     * @param currency   交易的货币单位
     * @param orderLines 交易的订单列表
     */
    public TdOrder(String orderId, float total, float revenue, float shipping, float tax, float discount, String couponId, Currency currency, ArrayList<TdOrderLine> orderLines) {
        this.orderId = orderId;
        this.total = total;
        this.revenue = revenue;
        this.shipping = shipping;
        this.tax = tax;
        this.discount = discount;
        this.couponId = couponId;
        this.currency = currency;
        this.orderLines = orderLines;
    }

}
