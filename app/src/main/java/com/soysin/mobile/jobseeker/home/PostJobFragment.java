package com.soysin.mobile.jobseeker.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.soysin.mobile.jobseeker.R;

import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;


public class PostJobFragment extends Fragment {

    ImageView btn_back,post_job_img;
    TextView btn_post;
    Button btn_choose_img;
    TextInputLayout ed_company_name,ed_requirement,ed_email,ed_number_phone,ed_address;
    private String company_name,requirement,email,number_phone,address;

    private static final int MY_PERMISSIONS_REQUEST = 100;
    private int PICK_IMAGE_FROM_GALLERY_REQUEST = 1;

    View root;
    public PostJobFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_post_job, container, false);
        btn_back = root.findViewById(R.id.btn_post_job_back);
        btn_post = root.findViewById(R.id.btn_post);
        ed_company_name = root.findViewById(R.id.post_job_ed_name_company);
        ed_requirement = root.findViewById(R.id.post_job_ed_requirement);
        ed_email = root.findViewById(R.id.post_job_ed_email);
        ed_number_phone = root.findViewById(R.id.post_job_ed_phone_number);
        ed_address = root.findViewById(R.id.post_job_ed_address);
        btn_choose_img = root.findViewById(R.id.post_job_button_choose_image);
        post_job_img = root.findViewById(R.id.post_job_image);

        if(ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST);
        }

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_frame,new HomeFragment()).commit();
            }
        });
        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postJob();
            }
        });

        btn_choose_img.setOnClickListener(new View.OnClickListener() {
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
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_FROM_GALLERY_REQUEST && resultCode == RESULT_OK && data != null && data.getData() !=null){
            Uri uri=data.getData();
            post_job_img.setImageURI(uri);

//            try {
//                File file= FileUtils.from(this,uri);
//                Log.d("File-up",file.getAbsolutePath());
//                uploadFile(file);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

        }
    }

    private void postJob(){
        company_name = ed_company_name.getEditText().getText().toString().trim();
        requirement = ed_requirement.getEditText().getText().toString().trim();
        email = ed_email.getEditText().getText().toString().trim();
        number_phone = ed_number_phone.getEditText().getText().toString().trim();
        address = ed_address.getEditText().getText().toString().trim();

    }
}