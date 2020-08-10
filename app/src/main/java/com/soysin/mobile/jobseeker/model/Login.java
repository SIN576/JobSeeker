package com.soysin.mobile.jobseeker.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Login {

    @Expose
    @SerializedName("success")
    private int success;
    @Expose
    @SerializedName("token")
    private String apiToken;

    @Expose
    @SerializedName("id")
    private int id;

    public int getId() {
        return id;
    }

    public int getSuccess() {
        return success;
    }

    public String getApiToken() {
        return apiToken;
    }
}

