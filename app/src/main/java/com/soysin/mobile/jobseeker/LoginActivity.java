package com.soysin.mobile.jobseeker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.soysin.mobile.jobseeker.apiconnection.Connection;
import com.soysin.mobile.jobseeker.model.Login;
import com.soysin.mobile.jobseeker.service.ApiService;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    TextView btn_register,tv_test_email;
    ImageView btn_back;
    Button btn_login;
    TextInputLayout ed_username,ed_password;

    String email,password;

    ApiService apiService;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();
        btn_register = findViewById(R.id.btn_register);
        btn_login = findViewById(R.id.btn_login_login);
        ed_password = findViewById(R.id.ed_password);
        ed_username = findViewById(R.id.ed_username);

//        Connection connection = new Connection();
//
//        apiService = connection.retrofit.create(ApiService.class);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (! validateUsername() || validatePassword()){
                    Intent intent = new Intent(getApplicationContext(),AppBarActivity.class);
                    startActivity(intent);
                }

            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
    private boolean validateUsername(){
        email = ed_username.getEditText().getText().toString().trim();
        if (email.isEmpty()){
            ed_username.setError("please input username");
            return false;
        }else {
            return true;
        }
    }

    private boolean validatePassword(){
        password = ed_password.getEditText().getText().toString().trim();
        if (password.isEmpty()){
            ed_password.setError("please input password");
            return false;
        }else {
            return true;
        }
    }
    private void login(){
        Map<String,String> stringMap=new HashMap<>();
        stringMap.put("email",email);
        stringMap.put("password",password);
        Call<Login> loginCall = apiService.userLogin(stringMap);

        loginCall.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                Login login = response.body();
                if(login.getSucess() == 1){
                    Intent intent = new Intent(getApplicationContext(),AppBarActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(),"username and password incorrect",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
    }
}
