package com.nucleuslife.restaurantreview;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.SupportMapFragment;
import com.nucleuslife.restaurantreview.Handlers.CitationHandler;
import com.nucleuslife.restaurantreview.Handlers.GoogleMapsHandler;
import com.nucleuslife.restaurantreview.Handlers.RestaurantHandler;
import com.nucleuslife.restaurantreview.fragments.AbstractCustomFragment;

public class RestaurantActivity extends FragmentActivity implements  View.OnClickListener, Animation.AnimationListener
{
    private static final String TAG = RestaurantActivity.class.getSimpleName();

    private GoogleMapsHandler googleMapsHandler;
    private RestaurantHandler restaurantHandler;
    private CitationHandler citationHandler;
    private RelativeLayout recyclerViewContainer;
    private Button searchButton;
    private Button showList;
    private RecyclerView recyclerView;
    private boolean isBusinessListVisible = false;


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
        this.showList = (Button) findViewById(R.id.show_list_button);
        this.recyclerView = (RecyclerView) findViewById(R.id.main_activity_recycler_view);
        this.recyclerViewContainer = (RelativeLayout) findViewById(R.id.recyler_view_container);

        this.searchButton.setOnClickListener(this);
        this.showList.setOnClickListener(this);
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
        } else if (view.equals(this.showList)) {

            Log.i("visiblesam", "isVis " + this.isBusinessListVisible );

            if (!this.isBusinessListVisible) {
                this.restaurantHandler.setBusinessAdapter();
            }

            int animType = this.isBusinessListVisible ? R.anim.slide_down : R.anim.slide_up;
            Animation slide = AnimationUtils.loadAnimation(getApplicationContext(), animType);
            slide.setAnimationListener(this);

            this.isBusinessListVisible = !this.isBusinessListVisible;
            this.recyclerViewContainer.startAnimation(slide);
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

    public RestaurantHandler getRestaurantHandler()
    {
        return restaurantHandler;
    }

    public RecyclerView getRecyclerView()
    {
        return recyclerView;
    }

    @Override
    public void onAnimationStart(Animation animation)
    {
        this.setAnimation();
    }

    @Override
    public void onAnimationEnd(Animation animation)
    {
        this.setAnimation();
    }

    @Override
    public void onAnimationRepeat(Animation animation)
    {

    }

    private void setAnimation()
    {
        int visibility = this.isBusinessListVisible ? View.VISIBLE : View.INVISIBLE;
        this.recyclerViewContainer.setVisibility(visibility);
    }
}
