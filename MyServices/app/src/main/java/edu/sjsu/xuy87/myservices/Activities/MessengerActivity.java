package edu.sjsu.xuy87.myservices.Activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.*;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import edu.sjsu.xuy87.myservices.R;
import edu.sjsu.xuy87.myservices.Services.MessengerService;

public class MessengerActivity extends AppCompatActivity {
    boolean isBound = false;
    EditText mEditText1;
    EditText mEditText2;
    TextView mTextView;
    Messenger mMessenger;
    int int1, int2;

    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            mMessenger = new Messenger(iBinder);
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    private class MessengerHandler extends Handler{
        @Override
        public void handleMessage(Message msgFromService) {
            switch (msgFromService.what){
                case 26:
                    Bundle bundle = msgFromService.getData();
                    mTextView.setText(String.valueOf(bundle.getFloat("result")));
                    break;
                default:
                    super.handleMessage(msgFromService);
                    break;
            }
        }
    }
    private Messenger incomingMessenger = new Messenger(new MessengerHandler());


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        mEditText1 = findViewById(R.id.editText3);
        mEditText2 = findViewById(R.id.editText4);
        mTextView = findViewById(R.id.textView_result1);

    }

    public void doCalculation1(View view) {
        int1 = Integer.parseInt(mEditText1.getText().toString());
        int2 = Integer.parseInt(mEditText2.getText().toString());

        Bundle bundle = new Bundle();
        bundle.putInt("int1", int1);
        bundle.putInt("int2", int2);
        bundle.putInt("mathOperator", view.getId());

        Message message = Message.obtain(null, 25);
        message.replyTo = incomingMessenger;
        message.setData(bundle);

        try {
            mMessenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void bindService(View view) {
        if(isBound == false) {
            Intent intent = new Intent(this, MessengerService.class);
            bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
            isBound = true;
        }
    }

    public void unbindService(View view) {
        if(isBound ==true ){
            unbindService(mServiceConnection);
            isBound = false;
        }
    }
}
