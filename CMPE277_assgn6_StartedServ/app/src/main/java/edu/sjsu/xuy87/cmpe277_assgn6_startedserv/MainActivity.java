package edu.sjsu.xuy87.cmpe277_assgn6_startedserv;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startDownloadByStartedServ(View view) {
        Intent intent = new Intent(this, DownloadActivity.class);
        startActivity(intent);
    }

    public void closeApp(View view) {
        finish();
    }

    public void startDownloadByBoundServ(View view) {
    }
}
