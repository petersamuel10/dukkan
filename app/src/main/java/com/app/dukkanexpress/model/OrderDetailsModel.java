package com.app.dukkanexpress.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetailsModel {

    @SerializedName("product_img")
    @Expose
    String product_img;

    @SerializedName("product_title_ar")
    @Expose
    String product_title_ar;

    @SerializedName("product_title_en")
    @Expose
    String product_title_en;

    @SerializedName("quantity")
    @Expose
    String quantity;

    public OrderDetailsModel() {
    }

    public OrderDetailsModel(String product_img, String product_title_ar, String product_title_en, String quantity) {
        this.product_img = product_img;
        this.product_title_ar = product_title_ar;
        this.product_title_en = product_title_en;
        this.quantity = quantity;
    }

    public String getProduct_img() {
        return product_img;
    }

    public void setProduct_img(String product_img) {
        this.product_img = product_img;
    }

    public String getProduct_title_ar() {
        return product_title_ar;
    }

    public void setProduct_title_ar(String product_title_ar) {
        this.product_title_ar = product_title_ar;
    }

    public String getProduct_title_en() {
        return product_title_en;
    }

    public void setProduct_title_en(String product_title_en) {
        this.product_title_en = product_title_en;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
