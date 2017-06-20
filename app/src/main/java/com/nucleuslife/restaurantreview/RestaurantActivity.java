package com.nucleuslife.restaurantreview;

import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nucleuslife.restaurantreview.fragments.RestaurantList;
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.SearchResponse;
import com.yelp.clientlib.entities.options.CoordinateOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMarkerClickListener
{
    private GoogleMap mMap;
    private Button searchResaurantButton;
    private ArrayList<Business> businessesArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        this.searchResaurantButton = (Button) findViewById(R.id.search_restaurant_button);
        this.searchResaurantButton.setOnClickListener(this);
    }




    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(40.7081, -73.9571);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker IN brooklyn"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        if (view.equals(this.searchResaurantButton)) {
            this.yelp();
        }
    }

    private void yelp()
    {
        String consumerKey = this.getString(R.string.yelp_consumer_key);
        String consumerSecretKey = this.getString(R.string.yelp_consumer_secret_key);
        String token = this.getString(R.string.yelp_token);
        String secretToken = this.getString(R.string.yelp_token_secret);

        YelpAPIFactory apiFactory = new YelpAPIFactory(consumerKey, consumerSecretKey, token, secretToken);
        YelpAPI yelpAPI = apiFactory.createAPI();

        Map<String, String> params = new HashMap<>();
        params.put("term", "food");
        params.put("limit", "10");
        params.put("lang", "en");

        CoordinateOptions coordinateOptions = new CoordinateOptions()
        {
            @Override
            public Double latitude()
            {
                return 40.7081;
            }

            @Override
            public Double longitude()
            {
                return -73.9571;
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
                RestaurantActivity.this.parseRestaurantData(searchResponse);

                // Update UI text with the searchResponse.
            }
            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                // HTTP error happened, do something to handle it.
            }
        };


        Call<SearchResponse> call = yelpAPI.search(coordinateOptions, params);
        call.enqueue(callback);
//        try {
//            Response<SearchResponse> response = call.execute();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    }


    private void parseRestaurantData(SearchResponse searchResponse)
    {
        this.businessesArrayList = new ArrayList<>();

        for (int i = 0; i < searchResponse.businesses().size() ; i++ )  {
            Business business = searchResponse.businesses().get(i);
            this.businessesArrayList.add(business);
            this.addMarkers(business);
        }


//        this.showRestaurantList();

    }

    private void addMarkers(Business business)
    {
        Double latitude = business.location().coordinate().latitude();
        Double longitude = business.location().coordinate().longitude();
        String restaurantTitle = business.name();
        String restaurantSnippet = business.snippetText();

        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title(restaurantTitle)
                .snippet(restaurantSnippet)
                ;


        Marker marker = mMap.addMarker(markerOptions);
        marker.setTag(business);
    }

    public ArrayList<Business> getBusinessesArrayList()
    {
        return businessesArrayList;
    }

    private void showRestaurantList()
    {
        RestaurantList fragment = new RestaurantList();
        Log.i("recyclersam", "showfragment");

        FragmentTransaction transaction = this.getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        transaction.replace(R.id.fragment_layout, fragment);
        transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.commit();
    }

    private void getCitations(Business business)
    {
        if (business.phone() != null) {
            Log.i("markersam", "business: " + business.phone());
            String phoneString = business.phone();
//            int phoneInt = Integer.parseInt(phoneString);

            // should be a singleton
            OkHttpClient client = new OkHttpClient();


            String uri = Uri.parse("https://data.cityofnewyork.us/resource/9w7m-hzhe.json?")
                    .buildUpon()
                    .appendQueryParameter("phone", phoneString)
                    .build().toString();
            Log.i("markersam", "url: " + uri);


        }

    }

    @Override
    public boolean onMarkerClick(Marker marker)
    {
        Business business = (Business) marker.getTag();
        if (business != null) {
            this.getCitations(business);
        }

        return false;
    }
}
