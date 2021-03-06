package com.soysin.mobile.jobseeker.findJob;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.soysin.mobile.jobseeker.apiconnection.Connection;
import com.soysin.mobile.jobseeker.databinding.ActivityJobDescriptionBinding;
import com.soysin.mobile.jobseeker.model.PostJob;
import com.soysin.mobile.jobseeker.service.ApiService;
import com.soysin.mobile.jobseeker.util.DateUtils;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class JobDescriptionActivity extends AppCompatActivity {

    int id;
    String token;
    int role;
    ActivityJobDescriptionBinding binding;
    private PostJob postJob;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJobDescriptionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        id = getIntent().getIntExtra("id",1);
        role = getIntent().getIntExtra("role",1);

        token = getIntent().getStringExtra("token");
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getJob();
        binding.btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("mailto:"+ postJob.getEmail()));
                startActivity(intent);
            }
        });
        if (role == 2){
            binding.Apply.setVisibility(View.GONE);
        }
    }
    private void getJob(){
        Retrofit retrofit = Connection.getClient();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<PostJob> call = apiService.getJob(id,"Bearer "+token);

        call.enqueue(new Callback<PostJob>() {
            @Override
            public void onResponse(Call<PostJob> call, Response<PostJob> response) {
                if (!response.isSuccessful()){
                    Log.e("notSuccess",response.message());
                    return;
                }
                postJob = response.body();
                if (postJob != null){
                    Log.e("success",response.message());
                    Picasso.get().load(Connection.BASEURL+"/api/postjob/getdownload/"+postJob.getId()+"/"+postJob.getImage())
                            .into(binding.imgProJob);
                    binding.titleJob.setText(postJob.getTitle());
                    binding.nameCompany.setText(postJob.getCompany_name());
                    binding.tvEmail.setText(postJob.getEmail());
                    binding.term.setText(postJob.getTerm());
                    binding.requirement.setText(postJob.getRequirement());
                    binding.experience.setText(postJob.getExperience());
                    Picasso.get()
                            .load(Connection.BASEURL+"/api/user/getDownloadProfile/"+postJob.getUser_id()+"/"+postJob.getProfile())
                            .into(binding.imgPro);
                    binding.dateAgo.setText(DateUtils.covertTimeToText(postJob.getUpdated_at()));
                }
            }

            @Override
            public void onFailure(Call<PostJob> call, Throwable t) {
                Log.e("fail",t.getMessage());
            }
        });
    }
}