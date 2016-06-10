package kis.hackathon.winners.yalla;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "main_activity";
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 789;
    private static final String SMS_MINUTES_TEXT ="SMS will be sent %d minutes\n before you arrive" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_auto_place);
        startService(new Intent(this, GPSservice.class));
        Log.d(TAG, "starting");
//        if(!SmsSender.getInstance().isAuthorised())
//          PermissionAsker.askForPermission(this,Manifest.permission.SEND_SMS,SmsSender.REQ_PERMISSION_SEND_SMS);
//      SmsSender.getInstance().sendSms(2, "0535246156");
        AutoPlaces.populateAutoPlaces(this);
       SeekBar seekBar=((SeekBar) findViewById(R.id.sb_ma));
        //seekBar.getThumb().setColorFilter(getColor(R.color.gray), PorterDuff.Mode.MULTIPLY);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int seekBarProgress = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarProgress = progress;
                updateMinutesTranceText(progress);

            }

            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }


        });
    }

    private void updateMinutesTranceText(int progress) {
        ImageView man = (ImageView) findViewById(R.id.iv_man);
        ImageView woman = (ImageView) findViewById(R.id.iv_woman);
        TextView textView= (TextView) findViewById(R.id.tv_minutes);
        float womanAlph = Math.min(1,(float)(progress+50)/100);
        float manAlph = Math.min(1, (1 - progress + 50) / 100);
        Log.d(TAG,"manAlph:"+manAlph+ " womanAlph"+womanAlph);
        man.setAlpha(manAlph);
        woman.setAlpha(womanAlph);
        if (between(0,20,progress)){
            textView.setText("SMS will be sent 2 minutes\n before you arrive");
            return;
        }
        if (between(20,40,progress)){
            textView.setText(String.format(SMS_MINUTES_TEXT,2));


            return;
        }
        if (between(40,60,progress)){
            textView.setText(String.format(SMS_MINUTES_TEXT,4));
            return;
        }
        if (between(60,80,progress)){

            textView.setText(String.format(SMS_MINUTES_TEXT,6));
            return;
        }
        if (between(80,100,progress)){

            textView.setText(String.format(SMS_MINUTES_TEXT,8));
            return;
        }
    }
    private boolean between(int start, int end, int numb){
        return start<= numb && numb<end;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == SmsSender.REQ_PERMISSION_SEND_SMS) {
            //TODO IF PERMISSION DENIED...

        }
        else
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);


    }
}
