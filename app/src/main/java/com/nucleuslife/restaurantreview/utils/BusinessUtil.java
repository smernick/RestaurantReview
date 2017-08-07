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

    public static int getCitationColor(CustomBusiness customBusiness)
    {
        int markerColor =  R.color.pin_none;

        switch (customBusiness.getCitationLevel()) {
            case NONE:
                markerColor = R.color.pin_none;
                break;
            case MODERATE:
                markerColor =  R.color.pin_medium;
                break;
            case SERIOUS:
                markerColor =  R.color.pin_serious;
                break;
        }

        return markerColor;
    }

    public static BitmapDescriptor getBusinessColor(CustomBusiness customBusiness, Context context)
    {
        int color = getCitationColor(customBusiness);

        return getMarkerIcon(ContextCompat.getColor(context, color));
    }

    private static BitmapDescriptor getMarkerIcon(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }

    public static String getFormmatedCitationString(Context context, CustomBusiness customBusiness, int citationCount)
    {
        //        TODO account for 0, 1 , and may citations
        return String.format(context.getString(R.string.business_list_adapter_citation_count), citationCount);
    }

    public static String formatDistance(Context context, CustomBusiness customBusiness)
    {
        double distance = convertMetersToMiles(customBusiness.getBusinessInfo().distance());
        return String.format(context.getString(R.string.business_list_adapter_distance), distance);
    }

    private static double convertMetersToMiles(double meters)
    {
        double distanceDouble = (meters * 0.000621371192);

        return round(distanceDouble, 2);
    }

    private static double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }


    public static String formatGoogleSeggestionsPhoneNumber(CharSequence phoneNumber)
    {
        String formattedString = phoneNumber.toString().replaceAll("[^\\d]", "");

        if (formattedString.length() > 10 ) {
            formattedString = formattedString.startsWith("1") ? formattedString.substring(1) : formattedString;
        }

        return formattedString;
    }

}
