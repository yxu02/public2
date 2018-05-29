package edu.sjsu.xuy87.myservices.Services;

import android.app.IntentService;
import android.content.Intent;
import android.os.*;
import android.util.Log;
import android.widget.Toast;

public class MyIntentService extends IntentService {
    static final String TAG = MyIntentService.class.getSimpleName();
    Handler mHandler = new Handler();

    public MyIntentService() {
        super("Worker Thread");
    }


/*    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service on start. Name is " + Thread.currentThread().getName());
        return START_STICKY;
    }*/

    @Override
    public void onCreate() {
        Log.i(TAG, "Service on create. Name is " + Thread.currentThread().getName());
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "Service on bind. Name is " + Thread.currentThread().getName());
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int sleepTime = intent.getIntExtra("sleepTime", 0);
        ResultReceiver resultReceiver = intent.getParcelableExtra("resultReceiver");
        Log.i(TAG, "Service on handle. Name is " + Thread.currentThread().getName());
        int ctr =1;
        while (ctr <= sleepTime){
            try {
                Thread.sleep(1000);
                Log.i(TAG, "Counter is " + ctr + ". Name is "+ Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ctr++;
        }
        final int ctr_final = ctr;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MyIntentService.this, "Sleep stopped at " + ctr_final + " minutes", Toast.LENGTH_SHORT).show();
            }
        });

        Bundle bundle = new Bundle();
        bundle.putString("result","Sleep stopped at " + ctr_final);
        resultReceiver.send(20,bundle);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Service on destroy. Name is " + Thread.currentThread().getName());
        super.onDestroy();
    }
}
