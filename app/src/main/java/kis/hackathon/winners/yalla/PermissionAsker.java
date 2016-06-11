package kis.hackathon.winners.yalla;

import android.*;
import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by odelya_krief on 09-Jun-16.
 *
 * used to check the permissions and ask for them if needed
 */
public class PermissionAsker {
    public static final int REQUEST_ASK_PERMISSIONS = 12345;


    /**
     *
     * @param activity
     * @return if authorised, return true. else - return false
     */
    public static boolean isAuthorised_AskPermissionAsyncIfNot(AppCompatActivity activity)
    {
        List<String> allPermissions = Arrays.asList(
                Manifest.permission.SEND_SMS,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.INTERNET);
        ArrayList<String> neededPermissions = new ArrayList<>();
        for (String permission : allPermissions) {
            int res = ContextCompat.checkSelfPermission(activity, permission);
            if(res != PackageManager.PERMISSION_GRANTED) {
                neededPermissions.add(permission);
            }
        }

        if(neededPermissions.size() == 0) return true;

        ActivityCompat.requestPermissions(activity,
                neededPermissions.toArray(new String[neededPermissions.size()]),
                REQUEST_ASK_PERMISSIONS);
        return false;

    }
}
