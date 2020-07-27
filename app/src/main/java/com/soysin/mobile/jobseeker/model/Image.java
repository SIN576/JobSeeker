package com.soysin.mobile.jobseeker.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Image {

    public Image(String updated_at, String created_at, String imageFile, int id) {
        this.updated_at = updated_at;
        this.created_at = created_at;
        this.imageFile = imageFile;
        this.id = id;
    }

    @Expose
    @SerializedName("updated_at")
    private String updated_at;
    @Expose
    @SerializedName("created_at")
    private String created_at;
    @Expose
    @SerializedName("imageFile")
    private String imageFile;
    @Expose
    @SerializedName("id")
    private int id;

    public String getUpdated_at() {
        return updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getImageFile() {
        return imageFile;
    }

    public int getId() {
        return id;
    }
}
