package com.soysin.mobile.jobseeker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.soysin.mobile.jobseeker.apiconnection.Connection;
import com.soysin.mobile.jobseeker.service.ApiService;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    TextView btn_login;
    ImageView btn_back;
    String gender;
    RadioButton r_male,r_female;
    RadioGroup radioGroup;

    Button btn_register;

    AwesomeValidation awesomeValidation;
    EditText ed_r_full_name,ed_r_email,ed_r_password,ed_r_conform_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();

        btn_login = findViewById(R.id.btn_login_register);;
        radioGroup = findViewById(R.id.r_group);
        ed_r_full_name = findViewById(R.id.ed_full_name_register);
        ed_r_email = findViewById(R.id.ed_email_register);
        ed_r_password = findViewById(R.id.ed_password_register);
        ed_r_conform_password = findViewById(R.id.ed_conform_password_register);
        btn_register = findViewById(R.id.btn_register_register);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";
        awesomeValidation.addValidation(this,R.id.ed_full_name_register, RegexTemplate.NOT_EMPTY,R.string.invalid_username);
        awesomeValidation.addValidation(this,R.id.ed_email_register, Patterns.EMAIL_ADDRESS,R.string.invalid_email);
        awesomeValidation.addValidation(this,R.id.ed_password_register,regexPassword,R.string.invalid_password);
        awesomeValidation.addValidation(this,R.id.ed_conform_password_register,R.id.ed_password_register,R.string.conform_password);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (awesomeValidation.validate()){
                    register();
                }
                else {
                    Toast.makeText(getApplicationContext(),"faild successfully",Toast.LENGTH_LONG).show();
                }
            }
        });


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    public void onClickRadioButtonRegister(View view){
            boolean isSelected = ((AppCompatRadioButton)view).isChecked();

            switch (view.getId()){
                case R.id.r_male:
                    gender = "male";
                    break;
                case R.id.r_female:
                    gender = "female";
                    break;
            }
    }
    private void register(){
        Connection connection = new Connection();

        ApiService apiService = connection.retrofit.create(ApiService.class);

        RequestBody username =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, ed_r_full_name.getText().toString());
        RequestBody email =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, ed_r_email.getText().toString());
        RequestBody password =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, ed_r_password.getText().toString());

        Call<ResponseBody> call = apiService.userRegister(username,
                email,
                password);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()){
                    Log.e("error: ",response.message());
                    return;
                }
                Intent intent = new Intent(getApplicationContext(),AppBarActivity.class);
                startActivity(intent);

                Log.e("success:",response.message());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("error: ",t.getMessage());
            }
        });
    }
}
