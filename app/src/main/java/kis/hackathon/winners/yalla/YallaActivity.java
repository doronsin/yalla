package kis.hackathon.winners.yalla;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * Created by Re'em on 6/10/2016.
 *
 * main activity
 */

public class YallaActivity extends AppCompatActivity {
    private static final boolean OPEN_SEARCH_VIEW = false;
    private static final String TAG = "yalla";
    private static final double MIN_ALPHA_FOR_VIEWS = 0.3f;
    TextView _tvPlace;
    ImageView _ivMan;
    ImageView _ivWoman;
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
        PermissionAsker.askForPermissionsIfNeeded(this);
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
        _ivMan = (ImageView) findViewById(R.id.iv_man);
        _ivWoman = (ImageView) findViewById(R.id.iv_woman);
        _tvUpdateMinutes = (TextView) findViewById(R.id.tv_minutes);
        _btnGo = findViewById(R.id.btn_go);
        _btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "creaetd");
                YallaSmsManager manager = YallaSmsManager.getInstance();
                if (manager.isReady()) {
                    // update database if needed
                    MyDataBase db = new MyDataBase(YallaActivity.this);
                    TableItem item = db.getItem(YallaSmsManager.getInstance().get_phoneNumber());
                    if (item != null) { // update
                        item.addressLatLng = manager.get_dest();
                        item.address = manager.get_placeName();
                        item.minutes = manager.get_minutesToArrive();
                        db.update(item);
                    } else { // insert
                        item = new TableItem(
                                manager.get_phoneNumber(),
                                manager.get_placeName(),
                                manager.get_minutesToArrive(),
                                manager.get_dest().lat,
                                manager.get_dest().lng
                        );
                        db.insert(item);
                    }


                    startService(new Intent(YallaActivity.this, GPSservice.class));
                    Toast.makeText(YallaActivity.this, "SMS will be sent on minutes.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(YallaActivity.this, manager.whyNotReady(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        _actvContacts = (AutoCompleteTextView) findViewById(R.id.actv_contacts);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, _allContactNames);
        _actvContacts.setAdapter(adapter);
        _actvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contact relevant = null;
                String current = adapter.getItem(position);
                for (Contact c: _allContacts)
                    if (c.name.equals(current))
                        relevant = c;
                assert relevant != null;

                YallaSmsManager.getInstance().set_phoneNumber(relevant.phone);

                MyDataBase db = new MyDataBase(YallaActivity.this);
                TableItem value = db.getItem(relevant.phone);
                if (value != null) {
                    // change address, change address on screen, change minutes, change minutes on screen
                    YallaSmsManager.getInstance().set_dest(value.addressLatLng);
                    YallaSmsManager.getInstance().set_minutesToArrive(value.minutes);
                    _tvPlace.setText(
                            String.format(getString(R.string.close_to_message), value.address)
                            );
                    _tvUpdateMinutes.setText(
                            String.format(
                                    getString(R.string.sms_minutes_show), value.minutes
                            )
                    );
                    _seek.setProgress(value.minutes);
                }


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
        _ivMan.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                iconClickedIsMan(true);
            }
        });
        _ivWoman.setOnClickListener(new View.OnClickListener(){
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
        _ivMan.setAlpha(manAlpha);
        _ivWoman.setAlpha(womanAlpha);
        _tvUpdateMinutes.setText(
                String.format(
                        getString(R.string.sms_minutes_show), progress
                )
        );
        YallaSmsManager.getInstance().set_minutesToArrive(progress);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode==PermissionAsker.REQUEST_ASK_PERMISSIONS){
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, R.string.try_again_permissions, Toast.LENGTH_LONG).show();
                    PermissionAsker.askForPermissionsIfNeeded(this);
                }

            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private class Contact {
        String name, phone;
        Contact(String name, String phone) {
            this.name = name;
            this.phone = phone;
        }
    }

}
