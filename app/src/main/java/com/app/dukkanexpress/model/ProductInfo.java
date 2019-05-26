package com.app.dukkanexpress.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductInfo {

    @SerializedName("product_id")
    @Expose
    String product_id;
    @SerializedName("product_price")
    @Expose
    String product_price;
    @SerializedName("product_quantity")
    @Expose
    String product_quantity;

    public ProductInfo() {
    }

    public ProductInfo(String product_id, String product_price, String product_quantity) {
        this.product_id = product_id;
        this.product_price = product_price;
        this.product_quantity = product_quantity;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(String product_quantity) {
        this.product_quantity = product_quantity;
    }
}
