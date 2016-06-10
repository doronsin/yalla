package kis.hackathon.winners.yalla;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.maps.model.LatLng;

/**
 * Created by odelya_krief on 09-Jun-16.
 *
 * handles the "place" fragment
 */
public class AutoPlaces {


    private static final String TAG ="AutoPlaces" ;
    public static void  populateAutoPlaces(final Activity activity, final int fragment_id, final TextView replacement){

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                activity.getFragmentManager().findFragmentById(fragment_id);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                YallaSmsManager.getInstance().set_dest(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude));
                YallaSmsManager.getInstance().set_destName(place.getName().toString());
                Log.d(TAG, "Place: " + place.getName());
                replacement.setText(String.format(
                        activity.getString(R.string.close_to_message),
                        place.getName()
                ));
//                activity.startActivity(new Intent(activity, TimeChooseActivity.class));
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.d(TAG, "An error occurred: " + status);
            }
        });
    }
}

