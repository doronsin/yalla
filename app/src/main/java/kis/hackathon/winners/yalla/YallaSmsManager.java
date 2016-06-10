package kis.hackathon.winners.yalla;

import android.app.PendingIntent;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.google.maps.model.LatLng;

/**
 * Created by odelya_krief on 09-Jun-16.
 * Singleton
 * In charge of sending the sms messages.
 */
public class YallaSmsManager {
    public static final int REQ_PERMISSION_SEND_SMS = 123;
    private static YallaSmsManager instance;
    private static PendingIntent smsSentIntent = null;
    private static PendingIntent smsDeliveredIntent = null;



    private YallaApp _app;
    private String _phoneNumber = null;
    private int _minutesToArrive = -1;
    private LatLng _dest = null;
    private String _placeName = null;




    public void set_phoneNumber(String _phoneNumber) {
        this._phoneNumber = _phoneNumber;
    }

    public int get_minutesToArrive() {
        return _minutesToArrive;
    }

    public void set_minutesToArrive(int _minutesToArrive) {
        this._minutesToArrive = _minutesToArrive;
    }


    public static void init(YallaApp yallaApp)
    {
        instance=new YallaSmsManager(yallaApp);
    }
    public static YallaSmsManager getInstance()
    {
        return instance;
    }

    public YallaSmsManager(YallaApp _app) {
        this._app = _app;
    }

    public void sendSms() {
        String content = _app.getString(R.string.sms_minutes);
        String msgToSend = String.format(content, _minutesToArrive);
        SmsManager smsManager=SmsManager.getDefault();
        smsManager.sendTextMessage(_phoneNumber, null, msgToSend, smsSentIntent, smsDeliveredIntent);

    }

    public LatLng get_dest() {
        return _dest;
    }

    public void set_dest(LatLng _dest) {
        this._dest = _dest;
    }

    public String get_placeName() {
        return _placeName;
    }

    public void set_destName(String _placeName) {
        this._placeName = _placeName;
    }

    //    public boolean isAuthorised()
//    {
//        int result=_app.checkSelfPermission(Manifest.permission.SEND_SMS);
//        return result== PackageManager.PERMISSION_GRANTED;
//    }
    public boolean isReady() {
        return _minutesToArrive >= 0
                && _phoneNumber != null
                && +_phoneNumber.length() > 0
                && _dest != null;
    }

    public String whyNotReady() {
        if (_phoneNumber == null ||_phoneNumber.length() > 0)
            return _app.getString(R.string.choose_a_contact);
        else {
            return _app.getString(R.string.choos_dest);
        }
    }
}
