package com.example.dukkanexpress.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AddOrder {

    @SerializedName("payment_status_id")
    @Expose
    String payment_status_id;

    @SerializedName("client_id")
    @Expose
    String client_id;

    @SerializedName("code_gift")
    @Expose
    String code_gift;

    @SerializedName("product_info")
    @Expose
    ArrayList<ProductInfo> product_info;

    public AddOrder() {
    }

    public AddOrder(String payment_status_id, String client_id, String code_gift, ArrayList<ProductInfo> product_info) {
        this.payment_status_id = payment_status_id;
        this.client_id = client_id;
        this.code_gift = code_gift;
        this.product_info = product_info;
    }

    public String getPayment_status_id() {
        return payment_status_id;
    }

    public void setPayment_status_id(String payment_status_id) {
        this.payment_status_id = payment_status_id;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getCode_gift() {
        return code_gift;
    }

    public void setCode_gift(String code_gift) {
        this.code_gift = code_gift;
    }

    public ArrayList<ProductInfo> getProduct_info() {
        return product_info;
    }

    public void setProduct_info(ArrayList<ProductInfo> product_info) {
        this.product_info = product_info;
    }
}
