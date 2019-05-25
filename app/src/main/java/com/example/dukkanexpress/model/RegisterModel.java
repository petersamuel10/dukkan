package com.example.dukkanexpress.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterModel {

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
    String user_phone;

    @SerializedName("user_address")
    @Expose
    String user_address;

    @SerializedName("jwt")
    @Expose
    String jwt;

    @SerializedName("user_id")
    @Expose
    String user_id;

    public RegisterModel(String user_email, String user_password) {
        this.user_email = user_email;
        this.user_password = user_password;
    }

    public RegisterModel(String user_name, String user_email, String user_password, String user_phone, String user_address) {
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_password = user_password;
        this.user_phone = user_phone;
        this.user_address = user_address;
    }

    public RegisterModel(String user_name, String user_email, String user_password, String user_phone, String user_address, String jwt, String user_id) {
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_password = user_password;
        this.user_phone = user_phone;
        this.user_address = user_address;
        this.jwt = jwt;
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

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
