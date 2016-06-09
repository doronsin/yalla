package kis.hackathon.winners.yalla.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import kis.hackathon.winners.yalla.GPSservice;
import kis.hackathon.winners.yalla.R;
import kis.hackathon.winners.yalla.YallaSmsManager;

/**
 * Created by Re'em on 6/10/2016.
 * third activity
 */

public class TimeChooseActivity extends Activity {


    public static final String SHOULD_EXIT = "need_to_exit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_time);

        /*
        1. listeners for the prev. chosen stuff
        2. set the seekbar
        3. start on press the button
         */

        // start first activity from the beginning
        TextView personView = (TextView) findViewById(R.id.tv_time_person);
        personView.setText(String.format(getString(R.string.contact_saved), YallaSmsManager.getInstance().get_contactName()));
        personView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startFresh = new Intent(TimeChooseActivity.this, PersonChooseActivity.class);
                startFresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(startFresh);
                finish();
            }
        });

        TextView placeView = (TextView) findViewById(R.id.tv_time_place);
        // just go to prev. activity
        placeView.setText(String.format(getString(R.string.close_to_message), YallaSmsManager.getInstance().get_placeName()));
        placeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        findViewById(R.id.btn_time).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(TimeChooseActivity.this, GPSservice.class));
                Intent finishHere = new Intent(TimeChooseActivity.this, PersonChooseActivity.class);
                finishHere.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finishHere.putExtra(TimeChooseActivity.SHOULD_EXIT, true);
                startActivity(finishHere);
//                finish();
            }
        });





    }
}
