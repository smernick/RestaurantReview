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
import com.nucleuslife.restaurantreview.MainActivity;
import com.nucleuslife.restaurantreview.R;
import com.nucleuslife.restaurantreview.structures.CustomBusiness;


public class BusinessListDialogFragment extends DialogFragment implements BusinessAdapter.BusinessSelectedListener
{
    private RecyclerView recyclerView;
    private MainActivity activityContext;


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

    private void init()
    {
        this.activityContext = (MainActivity) this.getActivity();
        BusinessAdapter businessAdapter = new BusinessAdapter(this.activityContext, this.activityContext.getBusinessHandler().getBusinessArrayList(), this);
        this.recyclerView.setAdapter(businessAdapter);

        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        this.recyclerView.setLayoutManager(llm);
    }
//
//    @Override
//    public void onBusinessSelected(CustomBusiness business)
//    {
//        this.dismiss();
//        ((MainActivity)this.getActivity()).getCitationHandler().showCitationListFragment(business);
//    }


    @Override
    public void onBusinessSelected(View view)
    {
        this.dismiss();
        int itemPosition = this.recyclerView.getChildLayoutPosition(view);
        CustomBusiness business = ((MainActivity) this.getActivity()).getBusinessHandler().getBusinessArrayList().get(itemPosition);
        this.activityContext.getCitationHandler().showCitationListFragment(business);

    }
}
