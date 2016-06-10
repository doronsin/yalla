package kis.hackathon.winners.yalla;

import android.*;
import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by odelya_krief on 09-Jun-16.
 */
public class PermissionAsker {
    public static final int REQUEST_ASK_PERMISSIONS = 12345;


    public static void askForPermissionsIfNeeded(AppCompatActivity activity)
    {
        List<String> allPermissions = Arrays.asList(
                Manifest.permission.SEND_SMS,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.INTERNET);
        ArrayList<String> neededPermissions = new ArrayList<>();
        for (String permission : allPermissions) {
            int res = activity.checkCallingOrSelfPermission(permission);
            if(res != PackageManager.PERMISSION_GRANTED) {
                neededPermissions.add(permission);
            }
        }

        if(neededPermissions.size() > 0) {
            ActivityCompat.requestPermissions(activity,
                    neededPermissions.toArray(new String[neededPermissions.size()]),
                    REQUEST_ASK_PERMISSIONS);
        }
    }
    //    if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//        PermissionAsker.askForPermissionsIfNeeded(mActivity, android.Manifest.permission.ACCESS_FINE_LOCATION, REQ_ACCESS_LOCATION);
}
