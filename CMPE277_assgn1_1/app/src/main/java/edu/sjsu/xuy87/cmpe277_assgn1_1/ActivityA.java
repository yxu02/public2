package edu.sjsu.xuy87.cmpe277_assgn1_1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class ActivityA extends AppCompatActivity implements View.OnClickListener {
    public static final int REQUEST_CODEA = 100;
    public static final String ACTIVITY_A_COUNTER = "activityA_counter";
    private static final int REQUEST_CODE_DIALOG = 1000;
    Button mActBButton;
    Button mDialogButton;
    Button mCloseApp;
    public static int countA = 0;
    TextView mCounterText;
    int mCounterA = 0;
    int mCounterB = 0;
    int mCounterC = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mActBButton = (Button) findViewById(R.id.startActivityB);
        mDialogButton = (Button) findViewById(R.id.dialogButton);
        mCloseApp = (Button) findViewById(R.id.closeApp);
        mCounterText = (TextView) findViewById(R.id.counterText);

        mActBButton.setOnClickListener(this);
        mDialogButton.setOnClickListener(this);
        mCloseApp.setOnClickListener(this);

        mCounterA = countA;
    }

    @Override
    public void onClick(View v) {
        if (v == mActBButton) {
            Intent intent = new Intent(ActivityA.this, ActivityB.class);
            intent.putExtra(ACTIVITY_A_COUNTER, mCounterA);
            startActivityForResult(intent, REQUEST_CODEA);
        }
        if (v == mDialogButton){
            Intent intent = new Intent(ActivityA.this, ActivityC.class);
            intent.putExtra(ACTIVITY_A_COUNTER, mCounterA);
            startActivityForResult(intent, REQUEST_CODE_DIALOG);
        }
        if (v==mCloseApp)
            finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODEA && resultCode == Activity.RESULT_OK) {
            mCounterA = data.getIntExtra(ACTIVITY_A_COUNTER, -1);
            mCounterB = data.getIntExtra(ActivityB.ACTIVITY_B_COUNTER, -1);
            return;
        } else if (requestCode == REQUEST_CODE_DIALOG && resultCode == Activity.RESULT_OK) {
            mCounterA = data.getIntExtra(ACTIVITY_A_COUNTER, -1);
            mCounterC = data.getIntExtra(ActivityC.ACTIVITY_C_COUNTER, -1);
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCounterText.setText("Counters for A/B/Dial: " + Integer.toString(mCounterA) + " / " + Integer.toString(mCounterB) + " / " + Integer.toString(mCounterC));
    }

    @Override
    public void onPause() {
        countA = ++mCounterA;
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
