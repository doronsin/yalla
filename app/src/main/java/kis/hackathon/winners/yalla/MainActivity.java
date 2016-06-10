package kis.hackathon.winners.yalla;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.Manifest;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.maps.model.LatLng;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity  {

    private static final String TAG = "main_activity";
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 789;
    private TextView resultText;
    private SearchView searchView;
    private String _nameOfContact;
    private String _phoneNumber;
    private static final String SMS_MINUTES_TEXT ="SMS will be sent %d minutes\n before you arrive" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main_auto_place);
        Log.d(TAG, "starting");
//        if(!SmsSender.getInstance().isAuthorised())
//          PermissionAsker.askForPermissionsIfNeeded//(this,Manifest.permission.SEND_SMS,SmsSender.REQ_PERMISSION_SEND_SMS);
//   	SmsSender.getInstance().sendSms(2, "0535246156");
        startService(new Intent(this, GPSservice.class));
        Log.d(TAG, "starting");
//        if(!SmsSender.getInstance().isAuthorised())
//          PermissionAsker.askForPermissionsIfNeeded(this,Manifest.permission.SEND_SMS,SmsSender.REQ_PERMISSION_SEND_SMS);
//      SmsSender.getInstance().sendSms(2, "0535246156");
//        AutoPlaces.populateAutoPlaces(this);

//        Log.d(TAG, "before!");
//        try {
//            DirectionsManager.sendRequest(new LatLng(31.750876, 35.223831), new LatLng(29.580795, 34.946855), this);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        resultText = (TextView)findViewById(R.id.searchViewResult);
//        setupSearchView();
//        AutoPlaces.populateAutoPlaces(this);
//       SeekBar seekBar=((SeekBar) findViewById(R.id.sb_ma));
        //seekBar.getThumb().setColorFilter(getColor(R.color.gray), PorterDuff.Mode.MULTIPLY);
//        seekBar.setProgress(50);
//        updateMinutesTranceText(seekBar.getProgress());

//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            int seekBarProgress = 0;
//
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                seekBarProgress = progress;
//                updateMinutesTranceText(progress);

        //https://maps.googleapis.com/maps/api/directions/json?origin=31.750876,%2035.223831&destination=31.774577,%2035.197986&key=AIzaSyBJK_IDdOZOegnfCPCBn4d016TpVWrBz44
    }
    private void setupSearchView() {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        searchView = (SearchView) findViewById(R.id.searchView);
//        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
//        searchView.setSearchableInfo(searchableInfo);
    }


            public void onStartTrackingTouch(SeekBar seekBar) {

//    @Override
//    public void onRequestPermissionsResult(int requestCode, //String[] permissions, int[] grantResults) {
//        if (requestCode == SmsSender.REQ_PERMISSION_SEND_SMS) //{
//            //TODO IF PERMISSION DENIED...
//
//        }
//        else
//            super.onRequestPermissionsResult(requestCode, //permissions, grantResults);
//
//
//        Log.d(TAG, "before!");
//        try {
//            DirectionsManager.sendRequest(new LatLng//(31.750876, 35.223831), new LatLng(29.580795, 34.946855), //this);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        //https://maps.googleapis.com/maps/api/directions/json?//origin=31.750876,%2035.223831&destination=31.774577,//%2035.197986&key=AIzaSyBJK_IDdOZOegnfCPCBn4d016TpVWrBz44
//
//    }
//

            }
//
//        });
//        SetIconesListener();
//    }
//
//    private void SetIconesListener() {
//        ImageView man = (ImageView) findViewById(R.id.iv_man);
//        ImageView woman = (ImageView) findViewById(R.id.iv_woman);
//        man.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View v) {
//                iconClickedIsMan(true);
//            }
//        });
//        woman.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View v) {
//                iconClickedIsMan(false);
//            }
//        });
//    }
//
//    private void iconClickedIsMan(boolean b) {
//        SeekBar seekBar=((SeekBar) findViewById(R.id.sb_ma));
//        int additon = b?-5:5;
//        seekBar.setProgress(Math.min(seekBar.getProgress()+ additon,100));
//
//    }
//
//    private void updateMinutesTranceText(int progress) {
//        ImageView man = (ImageView) findViewById(R.id.iv_man);
//        ImageView woman = (ImageView) findViewById(R.id.iv_woman);
//        TextView textView= (TextView) findViewById(R.id.tv_minutes);
//        float womanAlph = Math.min(1,(float)(progress+50)/100);
//        float manAlph = Math.min(1, (float)1-(float)progress/100+(float)0.5);
//        Log.d(TAG,"manAlph:"+manAlph+ " womanAlph"+womanAlph);
//        man.setAlpha(manAlph);
//        woman.setAlpha(womanAlph);
//        if (between(0,20,progress)){
//            textView.setText("SMS will be sent 2 minutes\n before you arrive");
//            return;
//        }
//        if (between(20,40,progress)){
//            textView.setText(String.format(SMS_MINUTES_TEXT,2));
//
//
//            return;
//        }
//        if (between(40,60,progress)){
//            textView.setText(String.format(SMS_MINUTES_TEXT,4));
//            return;
//        }
//        if (between(60,80,progress)){
//
//            textView.setText(String.format(SMS_MINUTES_TEXT,6));
//            return;
//        }
//        if (between(80,100,progress)){
//
//            textView.setText(String.format(SMS_MINUTES_TEXT,8));
//            return;
//        }
//    }
//
//    private boolean between(int start, int end, int numb){
//        return start<= numb && numb<end;
//    }

//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        if (ContactsContract.Intents.SEARCH_SUGGESTION_CLICKED.equals(intent.getAction())) {
//            //handles suggestion clicked query
//            String displayName = getDisplayNameForContact(intent);
//            resultText.setText(displayName);
//        } else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//            // handles a search query
//            String query = intent.getStringExtra(SearchManager.QUERY);
//            resultText.setText("should search for query: '" + query + "'...");
//        }
//
//    }
//    private String getDisplayNameForContact(Intent intent) {
//        Cursor phoneCursor = getContentResolver().query(intent.getData(), null, null, null, null);
//        phoneCursor.moveToFirst();
//        int idDisplayName = phoneCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
//        int hasNumber = phoneCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
//        String s = ContactsContract.Contacts.HAS_PHONE_NUMBER;
////        int idDisplayName = phoneCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
//        _nameOfContact = phoneCursor.getString(idDisplayName);
//        _phoneNumber = getPhoneNumber(_nameOfContact);
//        // TODO call SmsSender and use the phone number
//
//
//        phoneCursor.close();
//        return _nameOfContact;
//    }
//    private String getPhoneNumber(String name) {
//        String ret = null;
//        String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " like'" + name + "'";
//        String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
//        Cursor c = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                projection, selection, null, null);
//        if (c != null) {
//            if (c.moveToFirst()) {
//                ret = c.getString(0);
//            }
//            c.close();
//        }
//        if (ret == null) {
//            ret = "Unsaved";
//        }
//        return ret;
//    }
//
//    @Override
//    public void onTimeFailure() {
//        Log.d(TAG, "time failure");
//    }
}
