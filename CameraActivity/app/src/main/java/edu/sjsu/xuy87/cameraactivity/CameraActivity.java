package edu.sjsu.xuy87.cameraactivity;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CameraActivity extends AppCompatActivity {
    ImageView mImageView;
    private File mPhotoFile;
    private Uri mPhotoURI;
    String photoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mImageView = findViewById(R.id.imageView);
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,}, 20);
        }
    }


    public void takeShot(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager())!= null) {
            mPhotoFile = createImageFile();
            if(mPhotoFile !=null) {
                photoPath = mPhotoFile.getAbsolutePath();
                mPhotoURI = FileProvider.getUriForFile(this,
                        "edu.sjsu.xuy87.cameraactivity.fileprovider", mPhotoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoURI);
                startActivityForResult(intent, 21);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==21 && resultCode == RESULT_OK){
            notifyGallery(this, photoPath);
        }
    }

    private void notifyGallery(Context context, String photoPath) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(photoPath);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
    }

    public void showPhoto(View view) {
        if(mPhotoURI!=null){
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mPhotoURI);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(bitmap !=null) {
                mImageView.setImageBitmap(resizeBitmap(bitmap));
            }
        }
    }

    private Bitmap resizeBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int maxSize = 480;

        if (width > maxSize){
            float ratio = (float) (width/maxSize);
            width = maxSize;
            height = (int) (height / ratio);
        } else if (height > maxSize){
            float ratio = (float) (height/maxSize);
            height = maxSize;
            width = (int) (width / ratio);
        } else {
            width = maxSize;
            height = maxSize;
        }
        Bitmap resizedImage = Bitmap.createScaledBitmap(bitmap, width, height, false);
        return resizedImage;
    }

    private File createImageFile(){
        String timeStamp = new SimpleDateFormat("yyyymmdd_hhmmss", Locale.US).format(new Date());
        String imageFileName = "IMG_" + timeStamp +".jpg";

        File imageFile = new File (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), imageFileName);
        return  imageFile;
    }
}
