package com.nucleuslife.restaurantreview.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.nucleuslife.restaurantreview.R;
import com.nucleuslife.restaurantreview.structures.CustomBusiness;

public class BusinessUtil
{

    public static BitmapDescriptor getBusinessMarkerColor(CustomBusiness customBusiness, Context context)
    {
        int markerColor =  R.color.pin_none;


        switch (customBusiness.getCitationLevel()) {
            case NONE:
                markerColor = R.color.pin_none;
                break;
            case MODETRATE:
                markerColor =  R.color.pin_medium;
                break;
            case SERIOUS:
                markerColor =  R.color.pin_serious;
                break;
        }



        return getMarkerIcon(ContextCompat.getColor(context, markerColor));
    }

    private static BitmapDescriptor getMarkerIcon(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }
}
