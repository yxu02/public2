package edu.sjsu.xuy87.studypal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;

public class NoteActivity extends AppCompatActivity {

    public static final int POSITION_NOT_SET = -1;
    public static ArrayList<String> courses = new ArrayList<>(Arrays.asList(
            "CMPE272: Enterprise Software Platforms Syllabus",
            "CMPE277: Mobile Application Development",
            "CMPE281: Cloud Technologies",
            "CMPE282: Cloud Services",
            "CMPE283: Virtualization Technologies"));
    public static final String NOTE_POSITION = "edu.sjsu.xuy87.studypal.NOTE_POSITION";
    private int mPosition;
    private Spinner spinnerCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spinnerCourse = (Spinner) findViewById(R.id.spinner_courses);

        ArrayAdapter<String> adapterCourses = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courses);
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCourse.setAdapter(adapterCourses);
        readDisplayStateValues();
        if(mPosition!= POSITION_NOT_SET) {
            displayNote(spinnerCourse, mPosition);
        } else {
            displayNote(spinnerCourse, 4);
        }
    }

    private void displayNote(Spinner spinnerCourse, int position) {
        spinnerCourse.setSelection(position);
    }

    private void readDisplayStateValues() {
        Intent intent = getIntent();
        mPosition = intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send_email) {
            sendEmail();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendEmail() {
        String subject = "StudyPal New Note: " + (String) spinnerCourse.getSelectedItem();
        String text = "You just created a note at StudyPal about the course \"" + subject + "\".\n";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc2822");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(intent);
    }
}
