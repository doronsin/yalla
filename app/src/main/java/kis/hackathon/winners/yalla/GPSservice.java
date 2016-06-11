package kis.hackathon.winners.yalla;

import android.Manifest;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.maps.model.LatLng;

/**
 * Created by odelya_krief on 09-Jun-16.
 * main thread. will send the SMSs
 */
public class GPSservice extends IntentService implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        TimeReady {


    private static final String TAG = "GPSservice";
    private static final long INTERVAL_TIME_MS = 10000;
    private static final float ACCURACY_METERS = 50;

    private GoogleApiClient mGoogleApiClient;


    public GPSservice() {
        super("GPService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "no permission to request API location");
            // TODO ONEDAY next time user uses the app, tell him about this
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, req, this);




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
        Log.d(TAG,location.getLongitude()+"---"+location.getLatitude()+"    accuracy:"+location.getAccuracy());
        if (location.hasAccuracy() && location.getAccuracy() <= ACCURACY_METERS) {
            // found a good location
            onGoodLocationFound(location);

        }
    }

    private void onGoodLocationFound(Location location) {
        Log.d(TAG, "onGoodLocationFound");
        disconnectLocations();
        try {
            DirectionsManager.sendRequest(
                    new LatLng(location.getLatitude(), location.getLongitude()),
                    YallaSmsManager.getInstance().get_dest(),
                    this
            ); // will be sent and then passed to "onTimeReady" or "onTimeFailure"
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void disconnectLocations() {
        Log.d(TAG, "disconnectLocations");

        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
    }

    public void killMyself() {
        Log.d(TAG, "killMyself");

        try {
            disconnectLocations();
        } catch (Exception ignore) {}
        stopSelf();
    }

    @Override
    public void onTimeFailure() {
        Log.d(TAG, "onTimeFailure");

    }

    @Override
    public void onTimeReady(int timeUntilArrive) {
        int minutesToSleep = timeUntilArrive - YallaSmsManager.getInstance().get_minutesToArrive();
        Log.d(TAG, "onTimeReady, minutes until arrive: "+timeUntilArrive);
        if (minutesToSleep <= 0) {
            YallaSmsManager.getInstance().set_minutesToArrive(timeUntilArrive);
            YallaSmsManager.getInstance().sendSms();
            Log.d(TAG, "sms sent!");
            killMyself();
        } else {
            try {
                Log.d(TAG, "sleeping for "+minutesToSleep+" minutes. minutes now is "+ SystemClock.currentThreadTimeMillis());
                Thread.sleep(minutesToSleep * 60 * 1000);
                Log.d(TAG, "minutes now is "+ SystemClock.currentThreadTimeMillis());
                mGoogleApiClient.connect();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
