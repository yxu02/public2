package edu.sjsu.xuy87.cmpe277_assgn5_yx.data;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "products.db";
    public static final String  TABLE_NAME = "Products_Table";
    private static final int DATABASE_VERSION = 1;
    public static final String _ID = "_id";
    public static final String COLUMN_ITEMNAME = "item_name";
    public static final String COLUMN_ITEMDESC = "item_description";
    public static final String COLUMN_ITEMPRICE = "item_price";
    public static final String COLUMN_ITEMREVIEW = "item_review";

    private final String SQL_CREATE_PRODUCTS_TABLE
            = "CREATE TABLE " + TABLE_NAME
            + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_ITEMNAME + " TEXT NOT NULL, "
            + COLUMN_ITEMDESC + " TEXT, "
            + COLUMN_ITEMPRICE + " TEST, "
            + COLUMN_ITEMREVIEW + " TEXT"
            + ");";

    /*
		TABLE NAME: Products_Table	Database Name: products.db

		 _id	item_name	item_description    item_price  Item_review
 		  1		 iPhone     Apple's iPhone      $800.00     Stylish
    */

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
