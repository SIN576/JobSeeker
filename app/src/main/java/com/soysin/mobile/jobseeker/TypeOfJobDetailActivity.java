package com.soysin.mobile.jobseeker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.soysin.mobile.jobseeker.adapter.FindJobAdapter;
import com.soysin.mobile.jobseeker.apiconnection.Connection;
import com.soysin.mobile.jobseeker.databinding.ActivityTypeOfJobDetailBinding;
import com.soysin.mobile.jobseeker.db.MyAppDatabase;
import com.soysin.mobile.jobseeker.db.MyDAO;
import com.soysin.mobile.jobseeker.model.Account;
import com.soysin.mobile.jobseeker.model.PostJob;
import com.soysin.mobile.jobseeker.service.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TypeOfJobDetailActivity extends AppCompatActivity {

    ActivityTypeOfJobDetailBinding binding;
    Account account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTypeOfJobDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        MyAppDatabase myAppDatabase = MyAppDatabase.getInstance(getApplicationContext());
        MyDAO myDAO = myAppDatabase.getMyDao();
        account = myDAO.getAccount(0);

        binding.typeOfJob.setText(getIntent().getStringExtra("title"));

        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getTypeOfJob();
    }
    private void getTypeOfJob(){
        Retrofit retrofit = Connection.getClient();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<List<PostJob>> call = apiService.getTypeOfJob("w","Bearer "+account.getToken());

        call.enqueue(new Callback<List<PostJob>>() {
            @Override
            public void onResponse(Call<List<PostJob>> call, Response<List<PostJob>> response) {
                if (!response.isSuccessful()){
                    Log.e("not success",response.message());
                }
                if (response.body() != null){
                    binding.recyclerViewTypeOfJob.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    FindJobAdapter findJobAdapter = new FindJobAdapter(getApplicationContext(),response.body());
                    binding.recyclerViewTypeOfJob.setAdapter(findJobAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<PostJob>> call, Throwable t) {

            }
        });
    }
}