package edu.sjsu.xuy87.cmpe277_assgn4;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import org.w3c.dom.Text;

import java.security.SecureRandom;

public class MainActivity extends AppCompatActivity {

    private TextView mXValue;
    private TextView mYValue;
    private TextView mZValue;
    private EditText editText_mCounter;
    private TextView mResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mXValue = findViewById(R.id.xValue);
        mYValue = findViewById(R.id.yValue);
        mZValue = findViewById(R.id.zValue);
        editText_mCounter = findViewById(R.id.count);
        mResult = findViewById(R.id.result);
    }

    public void generateNum(View view) {
        if(editText_mCounter !=null) {
            int count = Integer.parseInt(editText_mCounter.getText().toString());
            new MyAsyncTask().execute(count);
        } else {
            mResult.setText("Error: please type in simulation numbers!");
        }
    }

    public void cancelApp(View view) {
        finish();
    }

    private class MyAsyncTask extends AsyncTask<Integer, String, Void>{

        SecureRandom mSecureRandom = new SecureRandom();
        String xValue, yValue, zValue;
        StringBuilder mStringBuilder = new StringBuilder();

        @Override
        protected Void doInBackground(Integer... params) {
            int simCount = params[0];
            int ctr = 1;

            while(ctr <= simCount) {
                try {
                    Thread.sleep(1000);
                    xValue = String.valueOf(mSecureRandom.nextInt(2000) - 1000);
                    yValue = String.valueOf(mSecureRandom.nextInt(2000) - 1000);
                    zValue = String.valueOf(mSecureRandom.nextInt(2000) - 1000);
                    publishProgress("Simulation count: " + ctr, xValue, yValue, zValue);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ctr++;
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            mStringBuilder.append(values[0]).append("\t").append("X: "+values[1]+", ")
                    .append("Y: "+values[2]+", ").append("Z: "+values[3]).append("\n");
            mXValue.setText(values[1]);
            mYValue.setText(values[2]);
            mZValue.setText(values[3]);
            mResult.setText(mStringBuilder);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mStringBuilder.append("Simulation Completed!");
            mResult.setText(mStringBuilder);
        }
    }
}
