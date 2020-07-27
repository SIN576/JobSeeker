package com.soysin.mobile.jobseeker.home;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.soysin.mobile.jobseeker.R;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    Button btn_post_job,btn_post_cv,btn_type_of_job,btn_type_of_cv;
    View root;
    ImageView img_post_job,img_post_cv,img_type_of_job,img_type_cv;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_home2, container, false);

        btn_post_job = root.findViewById(R.id.btn_post_job);
        btn_post_cv = root.findViewById(R.id.btn_post_cv);
        btn_type_of_cv = root.findViewById(R.id.btn_type_of_cv);
        btn_type_of_job = root.findViewById(R.id.btn_type_of_job);
        img_post_job = root.findViewById(R.id.image_post_job);
        img_post_cv = root.findViewById(R.id.image_post_cv);
        img_type_cv = root.findViewById(R.id.type_of_cv);
        img_type_of_job = root.findViewById(R.id.type_of_job);

        Picasso.get().load("https://bongpheak.com/images/how-it-works/search-job.jpg").into(img_post_job);
        Picasso.get().load("https://officialcv.com/cv/img/core-img/logo.png").into(img_post_cv);
        Picasso.get().load("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcTiB_D-82EkM3Pto5n_7CkMATnnVJMUj168Vw&usqp=CAU").into(img_type_cv);
        Picasso.get().load("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcR_hpFaeDfOpZpo_qRcvPdCGEBTINC9AzbZCQ&usqp=CAU").into(img_type_of_job);

        btn_post_job.setOnClickListener(this);
        btn_post_cv.setOnClickListener(this);
        btn_type_of_cv.setOnClickListener(this);
        btn_type_of_job.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_post_job:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_frame,new PostJobFragment()).commit();
                break;
            case R.id.btn_post_cv:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_frame,new PostCvFragment()).commit();
                break;
            case R.id.btn_type_of_job:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_frame,new TypeOfJobFragment()).commit();
                break;
            case R.id.btn_type_of_cv:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_frame,new TypeOfCvFragment()).commit();
                break;
        }
    }
}
