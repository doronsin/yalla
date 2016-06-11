package kis.hackathon.winners.yalla;

import com.google.maps.model.LatLng;

import lombok.AllArgsConstructor;

/**
 * Created by odelya_krief on 09-Jun-16.
 *
 * used to get out objects from the db
 */
@AllArgsConstructor
class TableItem {
    public String phone;
    public String address;
    public int minutes;
    public String msg;
    public LatLng addressLatLng;
}
