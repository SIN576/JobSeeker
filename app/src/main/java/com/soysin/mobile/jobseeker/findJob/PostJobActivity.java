package com.soysin.mobile.jobseeker.findJob;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.soysin.mobile.jobseeker.NewJobActivity;
import com.soysin.mobile.jobseeker.apiconnection.Connection;
import com.soysin.mobile.jobseeker.databinding.ActivityPostJobBinding;
import com.soysin.mobile.jobseeker.db.MyAppDatabase;
import com.soysin.mobile.jobseeker.db.MyDAO;
import com.soysin.mobile.jobseeker.model.Account;
import com.soysin.mobile.jobseeker.model.PostJob;
import com.soysin.mobile.jobseeker.service.ApiService;
import com.soysin.mobile.jobseeker.util.FileUtils;
import com.soysin.mobile.jobseeker.GetRequestBody;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PostJobActivity extends AppCompatActivity {

    ActivityPostJobBinding binding;
    private static final int MY_PERMISSIONS_REQUEST = 100;
    private int PICK_IMAGE_FROM_GALLERY_REQUEST = 1;
    File file;
    Account account;
    Bundle bundle;
    PostJob postJob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostJobBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(ContextCompat.checkSelfPermission(PostJobActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(PostJobActivity.this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST);
        }
        MyAppDatabase myAppDatabase = MyAppDatabase.getInstance(getApplicationContext());
        MyDAO myDAO = myAppDatabase.getMyDao();
        account = myDAO.getAccount(0);


        if (getIntent().getExtras()!=null){
            bundle = getIntent().getBundleExtra("bundle_key");
            String action = bundle.getString("action");
            if (action.equals("edit")){
                edit();
                binding.btnPost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        update();
                    }
                });
            }
            else {
                binding.btnPost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        postJob(file);
                    }
                });
            }
        }

        binding.postJobButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(
                        Intent.createChooser(intent, "Select Picture"),
                        PICK_IMAGE_FROM_GALLERY_REQUEST
                );
            }
        });
    }
    private void update(){
        if(file != null){
            RequestBody requestBody = GetRequestBody.getRequestBody(file, (int) account.getAccountId());
        }
        else {
            Toast.makeText(getApplicationContext(),"not file",Toast.LENGTH_LONG).show();
        }
    }

    private void edit(){

        postJob = new PostJob((int) account.getAccountId(),bundle.getString("email"),
                bundle.getString("image"),bundle.getString("experience"),
                bundle.getString("phone_number"),bundle.getString("address"),
                bundle.getString("company_name"), bundle.getString("requirement"),
                bundle.getString("term"),bundle.getString("title"),
                bundle.getString("last_date"),bundle.getInt("id"));

        binding.titleApp.setText("Edit");
        binding.btnPost.setText("Save");
        binding.postJobButtonChooseImage.setText("change image");

        binding.edCompanyName.setText(postJob.getCompany_name());
        binding.edTerm.setText(postJob.getTerm());
        binding.edTitle.setText(postJob.getTitle());
        binding.edExperience.setText(bundle.getString("requirement"));
        binding.edLastDate.setText(postJob.getLast_date());
        binding.edAddress.setText(postJob.getAddress());
        binding.edEmail.setText(bundle.getString("email"));
        binding.edNumberPhone.setText(bundle.getString("phone_number"));
        Picasso.get()
                .load(Connection.BASEURL+"/api/postjob/getdownload/"+postJob.getId())
                .into(binding.postJobImage);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_FROM_GALLERY_REQUEST && resultCode == RESULT_OK && data != null && data.getData() !=null){
            Uri uri=data.getData();
            binding.postJobImage.setImageURI(uri);
            try {
                file= FileUtils.from(this,uri);
                Log.d("File-up",file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case MY_PERMISSIONS_REQUEST:{
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_DENIED){

                }else {

                }
                return;
            }
        }
    }
    private void postJob(File file){

        String et=file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".")+1);
        String mimeType= MimeTypeMap.getSingleton().getMimeTypeFromExtension(et);
        final RequestBody requestFile = RequestBody.create(file, MediaType.get(mimeType));
        final MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        MultipartBody.Part fileImage = MultipartBody.Part.createFormData("photo",file.getName(),requestFile);
        requestBodyBuilder.addPart(fileImage);
        requestBodyBuilder.addFormDataPart("user_id",account.getAccountId()+"");
        requestBodyBuilder.addFormDataPart("company_name",binding.postJobEdNameCompany.getEditText().getText().toString());
        requestBodyBuilder.addFormDataPart("term",binding.postJobEdTerm.getEditText().getText().toString());
        requestBodyBuilder.addFormDataPart("email",binding.postJobEdEmail.getEditText().getText().toString());
        requestBodyBuilder.addFormDataPart("late_date",binding.postJobEdLastDate.getEditText().getText().toString());
        requestBodyBuilder.addFormDataPart("title",binding.postJobEdTitle.getEditText().getText().toString());
        requestBodyBuilder.addFormDataPart("address",binding.postJobEdAddress.getEditText().getText().toString());
        requestBodyBuilder.addFormDataPart("number_phone",binding.postJobEdPhoneNumber.getEditText().getText().toString());
        requestBodyBuilder.addFormDataPart("requirement",binding.postJobEdRequirement.getEditText().getText().toString());

        Retrofit retrofit = Connection.getClient();
        ApiService apiService = retrofit.create(ApiService.class);

       RequestBody requestBody=requestBodyBuilder.build();
        Call<ResponseBody> call = apiService.createPostJob(requestBody,"Bearer "+account.getToken());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()){
                    Log.e("error: ",response.message());
                    return;
                }
                Log.e("success: ",response.message());
                Intent intent = new Intent(getApplicationContext(), NewJobActivity.class);
                intent.putExtra("token",account.getToken());
                intent.putExtra("id",account.getId());
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("error: ",t.getMessage());
            }
        });
    }
}