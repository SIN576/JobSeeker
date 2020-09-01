package com.soysin.mobile.jobseeker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.soysin.mobile.jobseeker.adapter.FindJobAdapter;
import com.soysin.mobile.jobseeker.adapter.ViewCVAdapter;
import com.soysin.mobile.jobseeker.apiconnection.Connection;
import com.soysin.mobile.jobseeker.databinding.ActivityProfileBinding;
import com.soysin.mobile.jobseeker.db.MyAppDatabase;
import com.soysin.mobile.jobseeker.db.MyDAO;
import com.soysin.mobile.jobseeker.findJob.JobDescriptionActivity;
import com.soysin.mobile.jobseeker.findJob.PostJobActivity;
import com.soysin.mobile.jobseeker.model.Account;
import com.soysin.mobile.jobseeker.model.Cv;
import com.soysin.mobile.jobseeker.model.PostJob;
import com.soysin.mobile.jobseeker.model.User;
import com.soysin.mobile.jobseeker.service.ApiService;
import com.soysin.mobile.jobseeker.util.FileUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileActivity extends AppCompatActivity implements FindJobAdapter.OnClickItemListener {

    ActivityProfileBinding binding;
    Account account;
    ViewCVAdapter viewCVAdapter;
    private static final int MY_PERMISSIONS_REQUEST = 100;
    private int PICK_IMAGE_FROM_GALLERY_REQUEST = 1;
    private int REQUEST_IMAGE_CAPTURE = 999;
    File file;
    private String currentImage;

    @Override
    protected void onResume() {
        super.onResume();

        MyAppDatabase myAppDatabase = MyAppDatabase.getInstance(getApplicationContext());
        MyDAO myDAO = myAppDatabase.getMyDao();
        account = myDAO.getAccount(0);

        if (account != null) {
            getUserData();
            Log.e("role",account.getAccountRole()+"");
            if (account.getAccountRole() == 2) {
                getPostJobUser();
            } else {
                getPostCvUser();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.rEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(intent);
            }
        });

        if (ContextCompat.checkSelfPermission(ProfileActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ProfileActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST);
        }
        MyAppDatabase myAppDatabase = MyAppDatabase.getInstance(getApplicationContext());
        MyDAO myDAO = myAppDatabase.getMyDao();
        account = myDAO.getAccount(0);

        binding.cameraEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(getBaseContext(), v);
                final MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.change_profile_menu, popup.getMenu());

                Object menuHelper;
                Class[] argTypes;
                try {
                    Field fMenuHelper = PopupMenu.class.getDeclaredField("mPopup");
                    fMenuHelper.setAccessible(true);
                    menuHelper = fMenuHelper.get(popup);
                    argTypes = new Class[]{boolean.class};
                    menuHelper.getClass().getDeclaredMethod("setForceShowIcon", argTypes).invoke(menuHelper, true);
                } catch (Exception e) {

                }
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_take_photo:
                                openCamera();
                                return true;
                            case R.id.action_select_photo:
                                openGallery();
                                return true;
                            default:
                                return false;
                        }

                    }
                });
                popup.show();
            }
        });

    }

    private void openGallery() {
        Intent intent = new Intent();

        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(
                Intent.createChooser(intent, "Select Picture"),
                PICK_IMAGE_FROM_GALLERY_REQUEST
        );
    }

    private void openCamera() {
        currentImage = FileUtils.createImageFile(this);
        if (currentImage != null) {
            File file = new File(currentImage);
            Uri photoURI = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", file);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Uri uriImg = Uri.fromFile(new File(currentImage));
            binding.profileImage1.setImageURI(uriImg);
            try {
                file = FileUtils.from(this, uriImg);
                updateProfile(file);
                Log.d("File-up", file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Uri uri = data.getData();
            binding.profileImage1.setImageURI(uri);
            try {
                file = FileUtils.from(this, uri);
                updateProfile(file);
                Log.d("File-up", file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateProfile(File file) {

        String et = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".") + 1);
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(et);
        final RequestBody requestFile = RequestBody.create(file, MediaType.get(mimeType));
        final MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        MultipartBody.Part fileImage = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);

        Log.e("path", file.getAbsolutePath());
        requestBodyBuilder.addPart(fileImage);
        requestBodyBuilder.addFormDataPart("id", account.getAccountId() + "");

        Retrofit retrofit = Connection.getClient();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<User> call = apiService.updatedProfile(requestBodyBuilder.build(), "Bearer " + account.getToken());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Log.e("not success profile", response.message());
                }
                MyAppDatabase myAppDatabase = MyAppDatabase.getInstance(getApplicationContext());
                MyDAO myDAO = myAppDatabase.getMyDao();
                User user = response.body();
                Account account = new Account(0,user.getApi_token(),user.getId(),user.getRole(),user.getProfile());
                myDAO.updateAccount(account);
                Log.e("success profile", response.message());
                Toast.makeText(getApplicationContext(), "Success full !", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }

    private void getPostJobUser() {
        Retrofit retrofit = Connection.getClient();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<List<PostJob>> call = apiService.getPostJobUser((int) account.getAccountId(), "Bearer " + account.getToken());

        call.enqueue(new Callback<List<PostJob>>() {
            @Override
            public void onResponse(Call<List<PostJob>> call, Response<List<PostJob>> response) {
                if (!response.isSuccessful()) {
                    Log.e("not success getPost", response.message());
                }
                Log.e("success", response.message());
                if (response.body() != null) {
                    binding.recyclerViewProfile.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    FindJobAdapter findJobAdapter = new FindJobAdapter(getApplicationContext(), response.body(),account.getToken());
                    binding.recyclerViewProfile.setAdapter(findJobAdapter);
                    findJobAdapter.setOnClickItemListener(ProfileActivity.this);
                }
            }

            @Override
            public void onFailure(Call<List<PostJob>> call, Throwable t) {
                Log.e("fail success", t.getMessage());
            }
        });

    }

    private void getUserData() {
        Retrofit retrofit = Connection.getClient();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<User> call = apiService.getUser((int) account.getAccountId(), "Bearer " + account.getToken());

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Log.e("not success", response.message());
                    return;
                }
                User user = response.body();
                if (response.body() != null) {
                    Log.e("success", response.message());

                    binding.accountInfoEmail.setText(user.getEmail());
                    binding.accountInfoAddress.setText(user.getAddress());
                    binding.tvNameProfile.setText(user.getFirst_name() + " " + user.getLast_name());
                    if (user.getProfile() != null) {
                        Picasso.get()
                                .load(Connection.BASEURL+"/api/user/getDownloadProfile/"+user.getId()+"/"+user.getProfile())
                                .into(binding.profileImage1);
                    }
                    return;
                }
                Log.e("not success", response.message());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("fail", t.getMessage());
            }
        });
    }

    private void getPostCvUser() {
        Retrofit retrofit = Connection.getClient();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<List<Cv>> call = apiService.getPostCvUser((int) account.getAccountId(), "Bearer " + account.getToken());

        call.enqueue(new Callback<List<Cv>>() {
            @Override
            public void onResponse(Call<List<Cv>> call, Response<List<Cv>> response) {
                if (!response.isSuccessful()) {
                    Log.e("not success", response.message());
                    return;
                }
                List<Cv> cvList = response.body();

                if (cvList != null) {
                    binding.recyclerViewProfile.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
                    viewCVAdapter = new ViewCVAdapter(getApplicationContext(), cvList,account.getToken());
                    binding.recyclerViewProfile.setAdapter(viewCVAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Cv>> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }

    @Override
    public void onItemClick(PostJob postJob) {
        Intent intent = new Intent(getApplicationContext(), JobDescriptionActivity.class);
        intent.putExtra("id",postJob.getId());
        intent.putExtra("token", account.getToken());
        startActivity(intent);
    }
}