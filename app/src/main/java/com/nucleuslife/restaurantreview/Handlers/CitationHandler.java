package com.nucleuslife.restaurantreview.Handlers;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.nucleuslife.restaurantreview.RestaurantActivity;
import com.nucleuslife.restaurantreview.fragments.CitationListFragment;
import com.nucleuslife.restaurantreview.structures.CustomBusiness;
import com.nucleuslife.restaurantreview.tasks.OkHttpHandler;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.nucleuslife.restaurantreview.Constants.CITATION_LIST_KEY;

public class CitationHandler implements OkHttpHandler.CitationCallback, Callback
{
    Context context;

    public CitationHandler(Context context)
    {
        this.context = context;
    }

    public void getCitations(CustomBusiness business)
    {
        if (business.getBusinessInfo().phone() != null) {
            String phoneString = business.getBusinessInfo().phone();

            String uri = Uri.parse("https://data.cityofnewyork.us/resource/9w7m-hzhe.json?")
                    .buildUpon()
                    .appendQueryParameter("phone", phoneString)
                    .build().toString();

            OkHttpHandler okHttpHandler = new OkHttpHandler(this, business);
            okHttpHandler.execute(uri);

//            Log.i("citationsam", "size: " + customBusiness.getCitations().size());
//            this.showCitationListFragment(customBusiness);
        }
    }

    public void showCitationListFragment(CustomBusiness customBusiness)
    {
        CitationListFragment fragment = new CitationListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(CITATION_LIST_KEY, customBusiness);
        fragment.setArguments(bundle);

        ((RestaurantActivity) this.context).showFragment(fragment);
    }

    @Override
    public void onCitationSuccess(CustomBusiness customBusiness)
    {
        Log.i("citationsam", "size: " + customBusiness.getCitations().size());
        ((RestaurantActivity)context).getRestaurantHandler().addMarkers(customBusiness);
//        this.showCitationListFragment(customBusiness);
    }

    @Override
    public void onCitationFailure()
    {

    }

    @Override
    public void onFailure(Call call, IOException e)
    {

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException
    {

    }
}
