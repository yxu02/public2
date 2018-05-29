package com.edu.sjsu.xuy87.android.mobile_streams;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.kinesis.kinesisrecorder.KinesisFirehoseRecorder;
import com.amazonaws.regions.Regions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity{

    private String cognitoIdentityPoolId;
    private String awsAccountId;
    private String cognitoUnauthRoleArn;
    private String cognitoAuthRoleArn;
    private Regions region;
    private String firehoseStreamName;

    protected static final String APPLICATION_NAME = "android-stream-to-firehose";
    protected KinesisFirehoseRecorder firehoseRecorder;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ");

    private String androidId;
    private Button button1;
    private Button button2;
    private Button button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        initUI();
        initRecorder();
        saveFirehoseRecord("onCreate");
    }

    private void initUI() {
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);

        button1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = "Button1 pressed";
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                saveFirehoseRecord(msg);
            }
        });
        button2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = "Button2 pressed";
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                saveFirehoseRecord(msg);
            }
        });
        button3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = "New visit event received. Streaming in process.";
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... v) {
                        String msg = "New visit event received. Streaming in process.";
                        try {
                            saveFirehoseRecord(msg);
                            firehoseRecorder.submitAllRecords();
                        } catch (AmazonClientException ace) {
                            Log.e(APPLICATION_NAME, "firehose.submitAll failed");
                            initRecorder();
                        }
                        return null;
                    }
                }.execute();
            }
        });
    }

    protected void initRecorder() {
        awsAccountId = getString(R.string.aws_account_id);
        cognitoIdentityPoolId = getString(R.string.cognito_identity_pool_id);
        cognitoUnauthRoleArn = getString(R.string.unauthRoleArn);
        cognitoAuthRoleArn = getString(R.string.authRoleArn);
        region = Regions.fromName(getString(R.string.region));
        androidId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        // Get credential from Cognito Identiry Pool
        File directory = getApplicationContext().getCacheDir();
        AWSCredentialsProvider provider = new CognitoCachingCredentialsProvider(
                getApplicationContext(), awsAccountId,
                cognitoIdentityPoolId, cognitoUnauthRoleArn, cognitoAuthRoleArn,
                region);
        firehoseRecorder = new KinesisFirehoseRecorder(directory, region, provider);
    }

    public void saveFirehoseRecord(String put_string) {
        firehoseStreamName = getString(R.string.firehose_stream_name);
        JSONObject json = new JSONObject();
        try {
            json.accumulate("time", sdf.format(new Date()));
            json.accumulate("model", Build.MODEL);
            json.accumulate("android_id", androidId);
            json.accumulate("app_name", APPLICATION_NAME);
            json.accumulate("message", put_string);
//            Log.e(APPLICATION_NAME, json.toString());
            Log.e(APPLICATION_NAME, json.toString());
            firehoseRecorder.saveRecord(json.toString(), firehoseStreamName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}