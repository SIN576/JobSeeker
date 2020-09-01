package com.soysin.mobile.jobseeker.findJob;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.soysin.mobile.jobseeker.R;
import com.soysin.mobile.jobseeker.TypeOfJobDetailActivity;
import com.soysin.mobile.jobseeker.adapter.FindJobAdapter;
import com.soysin.mobile.jobseeker.adapter.TypeOfJobAdapter;
import com.soysin.mobile.jobseeker.apiconnection.Connection;
import com.soysin.mobile.jobseeker.databinding.FragmentFindJob2Binding;
import com.soysin.mobile.jobseeker.db.MyAppDatabase;
import com.soysin.mobile.jobseeker.db.MyDAO;
import com.soysin.mobile.jobseeker.model.Account;
import com.soysin.mobile.jobseeker.model.Data;
import com.soysin.mobile.jobseeker.model.FilterData;
import com.soysin.mobile.jobseeker.model.PostJob;
import com.soysin.mobile.jobseeker.model.PostJobPagination;
import com.soysin.mobile.jobseeker.model.TypeOfJob;
import com.soysin.mobile.jobseeker.service.ApiService;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
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
    List<PostJob> postJobs,postJobs1,postJobs2;
    RecyclerView recyclerView_vertical;
    View root;
    Account account;
    private String token,text=null,province = null,title=null;
    private int page = 1, limit, itemChecked= 0, number;



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
        binding.progressBar.setVisibility(View.VISIBLE);
        recyclerView_vertical = root.findViewById(R.id.recycler_view_find_job_vertical);

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.typeOfJob, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(arrayAdapter);
        binding.spinner.setOnItemSelectedListener(this);



//        binding.searchJob.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                //filter(s.toString());
//                title = s.toString();
//                getPostJob(text);
//            }
//        });

        getTitle();

        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = binding.searchJob.getText().toString().trim();
                page = 1;
                getPostJob(text);
            }
        });
        binding.nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()){
                    page++;
                    binding.progressBar.setVisibility(View.VISIBLE);
                    if (page <= limit){
                        getPostJob(text);
                    }else {
                        binding.progressBar.setVisibility(View.GONE);
                    }

                }
            }
        });

        binding.btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemChecked = number;
                getLocation();
            }
        });
        return root;
    }
    private void getTitle(){
        ApiService  apiService= Connection.getClient().create(ApiService.class);
        Call<List<FilterData>> listCall = apiService.getTitle();

        listCall.enqueue(new Callback<List<FilterData>>() {
            @Override
            public void onResponse(Call<List<FilterData>> call, Response<List<FilterData>> response) {
                if (response.isSuccessful() && response.body()!=null){
                    List<FilterData> filterData = response.body();
                    List<String> titles = new ArrayList<>();

                    for (int i=0;i<filterData.size();i++){
                        titles.add(filterData.get(i).getTitle());
                    }

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),
                            android.R.layout.simple_list_item_1,titles);
                    binding.searchJob.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<FilterData>> call, Throwable t) {

            }
        });
    }

    private void getLocation(){
        final String[] items = Data.COUNTRIES;
        new MaterialAlertDialogBuilder(getActivity())
                .setTitle("Dialog")
                .setSingleChoiceItems(items, itemChecked, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        province = items[which];
                        number = which;
                    }
                })
                .setPositiveButton("Ok", /* listener = */ new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        binding.filterTitle.setText(province);
                        page = 1;
                        getPostJob(text);
                    }
                })
                .setNegativeButton("Cancel", /* listener = */ new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        province = null;
                    }
                })
                .show();
    }

    private void getPostJob(String term) {
        Retrofit retrofit = Connection.getClient();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<PostJobPagination> listCall = apiService.getPostJob("Bearer " + account.getToken(),term,province,title,page);
        listCall.enqueue(new Callback<PostJobPagination>() {
            @Override
            public void onResponse(Call<PostJobPagination> call, Response<PostJobPagination> response) {
                if (!response.isSuccessful()) {
                    Log.e("eerror", response.message());
                }
                if(response.body().getData().isEmpty()){
                    
                    binding.tvNoData.setVisibility(View.VISIBLE);
                    binding.recyclerViewFindJobVertical.setVisibility(View.GONE);
                }
                limit = response.body().getLast_page();
                if (response.isSuccessful() && response.body() != null && page <= limit) {
                    try {
                        postJobs = response.body().getData();
//                        postJobs.addAll(response.body().getData());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    if (!postJobs.isEmpty()){
                        binding.tvNoData.setVisibility(View.GONE);
                        binding.recyclerViewFindJobVertical.setVisibility(View.VISIBLE);
                        if (page == 1){
                            binding.progressBar.setVisibility(View.GONE);
                            recyclerView_vertical.setLayoutManager(new LinearLayoutManager(getActivity()));
                            adapter = new FindJobAdapter(getActivity(), response.body().getData());
                            recyclerView_vertical.setAdapter(adapter);
                            adapter.setOnClickItemListener(FindJobFragment.this);
                        }else {
                            adapter.addList(response.body().getData());
                        }

                    }


                }
            }

            @Override
            public void onFailure(Call<PostJobPagination> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }

    @Override
    public void onItemClick(PostJob postJob) {
        Intent intent = new Intent(getActivity(), JobDescriptionActivity.class);
        intent.putExtra("id", postJob.getId());
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
            text = null;
            page = 1;
            getPostJob(text);
        }else {
            Log.e("term",text);
            page = 1;
            getPostJob(text);
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
