package com.nucleuslife.restaurantreview.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nucleuslife.restaurantreview.Adapters.CitationAdapter;
import com.nucleuslife.restaurantreview.R;
import com.nucleuslife.restaurantreview.structures.CustomBusiness;

import static com.nucleuslife.restaurantreview.Constants.BUSINESS_KEY;

public class CitationListFragment extends AbstractCustomFragment implements View.OnClickListener
{
    private RecyclerView recyclerView;

    private TextView businessTitleTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.restaurant_list_layout, container, false);
        this.recyclerView = (RecyclerView) view.findViewById(R.id.business_adapter);
        this.businessTitleTextView = (TextView) view.findViewById(R.id.business_title_text_view);

        Button button = (Button) view.findViewById(R.id.back_button);
        button.setOnClickListener(this);

        this.init();

        return view;
    }

    private void init()
    {
        CustomBusiness customBusiness = (CustomBusiness) getArguments().getSerializable(BUSINESS_KEY);

        this.businessTitleTextView.setText(customBusiness.getBusinessInfo().name());

        CitationAdapter citationAdapter = new CitationAdapter(this.getActivity(), customBusiness);
        this.recyclerView.setAdapter(citationAdapter);

        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        this.recyclerView.setLayoutManager(llm);
    }

    @Override
    public void onClick(View v)
    {
        this.goBack();
    }
}
