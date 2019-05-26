package com.app.dukkanexpress.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Slider {

    @SerializedName("photos_id")
    @Expose
    String photos_id;

    @SerializedName("photos_path")
    @Expose
    String photos_path;

    public Slider(String photos_id, String photos_path) {
        this.photos_id = photos_id;
        this.photos_path = photos_path;
    }

    public String getPhotos_id() {
        return photos_id;
    }

    public void setPhotos_id(String photos_id) {
        this.photos_id = photos_id;
    }

    public String getPhotos_path() {
        return photos_path;
    }

    public void setPhotos_path(String photos_path) {
        this.photos_path = photos_path;
    }
}
