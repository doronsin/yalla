package kis.hackathon.winners.yalla;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.NonNull;
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
public class GPSservice extends Service implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        TimeReady {


    private static final String TAG = "GPSservice";
    private static final long INTERVAL_TIME_MS = 10000;
    private static final float ACCURACY_METERS = 50;

    private GoogleApiClient mGoogleApiClient;

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

    /**
     * Called by the system every time a client explicitly starts the service by calling
     * {@link Context#startService}, providing the arguments it supplied and a
     * unique integer token representing the start request.  Do not call this method directly.
     * <p>
     * <p>For backwards compatibility, the default implementation calls
     * {@link #onStart} and returns either {@link #START_STICKY}
     * or {@link #START_STICKY_COMPATIBILITY}.
     * <p>
     * <p>If you need your application to run on platform versions prior to API
     * level 5, you can use the following model to handle the older {@link #onStart}
     * callback in that case.  The <code>handleCommand</code> method is implemented by
     * you as appropriate:
     * <p class="caution">Note that the system calls this on your
     * service's main thread.  A service's main thread is the same
     * thread where UI operations take place for Activities running in the
     * same process.  You should always avoid stalling the main
     * thread's event loop.  When doing long-running operations,
     * network calls, or heavy disk I/O, you should kick off a new
     * thread, or use {@link AsyncTask}.</p>
     *
     * @param intent  The Intent supplied to {@link Context#startService},
     *                as given.  This may be null if the service is being restarted after
     *                its process has gone away, and it had previously returned anything
     *                except {@link #START_STICKY_COMPATIBILITY}.
     * @param flags   Additional data about this start request.  Currently either
     *                0, {@link #START_FLAG_REDELIVERY}, or {@link #START_FLAG_RETRY}.
     * @param startId A unique integer representing this specific request to
     *                start.  Use with {@link #stopSelfResult(int)}.
     * @return The return value indicates what semantics the system should
     * use for the service's current started state.  It may be one of the
     * constants associated with the {@link #START_CONTINUATION_MASK} bits.
     * @see #stopSelfResult(int)
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    /**
     * Return the communication channel to the service.  May return null if
     * clients can not bind to the service.  The returned
     * {@link IBinder} is usually for a complex interface
     * that has been <a href="{@docRoot}guide/components/aidl.html">described using
     * aidl</a>.
     * <p>
     * <p><em>Note that unlike other application components, calls on to the
     * IBinder interface returned here may not happen on the main thread
     * of the process</em>.  More information about the main thread can be found in
     * <a href="{@docRoot}guide/topics/fundamentals/processes-and-threads.html">Processes and
     * Threads</a>.</p>
     *
     * @param intent The Intent that was used to bind to this service,
     *               as given to {@link Context#bindService
     *               Context.bindService}.  Note that any extras that were included with
     *               the Intent at that point will <em>not</em> be seen here.
     * @return Return an IBinder through which clients can call on to the
     * service.
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected");
        LocationRequest req = LocationRequest.create();
        req.setInterval(INTERVAL_TIME_MS);
        req.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        req.setFastestInterval(INTERVAL_TIME_MS);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "no permission to request API location");
            // ONEDAY next time user uses the app, tell him about this
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, req, this);




    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "suspended");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
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
        DirectionsManager.sendRequest(
                new LatLng(location.getLatitude(), location.getLongitude()),
                YallaSmsManager.getInstance().getDest(),
                this
        ); // will be sent and then passed to "onTimeReady"

    }

    private void disconnectLocations() {
        Log.d(TAG, "disconnectLocations");

        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
    }

    private void killMyself() {
        Log.d(TAG, "killMyself");

        try {
            disconnectLocations();
        } catch (Exception ignore) {}
        stopSelf();
    }

    @Override
    public void onTimeReady(int timeUntilArrive) {
        int minutesToSleep = timeUntilArrive - YallaSmsManager.getInstance().getMinutesToArrive();
        Log.d(TAG, "onTimeReady, minutes until arrive: "+timeUntilArrive);
        if (minutesToSleep <= 0) {
            YallaSmsManager.getInstance().setMinutesToArrive(timeUntilArrive);
            YallaSmsManager.getInstance().sendSms();
            Log.d(TAG, "sms sent!");
            killMyself();
        } else {
            try {
                Log.d(TAG, "sleeping for "+minutesToSleep+" minutes. milisec from initiation are "+ SystemClock.uptimeMillis());
                Thread.sleep(minutesToSleep * 60 * 1000);
                Log.d(TAG, "minutes now is "+ SystemClock.currentThreadTimeMillis());
                mGoogleApiClient.connect();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
