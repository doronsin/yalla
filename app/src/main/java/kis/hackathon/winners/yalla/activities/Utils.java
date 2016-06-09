package kis.hackathon.winners.yalla.activities;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.view.View;

import kis.hackathon.winners.yalla.R;

/**
 * Created by Re'em on 6/10/2016.
 */

public class Utils {
    public static Bundle MakeCoolTransition(Activity sender, Pair<View, String> ... views) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            return new Bundle();

        return ActivityOptionsCompat.makeSceneTransitionAnimation(
                sender,
                views
        ).toBundle();

    }
}
