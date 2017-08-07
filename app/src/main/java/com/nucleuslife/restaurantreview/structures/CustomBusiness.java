package com.nucleuslife.restaurantreview.structures;

import com.google.android.gms.location.places.Place;
import com.nucleuslife.restaurantreview.utils.CitationUtil;
import com.yelp.clientlib.entities.Business;

import java.io.Serializable;
import java.util.ArrayList;

public class CustomBusiness implements Serializable
{
    private Business businessInfo;
    private Place placeInfo;

    private ArrayList<Citation> citations = new ArrayList<>();

    public CustomBusiness(Business businessInfo)
    {
        this.businessInfo = businessInfo;
    }

    public CustomBusiness(Place placeInfo)
    {
        this.placeInfo = placeInfo;
    }

    public Business getBusinessInfo()
    {
        return businessInfo;
    }

    public String getBusinessName()
    {
        return this.getBusinessInfo() != null ? this.getBusinessInfo().name() : this.getPlaceInfo().getName().toString();
    }

    public ArrayList<Citation> getCitations()
    {
        return this.citations;
    }

    public void setCitations(ArrayList<Citation> citations)
    {
        this.citations = citations;
    }

    public CitationLevel getCitationLevel()
    {
        return CitationUtil.getCitationLevel(this);
    }

    public Place getPlaceInfo()
    {
        return placeInfo;
    }

    public enum CitationLevel
    {
        SERIOUS,
        MODERATE,
        NONE
    }
}
