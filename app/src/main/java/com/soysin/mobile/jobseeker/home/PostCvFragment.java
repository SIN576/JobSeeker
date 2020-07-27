package com.soysin.mobile.jobseeker.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.soysin.mobile.jobseeker.R;


public class PostCvFragment extends Fragment {
    View root;
    ImageView btn_back;

    public PostCvFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_post_cv, container, false);

        btn_back = root.findViewById(R.id.btn_post_cv_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_frame,new HomeFragment()).commit();
            }
        });
        return root;
    }
}