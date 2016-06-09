package kis.hackathon.winners.yalla;

import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.maps.DirectionsApi;
import com.google.maps.model.LatLng;

public class MainActivity extends AppCompatActivity implements TimeReady {

    private static final String TAG = "main_activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "before!");
        try {
            DirectionsManager.sendRequest(new LatLng(31.750876, 35.223831), new LatLng(29.580795, 34.946855), this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //https://maps.googleapis.com/maps/api/directions/json?origin=31.750876,%2035.223831&destination=31.774577,%2035.197986&key=AIzaSyBJK_IDdOZOegnfCPCBn4d016TpVWrBz44
    }

    @Override
    public void onTimeReady(int timeUntilArrive) {
        Log.d(TAG, "time ready!!!" + timeUntilArrive);
    }

}
