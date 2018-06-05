package edu.sjsu.xuy87.androidnoteapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class EditNoteActivity extends AppCompatActivity {

    private EditText editText;
    int index;
    private SharedPreferences shdprf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        editText = findViewById(R.id.editText);

        Intent intent = getIntent();
        index = intent.getIntExtra("index",-1);
        if (MainActivity.notes == null || MainActivity.notes.size() == 0 || index - MainActivity.notes.size() == 1){
        } else {
            editText.setText(MainActivity.notes.get(index));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (MainActivity.notes == null || MainActivity.notes.size() == 0 || index - MainActivity.notes.size() == 1){
            MainActivity.notes.add(editText.getText().toString());
        } else {
            MainActivity.notes.set(index, editText.getText().toString());
            Log.i("note text is: ", MainActivity.notes.get(index));
        }
        shdprf = getSharedPreferences("edu.sjsu.xuy87.androidnoteapp", MODE_PRIVATE);
        try {
            shdprf.edit().putString("notes", ObjectSerializer.serialize(MainActivity.notes)).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("index is: ", String.valueOf(index));
        MainActivity.arrAdapter.notifyDataSetChanged();
    }
}
