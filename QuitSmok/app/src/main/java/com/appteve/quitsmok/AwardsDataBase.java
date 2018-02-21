package com.appteve.quitsmok;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

/**
 * Created by appteve on 06/02/2017.
 */

public class AwardsDataBase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Awards";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_DATA = "UserAwards";
    private static final String KEY_ID ="_id";
    private static final String KEY_DATE = "date";
    private static final String KEY_TITLE = "title";
    private static final String KEY_COST = "cost";

    public AwardsDataBase(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MY_TABLE = "CREATE TABLE " + TABLE_DATA + "(" + KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_DATE + " DATE," + KEY_TITLE + " TEXT,"  + KEY_COST + " INTEGER" + ")";
        db.execSQL(CREATE_MY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop table if already created
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_DATA);
        //create table again
        onCreate(db);
    }

    public void addData(String title, int cost){


        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(KEY_TITLE,title);
        values.put(KEY_COST,cost);
        values.put(KEY_DATE,getDateTime());
        //Insert all the values in column
        db.insert(TABLE_DATA,null,values);
        db.close();
    }


        public String[] getAppCategorydetail() {
            String Table_Name=TABLE_DATA;

            String selectQuery = "SELECT  * FROM " + Table_Name;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            String[] data = null;
            if (cursor.moveToFirst()) {
                do {
                    // get  the  data into array,or class variable
                } while (cursor.moveToNext());
            }
            db.close();
            return data;
        }



    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }







}
