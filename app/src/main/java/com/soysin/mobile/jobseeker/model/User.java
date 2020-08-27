package com.soysin.mobile.jobseeker.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @Expose
    @SerializedName("api_token")
    private String api_token;
    @Expose
    @SerializedName("role")
    private int role;
    @Expose
    @SerializedName("updated_at")
    private String updated_at;
    @Expose
    @SerializedName("created_at")
    private String created_at;
    @Expose
    @SerializedName("email_verified_at")
    private String email_verified_at;
    @Expose
    @SerializedName("address")
    private String address;
    @Expose
    @SerializedName("phone_number")
    private String phone_number;
    @Expose
    @SerializedName("email")
    private String email;
    @Expose
    @SerializedName("company_name")
    private String company_name;
    @Expose
    @SerializedName("birth")
    private String birth;
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
    @SerializedName("id")
    private int id;

    public String getApi_token() {
        return api_token;
    }

    public int getRole() {
        return role;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getEmail_verified_at() {
        return email_verified_at;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getEmail() {
        return email;
    }

    public String getCompany_name() {
        return company_name;
    }

    public String getBirth() {
        return birth;
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

    public int getId() {
        return id;
    }
}
