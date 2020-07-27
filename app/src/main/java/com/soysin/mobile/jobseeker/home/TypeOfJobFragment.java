package com.soysin.mobile.jobseeker.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.soysin.mobile.jobseeker.R;
import com.soysin.mobile.jobseeker.adapter.TypeOfJobAdapter;
import com.soysin.mobile.jobseeker.model.TypeOfJob;

import java.util.ArrayList;
import java.util.List;


public class TypeOfJobFragment extends Fragment {

    List<TypeOfJob> typeOfJobs;
    View root;
    RecyclerView recyclerView;
    TypeOfJobAdapter adapter;
    ImageView btn_back;

    public TypeOfJobFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_type_of_job, container, false);
        recyclerView = root.findViewById(R.id.type_of_job_recyclerview);
        typeOfJobs = new ArrayList<>();
        typeOfJobs.add(new TypeOfJob("https://psmarketingimages.s3.amazonaws.com/blog/wp-content/uploads/2018/03/30115641/picture-id831121290.jpg",
                "Accounting"));
        typeOfJobs.add(new TypeOfJob("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSCaxQ_qNy_V3CHrK9U5-fe5VHAcUUEgTmVCA&usqp=CAU",
                "Digital Marketing"));
        typeOfJobs.add(new TypeOfJob("https://s3-us-west-2.amazonaws.com/robogarden-new/Articles/upload/blogs/lg-lean-to-code-to-be-a-mobile-developer.jpg",
                "Mobile Developer"));
        typeOfJobs.add(new TypeOfJob("https://www.jenyalestina.com/blog/wp-content/uploads/2019/05/web-development.jpg",
                "Web Developer"));

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        adapter = new TypeOfJobAdapter(typeOfJobs,getActivity());
        recyclerView.setAdapter(adapter);

        btn_back = root.findViewById(R.id.btn_type_of_job_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_frame,new HomeFragment()).commit();
            }
        });
        return root;
    }
}