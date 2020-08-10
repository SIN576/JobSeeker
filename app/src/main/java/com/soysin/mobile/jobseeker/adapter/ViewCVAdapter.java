package com.soysin.mobile.jobseeker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.soysin.mobile.jobseeker.R;
import com.soysin.mobile.jobseeker.model.Cv;
import com.soysin.mobile.jobseeker.viewCV.ViewCVFragment;

import java.util.List;

public class ViewCVAdapter extends RecyclerView.Adapter<ViewCVAdapter.ViewCVHolder> {

    Context context;
    List<Cv> findJobModels;
    ViewCVAdapter.OnClickItemListener mListener;

    public interface OnClickItemListener {
        public void onItemClick(int position);

    }

    public void setOnClickItemListener(ViewCVAdapter.OnClickItemListener mListener) {
        this.mListener = mListener;
    }


    public ViewCVAdapter(Context context, List<Cv> findJobModels) {
        this.context = context;
        this.findJobModels = findJobModels;
    }

    @NonNull
    @Override
    public ViewCVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_cv,parent,false);
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

    public class ViewCVHolder extends RecyclerView.ViewHolder {
        TextView tv_email;
        public ViewCVHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_email = itemView.findViewById(R.id.tv_cv_email);
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
    }
}
