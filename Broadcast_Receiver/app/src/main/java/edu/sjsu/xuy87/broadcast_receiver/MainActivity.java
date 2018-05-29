package edu.sjsu.xuy87.broadcast_receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button mButton;
    private LocalBroadcastManager mLocalBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = findViewById(R.id.button1);
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    public void sendLocalBroadcast(View view) {
        Intent firstIntent = new Intent("mycustom.intent");
        firstIntent.putExtra("a", 10);
        firstIntent.putExtra("b", 20);
        sendBroadcast(firstIntent);
    }
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int sum = intent.getIntExtra("sum", -1);
            Toast.makeText(context, "Call back received! " + sum, Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onResume() {
        IntentFilter intentFilter = new IntentFilter("mycustom.local.intent");
        mLocalBroadcastManager.registerReceiver(mBroadcastReceiver, intentFilter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        mLocalBroadcastManager.unregisterReceiver(mBroadcastReceiver);
        super.onPause();
    }
}
