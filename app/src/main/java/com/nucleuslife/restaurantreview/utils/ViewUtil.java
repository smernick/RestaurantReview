package com.nucleuslife.restaurantreview.utils;

import android.view.View;

public class ViewUtil
{
    private static final float UNPRESSED_SCALE = 1.0f;
    private static final float PRESSED_SCALE = 0.95f;

    public static void setScale(View view, boolean pressed)
    {
        float scale = pressed ? PRESSED_SCALE : UNPRESSED_SCALE;

        view.setScaleX(scale);
        view.setScaleY(scale);
    }

    public static boolean isOutsideView(View view, int x, int y)
    {
        if (x < 0 || y < 0 || x > view.getWidth() || y > view.getHeight()) {
            return true;
        }

        return false;
    }
}
