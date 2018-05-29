package edu.sjsu.xuy87.cmpe277_assgn6_startedserv;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class DownloadActivity extends AppCompatActivity {

    private Intent mIntent;

    boolean isBound = false;
    private DownloadByBoundService myBoundService;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {

            DownloadByBoundService.MyLocalBinder myLocalBinder = (DownloadByBoundService.MyLocalBinder) iBinder;
            myBoundService = myLocalBinder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(this, DownloadByBoundService.class);
        bindService(intent, mConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (isBound) {
            unbindService(mConnection);
            isBound = false;
        }
    }

    public void startDownloadByStartedServ(View view) {

        mIntent = new Intent(this, DownloadByStartedService.class);
        startService(mIntent);
    }

    @Override
    protected void onDestroy() {
        stopService(mIntent);
        super.onDestroy();
    }

    public void startDownloadByBoundServ(View view) {
        if(isBound){
            myBoundService.startDownload();
        }
    }
}
