package com.nucleuslife.restaurantreview.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nucleuslife.restaurantreview.Adapters.CitationAdapter;
import com.nucleuslife.restaurantreview.R;
import com.nucleuslife.restaurantreview.structures.CustomBusiness;
import com.nucleuslife.restaurantreview.views.DividerItemDecoration;
import com.nucleuslife.restaurantreview.views.TopViewClass;

import static com.nucleuslife.restaurantreview.Constants.BUSINESS_KEY;

public class CitationListFragment extends AbstractCustomFragment implements View.OnClickListener
{
    private RecyclerView recyclerView;
    private TopViewClass topViewClass;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.citation_list_layout, container, false);
        this.recyclerView = (RecyclerView) view.findViewById(R.id.business_adapter);
        this.topViewClass = (TopViewClass) view.findViewById(R.id.top_class_view);
        this.recyclerView.addItemDecoration(new DividerItemDecoration(this.getActivity(), R.dimen.business_list_fragment_padding_side));


        this.init();

        return view;
    }

    private void init()
    {
        this.topViewClass.setLeftImageView(ContextCompat.getDrawable(this.getActivity(), R.drawable.back_button));
        this.topViewClass.getLeftImageView().setOnClickListener(this);

        CustomBusiness customBusiness = (CustomBusiness) getArguments().getSerializable(BUSINESS_KEY);
        this.topViewClass.setTitleText(customBusiness.getBusinessInfo().name());

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
