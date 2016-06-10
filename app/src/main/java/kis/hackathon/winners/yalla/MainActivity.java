package kis.hackathon.winners.yalla;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.maps.model.LatLng;

import android.view.View;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "main222";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_auto_place);
        Log.d(TAG, "test");
        MyDataBase x = new MyDataBase(this, "address", null, 1);
        Log.d(TAG, "test2");
        TableItem y = x.getItem("0505708855");
        Log.d(TAG, y.address);
//        x.insert(new TableItem("0505708855", "beytar", 5));
    }
}
