package com.nucleuslife.restaurantreview.views;

import android.view.View;

import com.nucleuslife.restaurantreview.utils.ViewUtil;

public class ScaleDownTouchListener implements TouchInterface
{

    @Override
    public void onTouchIn(View view)
    {
        ViewUtil.setScale(view, true);
    }

    @Override
    public void onTouchOut(View view)
    {
        ViewUtil.setScale(view, false);
    }
}
