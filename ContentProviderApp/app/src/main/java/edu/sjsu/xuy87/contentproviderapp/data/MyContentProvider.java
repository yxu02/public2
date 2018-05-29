package edu.sjsu.xuy87.contentproviderapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import edu.sjsu.xuy87.contentproviderapp.data.DBManager;

import static android.content.ContentValues.TAG;
import static edu.sjsu.xuy87.contentproviderapp.data.DBContract.NationLists.CONTENT_AUTHORITY;
import static edu.sjsu.xuy87.contentproviderapp.data.DBContract.NationLists.PATH_NAME;
import static edu.sjsu.xuy87.contentproviderapp.data.DBContract.NationLists.TABLE_NAME;

public class MyContentProvider extends ContentProvider {
    private DBManager mDbManager;

    public static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        mUriMatcher.addURI(CONTENT_AUTHORITY, PATH_NAME, 1);
//        mUriMatcher.addURI(CONTENT_AUTHORITY, PATH_NAME + "/#", 2);
//        mUriMatcher.addURI(CONTENT_AUTHORITY, PATH_NAME + "/*", 3);
    }

    private SQLiteDatabase mSqLiteDatabase;

    @Override
    public boolean onCreate() {
        mDbManager = new DBManager(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        mSqLiteDatabase = mDbManager.getReadableDatabase();
        switch (mUriMatcher.match(uri)){
            case 1: {
                cursor = mSqLiteDatabase.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            default: throw new IllegalArgumentException(TAG + ": Query error occurred with URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        mSqLiteDatabase = mDbManager.getWritableDatabase();

        switch (mUriMatcher.match(uri)){
            case 1: {
                long rowID = mSqLiteDatabase.insert(TABLE_NAME, null,values);
                if(rowID == -1){
                    Log.e(TAG, "Insert error for URI" + uri);
                    return null;
                } else {
                    getContext().getContentResolver().notifyChange(uri,null);
                    return Uri.withAppendedPath(uri, String.valueOf(rowID));
                }
            }
            default: throw new IllegalArgumentException(TAG + ": Insert unknown URI " + uri);
        }
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        mSqLiteDatabase = mDbManager.getWritableDatabase();
        if (mUriMatcher.match(uri)==1){
            getContext().getContentResolver().notifyChange(uri,null);
            return mSqLiteDatabase.delete(TABLE_NAME,selection,selectionArgs);
        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        mSqLiteDatabase = mDbManager.getWritableDatabase();
        if (mUriMatcher.match(uri)==1){
            getContext().getContentResolver().notifyChange(uri,null);
            return mSqLiteDatabase.update(TABLE_NAME,values,selection,selectionArgs);
        }
        return 0;
    }
}
