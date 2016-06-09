package kis.hackathon.winners.yalla;

import android.app.PendingIntent;
import android.telephony.SmsManager;

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
    private String _phoneNumber;
    private String _contactName;
    private int _minutesToArrive;
    private LatLng _dest;
    private String _placeName;


    public String get_contactName() {
        return _contactName;
    }

    public void set_contactName(String _contactName) {
        this._contactName = _contactName;
    }



    public String get_phoneNumber() {
        return _phoneNumber;
    }

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


}