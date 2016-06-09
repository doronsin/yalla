package kis.hackathon.winners.yalla.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import kis.hackathon.winners.yalla.R;

/**
 * Created by Re'em on 6/10/2016.
 * third activity
 */

public class MainActivity extends Activity {


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
        findViewById(R.id.tv_time_person).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startFresh = new Intent(MainActivity.this, PersonChooseActivity.class);
                startFresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(startFresh);
                finish();
            }
        });

        // just go to prev. activity
        findViewById(R.id.tv_time_place).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });









    }
}
