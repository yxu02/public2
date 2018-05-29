package edu.sjsu.xuy87.cmpe277_assgn3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class ActivityB extends AppCompatActivity {
    TextView mTextView1, mTextView2;
    private int mAmount;
    private String mCurrency;
    private float result = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);
        mTextView1 = findViewById(R.id.dollarAmount);
        mTextView2 = findViewById(R.id.currencyName);
    }

    @Override
    protected void onResume() {
        Intent intent = getIntent();
        mAmount = intent.getIntExtra("amount", 0);
        mCurrency = intent.getStringExtra("currency");
        mTextView1.setText("Dollar Amount: $ " + String.valueOf(mAmount));
        mTextView2.setText("Convert to: " + mCurrency);
        super.onResume();
    }

    public void startApply(View view) {
        for (int i=0; i< MainActivity.currencies.length; i++){
        if (mCurrency.equals(MainActivity.currencies[i])) {
            result = mAmount * MainActivity.exchangeRates[i];
            break;
            }
        }
        Intent intent = new Intent("currency.exchange");
        intent.putExtra("amount", mAmount);
        intent.putExtra("currency", mCurrency);
        intent.putExtra("result", result);
        sendBroadcast(intent);
        finish();
    }

    public void closeActivityB(View view) {
        finish();
    }
}
