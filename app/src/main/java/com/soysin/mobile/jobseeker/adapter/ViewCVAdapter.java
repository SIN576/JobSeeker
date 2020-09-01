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
import com.soysin.mobile.jobseeker.model.Cv;
import com.soysin.mobile.jobseeker.util.DateUtils;
import com.soysin.mobile.jobseeker.viewCV.DeleteCV;
import com.soysin.mobile.jobseeker.viewCV.PostCvActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ViewCVAdapter extends RecyclerView.Adapter<ViewCVAdapter.ViewCVHolder> {

    Context context;
    List<Cv> findJobModels;
    String token;
    ViewCVAdapter.OnClickItemListener mListener;
    ViewCVAdapter.OnLongClickItemListener longListener;

    public void addList(List<Cv> data) {
        for (Cv cv: data){
            findJobModels.add(cv);
        }
        notifyDataSetChanged();
    }

    public interface OnClickItemListener {
        public void onItemClick(int position);

    }

    public void setOnClickItemListener(ViewCVAdapter.OnClickItemListener mListener) {
        this.mListener = mListener;
    }

    public interface OnLongClickItemListener {
        public void onItemLongClick(int position);

    }


    public ViewCVAdapter(Context context, List<Cv> findJobModels) {
        this.context = context;
        this.findJobModels = findJobModels;
    }

    public ViewCVAdapter(Context context, List<Cv> findJobModels, String token) {
        this.context = context;
        this.findJobModels = findJobModels;
        this.token = token;
    }

    @NonNull
    @Override
    public ViewCVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_cv,parent,false);
        return new ViewCVHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewCVHolder holder, int position) {

        if (token != null){
            holder.btn_more_option.setVisibility(View.VISIBLE);
        }
        holder.tv_email.setText(findJobModels.get(position).getEmail());
        Picasso.get().load(Connection.BASEURL+"/api/user/getDownloadProfile/"+findJobModels.get(position).getUser_id()+"/"+findJobModels.get(position).getProfile())
        .into(holder.imgPro);
        holder.tvTitle.setText(findJobModels.get(position).getTitle());
        holder.tvExperience.setText(findJobModels.get(position).getExperience());
        holder.tvDayAgo.setText(DateUtils.covertTimeToText(findJobModels.get(position).getUpdated_at()));
        holder.tvName.setText(findJobModels.get(position).getFirst_name()+" "+findJobModels.get(position).getLast_name());
    }

    @Override
    public int getItemCount() {
        return findJobModels.size();

    }

    public class ViewCVHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        TextView tv_email,tvDayAgo,tvExperience,tvName,tvTitle;
        ImageView btn_more_option,imgPro;
        public ViewCVHolder(@NonNull View itemView) {
            super(itemView);
            this.tvTitle = itemView.findViewById(R.id.tv_cv_skill);
            this.tv_email = itemView.findViewById(R.id.tv_cv_email);
            this.btn_more_option = itemView.findViewById(R.id.more_option);
            this.tvDayAgo = itemView.findViewById(R.id.tv_cv_date_post);
            this.tvExperience = itemView.findViewById(R.id.tv_cv_year_of_developer);
            this.tvName = itemView.findViewById(R.id.tv_cv_name);
            this.imgPro = itemView.findViewById(R.id.profile_image);
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

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (longListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            longListener.onItemLongClick(position);
                            return true;
                        }
                    }
                    return false;
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
                    Intent intent = new Intent(context, PostCvActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("action","edit");
                    bundle.putInt("id",findJobModels.get(getAdapterPosition()).getId());
                    bundle.putString("pdf",findJobModels.get(getAdapterPosition()).getPdf());
                    bundle.putString("title",findJobModels.get(getAdapterPosition()).getTitle());
                    bundle.putString("experience",findJobModels.get(getAdapterPosition()).getExperience());
                    bundle.putString("email",findJobModels.get(getAdapterPosition()).getEmail());
                    bundle.putString("phoneNumber",findJobModels.get(getAdapterPosition()).getPhone_number());
                    intent.putExtra("bundle_key", bundle);
                    context.startActivity(intent);
                    Log.e("menu message","action_edit: "+findJobModels.get(getAdapterPosition()).getId());
                    return true;
                case R.id.action_delete:
                    DeleteCV.Delete(findJobModels.get(getAdapterPosition()).getId(),token);
                    findJobModels.remove(getAdapterPosition());
                    notifyDataSetChanged();
                    Log.e("menu message","action_delete: "+getAdapterPosition());
                    return true;
                default:
                    return false;
            }
        }
    }
}
