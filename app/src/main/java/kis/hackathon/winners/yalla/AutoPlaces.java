package kis.hackathon.winners.yalla;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.maps.model.LatLng;

import kis.hackathon.winners.yalla.activities.TimeChooseActivity;

/**
 * Created by odelya_krief on 09-Jun-16.
 *
 * handles the "place" fragment
 */
public class AutoPlaces {


    private static final String TAG ="AutoPlaces" ;
    public static void  populateAutoPlaces(final AppCompatActivity activity, final int fragment_id, final Bundle bundle){

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                activity.getFragmentManager().findFragmentById(fragment_id);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                YallaSmsManager.getInstance().set_dest(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude));
                YallaSmsManager.getInstance().set_destName(place.getName().toString());
                Log.d(TAG, "Place: " + place.getName());
                activity.startActivity(new Intent(activity, TimeChooseActivity.class), bundle);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.d(TAG, "An error occurred: " + status);
            }
        });
    }
}

