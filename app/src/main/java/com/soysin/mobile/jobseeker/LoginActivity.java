package com.soysin.mobile.jobseeker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.soysin.mobile.jobseeker.apiconnection.Connection;
import com.soysin.mobile.jobseeker.databinding.ActivityLoginBinding;
import com.soysin.mobile.jobseeker.db.MyAppDatabase;
import com.soysin.mobile.jobseeker.db.MyDAO;
import com.soysin.mobile.jobseeker.methods.Validate;
import com.soysin.mobile.jobseeker.model.Account;
import com.soysin.mobile.jobseeker.model.Login;
import com.soysin.mobile.jobseeker.service.ApiService;

import java.util.HashMap;
import java.util.Locale;
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
    int i = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

//        i = getIntent().getIntExtra("i", 1);
//        if (i == 1) {
//            getDialog();
//        }

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
                    myDAO.createAccount(new Account(0, login.getUser().getApi_token(), login.getUser().getId(), login.getUser().getRole(), login.getUser().getProfile(), login.getUser().getUpdated_at()));
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

    private void getDialog() {
        final String[] language = {"English", "ខ្មែរ"};
        final int itemChecked = 0;
        new MaterialAlertDialogBuilder(LoginActivity.this)
                .setTitle("Dialog")
                .setSingleChoiceItems(language, itemChecked, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            setLocale("en");
                        } else {
                            setLocale("km");
                        }
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void setLocale(String lang) {
        Log.e("lang", lang);
        Locale myLocale = new Locale(lang);

        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        SharedPreferences prefe = getSharedPreferences("settings", Activity.MODE_PRIVATE);
        prefe.edit().putString("MyLang", lang).apply();
//        Intent refresh = new Intent(this, LoginActivity.class);
//        finish();
//        refresh.putExtra("i", 2);
//        startActivity(refresh);
        recreate();
    }

    public void changeLanguage(View view) {
        String text = binding.btnLanguage.getText().toString().trim();
        if (text.equals("English")) {
            setLocale("en");
        } else {
            setLocale("km");
        }
    }
}
