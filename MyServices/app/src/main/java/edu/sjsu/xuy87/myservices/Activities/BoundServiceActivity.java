package edu.sjsu.xuy87.myservices.Activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import edu.sjsu.xuy87.myservices.Services.MyBoundService;
import edu.sjsu.xuy87.myservices.R;

public class BoundServiceActivity extends AppCompatActivity {
    boolean isBound = false;
    EditText mEditText1;
    EditText mEditText2;
    TextView mTextView;
/*    private Button mAdd;
    private Button mSubtract;
    private Button mMultiply;
    private Button mDivide;*/
    int int1, int2;
    MyBoundService mMyBoundService;

    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            MyBoundService.MyLocalBinder binder = (MyBoundService.MyLocalBinder) iBinder;
            mMyBoundService = binder.getInstance();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    @Override
    protected void onStart() {
        Intent intent = new Intent(this, MyBoundService.class);
        super.onStart();
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(mServiceConnection);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boundservice);
        mEditText1 = findViewById(R.id.editText1);
        mEditText2 = findViewById(R.id.editText2);
        mTextView = findViewById(R.id.textView_result);

    }

    public void doCalculation(View view) {
        int1 = Integer.parseInt(mEditText1.getText().toString());
        int2 = Integer.parseInt(mEditText2.getText().toString());
        String resultStr;

        switch (view.getId()){
            case R.id.doAdd: resultStr=String.valueOf(mMyBoundService.add(int1, int2)); break;
            case R.id.doSubtract: resultStr=String.valueOf(mMyBoundService.subtract(int1, int2)); break;
            case R.id.doMultiply: resultStr=String.valueOf(mMyBoundService.multiply(int1, int2)); break;
            case R.id.doDivide: resultStr=String.valueOf(mMyBoundService.divide(int1, int2)); break;
            default: resultStr="calculation error"; break;
        }
        mTextView.setText(resultStr);
    }
}
