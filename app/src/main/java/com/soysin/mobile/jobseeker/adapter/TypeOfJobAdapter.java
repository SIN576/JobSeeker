package com.soysin.mobile.jobseeker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.soysin.mobile.jobseeker.R;
import com.soysin.mobile.jobseeker.model.TypeOfJob;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TypeOfJobAdapter extends RecyclerView.Adapter<TypeOfJobAdapter.TypeOfJobHolder> {

    List<TypeOfJob> typeOfJobs;
    Context context;

    public TypeOfJobAdapter(List<TypeOfJob> typeOfJobs, Context context) {
        this.typeOfJobs = typeOfJobs;
        this.context = context;
    }

    @NonNull
    @Override
    public TypeOfJobHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_type_of_job,parent,false);
        return new TypeOfJobHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TypeOfJobHolder holder, int position) {
            holder.textView_type_of_job.setText(typeOfJobs.get(position).getJob_status());
        Picasso.get().load(typeOfJobs.get(position).getUrl()).into(holder.imageView_type_of_job);
    }

    @Override
    public int getItemCount() {
        return typeOfJobs.size();
    }

    public static class TypeOfJobHolder extends RecyclerView.ViewHolder {

        ImageView imageView_type_of_job;
        TextView textView_type_of_job;

        public TypeOfJobHolder(@NonNull View itemView) {
            super(itemView);

            this.imageView_type_of_job = itemView.findViewById(R.id.type_of_job_image);
            this.textView_type_of_job = itemView.findViewById(R.id.type_of_job_status);
        }
    }
}
