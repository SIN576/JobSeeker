package com.soysin.mobile.jobseeker.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostJobPagination {

    @Expose
    @SerializedName("total")
    private int total;
    @Expose
    @SerializedName("to")
    private int to;
    @Expose
    @SerializedName("prev_page_url")
    private String prev_page_url;
    @Expose
    @SerializedName("per_page")
    private int per_page;
    @Expose
    @SerializedName("path")
    private String path;
    @Expose
    @SerializedName("next_page_url")
    private String next_page_url;
    @Expose
    @SerializedName("last_page_url")
    private String last_page_url;
    @Expose
    @SerializedName("last_page")
    private int last_page;
    @Expose
    @SerializedName("from")
    private int from;
    @Expose
    @SerializedName("first_page_url")
    private String first_page_url;
    @Expose
    @SerializedName("data")
    private List<PostJob> data;
    @Expose
    @SerializedName("current_page")
    private int current_page;

    public int getTotal() {
        return total;
    }

    public int getTo() {
        return to;
    }

    public String getPrev_page_url() {
        return prev_page_url;
    }

    public int getPer_page() {
        return per_page;
    }

    public String getPath() {
        return path;
    }

    public String getNext_page_url() {
        return next_page_url;
    }

    public String getLast_page_url() {
        return last_page_url;
    }

    public int getLast_page() {
        return last_page;
    }

    public int getFrom() {
        return from;
    }

    public String getFirst_page_url() {
        return first_page_url;
    }

    public List<PostJob> getData() {
        return data;
    }

    public int getCurrent_page() {
        return current_page;
    }


}
