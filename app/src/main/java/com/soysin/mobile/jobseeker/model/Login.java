package com.soysin.mobile.jobseeker.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Login {


    @Expose
    @SerializedName("user")
    private User user;
    @Expose
    @SerializedName("success")
    private int success;

    public User getUser() {
        return user;
    }

    public int getSuccess() {
        return success;
    }
}

