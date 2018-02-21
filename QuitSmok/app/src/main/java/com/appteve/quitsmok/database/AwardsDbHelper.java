package com.appteve.quitsmok.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by appteve on 07/02/2017.
 */

public class AwardsDbHelper extends SQLiteOpenHelper {

    public AwardsDbHelper(Context context){

        super(context, AwardsContract.DB_NAME,null,AwardsContract.DB_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase db){

        String createTable = "CREATE TABLE " + AwardsContract.AwardsEntry.TABLE + " ( " + AwardsContract.AwardsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + AwardsContract.AwardsEntry.COL_TASK_PRICE + " INTEGER, " + AwardsContract.AwardsEntry.COL_TASK_DATE + " TEXT, " + AwardsContract.AwardsEntry.COL_TASK_TITLE + " TEXT NOT NULL);";
        db.execSQL(createTable);
    }

    @Override
    public  void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

        db.execSQL("DROP TABLE IF EXISTS " + AwardsContract.AwardsEntry.TABLE);
        onCreate(db);

    }
}
