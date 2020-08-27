package com.soysin.mobile.jobseeker.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cv {
    public Cv(String email, String phone_number, String experience, String title, String pdf, int id) {
        this.email = email;
        this.phone_number = phone_number;
        this.experience = experience;
        this.title = title;
        this.pdf = pdf;
        this.id = id;
    }

    @Expose
    @SerializedName("email")
    private String email;
    @Expose
    @SerializedName("phone_number")
    private String phone_number;
    @Expose
    @SerializedName("experience")
    private String experience;
    @Expose
    @SerializedName("updated_at")
    private String updated_at;
    @Expose
    @SerializedName("title")
    private String title;
    @Expose
    @SerializedName("last_name")
    private String last_name;
    @Expose
    @SerializedName("first_name")
    private String first_name;
    @Expose
    @SerializedName("profile")
    private String profile;
    @Expose
    @SerializedName("user_id")
    private int user_id;
    @Expose
    @SerializedName("pdf")
    private String pdf;
    @Expose
    @SerializedName("id")
    private int id;

    public String getEmail() {
        return email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getExperience() {
        return experience;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getTitle() {
        return title;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getProfile() {
        return profile;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getPdf() {
        return pdf;
    }

    public int getId() {
        return id;
    }
}
