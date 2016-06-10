package kis.hackathon.winners.yalla;

import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TwoLineListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Re'em on 6/10/2016.
 *
 * main activity
 */

public class YallaActivity extends Activity {
    private static final boolean OPEN_SEARCH_VIEW = false;
    TextView _tvPlace;
    View _vFragment, _btnGo;
    AutoCompleteTextView _actvContacts;
//    SearchView _vSearch;

    ArrayList<Contact> _allContacts;
    private String[] _allContactNames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateContacts();
        initViews();
        AutoPlaces.populateAutoPlaces(this, R.id.frag_place, _tvPlace);
    }

    private void populateContacts() {
        _allContacts = new ArrayList<>();
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        if (phones == null) {
            _allContactNames = new String[0];
            return;
        }
        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            if (name==null || name.length()==0 || phoneNumber==null || phoneNumber.length()==0) continue;
            _allContacts.add(new Contact(name, phoneNumber));

        }
        phones.close();
        _allContactNames = new String[_allContacts.size()];
        for (int i = 0; i < _allContacts.size(); i++) {
            _allContactNames[i] = _allContacts.get(i).name;
        }
    }

    private void initViews() {
        // TODO also the seekbar
        _tvPlace = (TextView) findViewById(R.id.tv_place);
        _vFragment = findViewById(R.id.frag_place);
        _vFragment.setAlpha(0f);
        _btnGo = findViewById(R.id.btn_time);
        _actvContacts = (AutoCompleteTextView) findViewById(R.id.actv_contacts);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, _allContactNames);
        _actvContacts.setAdapter(adapter);
        _actvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contact relevant = null;
                String current = _allContactNames[position];
                for (Contact c: _allContacts)
                    if (c.name.equals(current))
                        relevant = c;
                assert relevant != null;
                YallaSmsManager.getInstance().set_phoneNumber(relevant.phone);
            }
        });

    }

    ////////////////////////////////////////// activity 1 /////////////////////////////////



//    private void setupSearchView() {
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView = (SearchView) findViewById(R.id.sv_person);
//        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
//        assert searchView != null;
//        searchView.setSearchableInfo(searchableInfo);
//        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (hasFocus) {
//                    showInputMethod(view.findFocus());
//                }
//            }
//        });
//
//    }
//
//
//    private void showInputMethod(View view) {
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (imm != null) {
//            imm.showSoftInput(view, 0);
//        }
//    }
//
//    protected void onNewIntent(Intent intent) {
//        if (ContactsContract.Intents.SEARCH_SUGGESTION_CLICKED.equals(intent.getAction())) {
//            //handles suggestion clicked query
//            if (!handlePersonWentOk(intent))
//                return;
//            else {
//                _vSearch.setVisibility(View.INVISIBLE);
//                _vSearch.setSearchableInfo(null);
//                _tvPerson.setText(String.format(
//                        getString(R.string.contact_saved),
//                        YallaSmsManager.getInstance().get_contactName()
//                ));
//                _tvPerson.setVisibility(View.VISIBLE);
//            }
//        } else {
//            super.onNewIntent(intent);
//        }
//    }
//
//    /**
//     * returns true on success, false otherwise
//     * @param intent
//     * @return
//     */
//    private boolean handlePersonWentOk(Intent intent) {
//        Cursor phoneCursor = getContentResolver().query(intent.getData(), null, null, null, null);
//        assert phoneCursor != null;
//        phoneCursor.moveToFirst();
//        int idDisplayName = phoneCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
//
//
//        String s = ContactsContract.Contacts.HAS_PHONE_NUMBER;
//
//        String nameOfContact = phoneCursor.getString(idDisplayName);
//        String phoneNumber = getPhoneNumber(nameOfContact);
//        if (phoneNumber == null) {
//            Toast.makeText(this, "No phone for this contact. please choose again", Toast.LENGTH_SHORT).show();
//            phoneCursor.close();
//            return false;
//        } else {
//            YallaSmsManager.getInstance().set_phoneNumber(phoneNumber);
//            YallaSmsManager.getInstance().set_contactName(nameOfContact);
//        }
//        phoneCursor.close();
//        return true;
//    }
//
//    @Nullable
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
//        return ret;
//    }

    ////////////////////////////////////////// activity 2 /////////////////////////////////


    private void clearPlace() {
        _tvPlace.setText(YallaSmsManager.getInstance().get_placeName());
        _tvPlace.setVisibility(View.VISIBLE);
        _vFragment.setVisibility(View.INVISIBLE);
    }

    private class Contact {
        public String name, phone;

        public Contact(String name, String phone) {
            this.name = name;
            this.phone = phone;
        }
    }

//    private class ContactAdapter extends ArrayAdapter<Contact> {
//        private List<Contact> _contacts;
//        /**
//         * Constructor
//         *
//         * @param context  The current context.
//         * @param resource The resource ID for a layout file containing a TextView to use when
//         *                 instantiating views.
//         * @param objects  The objects to represent in the ListView.
//         */
//        public ContactAdapter(Context context, int resource, List<Contact> objects) {
//            super(context, resource, objects);
//            _contacts = objects;
//        }
//
//        /**
//         * {@inheritDoc}
//         *
//         * @param position
//         * @param convertView
//         * @param parent
//         */
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            TwoLineListItem retval;
//            Contact currentContact = _contacts.get(position);
//            if (convertView == null)
//                retval = (TwoLineListItem) getLayoutInflater().inflate(R.layout.line, parent, false);
//            else
//                retval = (TwoLineListItem) convertView;
//            TextView tvContact, tvPhone;
//            tvContact = retval.getText1();
//            tvContact.setText(currentContact.name);
//            tvPhone = retval.getText2();
//            tvPhone.setText(currentContact.phone);
//            return retval;
//        }
//    }
}
