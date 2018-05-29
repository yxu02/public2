package edu.sjsu.xuy87.cmpe277_assgn5_yx;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import edu.sjsu.xuy87.cmpe277_assgn5_yx.data.DbHelper;
import static edu.sjsu.xuy87.cmpe277_assgn5_yx.data.DbHelper.*;

public class AddItemActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;
    private EditText mEt_itemName;
    private EditText mEt_itemDesc;
    private EditText mEt_itemPrice;
    private EditText mEt_itemReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        mEt_itemName = findViewById(R.id.itemName);
        mEt_itemDesc = findViewById(R.id.itemDesc);
        mEt_itemPrice = findViewById(R.id.itemPrice);
        mEt_itemReview = findViewById(R.id.itemReview);
        mDbHelper = new DbHelper(this);
        mDb = mDbHelper.getWritableDatabase();
    }

    public void addButton(View view) {
        String itemName = mEt_itemName.getText().toString();
        String itemDesc = mEt_itemDesc.getText().toString();
        String itemPrice = mEt_itemPrice.getText().toString();
        String itemReview = mEt_itemReview.getText().toString();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ITEMNAME, itemName);
        contentValues.put(COLUMN_ITEMDESC, itemDesc);
        contentValues.put(COLUMN_ITEMPRICE, itemPrice);
        contentValues.put(COLUMN_ITEMREVIEW, itemReview);

        long rowId = mDb.insert(TABLE_NAME, null, contentValues);
        Log.i(TAG, "Items inserted in table with row id: " + rowId);
        Toast.makeText(this, "Product info saved in database!", Toast.LENGTH_SHORT).show();
    }

    public void cancelButton(View view) {
        finish();
    }
    @Override
    protected void onDestroy() {
        mDb.close();
        super.onDestroy();
    }
}
