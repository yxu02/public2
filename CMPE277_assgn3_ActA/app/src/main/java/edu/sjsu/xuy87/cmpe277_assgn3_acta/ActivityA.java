package edu.sjsu.xuy87.cmpe277_assgn3_acta;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityA extends AppCompatActivity {
    TextView mCurrencyName, mAmount, mResult;
    int amount;
    public static final String[] currencies = {"Euro", "Indian Rupee", "British Pound"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);
        mAmount = findViewById(R.id.amount);
        mCurrencyName = findViewById(R.id.currency);
        mResult = findViewById(R.id.result);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("IntentBtoA");
        registerReceiver(mReceiverA, intentFilter);
    }
    public void isEuro(View view) {
        mCurrencyName.setText(currencies[0]);
    }

    public void isRupee(View view) {
        mCurrencyName.setText(currencies[1]);
    }

    public void isPound(View view) {
        mCurrencyName.setText(currencies[2]);
    }

    public void startConvert(View view) {
        if(mAmount!=null)
            amount = Integer.parseInt(mAmount.getText().toString());
        Intent intent = new Intent("IntentAtoB");
        intent.putExtra("amount", amount);
        intent.putExtra("currency", mCurrencyName.getText().toString());
        sendBroadcast(intent);
    }

    public void closeApp(View view) {
        finish();
    }
    BroadcastReceiver mReceiverA = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int amt = intent.getIntExtra("amount", 0);
            String curr = intent.getStringExtra("currency");
            float result = intent.getFloatExtra("result", 0f);
            mResult.setText("Dollar amount $" + amt + " converted to " + result + " " + curr);
            Toast.makeText(context, "broadcast from B to A received!", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unregister receiver at destroy so the receiver is active while activity A to B and B back to A
        // (not unregistered at onPause)
        unregisterReceiver(mReceiverA);
    }
}
