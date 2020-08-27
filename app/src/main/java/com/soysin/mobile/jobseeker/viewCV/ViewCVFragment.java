package com.soysin.mobile.jobseeker.viewCV;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.soysin.mobile.jobseeker.ProfileActivity;
import com.soysin.mobile.jobseeker.R;
import com.soysin.mobile.jobseeker.TypeOfCvActivity;
import com.soysin.mobile.jobseeker.TypeOfJobDetailActivity;
import com.soysin.mobile.jobseeker.ViewCVDetailActivity;
import com.soysin.mobile.jobseeker.adapter.TypeOfJobAdapter;
import com.soysin.mobile.jobseeker.adapter.ViewCVAdapter;
import com.soysin.mobile.jobseeker.apiconnection.Connection;
import com.soysin.mobile.jobseeker.model.Account;
import com.soysin.mobile.jobseeker.model.Cv;
import com.soysin.mobile.jobseeker.model.FindJobModel;
import com.soysin.mobile.jobseeker.model.TypeOfJob;
import com.soysin.mobile.jobseeker.service.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewCVFragment extends Fragment implements ViewCVAdapter.OnClickItemListener, TypeOfJobAdapter.OnClickItemListener {

    private RecyclerView recyclerView,recyclerView1;
    private View root;
    private ViewCVAdapter adapter;
    Account account;
    List<Cv> cvList;


    public ViewCVFragment(Account account) {
        this.account = account;
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

        typeOfJobAdapter.setOnClickItemListener(this);
        recyclerView1 = root.findViewById(R.id.recycler_view_view_cv_vertical);
        getPostCv();
        return root;
    }
    private void getPostCv(){
        Retrofit retrofit = Connection.getClient();
        final ApiService apiService = retrofit.create(ApiService.class);

        Call<List<Cv>> call = apiService.getPostCv("Bearer "+account.getToken());

        call.enqueue(new Callback<List<Cv>>() {
            @Override
            public void onResponse(Call<List<Cv>> call, Response<List<Cv>> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(getContext(),"error:"+response.message(),Toast.LENGTH_LONG).show();
                }
                cvList = response.body();
                if (cvList != null){
                    Log.e("success","success");
                    recyclerView1.setLayoutManager(new GridLayoutManager(getActivity(),1));
                    adapter = new ViewCVAdapter(getActivity(),cvList);
                    recyclerView1.setAdapter(adapter);
                    adapter.setOnClickItemListener(ViewCVFragment.this);
                }else {
                    Log.e("success","fail");
                }
            }

            @Override
            public void onFailure(Call<List<Cv>> call, Throwable t) {
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
