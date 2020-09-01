package com.soysin.mobile.jobseeker.findJob;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.soysin.mobile.jobseeker.adapter.FindJobAdapter;
import com.soysin.mobile.jobseeker.apiconnection.Connection;
import com.soysin.mobile.jobseeker.databinding.ActivitySearchJobBinding;
import com.soysin.mobile.jobseeker.model.PostJob;
import com.soysin.mobile.jobseeker.model.PostJobPagination;
import com.soysin.mobile.jobseeker.service.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchJobActivity extends AppCompatActivity implements FindJobAdapter.OnClickItemListener{

    ActivitySearchJobBinding binding;
    FindJobAdapter findJobAdapter;
    List<PostJob> postJobs;
    String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchJobBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        token= getIntent().getStringExtra("token");
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.recyclerViewSearchJob.setLayoutManager(new LinearLayoutManager(this));
        binding.searchJob.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
       getPostJob();
//        findJobAdapter = new FindJobAdapter(this,findJobModels);
//        binding.recyclerViewSearchJob.setAdapter(findJobAdapter);


    }
    private void filter(String text){
        List<PostJob> postJobList = new ArrayList<>();

        for (PostJob postJob: postJobs){
            if (postJob.getTitle().toLowerCase().contains(text.toLowerCase())){
                postJobList.add(postJob);
            }
        }
        findJobAdapter.filterList(postJobList);
    }
    private void getPostJob(){
        Retrofit retrofit = Connection.getClient();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<PostJobPagination> listCall = apiService.getPostJob("Bearer "+token,null,null,null,1);
        listCall.enqueue(new Callback<PostJobPagination>() {
            @Override
            public void onResponse(Call<PostJobPagination> call, Response<PostJobPagination> response) {
                if (!response.isSuccessful()) {
                    Log.e("error", response.message());
                }
//                Log.e("PostJob",postJobs.get(1).getCompany_name().toString());
//                if (response.isSuccessful() && response.body() != null) {
//                    postJobs = response.body().get(0).getData();
//                    binding.recyclerViewSearchJob.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//                    findJobAdapter = new FindJobAdapter(getApplicationContext(),postJobs);
//                    binding.recyclerViewSearchJob.setAdapter(findJobAdapter);
//                    findJobAdapter.setOnClickItemListener(SearchJobActivity.this);
//                }
            }

            @Override
            public void onFailure(Call<PostJobPagination> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }

    @Override
    public void onItemClick(PostJob postJob) {
        Intent intent = new Intent(this, JobDescriptionActivity.class);
        intent.putExtra("id",postJob.getId());
        intent.putExtra("token",token);
        startActivity(intent);
    }
}