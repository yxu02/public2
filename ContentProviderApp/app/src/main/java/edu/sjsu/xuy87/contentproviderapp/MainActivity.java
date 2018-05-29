package edu.sjsu.xuy87.contentproviderapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import edu.sjsu.xuy87.contentproviderapp.data.DBContract.NationLists;
import static edu.sjsu.xuy87.contentproviderapp.data.DBContract.NationLists.CONTENT_URI;

public class MainActivity extends AppCompatActivity {

    private EditText etCountry, etContinent, etWhereToUpdate, etNewContinent, etWhereToDelete, etQueryRowById;
    private static String TAG = MainActivity.class.getSimpleName();
    private SQLiteDatabase mSqLiteDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCountry = (EditText) findViewById(R.id.etCountry);
        etContinent = (EditText) findViewById(R.id.etContinent);
        etWhereToUpdate = (EditText) findViewById(R.id.etWhereToUpdate);
        etNewContinent = (EditText) findViewById(R.id.etUpdateContinent);
        etQueryRowById = (EditText) findViewById(R.id.etQueryByRowId);
        etWhereToDelete = (EditText) findViewById(R.id.etWhereToDelete);
    }

    public void insert(View view) {
        String countryName = etCountry.getText().toString();
        String continentName = etContinent.getText().toString();

        ContentValues contentValues = new ContentValues();
        contentValues.put(NationLists.COLUMN_CONTINENT, continentName);
        contentValues.put(NationLists.COLUMN_COUNTRY, countryName);
        Uri uri = getContentResolver().insert(CONTENT_URI, contentValues);
        Log.i(TAG, "data inserted into Uri: " + uri);
    }

    public void update(View view) {
        String whereCountry = etWhereToUpdate.getText().toString();
        String newContinent = etNewContinent.getText().toString();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NationLists.COLUMN_CONTINENT, newContinent);
        String selection = NationLists.COLUMN_COUNTRY + " = ?";
        String[] selectionArgs = {whereCountry};

        int rowUpdated = getContentResolver().update(CONTENT_URI, contentValues, selection, selectionArgs);
        Log.i(TAG, "# of rows updated: " + rowUpdated);
    }

    public void delete(View view) {
        String deleteCountry = etWhereToDelete.getText().toString();
        String selection = NationLists.COLUMN_COUNTRY + " =?";
        String[] selectionArgs = {deleteCountry};
        int rowDeleted = getContentResolver().delete(CONTENT_URI, selection, selectionArgs);
        Log.i(TAG, "# of rows deleted: " + rowDeleted);
    }

    public void queryById(View view) {
        final String queriedID = etQueryRowById.getText().toString();
        String[] columns = {NationLists._ID, NationLists.COLUMN_COUNTRY, NationLists.COLUMN_CONTINENT};
        String selection = NationLists._ID + "=?";
        String[] selectionArgs = {queriedID};
        Cursor cursor = getContentResolver().query(CONTENT_URI, columns, selection, selectionArgs,null);
        displayCursor(cursor);
    }

    public void queryDisplayAll(View view) {

        Intent intent = new Intent(this, NationListActivity.class);

        String[] columns = {NationLists._ID, NationLists.COLUMN_COUNTRY, NationLists.COLUMN_CONTINENT};

        Cursor cursor = getContentResolver().query(CONTENT_URI, columns, null, null, null);
/*        Cursor cursor = mSqLiteDatabase.query(NationLists.TABLE_NAME, columns, null,
                null, null, null, null);*/
        displayCursor(cursor);
        startActivity(intent);

    }

    private void displayCursor(Cursor cursor) {
        String str = "";
        String[] columns;
        if(cursor!=null){
            columns = cursor.getColumnNames();
            while(cursor.moveToNext()){
                for (String col: columns){
                    str += "\t" + cursor.getString(cursor.getColumnIndex(col));
                }
                str += "\n";
            }
        }
        cursor.close();
        Log.i (TAG, str);
    }

    @Override
    protected void onDestroy() {
        mSqLiteDatabase.close();
        super.onDestroy();
    }
}
