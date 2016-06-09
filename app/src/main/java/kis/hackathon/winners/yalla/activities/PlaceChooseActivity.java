package kis.hackathon.winners.yalla.activities;

import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import kis.hackathon.winners.yalla.AutoPlaces;
import kis.hackathon.winners.yalla.R;
import kis.hackathon.winners.yalla.YallaSmsManager;

/**
 * Created by Re'em on 6/10/2016.
 * second screen
 */

public class PlaceChooseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("reem", YallaSmsManager.getInstance().get_phoneNumber());
        setContentView(R.layout.activity_choose_place);

        TextView person = (TextView) findViewById(R.id.tv_place_person);
        person.setText(String.format("Send SMS to %s", YallaSmsManager.getInstance().get_contactName()));
        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Pair<View, String> contactPair, placePair;
        contactPair = new Pair<>(findViewById(R.id.tv_place_person), getString(R.string.trans_contact));
        placePair = new Pair<>(findViewById(R.id.frag_place), getString(R.string.trans_place));

        Bundle bundle = Utils.MakeCoolTransition(this, contactPair, placePair);

        AutoPlaces.populateAutoPlaces(this, R.id.frag_place, bundle);

    }
}
