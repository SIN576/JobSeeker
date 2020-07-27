package com.soysin.mobile.jobseeker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.soysin.mobile.jobseeker.R;
import com.soysin.mobile.jobseeker.model.FindJobModel;

import java.util.List;

public class ViewCVAdapter extends RecyclerView.Adapter<ViewCVAdapter.ViewCVHolder> {

    Context context;
    List<FindJobModel> findJobModels;

    public ViewCVAdapter(Context context, List<FindJobModel> findJobModels) {
        this.context = context;
        this.findJobModels = findJobModels;
    }

    @NonNull
    @Override
    public ViewCVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_cv,null);
        return new ViewCVHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewCVHolder holder, int position) {
        holder.tv_email.setText(findJobModels.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return findJobModels.size();
    }

    static class ViewCVHolder extends RecyclerView.ViewHolder {
        TextView tv_email;
        public ViewCVHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_email = itemView.findViewById(R.id.tv_cv_email);
        }
    }
}
