package edu.sjsu.xuy87.contentproviderapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static edu.sjsu.xuy87.contentproviderapp.data.DBContract.NationLists;

public class DBManager extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "nations.db";
    private static final int DATABASE_VERSION = 1;

    //Database table schema
    private final String SQL_CREATE_COUNTRY_TABLE
            = "CREATE TABLE " + NationLists.TABLE_NAME
            + " (" + NationLists._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + NationLists.COLUMN_COUNTRY + " TEXT NOT NULL, "
            + NationLists.COLUMN_CONTINENT + " TEXT"
            + ");";

    public DBManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_COUNTRY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + NationLists.TABLE_NAME);
        onCreate(db);
    }
}
