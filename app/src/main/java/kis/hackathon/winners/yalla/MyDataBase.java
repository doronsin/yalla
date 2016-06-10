package kis.hackathon.winners.yalla;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by odelya_krief on 09-Jun-16.
 */
public class MyDataBase extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "main_table" ;
    private static final String MA_KEY_PHONE = "phone";
    private static final String MA_KEY_ADDRESS ="address" ;

    private static final String TAG ="myDataBase" ;
    private static final String MA_KEY = "primary_key";
    private static final String MA_KEY_TIME = "minutes";
    private static final String MA_KEY_LAT = "lat";
    private static final String MA_KEY_LNG = "lng";

    private static final int DB_VERSION = 2;
    private static final String DB_NAME = "MyDB";

    public MyDataBase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MAIN_TABLE="CREATE TABLE IF NOT EXISTS "+ TABLE_NAME+ "(" +
                MA_KEY+" INTEGER PRIMARY KEY,"+
                MA_KEY_PHONE + " INTEGER," +
                MA_KEY_ADDRESS + " TEXT,"+
                MA_KEY_TIME+" INTEGER," +
                MA_KEY_LAT + " DOUBLE,"+
                MA_KEY_LNG + " DOUBLE"+
                ")";
        Log.d(TAG, CREATE_MAIN_TABLE);
        db.execSQL(CREATE_MAIN_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE "+ TABLE_NAME);
        onCreate(db);


    }
    public void insert(TableItem tableItem)
    {
        ContentValues values = new ContentValues();
        values.put(MA_KEY_PHONE, tableItem.phone);
        values.put(MA_KEY_ADDRESS, tableItem.address);
        values.put(MA_KEY_TIME, tableItem.minutes);
        values.put(MA_KEY_LAT, tableItem.addressLatLng.lat);
        values.put(MA_KEY_LNG, tableItem.addressLatLng.lng);
        this.getWritableDatabase().insert(TABLE_NAME,null,values);
    }
    public void update(TableItem tableItem)
    {
        ContentValues values = new ContentValues();
        values.put(MA_KEY_PHONE, tableItem.phone);
        values.put(MA_KEY_ADDRESS, tableItem.address);
        values.put(MA_KEY_TIME, tableItem.minutes);
        values.put(MA_KEY_LAT, tableItem.addressLatLng.lat);
        values.put(MA_KEY_LNG, tableItem.addressLatLng.lng);
        this.getWritableDatabase().update(TABLE_NAME, values, String.format(MA_KEY_PHONE + "=?"), new String[]{tableItem.phone});
    }
    public void delete(TableItem tableItem)
    {
        //TODO in the future if we  ever want to delete
    }
    public TableItem getItem(String phone)
    {
        String sql="select * from "+TABLE_NAME+" where "+MA_KEY_PHONE+"="+phone;
//        Cursor result= this.getWritableDatabase().rawQuery(sql, null);//not sure if it's ok
        Cursor cursor=this.getWritableDatabase().query(TABLE_NAME,new String[]{
                    MA_KEY_PHONE,
                    MA_KEY_ADDRESS,
                    MA_KEY_TIME,
                    MA_KEY_LAT,
                    MA_KEY_LNG}
                ,MA_KEY_PHONE+"=?",new String[]{phone},null,null,null,null);
        if (cursor!=null)
        {
            if (!cursor.moveToFirst()) return null;
            TableItem tableItem=new TableItem(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getDouble(3),
                    cursor.getDouble(4)
                    );
            cursor.close();
            return tableItem;
        }
        return  null;

    }

}
