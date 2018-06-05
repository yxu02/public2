package edu.sjsu.xuy87.androidnoteapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter arrAdapter;
    private ListView lv;
    private boolean isLongClick = false;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()){
            case R.id.addNote:
                createNote();
                return true;
            default:
                return false;
        }
    }

    private void createNote() {
        int index;
        if(notes == null || notes.size() == 0){
            index = 0;
        } else{
            index = notes.size()+1;
        }
        Intent intent = new Intent(this, EditNoteActivity.class);
        intent.putExtra("index", index);
//        notes.add("");
//        arrAdapter.notifyDataSetChanged();
        startActivity(intent);
    }

    private void updateNote(int position) {
        int index;
        if(notes == null || notes.size() == 0){
            Toast.makeText(this, "Note update error!", Toast.LENGTH_SHORT).show();
            return;
        } else{
            index = position;
        }
        Intent intent = new Intent(this, EditNoteActivity.class);
        intent.putExtra("index", index);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notes.clear();
        SharedPreferences shdprf = getSharedPreferences("edu.sjsu.xuy87.androidnoteapp", MODE_PRIVATE);
        try {
            notes = (ArrayList<String>) ObjectSerializer.deserialize(shdprf.getString("notes", ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (IOException e) {
            e.printStackTrace();
        }

        lv = findViewById(R.id.lv_notes);
        arrAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, notes);
        lv.setAdapter(arrAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(!isLongClick) {
                    updateNote(position);
                }
                isLongClick = false;
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                isLongClick = true;
                final int index = position;
                new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_DeviceDefault_Dialog_Alert)
                        .setTitle("Delete this note?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(notes.size()>1) {
                            notes.remove(index);
                            arrAdapter.notifyDataSetChanged();
                            SharedPreferences shdprf = getSharedPreferences("edu.sjsu.xuy87.androidnoteapp", MODE_PRIVATE);
                            try {
                                shdprf.edit().putString("notes", ObjectSerializer.serialize(notes)).apply();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).setNegativeButton("No", null).show();

                return false;
            }
        });
    }

}
