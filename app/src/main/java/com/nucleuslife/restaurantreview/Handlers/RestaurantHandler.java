package com.nucleuslife.restaurantreview.Handlers;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nucleuslife.restaurantreview.R;
import com.nucleuslife.restaurantreview.RestaurantActivity;
import com.nucleuslife.restaurantreview.structures.CustomBusiness;
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.SearchResponse;
import com.yelp.clientlib.entities.options.CoordinateOptions;

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

    private Context context;

    public RestaurantHandler(Context context)
    {
        this.context = context;
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
        params.put("term", "food");
        params.put("limit", "10");
        params.put("lang", "en");

        return params;
    }

    private CoordinateOptions coordinateOptions = new CoordinateOptions()
    {
        @Override
        public Double latitude()
        {
            return ((RestaurantActivity)context).getGoogleMapsHandler().getMap().getCameraPosition().target.latitude;
        }

        @Override
        public Double longitude()
        {
            return ((RestaurantActivity)context).getGoogleMapsHandler().getMap().getCameraPosition().target.longitude;
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
        for (int i = 0; i < searchResponse.businesses().size() ; i++ )  {
            Business business = searchResponse.businesses().get(i);
            CustomBusiness customBusiness = new CustomBusiness(business);
            this.addMarkers(customBusiness);
        }
    }

    private void addMarkers(CustomBusiness business)
    {
        Double latitude = business.getBusinessInfo().location().coordinate().latitude();
        Double longitude = business.getBusinessInfo().location().coordinate().longitude();
        String restaurantTitle = business.getBusinessInfo().name();
        String restaurantSnippet = business.getBusinessInfo().distance().toString();

        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title(restaurantTitle)
                .snippet(restaurantSnippet);

        Marker marker = ((RestaurantActivity)context).getGoogleMapsHandler().getMap().addMarker(markerOptions);
        marker.setTag(business);
    }

}
