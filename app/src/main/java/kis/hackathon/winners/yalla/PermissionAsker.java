package kis.hackathon.winners.yalla;

import android.*;
import android.app.Activity;
import android.support.v4.app.ActivityCompat;

/**
 * Created by odelya_krief on 09-Jun-16.
 */
public class PermissionAsker {

    public static void askForPermission(Activity activity, String permission, int requestCode)
    {

        ActivityCompat.requestPermissions(activity,
                new String[]{permission},
                requestCode);
    }
    //    if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//        PermissionAsker.askForPermission(mActivity, android.Manifest.permission.ACCESS_FINE_LOCATION, REQ_ACCESS_LOCATION);
}
