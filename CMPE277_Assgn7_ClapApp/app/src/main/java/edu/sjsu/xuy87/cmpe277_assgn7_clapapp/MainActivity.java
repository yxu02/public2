package edu.sjsu.xuy87.cmpe277_assgn7_clapapp;

import android.hardware.*;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener{
    private final String TAG = this.getClass().getSimpleName();
    private SensorManager mSM;
    private Sensor mProxSensor;
    private static final int SENSOR_SENSITIVITY = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSM = (SensorManager) getSystemService(SENSOR_SERVICE);

        setContentView(R.layout.activity_main);

        mProxSensor = mSM.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        if(mProxSensor != null){
            Toast.makeText(this, "Sensor.TYPE_PROXIMITY Available", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Sensor.TYPE_PROXIMITY Unavailable", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (event.values[0] >= -SENSOR_SENSITIVITY && event.values[0] <= SENSOR_SENSITIVITY){
                Log.i(TAG, "start clapping!");
                audioPlayer();
            } else{
                Log.i(TAG, "stop clapping!");
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSM.registerListener(this, mProxSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mSM.unregisterListener(this);
    }

    private void audioPlayer(){

        try {
            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.applause);
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
