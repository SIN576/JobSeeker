package com.soysin.mobile.jobseeker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;

import com.soysin.mobile.jobseeker.db.MyAppDatabase;
import com.soysin.mobile.jobseeker.db.MyDAO;
import com.soysin.mobile.jobseeker.model.Account;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadLocale();
        MyAppDatabase myAppDatabase = MyAppDatabase.getInstance(this);
        MyDAO myDAO = myAppDatabase.getMyDao();

        final Account account = myDAO.getAccount(0);

        int SPLASH_SCREEN = 1000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (account == null) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), NewJobActivity.class);
                    intent.putExtra("token", account.getToken());
                    intent.putExtra("id", account.getId());
                    startActivity(intent);
                }
                finish();
            }
        }, SPLASH_SCREEN);

    }

    public void setLocale(String lang) {
        Log.e("lang", lang);
        Locale myLocale = new Locale(lang);

//        Locale.setDefault(myLocale);
//        Configuration config = new Configuration();
//        config.locale = myLocale;
//        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
//
//        SharedPreferences.Editor editor = getSharedPreferences("settings",MODE_PRIVATE).edit();
//        editor.putString("MyLang",lang);
//        editor.apply();
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    private void loadLocale() {
        SharedPreferences prefe = getSharedPreferences("settings", Activity.MODE_PRIVATE);
        String language = prefe.getString("MyLang", "");
        setLocale(language);
    }
}
