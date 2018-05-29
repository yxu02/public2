package edu.sjsu.xuy87.myservices.Services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MyStartService extends Service {
    static final String TAG = MyStartService.class.getSimpleName();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service on start. Name is " + Thread.currentThread().getName());
        int sleepTime = intent.getIntExtra("sleepTime", 0);
        new MyAsynTask().execute(sleepTime);
        return START_STICKY;
    }

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
    public void onDestroy() {
        Log.i(TAG, "Service on destroy. Name is " + Thread.currentThread().getName());
        super.onDestroy();
    }

    class MyAsynTask extends AsyncTask<Integer, String, Integer> {
        @Override
        protected void onPreExecute() {
            Log.i(TAG, "Service on pre-execute. Name is " + Thread.currentThread().getName());
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer[] sleeptime) {
            int ctr =1;
            int time = sleeptime[0];
            Log.i(TAG, "Service on pre-execute. Name is " + Thread.currentThread().getName());

            while (ctr <= time){
                publishProgress("Counter is now " + ctr);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ctr++;
            }

            return ctr;
        }

        @Override
        protected void onProgressUpdate(String[] values) {
            Log.i(TAG, "Service running in background. " + values[0] + ". Name is " + Thread.currentThread().getName());
            Toast.makeText(MyStartService.this, "sleep for" + values[0] + "minutes", Toast.LENGTH_SHORT).show();
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Integer ctr) {
            stopSelf();
            Log.i(TAG, "Service on post-execute. Name is " + Thread.currentThread().getName());
            super.onPostExecute(ctr);

            if (ctr!=null){
                Intent intent = new Intent("mybroadcast.action");
                intent.putExtra("result", ctr);
                sendBroadcast(intent);
            }
        }
    }
}
