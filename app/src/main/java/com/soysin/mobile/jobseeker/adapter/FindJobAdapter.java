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
import com.soysin.mobile.jobseeker.SearchJobActivity;
import com.soysin.mobile.jobseeker.findJob.FindJobFragment;
import com.soysin.mobile.jobseeker.model.FindJobModel;
import com.soysin.mobile.jobseeker.model.PostJob;
import com.soysin.mobile.jobseeker.util.DateUtils;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class FindJobAdapter extends RecyclerView.Adapter<FindJobAdapter.FindJobHolder> {

    Context context;
    List<PostJob> findJobModels;

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
        logo = new ArrayList<>();
        logo.add("https://dcassetcdn.com/design_img/2980691/681558/681558_16439713_2980691_e831b887_image.png");
        logo.add("https://images-platform.99static.com/zVp77I2_hsIjT0DN0EGObmtGuL4=/500x500/top/smart/99designs-contests-attachments/5/5957/attachment_5957740");
        logo.add("https://d1csarkz8obe9u.cloudfront.net/posterpreviews/corporate-company-logo-design-template-2402e0689677112e3b2b6e0f399d7dc3_screen.jpg?ts=1561532453");
        logo.add("https://cdn1.vectorstock.com/i/1000x1000/77/75/business-finance-stock-company-logo-vector-19957775.jpg");
        int i = position;
        if (position >= 4) {
            i = i / 4;
        }

        holder.tv_email.setText(findJobModels.get(position).getEmail());
        holder.tv_job_title.setText(findJobModels.get(position).getTitle());
        Picasso.get().load(logo.get(i))
                .into(holder.imageView);
        holder.tv_company_name.setText(findJobModels.get(position).getCompany_name());
        holder.tv_term.setText(findJobModels.get(position).getTerm());

        holder.tv_date_ago.setText(DateUtils.getDiffDate(findJobModels.get(position).getUpdated_at()) + " day ago");
    }

    @Override
    public int getItemCount() {
        return findJobModels.size();
    }

    public class FindJobHolder extends RecyclerView.ViewHolder {
        TextView tv_email, tv_job_title, tv_company_name, tv_term, tv_date_ago;
        ImageView imageView;

        public FindJobHolder(@NonNull View itemView) {
            super(itemView);
            tv_date_ago = itemView.findViewById(R.id.date_ago);
            tv_term = itemView.findViewById(R.id.term);
            tv_email = itemView.findViewById(R.id.tv_email);
            tv_job_title = itemView.findViewById(R.id.title_job);
            imageView = itemView.findViewById(R.id.img_pro);
            tv_company_name = itemView.findViewById(R.id.name_company);
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

    public void filterList(List<PostJob> postJobList) {
        this.findJobModels = postJobList;
        notifyDataSetChanged();
    }
}
