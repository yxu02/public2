package edu.sjsu.xuy87.mapapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.*;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager lmgr;
    private LocationListener locationListener;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED){
                    lmgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, locationListener);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Step 1: declare location manager and listener
        lmgr = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mMap.clear();
                LatLng currLoc = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(currLoc).title("Current Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(currLoc));

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
                        Toast.makeText(MapsActivity.this, address.toString(), Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
//        //Step 3: use requestLocationUpdates method. Use auto completion to add the if statement
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        lmgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
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
                mMap.clear();
                LatLng currLoc = new LatLng(lastKnownLoc.getLatitude(), lastKnownLoc.getLongitude());
                mMap.addMarker(new MarkerOptions().position(currLoc).title("Current Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(currLoc));
            }
        }
    }
}
