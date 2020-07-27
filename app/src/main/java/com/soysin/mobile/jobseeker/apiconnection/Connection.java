package com.soysin.mobile.jobseeker.apiconnection;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Connection {

    public Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.43.183:8000")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static Retrofit getClient(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        return  new Retrofit.Builder()
                .baseUrl("http://192.168.43.183:8000")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }
}
