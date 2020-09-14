package com.soysin.mobile.jobseeker.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.soysin.mobile.jobseeker.apiconnection.Connection;
import com.soysin.mobile.jobseeker.databinding.ActivityAboutBinding;
import com.soysin.mobile.jobseeker.db.MyAppDatabase;
import com.soysin.mobile.jobseeker.db.MyDAO;
import com.soysin.mobile.jobseeker.model.Account;
import com.soysin.mobile.jobseeker.model.User;
import com.soysin.mobile.jobseeker.service.ApiService;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AboutActivity extends AppCompatActivity {

    ActivityAboutBinding binding;
    private User user;

    private Account account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutBinding.inflate(getLayoutInflater());
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
        if (account != null) {
            getUserData();
        }
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
                finish();
            }
        });
    }
    private void getUserData() {
        Retrofit retrofit = Connection.getClient();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<User> call = apiService.getUser((int) account.getAccountId(),"", "Bearer " + account.getToken());

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Log.e("not success", response.message());
                    return;
                }
                user = response.body();
                if (response.body() != null) {
                    Log.e("success", response.message());

                    binding.firstName.setText(user.getFirst_name());
                    binding.lastName.setText(user.getLast_name());
                    binding.Birth.setText(user.getBirth());
                    binding.accountInfoEmail.setText(user.getEmail());
                    binding.accountInfoPhoneNumber.setText(user.getPhone_number());
                    if(user.getRole()==2) {
                        binding.accountInfoCompanyName.setVisibility(View.VISIBLE);
                        binding.accountInfoCompanyName.setText(user.getCompany_name());
                    }
                    binding.accountInfoAddress.setText(user.getAddress());
                    return;
                }
                Log.e("not success", response.message());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("fail", t.getMessage());
            }
        });
    }
    private void update(){
        Retrofit retrofit = Connection.getClient();
        ApiService apiService = retrofit.create(ApiService.class);

        final MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        requestBodyBuilder.addFormDataPart("first_name",binding.firstName.getText().toString().trim());
        requestBodyBuilder.addFormDataPart("last_name",binding.lastName.getText().toString().trim());
        requestBodyBuilder.addFormDataPart("email",binding.accountInfoEmail.getText().toString());
        requestBodyBuilder.addFormDataPart("company_name",binding.accountInfoCompanyName.getText().toString());
        requestBodyBuilder.addFormDataPart("role",user.getRole()+"");
        requestBodyBuilder.addFormDataPart("birth",binding.Birth.getText().toString());
        requestBodyBuilder.addFormDataPart("phone_number",binding.accountInfoPhoneNumber.getText().toString());
        requestBodyBuilder.addFormDataPart("address",binding.accountInfoAddress.getText().toString());

        RequestBody requestBody=requestBodyBuilder.build();
        Call<User> call = apiService.update(requestBody,user.getId(),"Bearer "+account.getToken());

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    MyAppDatabase myAppDatabase = MyAppDatabase.getInstance(getApplicationContext());
                    MyDAO myDAO = myAppDatabase.getMyDao();
                    User user = response.body();
                    Account account = new Account(0,user.getApi_token(),user.getId(),user.getRole(),user.getProfile(),user.getUpdated_at());
                    myDAO.updateAccount(account);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("Successful",t.getMessage());
            }
        });
    }
}