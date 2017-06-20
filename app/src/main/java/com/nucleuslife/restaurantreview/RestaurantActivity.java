package com.nucleuslife.restaurantreview;

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
import com.google.android.gms.maps.model.MarkerOptions;
import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.SearchResponse;
import com.yelp.clientlib.entities.options.CoordinateOptions;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener
{
    private GoogleMap mMap;
    private Button searchResaurantButton;

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
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
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
                // Update UI text with the searchResponse.
            }
            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                // HTTP error happened, do something to handle it.
            }
        };


    }



}
