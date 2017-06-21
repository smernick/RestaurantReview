package com.nucleuslife.restaurantreview.structures;

import com.yelp.clientlib.entities.Business;

import java.io.Serializable;
import java.util.ArrayList;

public class CustomBusiness implements Serializable
{
    private Business businessInfo;
    private ArrayList<Citation> citations;

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
}
