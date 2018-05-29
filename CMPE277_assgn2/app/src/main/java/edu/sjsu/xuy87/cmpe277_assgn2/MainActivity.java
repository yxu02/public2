package edu.sjsu.xuy87.cmpe277_assgn2;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText url;
    EditText phoneNumber;
    Button launch_button;
    Button ring_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        url = findViewById(R.id.editText_url);
        phoneNumber = findViewById(R.id.editText_phone);
        launch_button = findViewById(R.id.button_Launch);
        ring_button = findViewById(R.id.button_Ring);
    }

    public void ringOnClick(View view) {
        Uri number = Uri.parse(phoneNumber.getText().toString());
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        if (callIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(callIntent);
        }else{
            Toast.makeText(this,"Incorrect URI!", Toast.LENGTH_LONG).show();
        }
    }

    public void launchOnClick(View view) {
        Uri webpage = Uri.parse(url.getText().toString());
        Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
        if (webIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(webIntent);
        }else{
            Toast.makeText(this,"Incorrect URI!", Toast.LENGTH_LONG).show();
        }
    }
}
