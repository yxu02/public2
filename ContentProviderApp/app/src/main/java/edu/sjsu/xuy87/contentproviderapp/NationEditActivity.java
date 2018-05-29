package edu.sjsu.xuy87.contentproviderapp;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import edu.sjsu.xuy87.contentproviderapp.data.DBContract;
import edu.sjsu.xuy87.contentproviderapp.data.DBContract.NationLists;

public class NationEditActivity extends AppCompatActivity {

	private static final String TAG = NationEditActivity.class.getSimpleName();

	private EditText etCountry, etContinent;
	private Button btnUpdate, btnDelete, btnInsert;

	long _id;
	String country, continent;
	private String mWhere;
	private String[] mSelectionArgs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nations_edit);

		etCountry 	= (EditText) findViewById(R.id.etCountry);
		etContinent = (EditText) findViewById(R.id.etContinent);

		btnUpdate = (Button) findViewById(R.id.btnUpdate);
		btnDelete = (Button) findViewById(R.id.btnDelete);
		btnInsert = (Button) findViewById(R.id.btnInsert);

		Intent intent = getIntent();
		_id = intent.getLongExtra("id", 0);
		country = intent.getStringExtra("country");
		continent = intent.getStringExtra("continent");

		if (_id != 0) {		// We want to delete or update data
			etCountry.setText(country);
			etContinent.setText(continent);
			btnInsert.setVisibility(View.GONE);
		} else {			// We want to insert data
			btnUpdate.setVisibility(View.GONE);
			btnDelete.setVisibility(View.GONE);
		}
	}

	public void update(View view) {
//		String[] selectionArgs = {etCountry.getText().toString(), etContinent.getText().toString()};
		ContentValues contentValues = new ContentValues();
		contentValues.put(NationLists.COLUMN_COUNTRY, etCountry.getText().toString());
		contentValues.put(NationLists.COLUMN_CONTINENT, etContinent.getText().toString());
		mWhere = NationLists._ID + " = ?";
		mSelectionArgs = new String[]{String.valueOf(_id)};
		getContentResolver().update(NationLists.CONTENT_URI, contentValues, mWhere, mSelectionArgs);
		finish();
	}

	public void delete(View view) {
		mWhere = NationLists._ID + " = ?";
		mSelectionArgs = new String[]{String.valueOf(_id)};
		getContentResolver().delete(NationLists.CONTENT_URI, mWhere, mSelectionArgs);
		finish();
	}

	public void insert(View view) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(NationLists.COLUMN_COUNTRY, etCountry.getText().toString());
		contentValues.put(NationLists.COLUMN_CONTINENT, etContinent.getText().toString());
		getContentResolver().insert(NationLists.CONTENT_URI, contentValues);
		finish();
	}
}
