package edu.sjsu.xuy87.contentproviderapp;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import edu.sjsu.xuy87.contentproviderapp.data.DBContract.NationLists;


public class NationListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

	private SimpleCursorAdapter simpleCursorAdapter;
	private Cursor mCursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nations);

		String[] from = {NationLists.COLUMN_COUNTRY, NationLists.COLUMN_CONTINENT};
		int[] to = {R.id.txvCountryName, R.id.txvContinentName};

		getLoaderManager().initLoader(10, null, this);
		simpleCursorAdapter = new SimpleCursorAdapter(this, R.layout.nation_list_item, null, from, to, 0);

		ListView listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(simpleCursorAdapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mCursor = (Cursor) parent.getItemAtPosition(position);
				Long _id = mCursor.getLong(mCursor.getColumnIndex(NationLists._ID));
				String country = mCursor.getString(mCursor.getColumnIndex(NationLists.COLUMN_COUNTRY));
				String continent = mCursor.getString(mCursor.getColumnIndex(NationLists.COLUMN_CONTINENT));

				Intent intent = new Intent(NationListActivity.this, NationEditActivity.class);
				intent.putExtra("id", _id);
				intent.putExtra("country", country);
				intent.putExtra("continent", continent);
				startActivity(intent);
			}
		});
		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabInsertData);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(NationListActivity.this, NationEditActivity.class);
				intent.putExtra("id", 0l);
				startActivity(intent);
			}
		});
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = {NationLists._ID, NationLists.COLUMN_COUNTRY, NationLists.COLUMN_CONTINENT};
		return new CursorLoader(this, NationLists.CONTENT_URI, projection, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		simpleCursorAdapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		simpleCursorAdapter.swapCursor(null);
	}
}
