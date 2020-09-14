package com.soysin.mobile.jobseeker.findJob;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Toast;

import com.soysin.mobile.jobseeker.NewJobActivity;
import com.soysin.mobile.jobseeker.methods.Project;
import com.soysin.mobile.jobseeker.R;
import com.soysin.mobile.jobseeker.methods.Validate;
import com.soysin.mobile.jobseeker.apiconnection.Connection;
import com.soysin.mobile.jobseeker.databinding.ActivityPostJobBinding;
import com.soysin.mobile.jobseeker.db.MyAppDatabase;
import com.soysin.mobile.jobseeker.db.MyDAO;
import com.soysin.mobile.jobseeker.model.Account;
import com.soysin.mobile.jobseeker.model.Data;
import com.soysin.mobile.jobseeker.model.PostJob;
import com.soysin.mobile.jobseeker.service.ApiService;
import com.soysin.mobile.jobseeker.util.FileUtils;
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

public class PostJobActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ActivityPostJobBinding binding;
    private static final int MY_PERMISSIONS_REQUEST = 100;
    private int PICK_IMAGE_FROM_GALLERY_REQUEST = 1;
    File file;
    Account account;
    Bundle bundle;
    PostJob postJob;
    private String term="Full time";
    private DatePickerDialog datePickerDialog;

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
        if (ContextCompat.checkSelfPermission(PostJobActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PostJobActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST);
        }
        MyAppDatabase myAppDatabase = MyAppDatabase.getInstance(getApplicationContext());
        MyDAO myDAO = myAppDatabase.getMyDao();
        account = myDAO.getAccount(0);

        View view = getWindow().getDecorView().findViewById(android.R.id.content);
