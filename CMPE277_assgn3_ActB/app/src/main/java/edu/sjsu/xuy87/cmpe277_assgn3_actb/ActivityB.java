package edu.sjsu.xuy87.cmpe277_assgn3_actb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityB extends AppCompatActivity {
    TextView mTextView1, mTextView2;
    private int mAmount;
    private String mCurrency;
    private float result = 0f;
    public static final String[] currencies = {"Euro", "Indian Rupee", "British Pound"};
    public static final float[] exchangeRates = {0.81f,64.39f,0.71f};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);
        mTextView1 = findViewById(R.id.dollarAmount);
        mTextView2 = findViewById(R.id.currencyName);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("IntentAtoB");
        registerReceiver(mReceiverB, intentFilter);
    }

    public void startApply(View view) {
        for (int i=0; i< currencies.length; i++){
            if (mCurrency.equals(currencies[i])) {
                result = mAmount * exchangeRates[i];
                break;
            }
        }
        Intent intent = new Intent("IntentBtoA");
        intent.putExtra("amount", mAmount);
        intent.putExtra("currency", mCurrency);
        intent.putExtra("result", result);
        sendBroadcast(intent);
    }

    public void closeActivityB(View view) {
        finish();
    }

    BroadcastReceiver mReceiverB = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                Toast.makeText(context, "Broadcast from A to B received", Toast.LENGTH_LONG).show();
                mAmount = intent.getIntExtra("amount", 0);
                mCurrency = intent.getStringExtra("currency");
                mTextView1.setText("Dollar Amount: $ " + String.valueOf(mAmount));
                mTextView2.setText("Convert to: " + mCurrency);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unregister receiver at destroy so the receiver is active while activity A to B and B back to A
        // (not unregistered at onPause)
        unregisterReceiver(mReceiverB);
    }
}
