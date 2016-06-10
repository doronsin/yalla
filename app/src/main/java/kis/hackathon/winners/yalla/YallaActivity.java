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
    private static final double MIN_ALPHA_FOR_VIEWS = 0.3f;
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
        _tvPlace = (TextView) findViewById(R.id.tv_place);
        _vFragment = findViewById(R.id.frag_place);
        _vFragment.setAlpha(0f);
        _tvMan = (ImageView) findViewById(R.id.iv_man);
        _tvWoman = (ImageView) findViewById(R.id.iv_woman);
        _tvUpdateMinutes = (TextView) findViewById(R.id.tv_minutes);
        _btnGo = findViewById(R.id.btn_go);
        _btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "creaetd");
                YallaSmsManager manager = YallaSmsManager.getInstance();
                if (manager.isReady()) {
                    startService(new Intent(YallaActivity.this, GPSservice.class));
                    Toast.makeText(YallaActivity.this, "SMS will be sent on time.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(YallaActivity.this, manager.whyNotReady(), Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                Log.d(TAG, "phone number changed");
            }
        });

        _seek = (SeekBar) findViewById(R.id.sb_ma);
        _seek.setProgress(10);
        updateMinutesTranceText(10);
        _seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateMinutesTranceText(progress);
            }
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        _tvMan.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                iconClickedIsMan(true);
            }
        });
        _tvWoman.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                iconClickedIsMan(false);
            }
        });
    }

    private void iconClickedIsMan(boolean b) {
        int additon = b?-1:1;
        _seek.setProgress(Math.max(0, Math.min(_seek.getProgress()+ additon,20)));
    }

    private void updateMinutesTranceText(int progress) {
        progress+=1;
        float womanAlpha =   (float) Math.max(progress * 0.05f, MIN_ALPHA_FOR_VIEWS);
        float manAlpha =     (float) Math.max(1f - (progress * 0.05f), MIN_ALPHA_FOR_VIEWS);
        Log.d(TAG,"manAlpha:"+manAlpha+ " womanAlpha"+womanAlpha);
        _tvMan.setAlpha(manAlpha);
        _tvWoman.setAlpha(womanAlpha);
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
