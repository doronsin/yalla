package kis.hackathon.winners.yalla;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


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
    RelativeLayout _rlMain;
    EditText _edtMsg;
    View _btnGo;
    View _vFragment;
    AutoCompleteTextView _actvContacts;
    SeekBar _seek;
//    SearchView _vSearch;

    private String[] _allContactNames;
    Map<String, Contact> _allContactsMap; // map from _allContactNames to the contacts

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //noinspection ConstantConditions
        findViewById(R.id.frag_place).setAlpha(0f);
        if(PermissionAsker.isAuthorisedAskPermissionIfNot(this)) {
            startEverything();
        }

    }

    private void startEverything() {
        populateContacts();
        initViews();
        AutoPlaces.populateAutoPlaces(this, R.id.frag_place, _tvPlace);
    }

    private void populateContacts() {
        _allContactNames = new String[0];
        if(1==1)return;
        ArrayList<Contact> allContacts = new ArrayList<>();
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
            allContacts.add(new Contact(name, phoneNumber));

        }
        phones.close();
        // create temp and remove duplicates
        ArrayList<String> tempContacts = new ArrayList<>();
        _allContactsMap = new HashMap<>();
        for (int i = 0; i < allContacts.size(); i++) {
            Contact contact = allContacts.get(i);
            String value = contact.name + " " + contact.phone;
            if (tempContacts.contains(value)) continue;
            tempContacts.add(value);
            _allContactsMap.put(value, contact);
        }
        _allContactNames = new String[tempContacts.size()];
        for (int i = 0; i < tempContacts.size(); i++) {
            _allContactNames[i] = tempContacts.get(i);
        }
    }

    private void initViews() {
        _rlMain = (RelativeLayout) findViewById(R.id.relative_layout);
        _tvPlace = (TextView) findViewById(R.id.tv_place);
        _vFragment = findViewById(R.id.frag_place);
        _ivMan = (ImageView) findViewById(R.id.iv_man);
        _ivWoman = (ImageView) findViewById(R.id.iv_woman);
        _tvUpdateMinutes = (TextView) findViewById(R.id.tv_minutes);
        _edtMsg = (EditText) findViewById(R.id.edt_msg);
        _btnGo = findViewById(R.id.btn_go);
        _btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YallaSmsManager manager = YallaSmsManager.getInstance();
                manager.set_msgToSend(_edtMsg.getText().toString());
                if (manager.isReady()) {
                    // update database if needed
                    MyDataBase db = new MyDataBase(YallaActivity.this);
                    TableItem item = db.getItem(YallaSmsManager.getInstance().get_phoneNumber());
                    if (item != null) { // update
                        item.addressLatLng = manager.get_dest();
                        item.address = manager.get_destName();
                        item.minutes = manager.get_minutesToArrive();
                        item.msg = manager.get_msgToSend();
                        db.update(item);
                    } else { // insert
                        item = new TableItem(
                                manager.get_phoneNumber(),
                                manager.get_destName(),
                                manager.get_minutesToArrive(),
                                manager.get_msgToSend(),
                                manager.get_dest()
                        );
                        db.insert(item);
                    }


                    startService(new Intent(YallaActivity.this, GPSservice.class));
                    Toast.makeText(YallaActivity.this, R.string.sms_will_be_sent, Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(YallaActivity.this, manager.whyNotReady(), Toast.LENGTH_LONG).show();
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
                String current = adapter.getItem(position);
                Contact relevant = _allContactsMap.get(current);
                assert relevant != null;

                YallaSmsManager.getInstance().set_phoneNumber(relevant.phone);

                // hide the keyboard and set focus to the whole screen (remove the blinking | )
                _rlMain.requestFocus();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(_rlMain.getWindowToken(), 0);


                MyDataBase db = new MyDataBase(YallaActivity.this);
                TableItem value = db.getItem(relevant.phone);
                if (value != null) {
                    // change address, change address on screen, change minutes, change minutes on screen
                    YallaSmsManager.getInstance().updateFromDb(value);
                    _seek.setProgress(value.minutes);
                    _edtMsg.setText(value.msg);
                    _tvPlace.setText(String.format(getString(R.string.close_to_message), value.address));
                    _tvUpdateMinutes.setText(String.format(getString(R.string.sms_minutes_show), value.minutes)
                    );
                }
            }
        });

        _seek = (SeekBar) findViewById(R.id.sb_ma);
        assert _seek != null;
        _seek.setProgress(_seek.getMax() / 2);
        updateMinutesTranceText(_seek.getMax() / 2);
        _seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateMinutesTranceText(progress);
            }
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        boolean isMan = true;
        ManWomanListener manListener = new ManWomanListener(isMan);
        _ivMan.setOnTouchListener(manListener);
        ManWomanListener womanListener = new ManWomanListener(!isMan);
        _ivWoman.setOnTouchListener(womanListener);
    }

    private void iconClickedOnImages(boolean isMan) {
        int additon = isMan?-1:1;
        _seek.setProgress(Math.max(0, Math.min(_seek.getMax(), _seek.getProgress()+ additon)));
    }

    private void updateMinutesTranceText(int progress) {
        progress+=1; // to run from 1 to max
        YallaSmsManager.getInstance().set_minutesToArrive(progress);
        // handle the UI
        float pf = (float) progress;
        float mf = (float) _seek.getMax();
        float womanAlpha =   (float) Math.max(pf / mf, MIN_ALPHA_FOR_VIEWS);
        float manAlpha =     (float) Math.max(1f - (pf / mf), MIN_ALPHA_FOR_VIEWS);
        _ivMan.setAlpha(manAlpha);
        _ivWoman.setAlpha(womanAlpha);

        _tvUpdateMinutes.setText(
                String.format(
                        getString(R.string.sms_minutes_show), progress
                )
        );
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==PermissionAsker.REQUEST_ASK_PERMISSIONS){
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.request_permissions_title)
                            .setMessage(R.string.try_again_permissions)
                            .setCancelable(false)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    PermissionAsker.isAuthorisedAskPermissionIfNot(YallaActivity.this);
                                }
                            })
                            .show();
                    return;
                }
            }
            // all permission came back positive. show the activity!
            startEverything();


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

    private class ManWomanListener implements View.OnTouchListener {
        final boolean _isMan;
        final Handler _handler;
        final Runnable _runner;
        boolean _stillTouching = false;
        boolean _alreadySentPostDelayed = false;
        static final int TIME_BETWEEN_LONGTOUCH_UPDATES_MS = 100;
        static final int TIME_UNTIL_IT_COUNTS_AS_LONGTOUCH = 500;

        ManWomanListener(boolean isMan) {
            _isMan = isMan;
            _handler = new Handler();
            _runner = new Runnable() {
                @Override
                public void run() {
                    if (!_stillTouching) return;
                    iconClickedOnImages(_isMan);
                    _handler.postDelayed(_runner, TIME_BETWEEN_LONGTOUCH_UPDATES_MS);
                }
            };
        }



        /**
         * Called when a touch event is dispatched to a view. This allows listeners to
         * get a chance to respond before the target view.
         *
         * @param v     The view the touch event has been dispatched to.
         * @param event The MotionEvent object containing full information about
         *              the event.
         * @return True if the listener has consumed the event, false otherwise.
         */
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    iconClickedOnImages(_isMan);
                    _stillTouching = true;
                    return true;
                case MotionEvent.ACTION_HOVER_EXIT:
                    _stillTouching = false;
                    return true;
                case MotionEvent.ACTION_UP:
                    _stillTouching = false;
                    return true;
                default:
                    if (_alreadySentPostDelayed) return false;
                    if (event.getEventTime()-event.getDownTime() < TIME_UNTIL_IT_COUNTS_AS_LONGTOUCH) return false;
                    _handler.post(_runner);
                    return true;
            }
        }
    }

}
