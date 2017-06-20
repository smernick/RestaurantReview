package com.nucleuslife.restaurantreview.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nucleuslife.restaurantreview.Adapters.BusinessAdapter;
import com.nucleuslife.restaurantreview.R;
import com.nucleuslife.restaurantreview.RestaurantActivity;
import com.yelp.clientlib.entities.Business;

import java.util.ArrayList;

public class RestaurantList extends Fragment
{
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.restaurant_list_layout, container, false);
        this.recyclerView = (RecyclerView) view.findViewById(R.id.business_adapter);

        this.init();

        return view;
    }

    private void init()
    {
        Log.i("recyclersam", "Restaurantlist init");

        ArrayList<Business> businesses = ((RestaurantActivity) this.getActivity()).getBusinessesArrayList();
        BusinessAdapter businessAdapter = new BusinessAdapter(businesses);
        this.recyclerView.setAdapter(businessAdapter);

        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        this.recyclerView.setLayoutManager(llm);
    }

}
