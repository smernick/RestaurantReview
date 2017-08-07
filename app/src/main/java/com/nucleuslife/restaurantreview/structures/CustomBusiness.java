package com.nucleuslife.restaurantreview.structures;

import com.nucleuslife.restaurantreview.utils.CitationUtil;
import com.yelp.clientlib.entities.Business;

import java.io.Serializable;
import java.util.ArrayList;

public class CustomBusiness implements Serializable
{
    private Business businessInfo;
    private ArrayList<Citation> citations = new ArrayList<>();

    public CustomBusiness(Business businessInfo)
    {
        this.businessInfo = businessInfo;
    }

    public Business getBusinessInfo()
    {
        return businessInfo;
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


    public enum CitationLevel
    {
        SERIOUS,
        MODERATE,
        NONE
    }
}
