package edu.sjsu.xuy87.face_detection;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.os.Environment.getExternalStoragePublicDirectory;

/**
 * Created by Yu Xu 4/12/2018
 */
public class MainActivity extends AppCompatActivity {

    private static String LOG_TAG = "FaceDetection";

    private static final int RC_HANDLE_PERMISSIONS = 2;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void takePicture(View view) {
        // Check for the camera permission before accessing the camera.  If the
        // permission is not granted yet, request permission.
        int cameraPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int writePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (cameraPermission == PackageManager.PERMISSION_GRANTED && writePermission == PackageManager.PERMISSION_GRANTED) {
            logAndShowSnackbar("Permissions granted");
            dispatchTakePictureIntent();
        } else {
            requestPermissions();
        }
    }

    private void requestPermissions() {
        logAndShowSnackbar("Camera and Write permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        ActivityCompat.requestPermissions(this, permissions,
                RC_HANDLE_PERMISSIONS);
    }

    private void logAndShowSnackbar(final String text) {
        Log.w(LOG_TAG, text);
        View container = findViewById(R.id.activity_face_detection);
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                logAndShowSnackbar("Error occurred while creating the File");
                return;
            }
            // Continue only if the File was successfully created

            if (photoFile != null) {
                mImageUri = FileProvider.getUriForFile(this,
                        "edu.sjsu.xuy87.face_detection.fileprovider",
                        photoFile);

                List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    grantUriPermission(packageName, mImageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != RC_HANDLE_PERMISSIONS) {
            Log.d(LOG_TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            logAndShowSnackbar("Permissions granted, fire off the intent");
            dispatchTakePictureIntent();
            return;
        }

        logAndShowSnackbar("Permission not granted: results len = " + grantResults.length +
                " Result code = " + (grantResults.length > 0 ? grantResults[0] : "(empty)"));
    }

    public Bitmap readBitmap(Uri selectedImage) {
        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 5;
        AssetFileDescriptor fileDescriptor =null;
        try {
            fileDescriptor = this.getContentResolver().openAssetFileDescriptor(selectedImage,"r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally{
            try {
                bm = BitmapFactory.decodeFileDescriptor(fileDescriptor.getFileDescriptor(), null, options);
                fileDescriptor.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bm;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            this.getContentResolver().notifyChange(mImageUri, null);
            Bitmap imageBitmap;
            try
            {
                imageBitmap = readBitmap(mImageUri);
                if(imageBitmap != null) {
                    detectFaces(imageBitmap);
                }
            }
            catch (Exception e)
            {
                Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, "Failed to load", e);
            }

        }
    }

    public void detectFaces(Bitmap imageBitmap) {
        StringBuilder faceInfo = new StringBuilder();

        FaceDetector faceDetector = new FaceDetector.Builder(this)
                .setProminentFaceOnly(true)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();

        if(!faceDetector.isOperational()) {
            Log.w(LOG_TAG, "Detector dependencies are not yet available.");
            // Check for low storage.  If there is low storage, the native library will not be
            // downloaded, so detection will not become operational.
            IntentFilter lowstorageFilter = new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW);
            boolean hasLowStorage = registerReceiver(null, lowstorageFilter) != null;

            if (hasLowStorage) {
                Toast.makeText(this,"Low Storage", Toast.LENGTH_LONG).show();
                Log.w(LOG_TAG, "Low Storage");
            }
        }


        Frame imageFrame = new Frame.Builder()
                .setBitmap(imageBitmap)
                .build();

        SparseArray<Face> faces = faceDetector.detect(imageFrame);

        for (int i = 0; i < faces.size(); i++) {
            Face face = faces.get(faces.keyAt(i));

            String message = "Face was detected... ";
            if(face.getIsSmilingProbability() == Face.UNCOMPUTED_PROBABILITY) {
                message += "But we could not tell if it was happy or not :/";
            }
            else if(face.getIsSmilingProbability() < 0.5) {
                message += "and it wasn't so happy";
            }
            else {
                message += "and it was really happy!";
            }
            faceInfo.append(message);
            faceInfo.append("\n\n");
        }

        String result = faceInfo.toString();
        if (result.isEmpty()) {
            result = "Detected no Face!";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Face Detection sample")
                .setMessage(result)
                .setPositiveButton("OK", null)
                .show();
    }
}
