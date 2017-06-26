package com.nucleuslife.restaurantreview.views;

import android.support.v4.content.ContextCompat;
import android.view.View;

import com.nucleuslife.restaurantreview.R;


public class CellTouchListener implements TouchInterface
{
    @Override
    public void onTouchIn(View view)
    {
        int color = ContextCompat.getColor(view.getContext(), R.color.active_cell);
        view.setBackgroundColor(color);
    }

    @Override
    public void onTouchOut(View view)
    {
        int color = ContextCompat.getColor(view.getContext(), R.color.inactive_cell);
        view.setBackgroundColor(color);
    }
}
