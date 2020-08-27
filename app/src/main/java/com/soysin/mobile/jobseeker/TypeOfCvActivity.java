package com.soysin.mobile.jobseeker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.soysin.mobile.jobseeker.adapter.ViewCVAdapter;
import com.soysin.mobile.jobseeker.apiconnection.Connection;
import com.soysin.mobile.jobseeker.databinding.ActivityTypeOfCvBinding;
import com.soysin.mobile.jobseeker.db.MyAppDatabase;
import com.soysin.mobile.jobseeker.db.MyDAO;
import com.soysin.mobile.jobseeker.model.Account;
import com.soysin.mobile.jobseeker.model.Cv;
import com.soysin.mobile.jobseeker.service.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TypeOfCvActivity extends AppCompatActivity {

    ActivityTypeOfCvBinding binding;
    Account account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTypeOfCvBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        MyAppDatabase myAppDatabase = MyAppDatabase.getInstance(getApplicationContext());
        MyDAO myDAO = myAppDatabase.getMyDao();
        account = myDAO.getAccount(0);

        getTypeOfCv();


    }
    private void getTypeOfCv(){
        Retrofit retrofit = Connection.getClient();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<List<Cv>> call = apiService.getTypeOfCv("w","Bearer "+account.getToken());

        call.enqueue(new Callback<List<Cv>>() {
            @Override
            public void onResponse(Call<List<Cv>> call, Response<List<Cv>> response) {
                if (!response.isSuccessful()){
                    Log.e("not success",response.message());
                }
                if (response.body() != null){
                    binding.recyclerViewTypeOfCv.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
                    ViewCVAdapter adapter = new ViewCVAdapter(getApplicationContext(),response.body());
                    binding.recyclerViewTypeOfCv.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Cv>> call, Throwable t) {

            }
        });
    }
}