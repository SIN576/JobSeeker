package com.soysin.mobile.jobseeker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.soysin.mobile.jobseeker.R;
import com.soysin.mobile.jobseeker.model.FindJobModel;

import java.util.List;

public class FindJobAdapter extends RecyclerView.Adapter<FindJobAdapter.FindJobHolder> {

    Context context;
    List<FindJobModel> findJobModels;

    public FindJobAdapter(Context context, List<FindJobModel> findJobModels) {
        this.context = context;
        this.findJobModels = findJobModels;
    }

    @NonNull
    @Override
    public FindJobHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_find_job,parent,false);
        return new FindJobHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FindJobHolder holder, int position) {

        holder.tv_email.setText(findJobModels.get(position).getEmail());

    }

    @Override
    public int getItemCount() {
        return findJobModels.size();
    }

    static class FindJobHolder extends RecyclerView.ViewHolder {
        TextView tv_email;
        public FindJobHolder(@NonNull View itemView) {
            super(itemView);
            tv_email = itemView.findViewById(R.id.tv_email);
        }
    }
}
