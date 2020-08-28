package com.soysin.mobile.jobseeker.method;

import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.soysin.mobile.jobseeker.adapter.FindJobAdapter;
import com.soysin.mobile.jobseeker.apiconnection.Connection;
import com.soysin.mobile.jobseeker.findJob.SearchJobActivity;
import com.soysin.mobile.jobseeker.model.PostJob;
import com.soysin.mobile.jobseeker.service.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DataPostJob {
    public static List<PostJob> postJobs;
    public static List<PostJob> getPostJob(String token){
        Retrofit retrofit = Connection.getClient();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<PostJob>> listCall = apiService.getPostJob("Bearer "+token,null);

        listCall.enqueue(new Callback<List<PostJob>>() {
            @Override
            public void onResponse(Call<List<PostJob>> call, Response<List<PostJob>> response) {
                if (!response.isSuccessful()){
                    Log.e("error",response.message());
                }
                postJobs = response.body();
                if (postJobs != null){

                }
            }

            @Override
            public void onFailure(Call<List<PostJob>> call, Throwable t) {
                Log.e("error",t.getMessage());
            }
        });
       return postJobs;
    }
}
