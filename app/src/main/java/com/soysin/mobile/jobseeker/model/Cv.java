package com.soysin.mobile.jobseeker.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cv {

    @Expose
    @SerializedName("updated_at")
    private String updated_at;
    @Expose
    @SerializedName("created_at")
    private String created_at;
    @Expose
    @SerializedName("phone_number")
    private String phone_number;
    @Expose
    @SerializedName("email")
    private String email;
    @Expose
    @SerializedName("experience")
    private String experience;
    @Expose
    @SerializedName("title")
    private String title;
    @Expose
    @SerializedName("pdf")
    private String pdf;
    @Expose
    @SerializedName("id")
    private int id;

    public String getUpdated_at() {
        return updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getEmail() {
        return email;
    }

    public String getExperience() {
        return experience;
    }

    public String getTitle() {
        return title;
    }

    public String getPdf() {
        return pdf;
    }

    public int getId() {
        return id;
    }
}
