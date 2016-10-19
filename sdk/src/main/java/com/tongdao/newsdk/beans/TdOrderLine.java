package com.tongdao.newsdk.beans;

public class TdOrderLine {

    private TdProduct product;

    private int quantity;

    /**
     * 同道内部调用,不建议使用
     */
    public TdProduct getProduct() {
        return product;
    }

    /**
     * 设置商品信息
     *
     * @param product 商品信息
     */
    public void setProduct(TdProduct product) {
        this.product = product;
    }

    /**
     * 同道内部调用,不建议使用
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * 设置商品个数
     *
     * @param quantity 商品个数
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * 无参构造函数，所有值都为空，可以使用set方法设置值
     */
    public TdOrderLine() {
    }

    /**
     * 构造函数
     *
     * @param product  商品信息
     * @param quantity 商品个数
     */
    public TdOrderLine(TdProduct product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }
}
