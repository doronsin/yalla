package kis.hackathon.winners.yalla;


import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.LatLng;

/**
 *
 * This class is used to find the minutes it takes to get from origin to destination
 */
class DirectionsManager {
    private static final String GOOGLE_KEY = "AIzaSyBJK_IDdOZOegnfCPCBn4d016TpVWrBz44";


    static void sendRequest(final LatLng origin, final LatLng dest, final TimeReady caller) {
//        DirectionsApiRequest req =
        getDirections(origin, dest, caller);
//        req.await(); not needed since using the setCallback which initiates it immediately
    }
    private static void getDirections(LatLng origin, LatLng destination, final TimeReady caller) {
        GeoApiContext key = new GeoApiContext();
        key.setApiKey(GOOGLE_KEY);
        DirectionsApiRequest req = DirectionsApi.newRequest(key);
        req = req.origin(origin).destination(destination);
        req.setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                caller.onTimeReady((int)result.routes[0].legs[0].duration.inSeconds/60);
            }

            @Override
            public void onFailure(Throwable e) {
            }
        });
    }

}

