package com.soysin.mobile.jobseeker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.soysin.mobile.jobseeker.apiconnection.Connection;
import com.soysin.mobile.jobseeker.databinding.ActivityLoginBinding;
import com.soysin.mobile.jobseeker.db.MyAppDatabase;
import com.soysin.mobile.jobseeker.db.MyDAO;
import com.soysin.mobile.jobseeker.model.Account;
import com.soysin.mobile.jobseeker.model.Login;
import com.soysin.mobile.jobseeker.service.ApiService;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    Button btn_login;
    TextInputLayout ed_username, ed_password;

    String email, password;

    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        btn_login = findViewById(R.id.btn_login_login);
        ed_password = findViewById(R.id.ed_password);
        ed_username = findViewById(R.id.ed_username);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Validate.empty(ed_username, "please input username") && Validate.empty(ed_password, "please input password")) {
                    login();
                }
            }
        });
        binding.loginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login() {
        Retrofit retrofit = Connection.getClient();
        apiService = retrofit.create(ApiService.class);
        email = ed_username.getEditText().getText().toString().trim();
        password = ed_password.getEditText().getText().toString().trim();
        Map<String, String> stringMap = new HashMap<>();
        stringMap.put("email", email);
        stringMap.put("password", password);
        Call<Login> loginCall = apiService.userLogin(stringMap);

        loginCall.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "errorr: " + response.message(), Toast.LENGTH_LONG).show();
                }
                Log.d("Login", response.isSuccessful() + "");
                Toast.makeText(getApplicationContext(), "fails: " + response.message(), Toast.LENGTH_LONG).show();
                Login login = response.body();
                if (login != null && login.getSuccess() == 1) {
                    MyAppDatabase myAppDatabase = MyAppDatabase.getInstance(getApplicationContext());
                    MyDAO myDAO = myAppDatabase.getMyDao();
                    myDAO.createAccount(new Account(0, login.getUser().getApi_token(), login.getUser().getId(), login.getUser().getRole(), login.getUser().getProfile()));
                    Intent intent = new Intent(getApplicationContext(), NewJobActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "error: don't have account ", Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
