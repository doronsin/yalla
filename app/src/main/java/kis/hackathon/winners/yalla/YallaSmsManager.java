package kis.hackathon.winners.yalla;

import android.app.PendingIntent;
import android.telephony.SmsManager;

import com.google.maps.model.LatLng;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by odelya_krief on 09-Jun-16.
 * Singleton
 * In charge of sending the sms messages.
 */
class YallaSmsManager {
//    private static final String SAVED_WORD_REGEX = "%%%";
    private static YallaSmsManager instance;
    private static final PendingIntent smsSentIntent = null;
    private static final PendingIntent smsDeliveredIntent = null;



    private final YallaApp _app;
    @Getter @Setter private String _phoneNumber = null;
    @Getter @Setter private String _contactName = null;
    @Getter @Setter private int _minutesToArrive = -1;
    @Getter @Setter private LatLng _dest = null;
    @Getter @Setter private String _destName = null;
    @Getter @Setter private String _msgToSend = null;



    static void init(YallaApp yallaApp)
    {
        instance=new YallaSmsManager(yallaApp);
    }
    static YallaSmsManager getInstance()
    {
        return instance;
    }

    private YallaSmsManager(YallaApp _app) {
        this._app = _app;
        this._msgToSend = _app.getString(R.string.sms_minutes);
    }

    void sendSms() {
        SmsManager smsManager=SmsManager.getDefault();
        smsManager.sendTextMessage(_phoneNumber, null, _msgToSend, smsSentIntent, smsDeliveredIntent);

    }

    boolean isReady() {
        return _minutesToArrive >= 0
                && _phoneNumber != null
                && +_phoneNumber.length() > 0
                && _dest != null
                && _msgToSend != null
                && _msgToSend.length() > 0;
    }

    String whyNotReady() {
        if (_phoneNumber == null ||_phoneNumber.length() == 0)
            return _app.getString(R.string.choose_a_contact);
        else if (_dest == null)
        {
            return _app.getString(R.string.choose_dest);
        } else { // must be a problem in the message
            return _app.getString(R.string.choose_msg_to_send);
        }
    }

    void updateFromDb(TableItem value) {
        _phoneNumber = value.phone;
        _minutesToArrive = value.minutes;
        _dest = value.addressLatLng;
        _destName = value.address;
        _msgToSend = value.msg;
    }

    public void replaceText(int oldValue, int newValue) {
        String replaceFrom = "" + oldValue;
        String replaceTo = "" + newValue;
        _msgToSend = _msgToSend.replace(replaceFrom, replaceTo);
    }
}
