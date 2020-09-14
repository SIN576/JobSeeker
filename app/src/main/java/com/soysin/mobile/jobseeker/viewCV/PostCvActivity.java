package com.soysin.mobile.jobseeker.viewCV;

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

import com.soysin.mobile.jobseeker.R;
import com.soysin.mobile.jobseeker.methods.Validate;
import com.soysin.mobile.jobseeker.apiconnection.Connection;
import com.soysin.mobile.jobseeker.databinding.ActivityPostCvBinding;
import com.soysin.mobile.jobseeker.db.MyAppDatabase;
import com.soysin.mobile.jobseeker.db.MyDAO;
import com.soysin.mobile.jobseeker.model.Account;
import com.soysin.mobile.jobseeker.model.Cv;
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

public class PostCvActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST = 100;
    private int PICK_CODE_REQUEST = 1;
    File file;
    ActivityPostCvBinding binding;
    Account account;
    Bundle bundle;
   Cv cv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostCvBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(ContextCompat.checkSelfPermission(PostCvActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(PostCvActivity.this,
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
                binding.btnPostCv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ( Validate.empty(binding.postJobEdTitle,"please input title") &&
                                Validate.empty(binding.postJobEdExperience,"please input experience") &&
                                Validate.empty(binding.postJobEdEmail,"please input email") &&
                                Validate.empty(binding.postJobEdPhoneNumber,"please input phone number"))
                        {
                            update();
                            finish();
                        }
                    }
                });
            }
            else {
                binding.btnPostCv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Validate.checkFile(file, PostCvActivity.this) &&
                        Validate.empty(binding.postJobEdTitle,"please input title") &&
                        Validate.empty(binding.postJobEdExperience,"please input experience") &&
                        Validate.empty(binding.postJobEdEmail,"please input email") &&
                        Validate.empty(binding.postJobEdPhoneNumber,"please input phone number")){
                            postCv(file);
                            finish();
                        }

                    }
                });
            }
        }

        binding.postJobButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(
                        Intent.createChooser(intent,"Select PDF"),
                        PICK_CODE_REQUEST);
            }
        });
    }
    private void edit(){

        cv = new Cv(bundle.getString("phoneNumber"),bundle.getString("email"),
                bundle.getString("experience"),bundle.getString("title"),
                bundle.getString("pdf"),bundle.getInt("id"));
        binding.titleApp.setText(getResources().getText(R.string.edit));
        binding.btnPostCv.setText(getResources().getText(R.string.save));
        binding.postJobButtonChooseImage.setText(getResources().getText(R.string.choose_cv));

        binding.edTitle.setText(bundle.getString("title"));
        binding.edExperience.setText(bundle.getString("experience"));
        binding.edEmail.setText(bundle.getString("email"));
        binding.edNumberPhone.setText(bundle.getString("phoneNumber"));
        Picasso.get()
                .load("https://lh3.googleusercontent.com/W1Jwfw3dKIo8BsQFaLc0y4UflpgSUlDKiWn4LgjKXFW1Uxj1t8qfwYu987CnBDWdsENT")
                .into(binding.postJobImage);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CODE_REQUEST && resultCode == RESULT_OK && data != null ){
            Uri selectPDF = data.getData();
            Picasso.get()
                    .load("https://lh3.googleusercontent.com/W1Jwfw3dKIo8BsQFaLc0y4UflpgSUlDKiWn4LgjKXFW1Uxj1t8qfwYu987CnBDWdsENT")
                    .into(binding.postJobImage);
            try {
                file= FileUtils.from(this,selectPDF);
                Log.d("File-up",file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void  update(){
        if (file != null){
            Log.e("filePath",file.getAbsolutePath());
            String et=file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".")+1);
            String mimeType= MimeTypeMap.getSingleton().getMimeTypeFromExtension(et);
            final RequestBody requestFile = RequestBody.create(file, MediaType.get(mimeType));

            final MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            MultipartBody.Part fileImage = MultipartBody.Part.createFormData("file",file.getName(),requestFile);

            requestBodyBuilder.addPart(fileImage);
            requestBodyBuilder.addFormDataPart("user_id",account.getAccountId()+"");
            requestBodyBuilder.addFormDataPart("title",binding.postJobEdTitle.getEditText().getText().toString());
            requestBodyBuilder.addFormDataPart("experience",binding.postJobEdExperience.getEditText().getText().toString());
            requestBodyBuilder.addFormDataPart("description",binding.postJobEdExperience.getEditText().getText().toString());

            Call<ResponseBody> call = Connection.getClient().create(ApiService.class).updateNotFile(cv.getId(),"Bearer "+account.getToken(),requestBodyBuilder.build());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (!response.isSuccessful()){

                    }
                    finish();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        }else {
            final MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

            requestBodyBuilder.addFormDataPart("user_id",account.getAccountId()+"");
            requestBodyBuilder.addFormDataPart("pdf",cv.getPdf());
            requestBodyBuilder.addFormDataPart("title",binding.postJobEdTitle.getEditText().getText().toString());
            requestBodyBuilder.addFormDataPart("experience",binding.postJobEdExperience.getEditText().getText().toString());
            requestBodyBuilder.addFormDataPart("description",binding.postJobEdExperience.getEditText().getText().toString());

            Call<ResponseBody> call = Connection.getClient().create(ApiService.class).updateNotFile(cv.getId(),"Bearer "+account.getToken(),requestBodyBuilder.build());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (!response.isSuccessful()){

                    }
                    finish();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }
    private void postCv(File file){
        String et=file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".")+1);
        String mimeType= MimeTypeMap.getSingleton().getMimeTypeFromExtension(et);
        final RequestBody requestFile = RequestBody.create(file, MediaType.get(mimeType));

        final MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        MultipartBody.Part fileImage = MultipartBody.Part.createFormData("file",file.getName(),requestFile);

        requestBodyBuilder.addPart(fileImage);
        requestBodyBuilder.addFormDataPart("user_id",account.getAccountId()+"");
        requestBodyBuilder.addFormDataPart("title",binding.postJobEdTitle.getEditText().getText().toString());
        requestBodyBuilder.addFormDataPart("experience",binding.postJobEdTitle.getEditText().getText().toString());
        requestBodyBuilder.addFormDataPart("description",binding.postJobEdTitle.getEditText().getText().toString());

        Retrofit retrofit = Connection.getClient();
        ApiService apiService = retrofit.create(ApiService.class);

        Call<ResponseBody> call = apiService.createPostCv(requestBodyBuilder.build(),"Bearer "+account.getToken());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!response.isSuccessful()){
                    Log.e("notSuccess: ",response.message());
                }
                Log.e("success: ",response.message());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("fail: ",t.getMessage());
            }
        });
    }
}