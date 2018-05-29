package edu.sjsu.xuy87.implicit_intents;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.AlarmClock;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_GET = 100;
    private static final int REQUESTCODE = 1000;
    private ImageView mImageView;
    private Uri mImageUri;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_IMAGE_GET && resultCode== Activity.RESULT_OK){
            mImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageUri);
                mImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(requestCode==REQUESTCODE && resultCode== Activity.RESULT_OK){
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = findViewById(R.id.image_view);
    }

    public void openLink(View view) {
        Uri uri = Uri.parse("http://www.sjsu.edu");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }else{
            Toast.makeText(this,"error!", Toast.LENGTH_SHORT).show();
        }
    }

    public void openMap(View view) {
        Uri uri = Uri.parse("https://www.google.com/maps?q=37.391752,-121.9871216");  //https://www.google.com/maps/@37.391752,-121.9871216,14z
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }else{
            Toast.makeText(this,"error!", Toast.LENGTH_SHORT).show();
        }
    }

    public void openGeo(View view) {
        Uri uri = Uri.parse("geo:37.391752,-121.9871216");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }else{
            Toast.makeText(this,"error!", Toast.LENGTH_SHORT).show();
        }
    }

    public void openDial(View view) {
        Uri uri = Uri.parse("tel:8148760520");
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }else{
            Toast.makeText(this,"error!", Toast.LENGTH_SHORT).show();
        }
    }

    public void openImage(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }else{
            Toast.makeText(this,"error!", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendText(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/*");
        intent.putExtra(Intent.EXTRA_TEXT, "Hello World!");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }else{
            Toast.makeText(this,"error!", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendImage(View view) {
        if(mImageUri==null){
            Toast.makeText(this, "image not found", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM,mImageUri);
        intent.setType(getContentResolver().getType(mImageUri));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }else{
            Toast.makeText(this,"error!", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendEmail(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, "yzx105psu@gmail.com");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Greetings!");
        intent.putExtra(Intent.EXTRA_TEXT, "Hello World!");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }else{
            Toast.makeText(this,"error!", Toast.LENGTH_SHORT).show();
        }
    }

    public void openCamera(View view) {
        Intent intent =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUESTCODE);
    }
}
