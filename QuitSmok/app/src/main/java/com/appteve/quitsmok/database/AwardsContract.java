package com.appteve.quitsmok.database;

import android.provider.BaseColumns;

/**
 * Created by appteve on 07/02/2017.
 */

public class AwardsContract {

    public static final String DB_NAME = "com.appteve.quitsmok.db";
    public static final int DB_VERSION = 1;

    public class AwardsEntry implements BaseColumns{

        public static final String TABLE = "awards";
        public static final String COL_TASK_TITLE = "title";
        public static final String COL_TASK_PRICE = "price";
        public static final String COL_TASK_DATE = "date";



    }
}
