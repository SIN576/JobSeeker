package com.soysin.mobile.jobseeker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.soysin.mobile.jobseeker.apiconnection.Connection;
import com.soysin.mobile.jobseeker.databinding.ActivityRegisterBinding;
import com.soysin.mobile.jobseeker.db.MyAppDatabase;
import com.soysin.mobile.jobseeker.db.MyDAO;
import com.soysin.mobile.jobseeker.model.Account;
import com.soysin.mobile.jobseeker.model.Login;
import com.soysin.mobile.jobseeker.service.ApiService;

import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {

    TextView btn_login;
    RadioGroup radioGroup;

    Button btn_register;

    int role;

    ActivityRegisterBinding binding;
    AwesomeValidation awesomeValidation;
    EditText ed_r_email,ed_r_password,ed_r_conform_password;
    private DatePickerDialog.OnDateSetListener onDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        btn_login = findViewById(R.id.btn_login_register);;
        radioGroup = findViewById(R.id.r_group);
        ed_r_email = findViewById(R.id.ed_email_register);
        ed_r_password = findViewById(R.id.ed_password_register);
        ed_r_conform_password = findViewById(R.id.ed_conform_password_register);
        btn_register = findViewById(R.id.btn_register_register);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

//        String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";
//        awesomeValidation.addValidation(this,R.id.ed_last_name_register, RegexTemplate.NOT_EMPTY,R.string.invalid_username);
//        awesomeValidation.addValidation(this,R.id.ed_email_register, Patterns.EMAIL_ADDRESS,R.string.invalid_email);
//        awesomeValidation.addValidation(this,R.id.ed_password_register,regexPassword,R.string.invalid_password);
//        awesomeValidation.addValidation(this,R.id.ed_conform_password_register,R.id.ed_password_register,R.string.conform_password);

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
                   role = 2;
                   binding.companyName.setVisibility(View.VISIBLE);
                    break;
                case R.id.r_female:
                    role = 3;
                    binding.companyName.setVisibility(View.GONE);
                    break;
            }
    }
    private void register(){

        Retrofit retrofit = Connection.getClient();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<Login> call = apiService.userRegister(binding.edFirstNameRegister.getText().toString(),
                binding.edLastNameRegister.getText().toString(),
                binding.edNumberPhoneRegister.getText().toString(),
                binding.edEmailRegister.getText().toString(),
                binding.edPasswordRegister.getText().toString(),
                binding.edConformPasswordRegister.getText().toString(),
                "Trendsec Solution",
                "jdha",
                "20 May 2000",role);

        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if (!response.isSuccessful()){
                    Log.e("error", response.message());
                    return;
                }
                Login login = response.body();
                if (response.body()!= null && login.getSuccess() != 0){
                    MyAppDatabase myAppDatabase = MyAppDatabase.getInstance(getApplicationContext());
                    MyDAO myDAO = myAppDatabase.getMyDao();

                    myDAO.createAccount(new Account(0,login.getUser().getApi_token(),login.getUser().getId(),login.getUser().getRole(),login.getUser().getProfile()));

                    Intent intent = new Intent(getApplicationContext(),NewJobActivity.class);
                    startActivity(intent);
                }
                Toast.makeText(getApplicationContext(),response.message(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }
}
