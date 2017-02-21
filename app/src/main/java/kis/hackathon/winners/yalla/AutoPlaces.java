package kis.hackathon.winners.yalla;

import android.app.Activity;
import android.util.Log;
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
class AutoPlaces {


    private static final String TAG ="AutoPlaces" ;
    static void  populateAutoPlaces(final Activity activity, final TextView replacement){

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                activity.getFragmentManager().findFragmentById(R.id.frag_place);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                YallaSmsManager.getInstance().setDest(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude));
                YallaSmsManager.getInstance().setDestName(place.getName().toString());
                Log.d(TAG, "Place: " + place.getName());
                replacement.setText(String.format(
                        activity.getString(R.string.close_to_message),
                        place.getName()
                ));
//                activity.startActivity(new Intent(activity, TimeChooseActivity.class));
            }

            @Override
            public void onError(Status status) {
                Log.d(TAG, "An error occurred with google autocomplete frame: " + status.getStatusMessage());
            }
        });
    }
}

