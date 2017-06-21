package com.nucleuslife.restaurantreview.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nucleuslife.restaurantreview.R;
import com.nucleuslife.restaurantreview.structures.Citation;
import com.nucleuslife.restaurantreview.structures.CustomBusiness;

import java.util.ArrayList;

public class CitationAdapter extends RecyclerView.Adapter<CitationAdapter.CitationViewHolder> {

    private CustomBusiness customBusiness;
    private ArrayList<Citation> citations;

    public CitationAdapter(CustomBusiness business) {
        this.customBusiness = business;
        this.citations = this.customBusiness.getCitations();
    }

    @Override
    public void onBindViewHolder(CitationViewHolder holder, int position)
    {
        Citation citation = this.citations.get(position);
        holder.violationCode.setText(citation.getViolationCode());
        holder.violationDescription.setText(citation.getViolationDesciption());

    }

    @Override
    public int getItemCount()
    {
        return this.citations.size();
    }

    @Override
    public CitationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.citation_row_layout, parent, false);
        return new CitationViewHolder(v);
    }


    /**
     * View holder class
     * */
    public class CitationViewHolder extends RecyclerView.ViewHolder {
        private TextView violationCode;
        private TextView violationDescription;

        private CitationViewHolder(View view) {
            super(view);
            this.violationCode = (TextView) view.findViewById(R.id.violation_code);
            this.violationDescription = (TextView) view.findViewById(R.id.violation_description);

        }
    }

}