package com.nucleuslife.restaurantreview.fragments;

import android.app.Fragment;

public class AbstractCustomFragment extends Fragment
{

    protected void goBack()
    {
        this.getFragmentManager().popBackStack();
    }

}
