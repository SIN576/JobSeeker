package com.soysin.mobile.jobseeker.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostJob {


    @Expose
    @SerializedName("updated_at")
    private String updated_at;
    @Expose
    @SerializedName("created_at")
    private String created_at;
    @Expose
    @SerializedName("user_id")
    private int user_id;
    @Expose
    @SerializedName("image")
    private String image;
    @Expose
    @SerializedName("phone_number")
    private String phone_number;
    @Expose
    @SerializedName("address")
    private String address;
    @Expose
    @SerializedName("last_date")
    private String last_date;
    @Expose
    @SerializedName("email")
    private String email;
    @Expose
    @SerializedName("experience")
    private String experience;
    @Expose
    @SerializedName("requirement")
    private String requirement;
    @Expose
    @SerializedName("title")
    private String title;
    @Expose
    @SerializedName("term")
    private String term;
    @Expose
    @SerializedName("company_name")
    private String company_name;
    @Expose
    @SerializedName("id")
    private int id;

    public String getUpdated_at() {
        return updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getImage() {
        return image;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getAddress() {
        return address;
    }

    public String getLast_date() {
        return last_date;
    }

    public String getEmail() {
        return email;
    }

    public String getExperience() {
        return experience;
    }

    public String getRequirement() {
        return requirement;
    }

    public String getTitle() {
        return title;
    }

    public String getTerm() {
        return term;
    }

    public String getCompany_name() {
        return company_name;
    }

    public int getId() {
        return id;
    }
}
