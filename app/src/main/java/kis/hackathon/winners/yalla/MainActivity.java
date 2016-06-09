package kis.hackathon.winners.yalla;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "main_activity";
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 789;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_auto_place);
        startService(new Intent(this, GPSservice.class));
        Log.d(TAG, "starting");
//        if(!SmsSender.getInstance().isAuthorised())
//          PermissionAsker.askForPermission(this,Manifest.permission.SEND_SMS,SmsSender.REQ_PERMISSION_SEND_SMS);
//      SmsSender.getInstance().sendSms(2, "0535246156");
        AutoPlaces.populateAutoPlaces(this);





    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == SmsSender.REQ_PERMISSION_SEND_SMS) {
            //TODO IF PERMISSION DENIED...

        }
        else
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);


    }
}
