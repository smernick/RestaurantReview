package com.nucleuslife.restaurantreview.Handlers;

import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nucleuslife.restaurantreview.MainActivity;
import com.nucleuslife.restaurantreview.R;
import com.nucleuslife.restaurantreview.fragments.BusinessListDialogFragment;
import com.nucleuslife.restaurantreview.fragments.BusinessListFragment;
import com.nucleuslife.restaurantreview.structures.CustomBusiness;
import com.nucleuslife.restaurantreview.utils.BusinessUtil;
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

import static com.nucleuslife.restaurantreview.Handlers.GoogleMapsHandler.DEFAULT_ZOOM;

public class BusinessHandler
{
    private static String YELP_API_TERM_PARAM = "term";
    private static String YELP_API_LIMIT_PARAM = "limit";
    private static String YELP_API_LANG_PARAM = "lang";

    private ArrayList<CustomBusiness> businessArrayList = new ArrayList<>();;
    private ArrayList<Marker> markerArrayList = new ArrayList<>();;
    private BusinessListDialogFragment businessListDialogFragment;
    private MainActivity context;
    private int yelpBusinessResponseSize;

    public BusinessHandler(MainActivity context)
    {
        this.context = context;
        this.businessListDialogFragment = new BusinessListDialogFragment();
//        this.businessListDialogFragment.setOn
    }

    public void searchRestaurants()
    {
        this.context.showLoadingDialog();

        YelpAPI yelpAPI = this.yelpApiCall();
        Call<SearchResponse> call = yelpAPI.search(this.coordinateOptions, this.getParams());
        call.enqueue(this.callback);
        Log.i("samsam", "searchRestaurants");
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
            Log.i("latsam", "info : " + context.getGoogleMapsHandler().getMap().getCameraPosition().target);
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
            Log.i("samsam", searchResponse.toString());
            BusinessHandler.this.parseRestaurantData(searchResponse);

        }
        @Override
        public void onFailure(Call<SearchResponse> call, Throwable t) {
            // HTTP error happened, do something to handle it.
        }
    };

    private void parseRestaurantData(SearchResponse searchResponse)
    {
        LatLng current = this.context.getGoogleMapsHandler().getMap().getCameraPosition().target;
        this.context.getGoogleMapsHandler().getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(current, DEFAULT_ZOOM));

        this.yelpBusinessResponseSize = searchResponse.businesses().size();
        this.businessArrayList.clear();
        this.clearMarkers();

        Log.i("searchsam", "size " + searchResponse.businesses().size());

        for (int i = 0; i < searchResponse.businesses().size() ; i++ )  {
            Log.i("searchsam", "size " + searchResponse.businesses().size());
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

        int citationCount =  (business.getCitations() != null) ? business.getCitations().size() : 0;
        String formattedCitationCount = String.format(context.getString(R.string.restaurant_citation_count), citationCount);


//        float markerColor = BusinessUtil.getBusinessMarkerColor(business);
        float opacityAlpha = (business.getCitationLevel() == CustomBusiness.CitationLevel.NONE) ? .7f : 1f;

        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title(restaurantTitle)
                .snippet(formattedCitationCount)
                .alpha(opacityAlpha)
                .icon(BusinessUtil.getBusinessColor(business, this.context));

        Marker marker = this.context.getGoogleMapsHandler().getMap().addMarker(markerOptions);
        marker.setTag(business);
        this.markerArrayList.add(marker);

        Log.i("samsam", "markerSize " + this.markerArrayList.size()  + ", businessSize " + this.businessArrayList.size());

        if (this.markerArrayList.size() == this.businessArrayList.size()) {
            Log.i("samsam", "showRestaurantList");
            this.context.getLoadingDialog().dismiss();
        }
    }

    private void clearMarkers()
    {
        for (int i = 0; i < this.markerArrayList.size(); i++){
            Marker marker = this.markerArrayList.get(i);
            marker.remove();
        }

        this.markerArrayList.clear();
    }

    public void showRestaurantList()
    {
        Log.i("samsam", "showRestaurantList");

//        this.context.getLoadingDialog().dismiss();
//        this.businessListDialogFragment.show(context.getFragmentManager(), "dialog");
        this.context.showFragment(new BusinessListFragment());
    }

    public BusinessListDialogFragment getBusinessListDialogFragment()
    {
        return businessListDialogFragment;
    }

    public ArrayList<CustomBusiness> getBusinessArrayList()
    {
        return businessArrayList;
    }

}
