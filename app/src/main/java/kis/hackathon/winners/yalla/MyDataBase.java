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
    private static final String MA_KEY_TIME = "time";

    public MyDataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MAIN_TABLE="CREATE TABLE NOT EXIST "+ TABLE_NAME+ "(" +
                MA_KEY+"INTEGER PRIMARY KEY "+
                MA_KEY_PHONE + " INTEGER," +
                MA_KEY_ADDRESS + " TEXT,"+
                MA_KEY_TIME+" INTEGER)";
        db.execSQL(CREATE_MAIN_TABLE);
        Log.d(TAG, CREATE_MAIN_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }
    public void insert(TableItem tableItem,SQLiteDatabase db)
    {
        ContentValues values = new ContentValues();
        values.put(MA_KEY_PHONE,tableItem.phone);
        values.put(MA_KEY_ADDRESS,tableItem.address);
        values.put(MA_KEY_TIME,tableItem.time);
        db.insert(TABLE_NAME,null,values);
    }
    public void update(TableItem tableItem,SQLiteDatabase db)
    {
        ContentValues values = new ContentValues();
        values.put(MA_KEY_PHONE,tableItem.phone);
        values.put(MA_KEY_ADDRESS,tableItem.address);
        values.put(MA_KEY_TIME,tableItem.time);
        db.update(TABLE_NAME, values, String.format(MA_KEY_PHONE + "=?"), new String[]{tableItem.phone});
    }
    public void delete(TableItem tableItem,SQLiteDatabase db)
    {
        //todo in the future if we  ever want to delete
    }
    public TableItem getItem(String phone,SQLiteDatabase db)
    {
        String sql="select * from "+TABLE_NAME+" where "+MA_KEY_PHONE+"="+phone;
        Cursor result= db.rawQuery(sql, null);//not sure if it's ok
        Cursor cursor=db.query(TABLE_NAME,new String[]{
                MA_KEY_PHONE,MA_KEY_ADDRESS,MA_KEY_TIME},MA_KEY_PHONE+"=?",new String[]{phone},null,null,null,null);
        if (cursor!=null)
        {
            cursor.moveToFirst();
            TableItem tableItem=new TableItem(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getInt(2));
            cursor.close();
            return tableItem;
        }
        return null;

    }

}
