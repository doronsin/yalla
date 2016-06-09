package kis.hackathon.winners.yalla;

import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "main_activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "starting");
        SmsSender.getInstance().askForPermission(this);
      SmsSender.getInstance().sendSms(2, "0535246156");
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == SmsSender.PERMISSION_SEND_SMS) {
            Log.d(TAG ,"" +grantResults[0]);
            Log.d(TAG ,"Granted is " + PackageManager.PERMISSION_GRANTED);
        } else
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
