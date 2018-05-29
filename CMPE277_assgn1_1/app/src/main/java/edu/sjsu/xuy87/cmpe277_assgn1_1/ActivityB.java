package edu.sjsu.xuy87.cmpe277_assgn1_1;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

public class ActivityB extends Activity implements View.OnClickListener {

    public static final String ACTIVITY_B_COUNTER = "activityB_counter";
    private Button mFinishBButton;
    private int total = 0;
    private int mCounterB=0;
    private Intent mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);

        mFinishBButton = (Button) findViewById(R.id.finishB);
        mFinishBButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == mFinishBButton) {
//            Intent intent = new Intent(ActivityB.this, ActivityA.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
            total = getIntent().getIntExtra(ActivityA.ACTIVITY_A_COUNTER, 0)+1;
            mData = new Intent();
            mData.putExtra(ActivityA.ACTIVITY_A_COUNTER, total);
            mData.putExtra(ActivityB.ACTIVITY_B_COUNTER, ++mCounterB);
            setResult(Activity.RESULT_OK, mData);
            finish();
        }
    }
}
