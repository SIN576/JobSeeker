package com.soysin.mobile.jobseeker.model;

public class TypeOfJob {

    private String url;
    private String job_status;

    public TypeOfJob(String url, String job_status) {
        this.url = url;
        this.job_status = job_status;
    }

    public String getUrl() {
        return url;
    }

    public String getJob_status() {
        return job_status;
    }
}
