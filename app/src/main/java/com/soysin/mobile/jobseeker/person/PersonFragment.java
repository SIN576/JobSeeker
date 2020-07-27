package com.soysin.mobile.jobseeker.person;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.soysin.mobile.jobseeker.R;
import com.soysin.mobile.jobseeker.adapter.ViewCVAdapter;
import com.soysin.mobile.jobseeker.model.FindJobModel;
import com.soysin.mobile.jobseeker.person.AccountInfoFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PersonFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<FindJobModel> findJobModels;
    private View root;
    private ViewCVAdapter adapter;
    ImageView btn_account_info;

    public PersonFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root= inflater.inflate(R.layout.fragment_person, container, false);
        recyclerView = root.findViewById(R.id.profile_recycler_view);
        btn_account_info = root.findViewById(R.id.btn_account_info);

        findJobModels = new ArrayList<>();
        findJobModels.add(new FindJobModel("soysin22@gmail.com"));
        findJobModels.add(new FindJobModel("vonseyha22@gmail.com"));
        findJobModels.add(new FindJobModel("khornsanit22@gmail.com"));
        findJobModels.add(new FindJobModel("pothsatyavotha22@gmail.com"));

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        adapter = new ViewCVAdapter(root.getContext(),findJobModels);
        recyclerView.setAdapter(adapter);

        btn_account_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_frame,new AccountInfoFragment()).commit();
            }
        });
        return root;
    }
}
