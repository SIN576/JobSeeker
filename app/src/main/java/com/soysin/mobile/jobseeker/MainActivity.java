package com.soysin.mobile.jobseeker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.soysin.mobile.jobseeker.db.MyAppDatabase;
import com.soysin.mobile.jobseeker.db.MyDAO;
import com.soysin.mobile.jobseeker.model.Account;

public class MainActivity extends AppCompatActivity {

    AppCompatRadioButton btn_employee,btn_admin,btn_seeker;
    Button btn_login,btn_register;
    private static int SPLASH_SCREEN = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyAppDatabase myAppDatabase = MyAppDatabase.getInstance(this);
        MyDAO myDAO = myAppDatabase.getMyDao();

        final Account account = myDAO.getAccount(0);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(account ==null){
                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(getApplicationContext(),NewJobActivity.class);
                    intent.putExtra("token",account.getToken());
                    intent.putExtra("id",account.getId());
                    startActivity(intent);
                }
                finish();
            }
        },SPLASH_SCREEN);

    }

}