//
//        String[] COUNTRIES = new String[] {"Full time", "Last time", "Internship", "Other"};

        if (getIntent().getExtras() != null) {

            Project.dropDrown(PostJobActivity.this,binding.filledExposedDropdown,Data.TERM);
            Project.dropDrown(PostJobActivity.this,binding.aTextView, Data.COUNTRIES);
            bundle = getIntent().getBundleExtra("bundle_key");
            String action = bundle.getString("action");
            if (action.equals("edit")) {
                edit();
                Project.dropDrown(PostJobActivity.this,binding.filledExposedDropdown,Data.TERM);
                Project.dropDrown(PostJobActivity.this,binding.aTextView,Data.COUNTRIES);
                binding.btnPost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Validate.empty(binding.postJobEdNameCompany,"please input company name") &&
                                Validate.empty(binding.term,"please choose term")&&
                                Validate.empty(binding.postJobEdTitle,"please input title")&&
                                Validate.empty(binding.postJobEdRequirement,"please input requirement")&&
                                Validate.empty(binding.postJobEdLastDate,"please pick last date")&&
                                Validate.empty(binding.postJobEdEmail,"please input email") &&
                                Validate.empty(binding.location,"please choose location")&&
                                Validate.empty(binding.postJobEdPhoneNumber,"please input phone number")){
                            update();

                        }
                    }
                });
            } else {
                binding.btnPost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Validate.checkFile(file,PostJobActivity.this)&&
                        Validate.empty(binding.postJobEdNameCompany,"please input company name") &&
                        Validate.empty(binding.term,"please choose term")&&
                        Validate.empty(binding.postJobEdTitle,"please input title")&&
                        Validate.empty(binding.postJobEdRequirement,"please input requirement")&&
                        Validate.empty(binding.postJobEdLastDate,"please pick last date")&&
                        Validate.empty(binding.postJobEdEmail,"please input email") &&
                        Validate.empty(binding.location,"please choose location")&&
                        Validate.empty(binding.postJobEdPhoneNumber,"please input phone number")){
                            postJob(file);

                        }
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

        binding.postJobEdLastDate.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Project.pickDate(PostJobActivity.this, binding.edLastDate,datePickerDialog);
            }
        });

    }
    private void update(){

        ApiService apiService = Connection.getClient().create(ApiService.class);


        if(file != null){
            String et=file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".")+1);
            String mimeType= MimeTypeMap.getSingleton().getMimeTypeFromExtension(et);
            final RequestBody requestFile = RequestBody.create(file, MediaType.get(mimeType));
            final MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            MultipartBody.Part fileImage = MultipartBody.Part.createFormData("photo",file.getName(),requestFile);
            requestBodyBuilder.addPart(fileImage);
            requestBodyBuilder.addFormDataPart("user_id",postJob.getUser_id()+"");
            requestBodyBuilder.addFormDataPart("company_name",binding.postJobEdNameCompany.getEditText().getText().toString());
            requestBodyBuilder.addFormDataPart("term",binding.term.getEditText().getText().toString().trim());
            requestBodyBuilder.addFormDataPart("title",binding.postJobEdTitle.getEditText().getText().toString());
            requestBodyBuilder.addFormDataPart("requirement",binding.postJobEdRequirement.getEditText().getText().toString());
            requestBodyBuilder.addFormDataPart("email",binding.postJobEdEmail.getEditText().getText().toString());
            requestBodyBuilder.addFormDataPart("address",binding.location.getEditText().getText().toString());
            requestBodyBuilder.addFormDataPart("phone_number",binding.postJobEdPhoneNumber.getEditText().getText().toString());
            requestBodyBuilder.addFormDataPart("last_date",binding.postJobEdLastDate.getEditText().getText().toString());
            RequestBody requestBody1=requestBodyBuilder.build();
            //final RequestBody requestBody = GetRequestBody.getRequestBody(file, (int) account.getAccountId(),PostJobActivity.this);
            Call<ResponseBody> call = apiService.updateJobFile(postJob.getId(),"Bearer "+account.getToken(),
                    requestBody1);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (!response.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"not success",Toast.LENGTH_LONG);
                    }else{
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("fail",t.getMessage());
                }
            });
        }
        else {
            final MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            requestBodyBuilder.addFormDataPart("user_id",postJob.getUser_id()+"");
            requestBodyBuilder.addFormDataPart("company_name",binding.postJobEdNameCompany.getEditText().getText().toString());
            requestBodyBuilder.addFormDataPart("term",binding.term.getEditText().getText().toString().trim());
            requestBodyBuilder.addFormDataPart("title",binding.postJobEdTitle.getEditText().getText().toString());
            requestBodyBuilder.addFormDataPart("requirement",binding.postJobEdRequirement.getEditText().getText().toString());
            requestBodyBuilder.addFormDataPart("email",binding.postJobEdEmail.getEditText().getText().toString());
            requestBodyBuilder.addFormDataPart("address",binding.location.getEditText().getText().toString());
            requestBodyBuilder.addFormDataPart("phone_number",binding.postJobEdPhoneNumber.getEditText().getText().toString());
            requestBodyBuilder.addFormDataPart("last_date",binding.postJobEdLastDate.getEditText().getText().toString());
            RequestBody requestBody1=requestBodyBuilder.build();

            Call<ResponseBody> call = apiService.updateJobNotFile(postJob.getId(),"Bearer "+account.getToken(),
                    requestBody1);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (!response.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"not success",Toast.LENGTH_LONG);
                    }else{
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("fail",t.getMessage());
                }
            });
        }
    }

    private void edit(){

        postJob = new PostJob((int) account.getAccountId(),bundle.getString("email"),
                bundle.getString("phone_number"),bundle.getString("experience"),
                bundle.getString("company_name"),bundle.getString("address"),
                bundle.getString("last_date"),bundle.getString("requirement"),
                bundle.getString("term"),bundle.getString("title"),
                bundle.getString("image"),bundle.getInt("id"));

        binding.titleApp.setText(getResources().getText(R.string.edit));
        binding.btnPost.setText(getResources().getText(R.string.save));
        binding.postJobButtonChooseImage.setText(getResources().getText(R.string.change_image));

        term = postJob.getTerm();
        binding.edCompanyName.setText(postJob.getCompany_name());
        binding.filledExposedDropdown.setText(term);
        binding.edTitle.setText(postJob.getTitle());
        binding.edExperience.setText(bundle.getString("requirement"));
        binding.edLastDate.setText(postJob.getLast_date());
        binding.aTextView.setText(postJob.getAddress());
        binding.edEmail.setText(bundle.getString("email"));
        binding.edNumberPhone.setText(bundle.getString("phone_number"));
        Picasso.get()
                .load(Connection.BASEURL+"/api/postjob/getdownload/"+postJob.getId()+"/"+postJob.getImage())
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
        requestBodyBuilder.addFormDataPart("term",term);
        requestBodyBuilder.addFormDataPart("email",binding.postJobEdEmail.getEditText().getText().toString());
        requestBodyBuilder.addFormDataPart("last_date",binding.postJobEdLastDate.getEditText().getText().toString());
        Log.e("late_date",binding.postJobEdLastDate.getEditText().getText().toString());
        requestBodyBuilder.addFormDataPart("title",binding.postJobEdTitle.getEditText().getText().toString());
        requestBodyBuilder.addFormDataPart("address",binding.location.getEditText().getText().toString());
        requestBodyBuilder.addFormDataPart("phone_number",binding.postJobEdPhoneNumber.getEditText().getText().toString());
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        term = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}