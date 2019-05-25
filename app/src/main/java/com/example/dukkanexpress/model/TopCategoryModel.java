package com.example.dukkanexpress.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TopCategoryModel {

    @SerializedName("category_id")
    @Expose
    String category_id;

    @SerializedName("category_title_ar")
    @Expose
    String category_title_ar;

    @SerializedName("category_title_en")
    @Expose
    String category_title_en;
    @SerializedName("category_icon")
    @Expose
    String category_icon;

    @SerializedName("product_item")
    @Expose
    ArrayList<ProductModel> product_item;

    public TopCategoryModel() {
    }

    public TopCategoryModel(String category_id, String category_title_ar, String category_title_en,
                            String category_icon, ArrayList<ProductModel> product_item) {
        this.category_id = category_id;
        this.category_title_ar = category_title_ar;
        this.category_title_en = category_title_en;
        this.category_icon = category_icon;
        this.product_item = product_item;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_title_ar() {
        return category_title_ar;
    }

    public void setCategory_title_ar(String category_title_ar) {
        this.category_title_ar = category_title_ar;
    }

    public String getCategory_title_en() {
        return category_title_en;
    }

    public void setCategory_title_en(String category_title_en) {
        this.category_title_en = category_title_en;
    }

    public String getCategory_icon() {
        return category_icon;
    }

    public void setCategory_icon(String category_icon) {
        this.category_icon = category_icon;
    }

    public ArrayList<ProductModel> getProduct_item() {
        return product_item;
    }

    public void setProduct_item(ArrayList<ProductModel> product_item) {
        this.product_item = product_item;
    }
}
