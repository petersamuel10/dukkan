package com.app.dukkanexpress.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserModel {

    @SerializedName("user_id")
    @Expose
    String user_id;

    @SerializedName("user_name")
    @Expose
    String user_name;

    @SerializedName("user_email")
    @Expose
    String user_email;

    @SerializedName("user_password")
    @Expose
    String user_password;

    @SerializedName("user_telep")
    @Expose
    String user_telep;

    @SerializedName("user_address")
    @Expose
    String user_address;

    @SerializedName("user_token_device")
    @Expose
    String user_token_device;

    public UserModel(String user_id, String user_name, String user_email, String user_password, String user_telep, String user_address, String user_token_device) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_password = user_password;
        this.user_telep = user_telep;
        this.user_address = user_address;
        this.user_token_device = user_token_device;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_telep() {
        return user_telep;
    }

    public void setUser_telep(String user_telep) {
        this.user_telep = user_telep;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getUser_token_device() {
        return user_token_device;
    }

    public void setUser_token_device(String user_token_device) {
        this.user_token_device = user_token_device;
    }
}
