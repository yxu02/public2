package edu.sjsu.xuy87.hikerpal;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.*;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView tv_long;
    private TextView tv_lat;
    private TextView tv_altitude;
    private TextView tv_addr;
    private LocationManager lmgr;
    private LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED){
                    lmgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_long = findViewById(R.id.longtitude);
        tv_lat = findViewById(R.id.latitude);
        tv_altitude = findViewById(R.id.altitude);
        tv_addr = findViewById(R.id.address);

        lmgr = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateLoc(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (Build.VERSION.SDK_INT < 23){
            lmgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, locationListener);
        } else{
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            } else{
                lmgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, locationListener);
                Location lastKnownLoc = lmgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                updateLoc(lastKnownLoc);
            }
        }


    }

    private void updateLoc(Location location) {
        tv_long.setText("Longtitude: " + String.format("%.2f",location.getLongitude()));
        tv_lat.setText("Latitude: " + String.format("%.2f",location.getLatitude()));
        tv_altitude.setText("Altitude(m): " + String.format("%.2f",location.getAltitude()));

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            if (addresses != null && addresses.size() >0 ){
                StringBuilder address = new StringBuilder();

                if (addresses.get(0).getThoroughfare() != null){
                    address.append(addresses.get(0).getThoroughfare() + ", ");
                }
                if (addresses.get(0).getLocale() != null){
                    address.append(addresses.get(0).getLocale()+ ", ");
                }
                if (addresses.get(0).getAdminArea() != null){
                    address.append(addresses.get(0).getAdminArea() + ", ");
                }
                if (addresses.get(0).getPostalCode() != null){
                    address.append(addresses.get(0).getPostalCode());
                }
                tv_addr.setText("Address: " + address.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
