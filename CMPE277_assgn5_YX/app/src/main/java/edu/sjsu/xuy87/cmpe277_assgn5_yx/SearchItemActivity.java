package edu.sjsu.xuy87.cmpe277_assgn5_yx;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import edu.sjsu.xuy87.cmpe277_assgn5_yx.data.DbHelper;

import static edu.sjsu.xuy87.cmpe277_assgn5_yx.data.DbHelper.*;

public class SearchItemActivity extends AppCompatActivity {

    private EditText mEt_qryByID;
    private EditText mEt_qryByName;

    private DbHelper mDbHelper;
    private SQLiteDatabase mDb;
    private TextView mTv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item);
        mEt_qryByID = findViewById(R.id.qryItemID);
        mEt_qryByName = findViewById(R.id.qryItemName);
        mTv_result = findViewById(R.id.tv_result);

        mDbHelper = new DbHelper(this);
        mDb = mDbHelper.getReadableDatabase();
    }

    public void queryByID(View view) {
        String rowID = mEt_qryByID.getText().toString();
        queryHelper(1, rowID);
    }

    public void queryByProdName(View view) {
        String prodName = mEt_qryByName.getText().toString();
        queryHelper(2, prodName);
    }

    public void queryAll(View view) {
        queryHelper(3, null);
    }

    private void queryHelper(int taskID, String queryItem) {

        String[] projection = {_ID, COLUMN_ITEMNAME, COLUMN_ITEMDESC, COLUMN_ITEMPRICE, COLUMN_ITEMREVIEW};
        String selection ="";
        String[] selectionArgs = null;

        switch (taskID){
            case 1:
                selection = _ID + " = ? ";
                selectionArgs = new String[]{queryItem};
                break;
            case 2:
                selection = COLUMN_ITEMNAME + " = ? ";
                selectionArgs = new String[]{queryItem};
                break;
            case 3:
                selection = null;
                selectionArgs = null;
                break;
        }

        String sortOrder = null;

        Cursor cursor = mDb.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);

        if (cursor != null && cursor.moveToNext()) {
            cursor.moveToPrevious();
            StringBuilder str = new StringBuilder();
            str.append(_ID + ",\t" + COLUMN_ITEMNAME + ",\t" + COLUMN_ITEMNAME + ",\t" + COLUMN_ITEMDESC + ",\t" + COLUMN_ITEMPRICE + ",\t" + COLUMN_ITEMREVIEW + "\n");
            while (cursor.moveToNext()) {
                String[] columns = cursor.getColumnNames();
                for (String column : columns) {
                    str.append(cursor.getString(cursor.getColumnIndex(column))).append(",\t");
                }
                str.append("\n");
            }
            cursor.close();
            mTv_result.setText(str.toString());
        } else {
            mTv_result.setText("Error or product item not found!");
        }
    }
    @Override
    protected void onDestroy() {
        mDb.close();
        super.onDestroy();
    }
}
