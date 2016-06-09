package kis.hackathon.winners.yalla.activities;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import kis.hackathon.winners.yalla.R;
import kis.hackathon.winners.yalla.YallaSmsManager;

/**
 * Created by Re'em on 6/10/2016.
 * first activity
 */

public class PersonChooseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean shouldExit = getIntent().getBooleanExtra(TimeChooseActivity.SHOULD_EXIT, false);
        if (shouldExit) {
            finish();
            return;
        }


        setContentView(R.layout.activity_choose_person);

        setupSearchView();

    }


    private void setupSearchView() {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) findViewById(R.id.sv_person);
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        assert searchView != null;
        searchView.setSearchableInfo(searchableInfo);
    }


    protected void onNewIntent(Intent intent) {
        if (ContactsContract.Intents.SEARCH_SUGGESTION_CLICKED.equals(intent.getAction())) {
            //handles suggestion clicked query
            if (handlePress(intent) == false)
                return;
            Pair<View, String> pair = new Pair<>(findViewById(R.id.sv_person), getString(R.string.trans_contact));
            startActivity(
                    new Intent(this, PlaceChooseActivity.class),
                    Utils.MakeCoolTransition(this, pair));
        } else {
            super.onNewIntent(intent);
        }
    }

    /**
     * returns true on success, false otherwise
     * @param intent
     * @return
     */
    private boolean handlePress(Intent intent) {
        Cursor phoneCursor = getContentResolver().query(intent.getData(), null, null, null, null);
        assert phoneCursor != null;
        phoneCursor.moveToFirst();
        int idDisplayName = phoneCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);


        String s = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        String nameOfContact = phoneCursor.getString(idDisplayName);
        String phoneNumber = getPhoneNumber(nameOfContact);
        if (phoneNumber == null) {
            Toast.makeText(this, "No phone for this contact. please choose again", Toast.LENGTH_SHORT).show();
            phoneCursor.close();
            return false;
        } else {
            YallaSmsManager.getInstance().set_phoneNumber(phoneNumber);
            YallaSmsManager.getInstance().set_contactName(nameOfContact);
        }
        phoneCursor.close();
        return true;
    }

    @Nullable
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
        return ret;
    }

}
