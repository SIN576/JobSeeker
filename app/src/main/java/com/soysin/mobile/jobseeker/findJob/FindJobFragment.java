package com.soysin.mobile.jobseeker.findJob;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.soysin.mobile.jobseeker.R;
import com.soysin.mobile.jobseeker.TypeOfJobDetailActivity;
import com.soysin.mobile.jobseeker.adapter.FindJobAdapter;
import com.soysin.mobile.jobseeker.adapter.TypeOfJobAdapter;
import com.soysin.mobile.jobseeker.apiconnection.Connection;
import com.soysin.mobile.jobseeker.databinding.FragmentFindJob2Binding;
import com.soysin.mobile.jobseeker.db.MyAppDatabase;
import com.soysin.mobile.jobseeker.db.MyDAO;
import com.soysin.mobile.jobseeker.model.Account;
import com.soysin.mobile.jobseeker.model.PostJob;
import com.soysin.mobile.jobseeker.model.TypeOfJob;
import com.soysin.mobile.jobseeker.service.ApiService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class FindJobFragment extends Fragment implements FindJobAdapter.OnClickItemListener, TypeOfJobAdapter.OnClickItemListener, AdapterView.OnItemSelectedListener {


    FindJobAdapter adapter;
    List<PostJob> postJobs;
    RecyclerView recyclerView_vertical;
    View root;
    Account account;
    private String token,text;


    FragmentFindJob2Binding binding;

    public FindJobFragment() {
        //this.account = account;
        // Required empty public constructor
    }

    public static FindJobFragment newInstance(String token) {
        FindJobFragment f = new FindJobFragment();
        Bundle args = new Bundle();
        args.putString("token", token);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        token = getArguments().getString("token");
        MyAppDatabase myAppDatabase = MyAppDatabase.getInstance(requireContext());
        MyDAO myDAO = myAppDatabase.getMyDao();
        account = myDAO.getAccount(0);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //account = new Account(1, token, 2L, 1, "jj");
        binding = FragmentFindJob2Binding.inflate(inflater, container, false);
//        recyclerView = root.findViewById(R.id.recycler_view_find_job);
        root = binding.getRoot();
        recyclerView_vertical = root.findViewById(R.id.recycler_view_find_job_vertical);

        TextView editText = root.findViewById(R.id.search_job);

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.typeOfJob, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(arrayAdapter);
        binding.spinner.setOnItemSelectedListener(this);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchJobActivity.class);
                intent.putExtra("token", account.getToken());
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

//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
//        typeOfJobAdapter = new TypeOfJobAdapter(typeOfJobs, getActivity());
//        recyclerView.setAdapter(typeOfJobAdapter);
//        typeOfJobAdapter.setOnClickItemListener(this);

        getPostJob();
        return root;
    }

    private void getPostJob() {
        Retrofit retrofit = Connection.getClient();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<PostJob>> listCall = apiService.getPostJob("Bearer " + account.getToken(),null);
        listCall.enqueue(new Callback<List<PostJob>>() {
            @Override
            public void onResponse(Call<List<PostJob>> call, Response<List<PostJob>> response) {
                if (!response.isSuccessful()) {
                    Log.e("error", response.message());
                }
                postJobs = response.body();
                Log.e("PostJob",postJobs.get(1).getCompany_name().toString());
                if (postJobs != null) {
                    recyclerView_vertical.setLayoutManager(new LinearLayoutManager(getActivity()));
                    adapter = new FindJobAdapter(getActivity(), postJobs);
                    recyclerView_vertical.setAdapter(adapter);
                    adapter.setOnClickItemListener(FindJobFragment.this);
                }
            }

            @Override
            public void onFailure(Call<List<PostJob>> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), JobDescriptionActivity.class);
        intent.putExtra("id", postJobs.get(position).getId());
        intent.putExtra("token", account.getToken());
        startActivity(intent);
    }

    @Override
    public void onClickJobType(TypeOfJob typeOfJob) {
        Intent intent = new Intent(getActivity(), TypeOfJobDetailActivity.class);
        intent.putExtra("title", typeOfJob.getJob_status());
        intent.putExtra("type", "job");
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
         text = parent.getItemAtPosition(position).toString();
        if (text.equals("All")){
            getPostJob();
        }else {
            getPostJobByTerm(text);
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void getPostJobByTerm(String term){
        Retrofit retrofit = Connection.getClient();
        ApiService apiService = retrofit.create(ApiService.class);
        final MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        requestBodyBuilder.addFormDataPart("term",term);

        RequestBody requestBody = requestBodyBuilder.build();

        Call<List<PostJob>> listCall = apiService.getJobByTerm(requestBody,"Bearer " + account.getToken());
        listCall.enqueue(new Callback<List<PostJob>>() {
            @Override
            public void onResponse(Call<List<PostJob>> call, Response<List<PostJob>> response) {
                if (!response.isSuccessful()) {
                    Log.e("error", response.message());
                }
                postJobs = response.body();
                if (postJobs != null) {
                    recyclerView_vertical.setLayoutManager(new LinearLayoutManager(getActivity()));
                    adapter = new FindJobAdapter(getActivity(), postJobs);
                    recyclerView_vertical.setAdapter(adapter);
                    adapter.setOnClickItemListener(FindJobFragment.this);
                }
            }

            @Override
            public void onFailure(Call<List<PostJob>> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });

    }
}
