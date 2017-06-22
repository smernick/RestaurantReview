package com.nucleuslife.restaurantreview.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nucleuslife.restaurantreview.R;
import com.nucleuslife.restaurantreview.structures.CustomBusiness;

import java.util.ArrayList;

public class BusinessAdapter extends RecyclerView.Adapter<BusinessAdapter.MyViewHolder> {

    private ArrayList<CustomBusiness> businessList;
    private Context context;

    /**
     * View holder class
     * */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView citationCount;

        private MyViewHolder(View view) {
            super(view);
            this.nameTextView = (TextView) view.findViewById(R.id.restaurant_name);
            this.citationCount = (TextView) view.findViewById(R.id.citation_count);
        }
    }

    public BusinessAdapter(Context context, ArrayList<CustomBusiness> businessList) {
        this.businessList = businessList;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CustomBusiness business = this.businessList.get(position);
        this.setCardTexts(holder, business, position);
    }

    @Override
    public int getItemCount() {
        return this.businessList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_row_layout, parent, false);
        return new MyViewHolder(v);
    }

    private void setCardTexts(MyViewHolder holder, CustomBusiness customBusiness, int position )
    {
        String restaurantName = customBusiness.getBusinessInfo().name();
        int citationCount =  ( customBusiness.getCitations() != null) ? customBusiness.getCitations().size() : 0;

        String formattedTitleInfo = String.format(context.getString(R.string.restaurant_list_formatted_name),Integer.toString((++position)), restaurantName);
        String formattedCitationCount = String.format(context.getString(R.string.restaurant_citation_count), citationCount);


        holder.nameTextView.setText(formattedTitleInfo);
        holder.citationCount.setText(formattedCitationCount);

    }
}