package com.soysin.mobile.jobseeker.findJob;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.soysin.mobile.jobseeker.R;
import com.soysin.mobile.jobseeker.adapter.FindJobAdapter;
import com.soysin.mobile.jobseeker.adapter.TypeOfJobAdapter;
import com.soysin.mobile.jobseeker.model.FindJobModel;
import com.soysin.mobile.jobseeker.model.TypeOfJob;

import java.util.ArrayList;
import java.util.List;


public class FindJobFragment extends Fragment {


    FindJobAdapter adapter;
    List<FindJobModel> findJobModels;
    RecyclerView recyclerView,recyclerView_vertical;
    View root;

    public FindJobFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        root = inflater.inflate(R.layout.fragment_find_job2, container, false);
        recyclerView = root.findViewById(R.id.recycler_view_find_job);
        recyclerView_vertical = root.findViewById(R.id.recycler_view_find_job_vertical);

        findJobModels = new ArrayList<>();
        for (int i=0;i<5;i++){
            findJobModels.add(new FindJobModel("soysin22@gmail.com"));
            findJobModels.add(new FindJobModel("khornsanit@gmail.com"));
        }

        List<TypeOfJob> typeOfJobs = new ArrayList<>();
        typeOfJobs.add(new TypeOfJob("https://psmarketingimages.s3.amazonaws.com/blog/wp-content/uploads/2018/03/30115641/picture-id831121290.jpg",
                "Accounting"));
        typeOfJobs.add(new TypeOfJob("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSCaxQ_qNy_V3CHrK9U5-fe5VHAcUUEgTmVCA&usqp=CAU",
                "Digital Marketing"));
        typeOfJobs.add(new TypeOfJob("https://s3-us-west-2.amazonaws.com/robogarden-new/Articles/upload/blogs/lg-lean-to-code-to-be-a-mobile-developer.jpg",
                "Mobile Developer"));
        typeOfJobs.add(new TypeOfJob("https://www.jenyalestina.com/blog/wp-content/uploads/2019/05/web-development.jpg",
                "Web Developer"));

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        TypeOfJobAdapter typeOfJobAdapter = new TypeOfJobAdapter(typeOfJobs,getActivity());
        recyclerView.setAdapter(typeOfJobAdapter);

        recyclerView_vertical.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new FindJobAdapter(getActivity(),findJobModels);
        recyclerView_vertical.setAdapter(adapter);


        return root;
    }

    }
