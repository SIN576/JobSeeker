package com.soysin.mobile.jobseeker.viewCV;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.soysin.mobile.jobseeker.R;
import com.soysin.mobile.jobseeker.adapter.TypeOfJobAdapter;
import com.soysin.mobile.jobseeker.adapter.ViewCVAdapter;
import com.soysin.mobile.jobseeker.model.FindJobModel;
import com.soysin.mobile.jobseeker.model.TypeOfJob;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewCVFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<FindJobModel> findJobModels;
    private View root;
    private ViewCVAdapter adapter;


    public ViewCVFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_view_c_v2, container, false);
        List<TypeOfJob> typeOfJobs = new ArrayList<>();

        typeOfJobs.add(new TypeOfJob("https://psmarketingimages.s3.amazonaws.com/blog/wp-content/uploads/2018/03/30115641/picture-id831121290.jpg",
                "Accounting"));
        typeOfJobs.add(new TypeOfJob("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSCaxQ_qNy_V3CHrK9U5-fe5VHAcUUEgTmVCA&usqp=CAU",
                "Digital Marketing"));
        typeOfJobs.add(new TypeOfJob("https://s3-us-west-2.amazonaws.com/robogarden-new/Articles/upload/blogs/lg-lean-to-code-to-be-a-mobile-developer.jpg",
                "Mobile Developer"));
        typeOfJobs.add(new TypeOfJob("https://www.jenyalestina.com/blog/wp-content/uploads/2019/05/web-development.jpg",
                "Web Developer"));
        recyclerView = root.findViewById(R.id.recycler_view_view_cv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        TypeOfJobAdapter typeOfJobAdapter = new TypeOfJobAdapter(typeOfJobs,getActivity());
        recyclerView.setAdapter(typeOfJobAdapter);

        RecyclerView recyclerView1 = root.findViewById(R.id.recycler_view_view_cv_vertical);
        List<FindJobModel> findJobModels = new ArrayList<>();
        findJobModels.add(new FindJobModel("soysin22@gmail.com"));
        findJobModels.add(new FindJobModel("vonseyha22@gmail.com"));
        findJobModels.add(new FindJobModel("khornsanit22@gmail.com"));
        findJobModels.add(new FindJobModel("pothsatyavotha22@gmail.com"));

        recyclerView1.setLayoutManager(new GridLayoutManager(getActivity(),2));
        ViewCVAdapter adapter = new ViewCVAdapter(getActivity(),findJobModels);
        recyclerView1.setAdapter(adapter);
        return root;
    }
}
