package com.soysin.mobile.jobseeker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.soysin.mobile.jobseeker.databinding.ActivityNewJobBinding;
import com.soysin.mobile.jobseeker.findJob.FindJobFragment;
import com.soysin.mobile.jobseeker.viewCV.ViewCVFragment;
import com.squareup.picasso.Picasso;


public class NewJobActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    ActivityNewJobBinding binding;
    TextView textView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewJobBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        textView = findViewById(R.id.title_app);
        drawerLayout = findViewById(R.id.drawer_home);
        navigationView = findViewById(R.id.navi_home);
        toolbar = findViewById(R.id.toolbar);
        ImageView img_pro = findViewById(R.id.img_pro);

        Picasso.get().load("https://images.pexels.com/photos/220453/pexels-photo-220453.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500")
                .into(img_pro);

        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.upload_job_id);
        menuItem.setVisible(false);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportFragmentManager().beginTransaction().replace(R.id.layout_frame,new FindJobFragment()).commit();

        binding.navigationBar.setOnNavigationItemSelectedListener(navigation);


        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawerOpen,R.string.drawerClose);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navigation = new
            BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment fragment = null;
                    switch (item.getItemId()){
                        case R.id.item_home:
                            textView.setText("New Jobs");
                            fragment = new FindJobFragment();
                            break;
                        case R.id.item_view_cv:
                            textView.setText("View Cv");
                            fragment = new ViewCVFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.layout_frame,fragment).commit();
                    return true;
                }
            };

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.view_cv_id:
                Toast.makeText(getApplicationContext(),"Home menu is selected",Toast.LENGTH_LONG).show();
                break;
            case R.id.type_of_job_id:
                Toast.makeText(getApplicationContext(),"Exit menu is selected",Toast.LENGTH_LONG).show();
                break;
            case R.id.type_of_Cv_id:
                Toast.makeText(getApplicationContext(),"Profile menu is selected",Toast.LENGTH_LONG).show();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START,true);
        return false;
    }
}