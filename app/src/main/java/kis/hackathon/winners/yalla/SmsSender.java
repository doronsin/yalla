package kis.hackathon.winners.yalla;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.app.PendingIntent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.support.v4.content.ContextCompat;
import java.text.Format;
import java.util.Formatter;

/**
 * Created by odelya_krief on 09-Jun-16.
 * Singleton
 * In charge of sending the sms messages.
 */
public class SmsSender {
    public static final int PERMISSION_SEND_SMS = 123;
    private static SmsSender instance;
    private static PendingIntent smsSentIntent = null;
    private static PendingIntent smsDeliveredIntent = null;



    private YallaApp _app;
    public static void init(YallaApp yallaApp)
    {
        instance=new SmsSender(yallaApp);
    }
    public static SmsSender getInstance()
    {
        return instance;
    }

    public SmsSender(YallaApp _app) {
        this._app = _app;
    }
    private void sendSms(String phoneNum,String content)
    {
        SmsManager smsManager=SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNum,null,content,smsSentIntent,smsDeliveredIntent);
    }

    public void sendSms(int minutesToarrive,String phoenNum)
    {
        String content=_app.getString(R.string.sms_minutes);
        sendSms(phoenNum, String.format(content, minutesToarrive));
    }
    public boolean isAuthorised()
    {
        int result=_app.checkSelfPermission(Manifest.permission.SEND_SMS);
        return result== PackageManager.PERMISSION_GRANTED;
    }

    public void askForPermission(Activity activity)
    {

        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.SEND_SMS},
                PERMISSION_SEND_SMS);
    }
}
