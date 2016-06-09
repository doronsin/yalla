package kis.hackathon.winners.yalla;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by odelya_krief on 09-Jun-16.
 * main thread. will send the SMSs
 */
public class GPSservice extends IntentService implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


    private static final String TAG ="GPSservice";
    private static final long INTERVAL_TIME_MS = 10000;
    private static final float TWENTY_METERS = 0;

    GoogleApiClient mGoogleApiClient;


    public GPSservice() {
        super("GPService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mGoogleApiClient= new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
//        if (mGoogleApiClient != null)
        mGoogleApiClient.connect();
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "started GPS service");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnceted");
        LocationRequest req = LocationRequest.create();
        req.setInterval(INTERVAL_TIME_MS);
        req.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        req.setFastestInterval(INTERVAL_TIME_MS);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, req, this);
        Log.d(TAG, "finished requesting");




    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "suspended");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG,"failed");
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location.hasAccuracy() && location.getAccuracy() <= TWENTY_METERS) {
            // found a good location
            /*
            a. if the location is close enough to dest, send SMS and close everything
            b. else - check how nuch time needed, and start a new service to wake up at that time
            c. anyway - kill yourself now
             */
            killMyself();
        }
    }

    public void killMyself() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
        stopSelf();
    }
}
