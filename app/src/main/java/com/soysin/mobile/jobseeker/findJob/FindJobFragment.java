package com.soysin.mobile.jobseeker.findJob;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


import com.soysin.mobile.jobseeker.JobDescriptionActivity;
import com.soysin.mobile.jobseeker.R;
import com.soysin.mobile.jobseeker.RegisterActivity;
import com.soysin.mobile.jobseeker.SearchJobActivity;
import com.soysin.mobile.jobseeker.adapter.FindJobAdapter;
import com.soysin.mobile.jobseeker.adapter.TypeOfJobAdapter;
import com.soysin.mobile.jobseeker.apiconnection.Connection;
import com.soysin.mobile.jobseeker.model.Account;
import com.soysin.mobile.jobseeker.model.FindJobModel;
import com.soysin.mobile.jobseeker.model.PostJob;
import com.soysin.mobile.jobseeker.model.TypeOfJob;
import com.soysin.mobile.jobseeker.service.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class FindJobFragment extends Fragment implements FindJobAdapter.OnClickItemListener {


    FindJobAdapter adapter;
    List<PostJob> postJobs;
    RecyclerView recyclerView,recyclerView_vertical;
    View root;
    Account account;

    public FindJobFragment(Account account) {
        this.account=account;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        root = inflater.inflate(R.layout.fragment_find_job2, container, false);
        recyclerView = root.findViewById(R.id.recycler_view_find_job);
        recyclerView_vertical = root.findViewById(R.id.recycler_view_find_job_vertical);

        TextView editText = root.findViewById(R.id.search_job);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchJobActivity.class);
                intent.putExtra("token",account.getToken());
                startActivity(intent);
            }
        });

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

//        recyclerView_vertical.setLayoutManager(new LinearLayoutManager(getActivity()));
//        adapter = new FindJobAdapter(getActivity(),findJobModels);
//        recyclerView_vertical.setAdapter(adapter);

        getPostJob();
        return root;
    }

    private void getPostJob(){
        Retrofit retrofit = Connection.getClient();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<PostJob>> listCall = apiService.getPostJob("Bearer "+account.getToken());
        listCall.enqueue(new Callback<List<PostJob>>() {
            @Override
            public void onResponse(Call<List<PostJob>> call, Response<List<PostJob>> response) {
                if (!response.isSuccessful()){
                    Log.e("error",response.message());
                }
                 postJobs = response.body();
                if (postJobs != null){
                    recyclerView_vertical.setLayoutManager(new LinearLayoutManager(getActivity()));
                    adapter = new FindJobAdapter(getActivity(),postJobs);
                    recyclerView_vertical.setAdapter(adapter);
                    adapter.setOnClickItemListener(FindJobFragment.this);
                }
            }

            @Override
            public void onFailure(Call<List<PostJob>> call, Throwable t) {
                Log.e("error",t.getMessage());
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), JobDescriptionActivity.class);
        intent.putExtra("id",account.getId());
        intent.putExtra("token",account.getToken());
        startActivity(intent);
    }
}
