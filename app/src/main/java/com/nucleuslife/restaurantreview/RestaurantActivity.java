package com.nucleuslife.restaurantreview;

import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nucleuslife.restaurantreview.fragments.RestaurantList;
import com.nucleuslife.restaurantreview.structures.CustomBusiness;
import com.nucleuslife.restaurantreview.tasks.OkHttpHandler;
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

public class RestaurantActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMarkerClickListener, OkHttpHandler.CitationCallback,     GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{
    private static final String TAG = RestaurantActivity.class.getSimpleName();
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;

    // The entry point to Google Play services, used by the Places API and Fused Location Provider.
    private GoogleApiClient mGoogleApiClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(40.7081, -73.9571);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";


    private Button searchResaurantButton;
    private ArrayList<CustomBusiness> businessesArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        setContentView(R.layout.activity_restaurant);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        mGoogleApiClient.connect();

        this.searchResaurantButton = (Button) findViewById(R.id.search_restaurant_button);
        this.searchResaurantButton.setOnClickListener(this);
//        this.getDeviceLocation();
    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
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
//    @Override
//    public void onMapReady(GoogleMap googleMap)
//    {
//        mMap = googleMap;
//
//        // Add a marker in Sydney and move the camera
////        LatLng sydney = new LatLng(40.7081, -73.9571);
////        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker IN brooklyn"));
////        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
////        mMap.animateCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM));
////        mMap.setOnMarkerClickListener(this);
//    }

    @Override
    public void onMapReady(GoogleMap map) {
        Log.i("mapmap", "mapREady");
        mMap = map;

        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.


        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onConnectionSuspended(int i)
    {
        Log.d(TAG, "Play services connection suspended");

    }

    private void getDeviceLocation() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */

        if (mLocationPermissionGranted) {
            mLastKnownLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
        }

        // Set the map's camera position to the current location of the device.
        if (mCameraPosition != null) {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        } else if (mLastKnownLocation != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mLastKnownLocation.getLatitude(),
                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
        } else {
            Log.d(TAG, "Current location is null. Using defaults.");
            Log.i("mapmap", "showmap");
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
        Log.i("locationsam", "mLastKnownLocation: "  + mLastKnownLocation + ", mCameraPosition: " +  mCameraPosition);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }

        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        if (mLocationPermissionGranted) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mLastKnownLocation = null;
        }
    }





    /**
     * Handles failure to connect to the Google Play services client.
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        // Refer to the reference doc for ConnectionResult to see what error codes might
        // be returned in onConnectionFailed.
        Log.d(TAG, "Play services connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
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
                return mMap.getCameraPosition().target.latitude;
            }

            @Override
            public Double longitude()
            {
                return mMap.getCameraPosition().target.longitude;
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
            CustomBusiness customBusiness = new CustomBusiness(business);
            this.businessesArrayList.add(customBusiness);
            this.addMarkers(customBusiness);
        }
//        this.showRestaurantList();

    }

    private void addMarkers(CustomBusiness business)
    {
        Double latitude = business.getBusinessInfo().location().coordinate().latitude();
        Double longitude = business.getBusinessInfo().location().coordinate().longitude();
        String restaurantTitle = business.getBusinessInfo().name();
        String restaurantSnippet = business.getBusinessInfo().snippetText();

        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title(restaurantTitle)
                .snippet(restaurantSnippet);


        Marker marker = mMap.addMarker(markerOptions);
        marker.setTag(business);
    }

    public ArrayList<CustomBusiness> getBusinessesArrayList()
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

    private void showCitationListFragment(CustomBusiness customBusiness)
    {
        RestaurantList fragment = new RestaurantList();
        Log.i("recyclersam", "showfragment");

        Bundle bundle = new Bundle();
        bundle.putSerializable("business", customBusiness);
        fragment.setArguments(bundle);

        FragmentTransaction transaction = this.getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        transaction.replace(R.id.fragment_layout, fragment);
        transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.commit();
    }

    private void getCitations(CustomBusiness business)
    {
        if (business.getBusinessInfo().phone() != null) {
            String phoneString = business.getBusinessInfo().phone();

            String uri = Uri.parse("https://data.cityofnewyork.us/resource/9w7m-hzhe.json?")
                    .buildUpon()
                    .appendQueryParameter("phone", phoneString)
                    .build().toString();

            OkHttpHandler okHttpHandler = new OkHttpHandler(this, business);
            okHttpHandler.execute(uri);
        }

    }

    @Override
    public boolean onMarkerClick(Marker marker)
    {
        CustomBusiness business = (CustomBusiness) marker.getTag();
        if (business != null) {
            this.getCitations(business);
        }

        return false;
    }


    @Override
    public void onCitationSuccess(CustomBusiness customBusiness)
    {
        Log.i("citationsam", "size: " + customBusiness.getCitations().size());
        this.showCitationListFragment(customBusiness);
    }

    @Override
    public void onCitationFailure()
    {


    }
}
