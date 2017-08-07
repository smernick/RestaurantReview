package com.nucleuslife.restaurantreview.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nucleuslife.restaurantreview.R;
import com.nucleuslife.restaurantreview.MainActivity;
import com.nucleuslife.restaurantreview.structures.CustomBusiness;
import com.nucleuslife.restaurantreview.views.CellTouchListener;
import com.nucleuslife.restaurantreview.views.TouchListener;

import java.util.ArrayList;

public class BusinessDialogAdapter extends RecyclerView.Adapter<BusinessDialogAdapter.BusinessViewHolder> implements  View.OnClickListener {

    private ArrayList<CustomBusiness> businessList;
    private MainActivity context;
    private BusinessSelectedListener businessSelectedListener;

    /**
     * View holder class
     * */
    public class BusinessViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView citationCount;

        private BusinessViewHolder(View view) {
            super(view);
            view.setOnClickListener(BusinessDialogAdapter.this);
            view.setOnTouchListener(new TouchListener(new CellTouchListener()));
            this.nameTextView = (TextView) view.findViewById(R.id.restaurant_name);
            this.citationCount = (TextView) view.findViewById(R.id.citation_count);
        }
    }

    public BusinessDialogAdapter(MainActivity context, ArrayList<CustomBusiness> businessList, BusinessSelectedListener businessSelectedListener) {
        this.businessList = businessList;
        this.context = context;
        this.businessSelectedListener = businessSelectedListener;
    }

    @Override
    public void onBindViewHolder(BusinessViewHolder holder, int position) {
        CustomBusiness business = this.businessList.get(position);
        this.setCardTexts(holder, business, position);
    }

    @Override
    public int getItemCount() {
        return this.businessList.size();
    }

    @Override
    public BusinessViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_row_layout, parent, false);
        return new BusinessViewHolder(v);
    }

    private void setCardTexts(BusinessViewHolder holder, CustomBusiness customBusiness, int position)
    {
        String restaurantName = customBusiness.getBusinessInfo().name();
        int citationCount =  ( customBusiness.getCitations() != null) ? customBusiness.getCitations().size() : 0;

        if (citationCount == 0) {
        }

        String formattedTitleInfo = String.format(context.getString(R.string.restaurant_list_formatted_name),Integer.toString((++position)), restaurantName);
        String formattedCitationCount = String.format(context.getString(R.string.restaurant_citation_count), citationCount);


        holder.nameTextView.setText(formattedTitleInfo);
        holder.citationCount.setText(formattedCitationCount);

    }
    @Override
    public void onClick(View view)
    {
//        int itemPosition = this.context.getRecyclerView().getChildLayoutPosition(view);
//        CustomBusiness business = this.businessList.get(itemPosition);
//        this.context.getCitationHandler().showCitationListFragment(business);

        this.businessSelectedListener.onBusinessSelected(view);
    }

    public interface BusinessSelectedListener
    {
        void onBusinessSelected(View view);
    }
}