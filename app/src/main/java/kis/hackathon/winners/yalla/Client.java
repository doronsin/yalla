package kis.hackathon.winners.yalla;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.maps.model.LatLng;

import java.io.IOException;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
public class Client {
    private static final String TAG = Client.class.getSimpleName();
    public static final String SERVER_URL = "http://kis.rapidapi.io/addentity";
    private void addUser(final String number, final LatLng place) {
        new AsyncTask<Void, Void, Void>() {
            OkHttpClient client = new OkHttpClient();
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    RequestBody formBody = new FormBody.Builder()
                            .add("address", place.lat+ "," + place.lng)
                            .add("number", number)
                            .build();
                    Request request = new Request.Builder()
                            .url(SERVER_URL)
                            .post(formBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected Code: " + response);
                    } else {
                        Log.d(TAG, response.body().string());
                    }
                } catch (IOException e) {
                    Log.d(TAG, e.toString(), e.getCause());
                }
                return null;
            }
        }.execute();
    }

    private void getAddress(final String number, final ) {
        ​
        new AsyncTask<Void, Void, Void>() {
            OkHttpClient client = new OkHttpClient();
            ​
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    RequestBody formBody = new FormBody.Builder()
                            .add("number", number)
                            .build();
                    ​
                    Request request = new Request.Builder()
                            .url(SERVER_URL)
                            .post(formBody)
                            .build();
                    ​
                    Response response = client.newCall(request).execute();
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected Code: " + response);
                    } else {
                        Log.d(TAG, response.body().string());
                    }
                } catch (IOException e) {
                    Log.d(TAG, e.toString(), e.getCause());
                }
                return null;
            }
        }.execute();
    }
}
}
