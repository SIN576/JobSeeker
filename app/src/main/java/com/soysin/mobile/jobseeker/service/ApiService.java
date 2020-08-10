package com.soysin.mobile.jobseeker.service;


import com.soysin.mobile.jobseeker.model.Cv;
import com.soysin.mobile.jobseeker.model.Login;
import com.soysin.mobile.jobseeker.model.PostJob;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiService {

    @POST("/api/login")
    Call<Login> userLogin(@QueryMap Map<String, String> parameters);

    @GET("/api/postcv/show")
    Call<List<Cv>> getPostCv(@Header("Authorization") String token);

    //post job
    @GET("/api/postjob/show")
    Call<List<PostJob>> getPostJob(@Header("Authorization") String token);

    @GET("/api/postjob/find/{id}")
    Call<PostJob> getJob(@Path("id") int id, @Header("Authorization") String token);

    @POST("/api/register")
    @FormUrlEncoded
    Call<ResponseBody> userRegister(@Field("first_name") String first_name,
                                    @Field("last_name") String last_name,
                                    @Field("email") String email,
                                    @Field("password") String password,
                                    @Field("company_name") String company_name,
                                    @Field("address") String address,
                                    @Field("birth") String birth);

    //@Multipart
    @POST("/api/postjob/create")
    Call<ResponseBody> createPostJob(@Body RequestBody requestBody,@Header("Authorization") String token);

}
