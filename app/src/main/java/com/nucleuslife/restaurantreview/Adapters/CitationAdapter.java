package com.nucleuslife.restaurantreview.Adapters;

import android.content.Context;
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
    private Context context;

    public CitationAdapter(Context context, CustomBusiness business) {
        this.context = context;
        this.customBusiness = business;
        this.citations = this.customBusiness.getCitations();
    }

    @Override
    public void onBindViewHolder(CitationViewHolder holder, int position)
    {
        Citation citation = this.citations.get(position);
        this.setCards(holder, citation, position);
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


    public class CitationViewHolder extends RecyclerView.ViewHolder {
        private TextView citationDate;
        private TextView citationType;
        private TextView citationCode;
        private TextView citationCritical;
        private TextView citationDescription;

        private CitationViewHolder(View view) {
            super(view);
            this.citationDate = (TextView) view.findViewById(R.id.citation_date);
            this.citationType = (TextView) view.findViewById(R.id.citation_inspection_type);
            this.citationCode = (TextView) view.findViewById(R.id.citation_code);
            this.citationCritical = (TextView) view.findViewById(R.id.citation_critical);
            this.citationDescription = (TextView) view.findViewById(R.id.citation_description);

        }
    }
    private void setCards(CitationViewHolder holder, Citation citation, int position)
    {

        String formattedDate = citation.getInspectionDate().split("T")[0];

        holder.citationType.setText(citation.getInspectionType());
//        holder.citationType.setTextColor(ContextCompat.getColor(this.context, BusinessUtil.getCitationColor(business)));
        holder.citationCritical.setText(citation.getCritical());
        holder.citationDate.setText(formattedDate);
        holder.citationCode.setText(citation.getViolationCode());
        holder.citationDescription.setText(citation.getCitationDescription());
    }

}