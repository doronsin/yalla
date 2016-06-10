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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.SeekBar;
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
    private static final String TAG = "yalla";
    TextView _tvPlace;
    ImageView _tvMan;
    ImageView _tvWoman;
    TextView _tvUpdateMinutes;

    View _vFragment, _btnGo;
    AutoCompleteTextView _actvContacts;
    SeekBar _seek;
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
        _tvMan = (ImageView) findViewById(R.id.iv_man);
        _tvWoman = (ImageView) findViewById(R.id.iv_woman);
        _tvUpdateMinutes = (TextView) findViewById(R.id.tv_minutes);
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

        _seek = (SeekBar) findViewById(R.id.sb_ma);
        _seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateMinutesTranceText(progress);
            }
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

    }

    private void updateMinutesTranceText(int progress) {
        float womanAlph = progress * 0.05f;
        float manAlph = 1f - (progress * 0.05f);
        Log.d(TAG,"manAlph:"+manAlph+ " womanAlph"+womanAlph);
        _tvMan.setAlpha(manAlph);
        _tvWoman.setAlpha(womanAlph);
        _tvUpdateMinutes.setText(
                String.format(
                        getString(R.string.sms_minutes_show), progress
                )
        );
        YallaSmsManager.getInstance().set_minutesToArrive(progress);
    }




    private class Contact {
        String name, phone;
        Contact(String name, String phone) {
            this.name = name;
            this.phone = phone;
        }
    }

}
