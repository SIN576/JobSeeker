package com.soysin.mobile.jobseeker.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Login {

    @Expose
    @SerializedName("sucess")
    private int sucess;

    public int getSucess() {
        return sucess;
    }
}
