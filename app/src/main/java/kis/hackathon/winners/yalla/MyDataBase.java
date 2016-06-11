package kis.hackathon.winners.yalla;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.maps.model.LatLng;

/**
 * Created by odelya_krief on 09-Jun-16.
 */
public class MyDataBase extends SQLiteOpenHelper {
    private static final String TAG ="myDataBase" ;

    private static final String TABLE_NAME = "main_table" ;
    private static final String DB_NAME = "MyDB";

    private static final String MA_KEY = "primary_key";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_ADDRESS ="address" ;
    private static final String KEY_TIME = "minutes";
    private static final String KEY_MSG = "msg";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LNG = "lng";

    private static final int DB_VERSION = 1;

    MyDataBase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MAIN_TABLE="CREATE TABLE IF NOT EXISTS "+ TABLE_NAME+ "(" +
                MA_KEY+" INTEGER PRIMARY KEY,"+
                KEY_PHONE + " INTEGER," +
                KEY_ADDRESS + " TEXT,"+
                KEY_TIME +" INTEGER," +
                KEY_MSG + " TEXT,"+
                KEY_LAT + " DOUBLE,"+
                KEY_LNG + " DOUBLE"+
                ")";
        Log.d(TAG, CREATE_MAIN_TABLE);
        db.execSQL(CREATE_MAIN_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE "+ TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    void insert(TableItem tableItem)
    {
        ContentValues values = new ContentValues();
        values.put(KEY_PHONE, tableItem.phone);
        values.put(KEY_ADDRESS, tableItem.address);
        values.put(KEY_TIME, tableItem.minutes);
        values.put(KEY_MSG, tableItem.msg);
        values.put(KEY_LAT, tableItem.addressLatLng.lat);
        values.put(KEY_LNG, tableItem.addressLatLng.lng);

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME,null,values);
        db.close();
    }
    void update(TableItem tableItem)
    {
        ContentValues values = new ContentValues();
        values.put(KEY_PHONE, tableItem.phone);
        values.put(KEY_ADDRESS, tableItem.address);
        values.put(KEY_TIME, tableItem.minutes);
        values.put(KEY_MSG, tableItem.msg);
        values.put(KEY_LAT, tableItem.addressLatLng.lat);
        values.put(KEY_LNG, tableItem.addressLatLng.lng);
        SQLiteDatabase db = getWritableDatabase();
        db.update(TABLE_NAME, values, KEY_PHONE + "=?", new String[]{tableItem.phone});
        db.close();
    }
//    public void delete(TableItem tableItem)
//    {
//        //TODO in the future if we  ever want to delete
//    }
    @Nullable
    TableItem getItem(String phone)
    {
        String sql="select * from "+TABLE_NAME+" where "+ KEY_PHONE +"="+phone;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor=db.query(TABLE_NAME,new String[]{
                        KEY_PHONE,
                        KEY_ADDRESS,
                        KEY_TIME,
                        KEY_MSG,
                        KEY_LAT,
                        KEY_LNG}
                , KEY_PHONE +"=?",new String[]{phone},null,null,null,null);
        if (cursor!=null)
        {
            if (!cursor.moveToFirst()) return null;
            TableItem tableItem=new TableItem(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getString(3),
                    new LatLng(
                            cursor.getDouble(4),
                            cursor.getDouble(5)
                    )
            );
            cursor.close();
            db.close();
            return tableItem;
        }
        db.close();
        return  null;

    }

}
