package com.nucleuslife.restaurantreview.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nucleuslife.restaurantreview.Adapters.BusinessListAdapter;
import com.nucleuslife.restaurantreview.MainActivity;
import com.nucleuslife.restaurantreview.R;
import com.nucleuslife.restaurantreview.structures.CustomBusiness;
import com.nucleuslife.restaurantreview.views.DividerItemDecoration;
import com.nucleuslife.restaurantreview.views.TopViewClass;

import java.util.Collections;
import java.util.Comparator;

public class BusinessListFragment extends AbstractCustomFragment implements View.OnClickListener, BusinessListAdapter.BusinessSelectedListener
{
    private RecyclerView recyclerView;
    private TopViewClass topViewClass;
    private MainActivity activityContext;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.business_list_fragment_layout, container, false);
        this.recyclerView = (RecyclerView) view.findViewById(R.id.business_list_recycler_view);
        this.topViewClass = (TopViewClass) view.findViewById(R.id.top_class_view);

        this.recyclerView.addItemDecoration(new DividerItemDecoration(this.getActivity(), R.dimen.business_list_fragment_padding_side));

        this.init();

        return view;
    }

    private void init()
    {
        this.topViewClass.setLeftImageView(ContextCompat.getDrawable(this.getActivity(), R.drawable.search_button));
        this.topViewClass.setTitleText(this.getString(R.string.app_name));
        this.topViewClass.setRightImageView(ContextCompat.getDrawable(this.getActivity(), R.drawable.show_map));
        this.topViewClass.getLeftImageView().setOnClickListener(this);
        this.topViewClass.getRightImageView().setOnClickListener(this);

        this.activityContext = (MainActivity) this.getActivity();

//        CustomBusiness customBusiness = (CustomBusiness) getArguments().getSerializable(BUSINESS_KEY);

//        this.businessTitleTextView.setText(customBusiness.getBusinessInfo().name());


        Log.i("sort", this.activityContext.getBusinessHandler().getBusinessArrayList().toString());

        Collections.sort(this.activityContext.getBusinessHandler().getBusinessArrayList(), new Comparator<CustomBusiness>() {
            @Override
            public int compare(CustomBusiness cb1, CustomBusiness cb2) {
                return Double.compare(cb1.getBusinessInfo().distance(), cb2.getBusinessInfo().distance());
            }
        });

        Log.i("sort", this.activityContext.getBusinessHandler().getBusinessArrayList().toString());

        BusinessListAdapter businessListAdapter = new BusinessListAdapter(this.activityContext, this.activityContext.getBusinessHandler().getBusinessArrayList(), this);
        this.recyclerView.setAdapter(businessListAdapter);

        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        this.recyclerView.setLayoutManager(llm);
    }

    @Override
    public void onClick(View v)
    {
        if (v == this.topViewClass.getLeftImageView()) {
            this.goBack();
        } else if (v == this.topViewClass.getRightImageView()) {
            this.goBack();
        }
    }

    @Override
    public void onBusinessSelected(View view)
    {
        int itemPosition = this.recyclerView.getChildLayoutPosition(view);
        CustomBusiness business = ((MainActivity) this.getActivity()).getBusinessHandler().getBusinessArrayList().get(itemPosition);
        if (business.getCitations().size() > 0) {
             this.activityContext.getCitationHandler().showCitationListFragment(business);
        }

    }

}
