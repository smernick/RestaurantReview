package com.nucleuslife.restaurantreview.Handlers;

import android.util.Log;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.nucleuslife.restaurantreview.MainActivity;
import com.nucleuslife.restaurantreview.R;
import com.nucleuslife.restaurantreview.structures.CustomBusiness;
import com.nucleuslife.restaurantreview.utils.BusinessUtil;

public class AutoCompleteHandler
{
    private static String TAG = AutoCompleteHandler.class.getSimpleName();
    private MainActivity context;
    public CustomBusiness customBusiness;

    public AutoCompleteHandler(MainActivity context)
    {
        this.context = context;
        this.getBusinessAutocomplete();
    }

    private void getBusinessAutocomplete()
    {
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) this.context.getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ESTABLISHMENT)
                .build();

        autocompleteFragment.setFilter(typeFilter);


        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.

                CustomBusiness customBusiness = new CustomBusiness(place);

                String formattedString = BusinessUtil.formatGoogleSeggestionsPhoneNumber(place.getPhoneNumber());
                context.getCitationHandler().getCitations(customBusiness, formattedString);

                Log.i(TAG, "Place: " + formattedString);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }


}
