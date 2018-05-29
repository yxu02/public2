package edu.sjsu.xuy87.myservices.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import edu.sjsu.xuy87.myservices.Services.MyIntentService;
import edu.sjsu.xuy87.myservices.Services.MyStartService;
import edu.sjsu.xuy87.myservices.R;

public class MainActivity extends AppCompatActivity {
    final static String TAG = MainActivity.class.getSimpleName();
    private Intent mIntent;
    TextView mTextView1, mTextView2;
    ResultReceiver mResultReceiver = new Result_Receiver(null);
    Handler mHandler = new Handler();

    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int ctr = intent.getIntExtra("result", 0);
            Toast.makeText(context, "Counter stopped at " + ctr, Toast.LENGTH_SHORT).show();
            mTextView1.setText("Counter stopped at " + ctr);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("mybroadcast.action");
        registerReceiver(mBroadcastReceiver,intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView1 = findViewById(R.id.textView1);
        mTextView2 = findViewById(R.id.textView2);
    }

    public void createService(View view) {
        mIntent = new Intent(this, MyStartService.class);
        mIntent.putExtra("sleepTime", 10);
        startService(mIntent);
    }

    public void stopService(View view) {
        stopService(mIntent);
    }

    public void startIntentService(View view) {
        mIntent = new Intent(this, MyIntentService.class);
        mIntent.putExtra("sleepTime", 10);
        mIntent.putExtra("resultReceiver", mResultReceiver);
        startService(mIntent);
    }

    public void startSecondService(View view) {
        Intent intent = new Intent(this, BoundServiceActivity.class);
        startActivity(intent);
    }

    public void startMessengerService(View view) {
        Intent intent = new Intent(this, MessengerActivity.class);
        startActivity(intent);
    }

    public class Result_Receiver extends ResultReceiver{

        public Result_Receiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if(resultCode==20 && resultData!=null){
                Log.i(TAG, "Current thread name is "+ Thread.currentThread().getName());
                final String result = resultData.getString("result");
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "Current thread name is "+ Thread.currentThread().getName());
                        mTextView2.setText(result);
                    }
                });
            }
        }
    }
}
