package com.nucleuslife.restaurantreview.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nucleuslife.restaurantreview.R;
import com.yelp.clientlib.entities.Business;

import java.util.ArrayList;

public class BusinessAdapter extends RecyclerView.Adapter<BusinessAdapter.MyViewHolder> {

    private ArrayList<Business> businessList;

    /**
     * View holder class
     * */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;

        public MyViewHolder(View view) {
            super(view);
            nameTextView = (TextView) view.findViewById(R.id.restaurant_name);
        }
    }

    public BusinessAdapter(ArrayList<Business> businessList) {
        this.businessList = businessList;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Business business = this.businessList.get(position);
        holder.nameTextView.setText(business.name());
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
}