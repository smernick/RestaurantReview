package com.nucleuslife.restaurantreview.Handlers;

import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nucleuslife.restaurantreview.Adapters.BusinessAdapter;
import com.nucleuslife.restaurantreview.R;
import com.nucleuslife.restaurantreview.RestaurantActivity;
import com.nucleuslife.restaurantreview.structures.CustomBusiness;
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.SearchResponse;
import com.yelp.clientlib.entities.options.CoordinateOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantHandler
{
    private static String YELP_API_TERM_PARAM = "term";
    private static String YELP_API_LIMIT_PARAM = "limit";
    private static String YELP_API_LANG_PARAM = "lang";

    private ArrayList<CustomBusiness> businessArrayList;


    private RestaurantActivity context;

    public RestaurantHandler(RestaurantActivity context)
    {
        this.context = context;
        this.businessArrayList = new ArrayList<>();
    }

    public void makeCall()
    {
        YelpAPI yelpAPI = this.yelpApiCall();
        Call<SearchResponse> call = yelpAPI.search(coordinateOptions, this.getParams());
        call.enqueue(this.callback);
    }

    private YelpAPI yelpApiCall()
    {
        String consumerKey = context.getString(R.string.YELP_CONSUMER_KEY);
        String consumerSecretKey = context.getString(R.string.YELP_CONSUMER_KEY_SECRET);
        String token = context.getString(R.string.YELP_TOKEN);
        String secretToken = context.getString(R.string.YELP_TOKEN_SECRET);

        YelpAPIFactory apiFactory = new YelpAPIFactory(consumerKey, consumerSecretKey, token, secretToken);
        YelpAPI yelpAPI = apiFactory.createAPI();

        return yelpAPI;
    }

    private Map<String, String> getParams()
    {
        Map<String, String> params = new HashMap<>();
        params.put(YELP_API_TERM_PARAM, "food");
        params.put(YELP_API_LIMIT_PARAM, "10");
        params.put(YELP_API_LANG_PARAM, "en");

        return params;
    }

    private CoordinateOptions coordinateOptions = new CoordinateOptions()
    {
        @Override
        public Double latitude()
        {
            return context.getGoogleMapsHandler().getMap().getCameraPosition().target.latitude;
        }

        @Override
        public Double longitude()
        {
            return context.getGoogleMapsHandler().getMap().getCameraPosition().target.longitude;
        }

        @Override
        public Double accuracy()
        {
            return null;
        }

        @Override
        public Double altitude()
        {
            return null;
        }

        @Override
        public Double altitudeAccuracy()
        {
            return null;
        }
    };

    Callback<SearchResponse> callback = new Callback<SearchResponse>() {
        @Override
        public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
            SearchResponse searchResponse = response.body();
            Log.i("yelpresponse", searchResponse.toString());
            RestaurantHandler.this.parseRestaurantData(searchResponse);

            // Update UI text with the searchResponse.
        }
        @Override
        public void onFailure(Call<SearchResponse> call, Throwable t) {
            // HTTP error happened, do something to handle it.
        }
    };

    private void parseRestaurantData(SearchResponse searchResponse)
    {
        this.businessArrayList.clear();

        for (int i = 0; i < searchResponse.businesses().size() ; i++ )  {
            Business business = searchResponse.businesses().get(i);
            CustomBusiness customBusiness = new CustomBusiness(business);
            this.context.getCitationHandler().getCitations(customBusiness);
            this.businessArrayList.add(customBusiness);
        }
    }

    public void addMarkers(CustomBusiness business)
    {
        Double latitude = business.getBusinessInfo().location().coordinate().latitude();
        Double longitude = business.getBusinessInfo().location().coordinate().longitude();
        String restaurantTitle = business.getBusinessInfo().name();
        String restaurantSnippet = business.getBusinessInfo().distance().toString();

        boolean hasCitations = business.getCitations().size() > 0;
        float markerColor = hasCitations ? BitmapDescriptorFactory.HUE_RED : BitmapDescriptorFactory.HUE_BLUE;

        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title(restaurantTitle)
                .snippet(restaurantSnippet)
                .icon(BitmapDescriptorFactory.defaultMarker(markerColor));

        Marker marker = this.context.getGoogleMapsHandler().getMap().addMarker(markerOptions);
        marker.setTag(business);
    }


    public void setBusinessAdapter()
    {
        BusinessAdapter businessAdapter = new BusinessAdapter(this.context, this.businessArrayList);
        this.context.getRecyclerView().setAdapter(businessAdapter);

        LinearLayoutManager llm = new LinearLayoutManager(this.context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        this.context.getRecyclerView().setLayoutManager(llm);
    }

}
