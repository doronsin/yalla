package kis.hackathon.winners.yalla;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.LatLng;

/**
 *
 * This class is used to find the time it takes to get from origin to destination
 */
public class DirectionsManager {
    private static final String TAG = DirectionsManager.class.getSimpleName();
    public static final String SERVER_URL = "https://maps.googleapis.com/maps/api/directions/json?origin=";
    private static final String GOOGLE_KEY = "AIzaSyBJK_IDdOZOegnfCPCBn4d016TpVWrBz44";
    public static final int PERMISSION_INTERNET = 3;


    public static void sendRequest(final LatLng origin, final LatLng dest, final TimeReady caller) throws Exception {
        DirectionsApiRequest req = getDirections(origin, dest, caller);
        req.await();
    }
    private static DirectionsApiRequest getDirections(LatLng origin, LatLng destination, final TimeReady caller) {
        Log.d(TAG, "lkasdjflkaszjdlkfjaslkdfjlasdf");
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
                Log.d(TAG, "failed receiving routess!"+e.getMessage());
            }
        });
        return req;
    }

}

