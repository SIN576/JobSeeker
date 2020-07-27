package com.soysin.mobile.jobseeker.service;


import com.soysin.mobile.jobseeker.model.Login;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;

public interface ApiService {

    @POST("/api/users/login")
    Call<Login> userLogin(@QueryMap Map<String, String> parameters);

    @Multipart
    @POST("/api/users/store")
    Call<ResponseBody> userRegister(@Part("user_name") RequestBody user_name,
                                    @Part("email") RequestBody email,
                                    @Part("password") RequestBody password);

    @Multipart
    @POST("/api/postJob/createPostJob")
    Call<ResponseBody> createPostJob(@Part("company_name") RequestBody user_name,
                                    @Part("requirement") RequestBody email,
                                    @Part("email") RequestBody password,
                                     @Part("address") RequestBody address,
                                     @Part("number_phone") RequestBody numberPhone,
                                     @Part MultipartBody.Part image);

}
