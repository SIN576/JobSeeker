package com.soysin.mobile.jobseeker.findJob;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.soysin.mobile.jobseeker.R;
import com.soysin.mobile.jobseeker.adapter.FindJobAdapter;
import com.soysin.mobile.jobseeker.model.FindJobModel;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FindJobFragment extends Fragment {

    AppCompatRadioButton btn_employee,btn_admin,btn_seeker;

    FindJobAdapter adapter;
    List<FindJobModel> findJobModels;
    RecyclerView recyclerView;

    RadioGroup radioGroup;
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

//        for (int i=1;i<5;i++){
//            FindJobModel findJobModel = new FindJobModel();
//            findJobModel.setNumber(i);
//            Log.e("Model: ",findJobModel.getNumber()+"" );
//            findJobModels.add(findJobModel);
//        }

        findJobModels = new ArrayList<>();
        for (int i=0;i<5;i++){
            findJobModels.add(new FindJobModel("soysin22@gmail.com"));
            findJobModels.add(new FindJobModel("khornsanit@gmail.com"));
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new FindJobAdapter(requireContext(),findJobModels);
        recyclerView.setAdapter(adapter);


//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                switch (checkedId) {
//                    case R.id.btn_employee:
//                        Toast.makeText(root.getContext(),checkedId+"",Toast.LENGTH_LONG).show();
//                        btn_employee.setTextColor(Color.WHITE);
//                        btn_admin.setTextColor(Color.BLACK);
//                        btn_seeker.setTextColor(Color.BLACK);
//                        break;
//                    case R.id.btn_admin:
//                        Toast.makeText(root.getContext(),checkedId+"",Toast.LENGTH_LONG).show();
//                        btn_admin.setTextColor(Color.WHITE);
//                        btn_employee.setTextColor(Color.BLACK);
//                        btn_seeker.setTextColor(Color.BLACK);
//                        break;
//                    case R.id.btn_seeker:
//                        Toast.makeText(root.getContext(),checkedId+"",Toast.LENGTH_LONG).show();
//                        btn_seeker.setTextColor(Color.WHITE);
//                        btn_employee.setTextColor(Color.BLACK);
//                        btn_admin.setTextColor(Color.BLACK);
//                        break;
//                }
//            }
//        });

        return root;
    }

    }
