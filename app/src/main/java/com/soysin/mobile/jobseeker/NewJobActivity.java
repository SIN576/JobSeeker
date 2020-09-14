package com.soysin.mobile.jobseeker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.soysin.mobile.jobseeker.apiconnection.Connection;
import com.soysin.mobile.jobseeker.databinding.ActivityNewJobBinding;
import com.soysin.mobile.jobseeker.db.MyAppDatabase;
import com.soysin.mobile.jobseeker.db.MyDAO;
import com.soysin.mobile.jobseeker.findJob.FindJobFragment;
import com.soysin.mobile.jobseeker.findJob.PostJobActivity;
import com.soysin.mobile.jobseeker.model.Account;
import com.soysin.mobile.jobseeker.model.User;
import com.soysin.mobile.jobseeker.profile.ProfileActivity;
import com.soysin.mobile.jobseeker.service.ApiService;
import com.soysin.mobile.jobseeker.viewCV.PostCvActivity;
import com.soysin.mobile.jobseeker.viewCV.ViewCVFragment;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class NewJobActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String CHANNEL_ID = "notification";
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    ActivityNewJobBinding binding;
    TextView textView;
    Account account;
    private TextView tvUsername, email;
    private ImageView imgPro;
    ImageView img_pro;
    private FindJobFragment findJobFragment;
    private ViewCVFragment viewCVFragment;

    @Override
    protected void onResume() {
        super.onResume();

        MyAppDatabase myAppDatabase = MyAppDatabase.getInstance(getApplicationContext());
        MyDAO myDAO = myAppDatabase.getMyDao();
        account = myDAO.getAccount(0);

        Picasso.get().load(Connection.BASEURL + "/api/user/getDownloadProfile/" + account.getAccountId() + "/" + account.getNameImageProfile())
                .into(imgPro);

        Picasso.get().load(Connection.BASEURL + "/api/user/getDownloadProfile/" + account.getAccountId() + "/" + account.getNameImageProfile())
                .into(img_pro);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("on-create", "on");
        binding = ActivityNewJobBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        textView = findViewById(R.id.title_app);
        drawerLayout = findViewById(R.id.drawer_home);
        navigationView = findViewById(R.id.navi_home);
        toolbar = findViewById(R.id.toolbar);
        img_pro = findViewById(R.id.img_pro);

        MyAppDatabase myAppDatabase = MyAppDatabase.getInstance(getApplicationContext());
        MyDAO myDAO = myAppDatabase.getMyDao();
        account = myDAO.getAccount(0);
        getUserData();

        View header = binding.naviHome.getHeaderView(0);
        tvUsername = header.findViewById(R.id.tvName);

        email = header.findViewById(R.id.tv_email_header);

        imgPro = header.findViewById(R.id.profile_image);

        Menu menu = navigationView.getMenu();
        if (account.getAccountRole() == 2) {
            MenuItem menuItem = menu.findItem(R.id.upload_job_Cv_id);
            menuItem.setVisible(false);
            MenuItem menuItem1 = menu.findItem(R.id.upload_job_id);
            menuItem1.setVisible(true);
        } else if (account.getAccountRole() == 1) {
            MenuItem menuItem = menu.findItem(R.id.upload_job_id);
            menuItem.setVisible(true);
            MenuItem menuItem1 = menu.findItem(R.id.upload_job_Cv_id);
            menuItem1.setVisible(true);
        } else {
            MenuItem menuItem = menu.findItem(R.id.upload_job_id);
            menuItem.setVisible(false);
            MenuItem menuItem1 = menu.findItem(R.id.upload_job_Cv_id);
            menuItem1.setVisible(true);
        }

        createNotificationChannel();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        findJobFragment = FindJobFragment.newInstance(account.getToken());
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_frame, findJobFragment).commit();

        binding.navigationBar.setOnNavigationItemSelectedListener(navigation);

        binding.imgPro.setOnClickListener(new View.OnClickListener() {
            private static final String CHANNEL_ID = "job";

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);

                // Create an explicit intent for an Activity in your app

//                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
//                        .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
//                        .setContentTitle("My notification")
//                        .setContentText("Much longer text that cannot fit one line...")
//                        .setStyle(new NotificationCompat.BigTextStyle()
//                                .bigText("Much longer text that cannot fit one line..."))
//                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
//
//// notificationId is a unique int for each notification that you must define
//                notificationManager.notify(0, builder.build());
            }
        });


        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawerOpen, R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        registerToken();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void registerToken() {

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("NewJob", "getInstanceId failed", task.getException());
                            return;
                        }
                        if (task.getResult() != null) {
                            // Get new Instance ID token
                            String token = task.getResult().getToken();
                            Log.d("FCM-Token", token + "");
                        }
                    }
                });
    }

    private void getUserData() {
        Retrofit retrofit = Connection.getClient();
        ApiService apiService = retrofit.create(ApiService.class);

        retrofit2.Call<User> call = apiService.getUser((int) account.getAccountId(), account.getUpdatedAt(), "Bearer " + account.getToken());

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(retrofit2.Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Log.e("not success", response.message());
                    return;
                }
                User user = response.body();
                tvUsername.setText(user.getFirst_name() + " " + user.getLast_name());
                email.setText(user.getEmail());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("fail", t.getMessage());
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigation = new
            BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment fragment = null;
                    switch (item.getItemId()) {
                        case R.id.item_home:
                            textView.setText(getResources().getText(R.string.new_jobs));
                            Log.d("create", "new-ffm");
                            if (findJobFragment == null) {
                                findJobFragment = FindJobFragment.newInstance(account.getToken());
                            }
                            fragment = findJobFragment;
                            break;
                        case R.id.item_view_cv:
                            textView.setText("View Cv");
                            if (viewCVFragment == null) {
                                viewCVFragment = ViewCVFragment.getInstance();
                            }
                            fragment = viewCVFragment;
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.layout_frame, fragment).commit();
                    return true;
                }
            };

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.view_cv_id:
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.upload_job_id:
                Intent intent1 = new Intent(this, PostJobActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("action", "post");
                intent1.putExtra("bundle_key", bundle);
                startActivity(intent1);
                break;
            case R.id.upload_job_Cv_id:
                Intent intent4 = new Intent(this, PostCvActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("action", "post");
                intent4.putExtra("bundle_key", bundle1);
                startActivity(intent4);
                break;
            case R.id.type_of_Cv_id:
                Toast.makeText(getApplicationContext(), "Profile menu is selected", Toast.LENGTH_LONG).show();
                break;
            case R.id.log_out_id:
                Log.e("log out: ", "true");
                MyDAO myDAO = MyAppDatabase.getInstance(getApplicationContext()).getMyDao();
                myDAO.deleteAccount(account);
                Intent intent12 = new Intent(getApplicationContext(), LoginActivity.class);
                intent12.putExtra("i", 2);
                startActivity(intent12);
                finish();
                break;
            case R.id.language:
                getDialog();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START, true);
        return false;
    }

    private void getDialog() {
        final String[] language = {"English", "ខ្មែរ"};
        final int itemChecked = 0;
        new MaterialAlertDialogBuilder(NewJobActivity.this)
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
        Intent refresh = new Intent(this, NewJobActivity.class);
        finish();
        startActivity(refresh);
    }

    private void loadLocale() {
        SharedPreferences prefe = getSharedPreferences("settings", Activity.MODE_PRIVATE);
        String language = prefe.getString("MyLang", "");
        setLocale(language);
    }
}