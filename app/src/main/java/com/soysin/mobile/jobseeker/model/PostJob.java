package com.soysin.mobile.jobseeker.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostJob {

    @Expose
    @SerializedName("profile")
    private String profile;
    @Expose
    @SerializedName("user_id")
    private int user_id;
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
    @SerializedName("company_name")
    private String company_name;
    @Expose
    @SerializedName("address")
    private String address;
    @Expose
    @SerializedName("last_date")
    private String last_date;
    @Expose
    @SerializedName("requirement")
    private String requirement;
    @Expose
    @SerializedName("term")
    private String term;
    @Expose
    @SerializedName("title")
    private String title;
    @Expose
    @SerializedName("image")
    private String image;
    @Expose
    @SerializedName("id")
    private int id;

    public PostJob(int user_id, String email, String phone_number, String experience, String company_name, String address, String last_date, String requirement, String term, String title, String image, int id) {
        this.user_id = user_id;
        this.email = email;
        this.phone_number = phone_number;
        this.experience = experience;
        this.company_name = company_name;
        this.address = address;
        this.last_date = last_date;
        this.requirement = requirement;
        this.term = term;
        this.title = title;
        this.image = image;
        this.id = id;
    }

    public String getProfile() {
        return profile;
    }

    public int getUser_id() {
        return user_id;
    }

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

    public String getCompany_name() {
        return company_name;
    }

    public String getAddress() {
        return address;
    }

    public String getLast_date() {
        return last_date;
    }

    public String getRequirement() {
        return requirement;
    }

    public String getTerm() {
        return term;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public int getId() {
        return id;
    }
}
