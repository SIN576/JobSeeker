package com.soysin.mobile.jobseeker.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FilterData {

    @Expose
    @SerializedName("title")
    private String title;

    public String getTitle() {
        return title;
    }
}
