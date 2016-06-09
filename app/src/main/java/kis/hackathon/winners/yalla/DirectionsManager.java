package kis.hackathon.winners.yalla;
import android.graphics.Point;
import android.os.AsyncTask;
import android.util.Log;
import java.io.IOException;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.google.gson.JsonObject;

/**
 *
 * This class is used to find the time it takes to get from origin to destination
 */
public class DirectionsManager {
    private static final String TAG = DirectionsManager.class.getSimpleName();
    public static final String SERVER_URL = "https://maps.googleapis.com/maps/api/directions/json?origin=";
    private static final String GOOGLE_KEY = "AIzaSyBJK_IDdOZOegnfCPCBn4d016TpVWrBz44";
    public void sendRequest(final Place origin, final Place dest, final TimeReady caller) {
        new AsyncTask<Void, Void, Integer>() {
            OkHttpClient client = new OkHttpClient();
            @Override
            protected Integer doInBackground(Void... params) {
                try {
                    RequestBody formBody = new FormBody.Builder().build();
                    String requestUrl = getFullUrl(origin, dest);
                    Request request = new Request.Builder().url(requestUrl).post(formBody).build();
                    Response response = client.newCall(request).execute();

                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected Code: " + response);
                    } else {
                        String retVal = response.body().string();
                        Log.d(TAG, response.body().string());
                    }
                } catch (IOException e) {
                    Log.d(TAG, e.toString(), e.getCause());
                }
                return 1;
            }

            @Override
            protected void onPostExecute(Integer integer) {
                caller.onTimeReady(integer.intValue());
            }
        }.execute();
    }

    private String getFullUrl(Place origin, Place dest) {
        return SERVER_URL + origin.getX() + ", " + origin.getY() + "&destination=" + dest.getX() + ", " + dest.getY() + "&key=" + GOOGLE_KEY;
    }
}

