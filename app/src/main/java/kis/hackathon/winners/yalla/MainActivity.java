package kis.hackathon.winners.yalla;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.Manifest;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "main_activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this,GPSservice.class));
        Log.d(TAG, "starting");
//        if(!SmsSender.getInstance().isAuthorised())
//          PermissionAsker.askForPermission(this,Manifest.permission.SEND_SMS,SmsSender.REQ_PERMISSION_SEND_SMS);
     //   SmsSender.getInstance().sendSms(2, "0535246156");

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
