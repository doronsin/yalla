package kis.hackathon.winners.yalla;

import com.google.maps.model.LatLng;

/**
 * Created by odelya_krief on 09-Jun-16.
 */
public class TableItem {
    public String phone;
    public String address;
    public LatLng addressLatLng;
    public int minutes;

    public TableItem(String p_phone, String p_address, int p_time, double lat, double lng) {
        phone=p_phone;
        address=p_address;
        minutes =p_time;
        addressLatLng = new LatLng(lat, lng);
    }
}
