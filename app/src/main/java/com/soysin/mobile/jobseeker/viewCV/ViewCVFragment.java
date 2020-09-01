package com.soysin.mobile.jobseeker.viewCV;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.soysin.mobile.jobseeker.ProfileActivity;
import com.soysin.mobile.jobseeker.R;
import com.soysin.mobile.jobseeker.TypeOfCvActivity;
import com.soysin.mobile.jobseeker.TypeOfJobDetailActivity;
import com.soysin.mobile.jobseeker.ViewCVDetailActivity;
import com.soysin.mobile.jobseeker.adapter.FindJobAdapter;
import com.soysin.mobile.jobseeker.adapter.TypeOfJobAdapter;
import com.soysin.mobile.jobseeker.adapter.ViewCVAdapter;
import com.soysin.mobile.jobseeker.apiconnection.Connection;
import com.soysin.mobile.jobseeker.databinding.FragmentViewCV2Binding;
import com.soysin.mobile.jobseeker.findJob.FindJobFragment;
import com.soysin.mobile.jobseeker.model.Account;
import com.soysin.mobile.jobseeker.model.Cv;
import com.soysin.mobile.jobseeker.model.FilterData;
import com.soysin.mobile.jobseeker.model.FindJobModel;
import com.soysin.mobile.jobseeker.model.PostCvPagination;
import com.soysin.mobile.jobseeker.model.TypeOfJob;
import com.soysin.mobile.jobseeker.service.ApiService;

import java.util.ArrayList;
import java.util.List;

import okhttp3.logging.LoggingEventListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewCVFragment extends Fragment implements ViewCVAdapter.OnClickItemListener, TypeOfJobAdapter.OnClickItemListener {

    private RecyclerView recyclerView1;
    private View root;
    private ViewCVAdapter adapter;
    Account account;
    List<Cv> cvList;
    FragmentViewCV2Binding binding;
    private int page = 1, limit;
    private String title = null;


    public ViewCVFragment(Account account) {
        this.account = account;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentViewCV2Binding.inflate(inflater,container,false);
        root = binding.getRoot();
        binding.progressBar.setVisibility(View.VISIBLE);
        recyclerView1 = root.findViewById(R.id.recycler_view_view_cv_vertical);

        getTitle();
        getPostCv();
        binding.nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()){
                    page++;
                    binding.progressBar.setVisibility(View.VISIBLE);
                    if (page <= limit){
                        getPostCv();
                    }else {
                        binding.progressBar.setVisibility(View.GONE);
                    }

                }
            }
        });

        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = 1;
                title = binding.searchJob.getText().toString().trim();
                getPostCv();
            }
        });
        return root;
    }
    private void getTitle(){
        ApiService  apiService= Connection.getClient().create(ApiService.class);
        Call<List<FilterData>> listCall = apiService.getTitlePostCv();

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
    private void getPostCv(){
        Retrofit retrofit = Connection.getClient();
        final ApiService apiService = retrofit.create(ApiService.class);

        Call<PostCvPagination> call = apiService.getPostCv("Bearer "+account.getToken(),title,page);

        call.enqueue(new Callback<PostCvPagination>() {
            @Override
            public void onResponse(Call<PostCvPagination> call, Response<PostCvPagination> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(getContext(),"error:"+response.message(),Toast.LENGTH_LONG).show();
                }
                if(response.body().getData().isEmpty()){

                    binding.tvNoData.setVisibility(View.VISIBLE);
                    binding.recyclerViewViewCvVertical.setVisibility(View.GONE);
                }
                limit = response.body().getLast_page();
                try {
                    cvList = response.body().getData();
//                        postJobs.addAll(response.body().getData());
                }catch (Exception e){
                    e.printStackTrace();
                }
                if (!cvList.isEmpty()){
                    binding.tvNoData.setVisibility(View.GONE);
                    binding.recyclerViewViewCvVertical.setVisibility(View.VISIBLE);
                    if (page == 1){
                        Log.e("pdf",cvList.get(0).getPdf());
                        binding.progressBar.setVisibility(View.GONE);
                        recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity()));
                        adapter = new ViewCVAdapter(getActivity(), cvList);
                        recyclerView1.setAdapter(adapter);
                        adapter.setOnClickItemListener(ViewCVFragment.this);
                    }else {
                        adapter.addList(response.body().getData());
                    }

                }
            }

            @Override
            public void onFailure(Call<PostCvPagination> call, Throwable t) {
                Log.e("fail",t.getMessage());
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), ViewCVDetailActivity.class);
        intent.putExtra("id",cvList.get(position).getId());
        startActivity(intent);
    }

    @Override
    public void onClickJobType(TypeOfJob typeOfJob) {
        Intent intent = new Intent(getActivity(), TypeOfCvActivity.class);
        intent.putExtra("title",typeOfJob.getJob_status());
        startActivity(intent);
    }
}
