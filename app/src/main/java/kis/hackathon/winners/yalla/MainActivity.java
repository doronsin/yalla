package kis.hackathon.winners.yalla;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements TimeReady {

    private static final String TAG = "main_activity6";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DirectionsManager x = new DirectionsManager();
        //https://maps.googleapis.com/maps/api/directions/json?origin=31.750876,%2035.223831&destination=31.774577,%2035.197986&key=AIzaSyBJK_IDdOZOegnfCPCBn4d016TpVWrBz44
        x.sendRequest(new Place(31.750876, 35.223831), new Place(31.774577, 35.197986), this);
    }

    @Override
    public void onTimeReady(int timeUntilArrive) {
        Log.d(TAG, "time ready!!!" + timeUntilArrive);
    }


}
