package com.soysin.mobile.jobseeker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.soysin.mobile.jobseeker.findJob.FindJobFragment;
import com.soysin.mobile.jobseeker.home.HomeFragment;
import com.soysin.mobile.jobseeker.home.PostJobFragment;
import com.soysin.mobile.jobseeker.home.TypeOfJobFragment;
import com.soysin.mobile.jobseeker.person.AccountInfoFragment;
import com.soysin.mobile.jobseeker.person.PersonFragment;
import com.soysin.mobile.jobseeker.viewCV.ViewCVFragment;

public class AppBarActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_bar);

        bottomNavigationView = findViewById(R.id.navigation_bar);
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_frame,new HomeFragment()).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(navigation);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navigation = new
            BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment fragment = null;
                    switch (item.getItemId()){
                        case R.id.item_home:
                            fragment = new HomeFragment();
                            break;
                        case R.id.item_find_job:
                            fragment = new FindJobFragment();
                            break;
                        case R.id.item_view_cv:
                            fragment = new ViewCVFragment();
                            break;
                        case R.id.item_person:
                            fragment = new PersonFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.layout_frame,fragment).commit();
                    return true;
                }
            };
}
