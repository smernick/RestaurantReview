package com.nucleuslife.restaurantreview;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.google.android.gms.maps.SupportMapFragment;
import com.nucleuslife.restaurantreview.Handlers.BusinessHandler;
import com.nucleuslife.restaurantreview.Handlers.CitationHandler;
import com.nucleuslife.restaurantreview.Handlers.GoogleMapsHandler;
import com.nucleuslife.restaurantreview.fragments.LoadingDialog;
import com.nucleuslife.restaurantreview.views.CitationButton;

public class MainActivity extends FragmentActivity implements  View.OnClickListener
{
    private static final String TAG = MainActivity.class.getSimpleName();

    protected LoadingDialog loadingDialog;

    private GoogleMapsHandler googleMapsHandler;
    private BusinessHandler businessHandler;
    private CitationHandler citationHandler;
    private CitationButton searchButton;
    private CitationButton showList;

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
        this.businessHandler = new BusinessHandler(this);
        this.citationHandler = new CitationHandler(this);
        this.searchButton = (CitationButton) findViewById(R.id.search_restaurant_button);
        this.showList = (CitationButton) findViewById(R.id.show_list_button);
        this.loadingDialog = new LoadingDialog();


        this.searchButton.setOnClickListener(this);
        this.showList.setOnClickListener(this);
    }

    public void showLoadingDialog()
    {
        this.loadingDialog.show(this.getFragmentManager(), LoadingDialog.TAG);
    }

    public void showFragment(Fragment fragment)
    {
        FragmentTransaction transaction = this.getFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.animator.slide_up, R.animator.slide_down);
        transaction.replace(R.id.fragment_layout, fragment);
        transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.commit();
    }

    @Override

    protected void onStart()
    {
        super.onStart();

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                MainActivity.this.businessHandler.searchRestaurants();
            }
        }, 500);
    }
    @Override
    public void onClick(View view)
    {
        if (view.equals(this.searchButton)) {
            this.businessHandler.searchRestaurants();
        } else if (view.equals(this.showList)) {
           this.businessHandler.showRestaurantList();
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

    public BusinessHandler getBusinessHandler()
    {
        return businessHandler;
    }

    public LoadingDialog getLoadingDialog()
    {
        return loadingDialog;
    }

}
