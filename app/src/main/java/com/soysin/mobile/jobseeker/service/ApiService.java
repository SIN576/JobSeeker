package com.soysin.mobile.jobseeker.service;


import androidx.room.Delete;

import com.soysin.mobile.jobseeker.model.Cv;
import com.soysin.mobile.jobseeker.model.FilterData;
import com.soysin.mobile.jobseeker.model.Login;
import com.soysin.mobile.jobseeker.model.PostCvPagination;
import com.soysin.mobile.jobseeker.model.PostJob;
import com.soysin.mobile.jobseeker.model.PostJobPagination;
import com.soysin.mobile.jobseeker.model.User;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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

    @POST("/api/register")
    @FormUrlEncoded
    Call<Login> userRegister(@Field("first_name") String first_name,
                             @Field("last_name") String last_name,
                             @Field("phone_number") String phoneNumber,
                             @Field("email") String email,
                             @Field("password") String password,
                             @Field("password_confirmation") String conform_password,
                             @Field("company_name") String company_name,
                             @Field("address") String address,
                             @Field("birth") String birth,
                             @Field("role") int role);

    @GET("/api/user/{id}/{updated_at}")
    Call<User> getUser(@Path("id") int id,@Path("id") String update_at, @Header("Authorization") String token);

    @POST("/api/user/updateprofile")
    Call<User> updatedProfile(@Body RequestBody requestBody, @Header("Authorization") String token);

    @POST("/api/user/update/{id}")
    Call<User> update(@Body RequestBody requestBody, @Path("id") int id, @Header("Authorization") String token);

    @DELETE("/api/postjob/delete/{id}")
    Call<Void> deleteJob(@Path("id") int id, @Header("Authorization") String token);

    //post cv
    @GET("/api/test/postcv/title")
    Call<List<FilterData>> getTitlePostCv();

    @GET("/api/postcv/read")
    Call<PostCvPagination> getPostCv(@Header("Authorization") String token,
                                     @Query("title") String title,
                                     @Query("page") Integer page);

    @GET("/api/postcv/user/{id}")
    Call<List<Cv>> getPostCvUser(@Path("id") int id, @Header("Authorization") String token);

    @GET("/api/postcv/readtypecv/{title}")
    Call<List<Cv>> getTypeOfCv(@Path("title") String title, @Header("Authorization") String token);

    @DELETE("/api/postcv/delete/{id}")
    Call<Void> deleteCV(@Path("id") int id, @Header("Authorization") String token);

    @POST("/api/postcv/update/{id}")
    Call<ResponseBody> updateNotFile(@Path("id") int id, @Header("Authorization") String token, @Body MultipartBody responseBody);

    @POST("/api/postcv/update/{id}")
    Call<ResponseBody> updateFile(@Path("id") int id, @Header("Authorization") String token, @Body MultipartBody responseBody);


    //post job

    @GET("/api/test/title")
    Call<List<FilterData>> getTitle();
    @GET("/api/postjob/read")
    Call<PostJobPagination> getPostJob(@Header("Authorization") String token,
                                       @Query("term") String term,
                                       @Query("location") String location,
                                       @Query("title") String title,
                                       @Query("page") Integer page);

    @GET("/api/postjob/user/{id}")
    Call<List<PostJob>> getPostJobUser(@Path("id") int id, @Header("Authorization") String token);

    @GET("/api/postjob/readtypejob/{title}")
    Call<List<PostJob>> getTypeOfJob(@Path("title") String title, @Header("Authorization") String token);

    @GET("/api/postjob/show/{id}")
    Call<PostJob> getJob(@Path("id") int id, @Header("Authorization") String token);

    @POST("/api/test/postjob/byTerm")
    Call<List<PostJob>> getJobByTerm(@Body RequestBody requestBody, @Header("Authorization") String token);

    @POST("/api/postjob/update/{id}")
    Call<ResponseBody> updateJobNotFile(@Path("id") int id, @Header("Authorization") String token, @Body RequestBody responseBody);

    @POST("/api/postjob/update/{id}")
    Call<ResponseBody> updateJobFile(@Path("id") int id, @Header("Authorization") String token, @Body RequestBody responseBody);


    //@Multipart
    @POST("/api/postjob/create")
    Call<ResponseBody> createPostJob(@Body RequestBody requestBody, @Header("Authorization") String token);

    @POST("/api/postcv/create")
    Call<ResponseBody> createPostCv(@Body RequestBody requestBody, @Header("Authorization") String token);
}
