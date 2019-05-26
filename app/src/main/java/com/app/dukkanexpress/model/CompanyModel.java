package com.app.dukkanexpress.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompanyModel {

    @SerializedName("category_id")
    @Expose
    String id;
    @SerializedName("category_icon")
    @Expose
    String category_icon;
    @SerializedName("category_title_ar")
    @Expose
    String category_title_ar;
    @SerializedName("category_title_en")
    @Expose
    String category_title_en;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory_icon() {
        return category_icon;
    }

    public void setCategory_icon(String category_icon) {
        this.category_icon = category_icon;
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
}
