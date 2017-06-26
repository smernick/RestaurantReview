package com.nucleuslife.restaurantreview.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.nucleuslife.restaurantreview.Adapters.BusinessAdapter;
import com.nucleuslife.restaurantreview.R;
import com.nucleuslife.restaurantreview.RestaurantActivity;


public class BusinessListDialogFragment extends DialogFragment
{
    private RecyclerView recyclerView;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setCanceledOnTouchOutside(true);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.gravity = Gravity.BOTTOM;
        windowParams.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(windowParams);


        return dialog;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.business_list_layout, container, false);
        this.recyclerView = (RecyclerView) view.findViewById(R.id.business_list_recycler_view);


        this.init();

        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        this.getDialog().setCanceledOnTouchOutside(true);

    }

    private void init()
    {
        BusinessAdapter businessAdapter = ((RestaurantActivity) this.getActivity()).getBusinessHandler().getBusinessAdapter();
        this.recyclerView.setAdapter(businessAdapter);

        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        this.recyclerView.setLayoutManager(llm);
    }
}
