package com.nucleuslife.restaurantreview;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.SupportMapFragment;
import com.nucleuslife.restaurantreview.Handlers.BusinessHandler;
import com.nucleuslife.restaurantreview.Handlers.CitationHandler;
import com.nucleuslife.restaurantreview.Handlers.GoogleMapsHandler;
import com.nucleuslife.restaurantreview.fragments.AbstractCustomFragment;
import com.nucleuslife.restaurantreview.views.CitationButton;

public class RestaurantActivity extends FragmentActivity implements  View.OnClickListener
{
    private static final String TAG = RestaurantActivity.class.getSimpleName();

    private GoogleMapsHandler googleMapsHandler;
    private BusinessHandler businessHandler;
    private CitationHandler citationHandler;
    private RelativeLayout recyclerViewContainer;
    private CitationButton searchButton;
    private CitationButton showList;
    private RecyclerView recyclerView;

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
        this.recyclerView = (RecyclerView) findViewById(R.id.main_activity_recycler_view);
        this.recyclerViewContainer = (RelativeLayout) findViewById(R.id.recyler_view_container);

        this.searchButton.setOnClickListener(this);
        this.showList.setOnClickListener(this);
    }



    public void showFragment(AbstractCustomFragment fragment)
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
                RestaurantActivity.this.businessHandler.searchRestaurants();
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

    public RecyclerView getRecyclerView()
    {
        return recyclerView;
    }

    public RelativeLayout getRecyclerViewContainer()
    {
        return recyclerViewContainer;
    }
}
