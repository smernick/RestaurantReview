package com.nucleuslife.restaurantreview.Adapters;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nucleuslife.restaurantreview.MainActivity;
import com.nucleuslife.restaurantreview.R;
import com.nucleuslife.restaurantreview.structures.CustomBusiness;
import com.nucleuslife.restaurantreview.utils.BusinessUtil;
import com.nucleuslife.restaurantreview.views.CircleImageView;

import java.util.ArrayList;

public class BusinessListAdapter extends RecyclerView.Adapter<BusinessListAdapter.BusinessListViewHolder> implements  View.OnClickListener {

    private ArrayList<CustomBusiness> businessList;
    private MainActivity context;
    private BusinessSelectedListener businessSelectedListener;



    public BusinessListAdapter(MainActivity context, ArrayList<CustomBusiness> businessList, BusinessSelectedListener businessSelectedListener) {
        this.businessList = businessList;
        this.context = context;
        this.businessSelectedListener = businessSelectedListener;
    }

    @Override
    public void onBindViewHolder(BusinessListViewHolder holder, int position) {
        CustomBusiness business = this.businessList.get(position);
        this.setCardTexts(holder, business, position);
    }

    @Override
    public int getItemCount() {
        return this.businessList.size();
    }

    @Override
    public BusinessListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.business_list_row_layout, parent, false);
        return new BusinessListViewHolder(v);
    }

    private void setCardTexts(BusinessListViewHolder holder, CustomBusiness business, int position)
    {

        int citationCount =  ( business.getCitations() != null) ? business.getCitations().size() : 0;
        String formattedCitationString = BusinessUtil.getFormmatedCitationString(this.context, business, citationCount);
        String distance = BusinessUtil.formatDistance(this.context, business);

        holder.circleImageView.setImageUrl(business.getBusinessInfo().imageUrl());
        holder.businessNameTextView.setText(business.getBusinessInfo().name());
        holder.citationCountTextView.setText(formattedCitationString);
        holder.citationCountTextView.setTextColor(ContextCompat.getColor(this.context, BusinessUtil.getCitationColor(business)));
        holder.distanceTextView.setText(distance);

    }
    @Override
    public void onClick(View view)
    {
        this.businessSelectedListener.onBusinessSelected(view);
    }

    public interface BusinessSelectedListener
    {
        void onBusinessSelected(View view);
    }
    /**
     * View holder class
     * */
    public class BusinessListViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView circleImageView;
        private TextView businessNameTextView;
        private TextView citationCountTextView;
        private TextView distanceTextView;

        private BusinessListViewHolder(View view) {
            super(view);
            view.setOnClickListener(BusinessListAdapter.this);
//            view.setOnTouchListener(new TouchListener(new CellTouchListener()));

            this.circleImageView = (CircleImageView) view.findViewById(R.id.business_circle_image_view);
            this.businessNameTextView = (TextView) view.findViewById(R.id.business_name);
            this.citationCountTextView = (TextView) view.findViewById(R.id.business_citation_count);
            this.distanceTextView = (TextView) view.findViewById(R.id.business_distance);
        }
    }
}