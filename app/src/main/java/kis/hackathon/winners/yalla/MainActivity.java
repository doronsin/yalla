package kis.hackathon.winners.yalla;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity implements TimeReady {

    private static final String TAG = "main_activity";
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 789;
    private TextView resultText;
    private SearchView searchView;
    private String _nameOfContact;
    private String _phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_auto_place);
        Log.d(TAG, "starting");
//        if(!SmsSender.getInstance().isAuthorised())
//          PermissionAsker.askForPermission//(this,Manifest.permission.SEND_SMS,SmsSender.REQ_PERMISSION_SEND_SMS);
//   	SmsSender.getInstance().sendSms(2, "0535246156");
        startService(new Intent(this, GPSservice.class));
        Log.d(TAG, "starting");
//        if(!SmsSender.getInstance().isAuthorised())
//          PermissionAsker.askForPermission(this,Manifest.permission.SEND_SMS,SmsSender.REQ_PERMISSION_SEND_SMS);
//      SmsSender.getInstance().sendSms(2, "0535246156");
//        AutoPlaces.populateAutoPlaces(this);

        Log.d(TAG, "before!");
        try {
            DirectionsManager.sendRequest(new LatLng(31.750876, 35.223831), new LatLng(29.580795, 34.946855), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        resultText = (TextView)findViewById(R.id.searchViewResult);
        setupSearchView();


        //https://maps.googleapis.com/maps/api/directions/json?origin=31.750876,%2035.223831&destination=31.774577,%2035.197986&key=AIzaSyBJK_IDdOZOegnfCPCBn4d016TpVWrBz44
    }
    private void setupSearchView() {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) findViewById(R.id.searchView);
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(searchableInfo);
    }


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
    @Override
    public void onTimeReady(int timeUntilArrive) {
        Log.d(TAG, "time ready!!!" + timeUntilArrive);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        if (ContactsContract.Intents.SEARCH_SUGGESTION_CLICKED.equals(intent.getAction())) {
            //handles suggestion clicked query
            String displayName = getDisplayNameForContact(intent);
            resultText.setText(displayName);
        } else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // handles a search query
            String query = intent.getStringExtra(SearchManager.QUERY);
            resultText.setText("should search for query: '" + query + "'...");
        }

    }
    private String getDisplayNameForContact(Intent intent) {
        Cursor phoneCursor = getContentResolver().query(intent.getData(), null, null, null, null);
        phoneCursor.moveToFirst();
        int idDisplayName = phoneCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        int hasNumber = phoneCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
        String s = ContactsContract.Contacts.HAS_PHONE_NUMBER;
//        int idDisplayName = phoneCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        _nameOfContact = phoneCursor.getString(idDisplayName);
        _phoneNumber = getPhoneNumber(_nameOfContact);
        // TODO call SmsSender and use the phone number


        phoneCursor.close();
        return _nameOfContact;
    }
    private String getPhoneNumber(String name) {
        String ret = null;
        String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " like'" + name + "'";
        String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor c = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection, selection, null, null);
        if (c != null) {
            if (c.moveToFirst()) {
                ret = c.getString(0);
            }
            c.close();
        }
        if (ret == null) {
            ret = "Unsaved";
        }
        return ret;
    }

    @Override
    public void onTimeFailure() {
        Log.d(TAG, "time failure");
    }
}
