package edu.sjsu.xuy87.myservices.Services;

import android.app.Service;
import android.content.Intent;
import android.os.*;
import android.widget.Toast;

public class MessengerService extends Service {

    float result;

    private class MessengerHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 25:
                    calculate(msg);
                    Messenger incomingMessenger = msg.replyTo;
                    Bundle bundle = new Bundle();
                    bundle.putFloat("result", result);
                    Message msgToActivity = Message.obtain(null, 26);
                    msgToActivity.setData(bundle);
                    try {
                        incomingMessenger.send(msgToActivity);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                super.handleMessage(msg);
                break;
            }
        }
    }

    private void calculate(Message msg) {
        Bundle bundle = msg.getData();
        int int1 = bundle.getInt("int1", 0);
        int int2 = bundle.getInt("int2", 1);
        int operator = bundle.getInt("mathOperator");
        switch (operator){
            case 2131427458:
                result = int1 + int2;
                Toast.makeText(getApplicationContext(), String.valueOf(result), Toast.LENGTH_SHORT).show();
                break;
            case 2131427459:
                result = int1 - int2;
                Toast.makeText(getApplicationContext(), String.valueOf(result), Toast.LENGTH_SHORT).show();
                break;
            case 2131427460:
                result = int1 * int2;
                Toast.makeText(getApplicationContext(), String.valueOf(result), Toast.LENGTH_SHORT).show();
                break;
            case 2131427461:
                if (int2 != 0) {
                    result = (float) int1 / (float) int2;
                    Toast.makeText(getApplicationContext(), String.valueOf(result), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid denominator!", Toast.LENGTH_SHORT).show();
                }
                break;
            default: Toast.makeText(getApplicationContext(), "Unknown Error!", Toast.LENGTH_SHORT).show();
        }
    }

    Messenger mMessenger = new Messenger(new MessengerHandler());

    @Override
    public IBinder onBind(Intent intent) {

        return mMessenger.getBinder();
    }
}
