package com.soysin.mobile.jobseeker;

import android.util.Log;
import android.webkit.MimeTypeMap;

import com.soysin.mobile.jobseeker.apiconnection.Connection;
import com.soysin.mobile.jobseeker.databinding.ActivityPostJobBinding;
import com.soysin.mobile.jobseeker.service.ApiService;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

public class GetRequestBody {

    private static ActivityPostJobBinding binding;
    public static RequestBody requestBody;

    public static RequestBody getRequestBody(File file,int id,String term){
        String et=file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".")+1);
        String mimeType= MimeTypeMap.getSingleton().getMimeTypeFromExtension(et);
        final RequestBody requestFile = RequestBody.create(file, MediaType.get(mimeType));
        final MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        MultipartBody.Part fileImage = MultipartBody.Part.createFormData("photo",file.getName(),requestFile);
        requestBodyBuilder.addPart(fileImage);
        requestBodyBuilder.addFormDataPart("user_id",id+"");
        Log.e("company_name",binding.postJobEdNameCompany.getEditText().getText().toString());
        requestBodyBuilder.addFormDataPart("company_name",binding.postJobEdNameCompany.getEditText().getText().toString());
        requestBodyBuilder.addFormDataPart("term",term);
        requestBodyBuilder.addFormDataPart("email",binding.postJobEdEmail.getEditText().getText().toString());
        requestBodyBuilder.addFormDataPart("late_date",binding.postJobEdLastDate.getEditText().getText().toString());
        requestBodyBuilder.addFormDataPart("title",binding.postJobEdTitle.getEditText().getText().toString());
        requestBodyBuilder.addFormDataPart("address",binding.postJobEdAddress.getEditText().getText().toString());
        requestBodyBuilder.addFormDataPart("number_phone",binding.postJobEdPhoneNumber.getEditText().getText().toString());
        requestBodyBuilder.addFormDataPart("requirement",binding.postJobEdRequirement.getEditText().getText().toString());

        Retrofit retrofit = Connection.getClient();
        ApiService apiService = retrofit.create(ApiService.class);

        requestBody=requestBodyBuilder.build();
        return requestBody;
    }
}
