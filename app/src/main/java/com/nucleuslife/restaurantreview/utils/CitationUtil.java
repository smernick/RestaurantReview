package com.nucleuslife.restaurantreview.utils;

import android.content.Context;

import com.nucleuslife.restaurantreview.structures.CustomBusiness;

import java.util.LinkedHashMap;

public class CitationUtil
{

    public static LinkedHashMap<String, String> getCitationGradeMap(Context context, int keyInt, int valuesInt)
    {
        String[] keys = context.getResources().getStringArray(keyInt);
        String[] values = context.getResources().getStringArray(valuesInt);

        LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();

        for (int i = 0; i < keys.length; ++i) {
            hashMap.put(keys[i], values[i]);
        }

        return hashMap;
    }

    public static CustomBusiness.CitationLevel getCitationLevel(CustomBusiness business)
    {
        CustomBusiness.CitationLevel citationLevel = CustomBusiness.CitationLevel.NONE;

        int citationSize = business.getCitations().size();

        if (citationSize > 0) {
            citationLevel = (citationSize > 9) ? CustomBusiness.CitationLevel.SERIOUS : CustomBusiness.CitationLevel.MODETRATE;
        }


        return citationLevel;
    }

}
