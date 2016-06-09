package kis.hackathon.winners.yalla.activities;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

/**
 * Created by Re'em on 6/10/2016.
 * exit
 *
 * should be created with FLAG_CLEAR_TOP
 */

public class CloseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        finish();
    }
}
