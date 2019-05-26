package com.app.dukkanexpress.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginData {

    @SerializedName("message")
    @Expose
    String message;

    @SerializedName("jwt")
    @Expose
    String jwt;

    @SerializedName("user")
    @Expose
    UserModel user;

    public LoginData(String message, String jwt, UserModel user) {
        this.message = message;
        this.jwt = jwt;
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}
