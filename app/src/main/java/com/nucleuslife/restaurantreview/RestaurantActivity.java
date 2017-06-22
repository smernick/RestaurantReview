package com.nucleuslife.restaurantreview;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.SupportMapFragment;
import com.nucleuslife.restaurantreview.Handlers.CitationHandler;
import com.nucleuslife.restaurantreview.Handlers.GoogleMapsHandler;
import com.nucleuslife.restaurantreview.Handlers.RestaurantHandler;
import com.nucleuslife.restaurantreview.fragments.AbstractCustomFragment;

public class RestaurantActivity extends FragmentActivity implements  View.OnClickListener
{
    private static final String TAG = RestaurantActivity.class.getSimpleName();

    private GoogleMapsHandler googleMapsHandler;
    private RestaurantHandler restaurantHandler;
    private CitationHandler citationHandler;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.googleMapsHandler = new GoogleMapsHandler(this);

        this.googleMapsHandler.onCreate(savedInstanceState);

        setContentView(R.layout.activity_restaurant);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        this.googleMapsHandler.getMapSync(mapFragment);

        this.init();
    }

    private void init()
    {
        this.restaurantHandler = new RestaurantHandler(this);
        this.citationHandler = new CitationHandler(this);
        this.searchButton = (Button) findViewById(R.id.search_restaurant_button);
        this.searchButton.setOnClickListener(this);
    }

    public void showFragment(AbstractCustomFragment fragment)
    {
        Log.i("recyclersam", "showfragment");

        FragmentTransaction transaction = this.getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        transaction.replace(R.id.fragment_layout, fragment);
        transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.commit();
    }

    @Override
    public void onClick(View view)
    {
        if (view.equals(this.searchButton)) {
            this.restaurantHandler.makeCall();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        this.googleMapsHandler.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        boolean mapIsNotNull = this.googleMapsHandler.onSaveInstanceState(outState);
        if (mapIsNotNull) {
            super.onSaveInstanceState(outState);
        }
    }

    public GoogleMapsHandler getGoogleMapsHandler()
    {
        return googleMapsHandler;
    }

    public CitationHandler getCitationHandler()
    {
        return citationHandler;
    }
}
