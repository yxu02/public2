package edu.sjsu.xuy87.sweetplaces;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, OnMapLongClickListener {

    private GoogleMap mMap;
    private LocationManager lmgr;
    private LocationListener locationListener;

    @Override
    public void onMapLongClick(LatLng point) {
        String address = "";
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(point.latitude,point.longitude,1);
            if (addresses != null && addresses.size() >0 ){
                if (addresses.get(0).getThoroughfare() != null){
                    if (addresses.get(0).getSubThoroughfare() != null) {
                        address = addresses.get(0).getSubThoroughfare()+ " ";
                        address += addresses.get(0).getThoroughfare();
                        Log.i("Long press address is: ", address);
                    }
                }
            } else {
                address = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            }
            MainActivity.places.add(address);
            MainActivity.latLngs.add(point);
            MainActivity.arrAdapter.notifyDataSetChanged();
            SharedPreferences addNote = getSharedPreferences("edu.sjsu.xuy87.sweetplaces", MODE_PRIVATE);
            MainActivity.updateSharedPref(addNote,MainActivity.places,MainActivity.latLngs);
//            try {
//                ArrayList<String> latitudeList = new ArrayList<>();
//                ArrayList<String> longtitudeList = new ArrayList<>();
//                for(LatLng latLng : MainActivity.latLngs){
//                    latitudeList.add(String.valueOf(latLng.latitude));
//                    longtitudeList.add(String.valueOf(latLng.longitude));
//                }
//                SharedPreferences addNote = getSharedPreferences("edu.sjsu.xuy87.sweetplaces", MODE_PRIVATE);
//
//                addNote.edit().putString("places", ObjectSerializer.serialize(MainActivity.places)).apply();
//                addNote.edit().putString("lats", ObjectSerializer.serialize(latitudeList)).apply();
//                addNote.edit().putString("lons", ObjectSerializer.serialize(longtitudeList)).apply();
//
//            } catch (Exception e){
//                e.printStackTrace();
//            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        mMap.addMarker(new MarkerOptions().position(point).title(address));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1){
            if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED){
                    lmgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, locationListener);
                    Location lastKnownLoc = lmgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    centerMap(lastKnownLoc, "Current Location");
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

    public void centerMap(Location location, String title){
        if (location!=null) {
            mMap.clear();
            LatLng currLoc = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(currLoc).title(title));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currLoc, 14));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);


        Intent intent = getIntent();
        final int index = intent.getIntExtra("index", -1);
        final String addrTitle = intent.getStringExtra("addrTitle");
        if (index == -1){
            finish();
            Log.i("Error: ", "no value received");
        } else if (index == 0) {
            lmgr = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    centerMap(location, "Current Location");

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

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                lmgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location lastKnownLoc = lmgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                centerMap(lastKnownLoc, "Current Location");
            }
        } else{
            Location custom = new Location(LocationManager.GPS_PROVIDER);
            custom.setLatitude(MainActivity.latLngs.get(index).latitude);
            custom.setLongitude(MainActivity.latLngs.get(index).longitude);
            centerMap(custom, addrTitle);
        }
    }
}
