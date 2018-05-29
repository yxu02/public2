package edu.sjsu.xuy87.cmpe277_assgn1_1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityC extends Activity implements View.OnClickListener {

    public static final String ACTIVITY_C_COUNTER = "activityC_counter";
    private Button mCloseButton;
    private int mCounterC = 0;
    private int total = 0;
    private Intent mData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dialog);

        mCloseButton = (Button) findViewById(R.id.dialog_button);
        mCloseButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mCloseButton) {
            total = getIntent().getIntExtra(ActivityA.ACTIVITY_A_COUNTER, 0)+1;
            mData = new Intent();
            mData.putExtra(ActivityA.ACTIVITY_A_COUNTER, total);
            mData.putExtra(ActivityC.ACTIVITY_C_COUNTER, ++mCounterC);
            setResult(Activity.RESULT_OK, mData);
            finish();
        }
    }
}
