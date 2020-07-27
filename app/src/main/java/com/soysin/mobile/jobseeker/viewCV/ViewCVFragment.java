package com.soysin.mobile.jobseeker.viewCV;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.soysin.mobile.jobseeker.R;
import com.soysin.mobile.jobseeker.adapter.ViewCVAdapter;
import com.soysin.mobile.jobseeker.model.FindJobModel;

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

        recyclerView = root.findViewById(R.id.view_cv_recycler_view);
        findJobModels = new ArrayList<>();
        findJobModels.add(new FindJobModel("soysin22@gmail.com"));
        findJobModels.add(new FindJobModel("vonseyha22@gmail.com"));
        findJobModels.add(new FindJobModel("khornsanit22@gmail.com"));
        findJobModels.add(new FindJobModel("pothsatyavotha22@gmail.com"));

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        adapter = new ViewCVAdapter(root.getContext(),findJobModels);
        recyclerView.setAdapter(adapter);
        return root;
    }
}
