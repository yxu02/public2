package edu.sjsu.xuy87.myservices.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class MyBoundService extends Service {
    private MyLocalBinder mMyLocalBinder = new MyLocalBinder();

    public class MyLocalBinder extends Binder{
        public MyBoundService getInstance(){
            return MyBoundService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return mMyLocalBinder;
    }

    public int add (int a, int b){
        return a + b;
    }
    public int subtract (int a, int b){
        return a - b;
    }
    public int multiply (int a, int b){
        return a * b;
    }
    public float divide (int a, int b){
        if (b == 0) return 0f;
        else return (float) a / (float) b;
    }
}
