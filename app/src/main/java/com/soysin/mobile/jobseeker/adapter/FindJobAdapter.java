package com.soysin.mobile.jobseeker.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.soysin.mobile.jobseeker.R;
import com.soysin.mobile.jobseeker.apiconnection.Connection;
import com.soysin.mobile.jobseeker.findJob.PostJobActivity;
import com.soysin.mobile.jobseeker.model.PostJob;
import com.soysin.mobile.jobseeker.util.DateUtils;
import com.soysin.mobile.jobseeker.viewCV.DeleteCV;
import com.soysin.mobile.jobseeker.viewCV.PostCvActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FindJobAdapter extends RecyclerView.Adapter<FindJobAdapter.FindJobHolder> {

    Context context;
    List<PostJob> findJobModels;
    private String token;

    public FindJobAdapter(Context context, List<PostJob> findJobModels, String token) {
        this.context = context;
        this.findJobModels = findJobModels;
        this.token = token;
    }

    FindJobAdapter.OnClickItemListener mListener;

    List<String> logo;


    public interface OnClickItemListener {
        public void onItemClick(int position);

    }

    public void setOnClickItemListener(FindJobAdapter.OnClickItemListener mListener) {
        this.mListener = mListener;
    }

    public FindJobAdapter(Context context, List<PostJob> findJobModels) {
        this.context = context;
        this.findJobModels = findJobModels;
    }

    @NonNull
    @Override
    public FindJobHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_find_job, parent, false);
        return new FindJobHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FindJobHolder holder, int position) {

        if (token != null){
            holder.btn_more_option.setVisibility(View.VISIBLE);
        }

        holder.tv_email.setText(findJobModels.get(position).getEmail());
        holder.tv_job_title.setText(findJobModels.get(position).getTitle());
        Picasso.get().load(Connection.BASEURL+"/api/user/getDownloadProfile/"+findJobModels.get(position).getUser_id()+"/"+findJobModels.get(position).getProfile())
                .into(holder.imageView);
        Picasso.get()
                .load(Connection.BASEURL+"/api/postjob/getdownload/"+findJobModels.get(position).getId())
                .into(holder.imgPostJob);
        holder.tv_company_name.setText(findJobModels.get(position).getCompany_name());
        holder.tv_term.setText(findJobModels.get(position).getTerm());
        holder.tv_date_ago.setText(DateUtils.covertTimeToText(findJobModels.get(position).getUpdated_at())+"");
    }

    @Override
    public int getItemCount() {
        return findJobModels.size();
    }

    public class FindJobHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener{
        TextView tv_email, tv_job_title, tv_company_name, tv_term, tv_date_ago;
        ImageView imageView,imgPostJob,btn_more_option;


        public FindJobHolder(@NonNull View itemView) {
            super(itemView);
            this.btn_more_option = itemView.findViewById(R.id.btnMore);
            imgPostJob = itemView.findViewById(R.id.img_post_job);
            tv_date_ago = itemView.findViewById(R.id.date_ago);
            tv_term = itemView.findViewById(R.id.term);
            tv_email = itemView.findViewById(R.id.tv_email);
            tv_job_title = itemView.findViewById(R.id.title_job);
            imageView = itemView.findViewById(R.id.img_pro);
            tv_company_name = itemView.findViewById(R.id.name_company);
            btn_more_option.setOnClickListener(this);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            showPopUpMenu(v);
        }

        private  void showPopUpMenu(View v){
            PopupMenu menu = new PopupMenu(v.getContext(),v);
            menu.inflate(R.menu.pop_up_menu);
            menu.setOnMenuItemClickListener(this);
            menu.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case R.id.action_edit:
                    Intent intent = new Intent(context, PostJobActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("action","edit");
                    bundle.putString("profile",findJobModels.get(getLayoutPosition()).getProfile());
                    bundle.putString("image",findJobModels.get(getLayoutPosition()).getImage());
                    bundle.putInt("id",findJobModels.get(getAdapterPosition()).getId());
                    bundle.putString("company_name",findJobModels.get(getAdapterPosition()).getCompany_name());
                    bundle.putString("term",findJobModels.get(getAdapterPosition()).getTerm());
                    bundle.putString("title",findJobModels.get(getAdapterPosition()).getTitle());
                    bundle.putString("requirement",findJobModels.get(getAdapterPosition()).getRequirement());
                    bundle.putString("experience",findJobModels.get(getAdapterPosition()).getExperience());
                    bundle.putString("email",findJobModels.get(getAdapterPosition()).getEmail());
                    bundle.putString("last_date",findJobModels.get(getAdapterPosition()).getLast_date());
                    bundle.putString("address",findJobModels.get(getAdapterPosition()).getAddress());
                    bundle.putString("phone_number",findJobModels.get(getAdapterPosition()).getPhone_number());
                    intent.putExtra("bundle_key", bundle);
                    context.startActivity(intent);
                    Log.e("menu message","action_edit: "+findJobModels.get(getAdapterPosition()).getId());
                    return true;
                case R.id.action_delete:
                    DeleteCV.DeleteJob(findJobModels.get(getAdapterPosition()).getId(),token);
                    findJobModels.remove(getAdapterPosition());
                    notifyDataSetChanged();
                    Log.e("menu message","action_delete: "+getAdapterPosition());
                    return true;
                default:
                    return false;
            }
        }
    }

    public void filterList(List<PostJob> postJobList) {
        this.findJobModels = postJobList;
        notifyDataSetChanged();
    }
}
