package kis.hackathon.winners.yalla;

import android.app.Application;

/**
 * Created by odelya_krief on 09-Jun-16.
 *
 * application class
 */
public class YallaApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        YallaSmsManager.init(this);
    }
}
