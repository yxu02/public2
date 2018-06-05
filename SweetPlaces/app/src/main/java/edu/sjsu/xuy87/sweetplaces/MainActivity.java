package edu.sjsu.xuy87.sweetplaces;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> places = new ArrayList<>();
    static ArrayList<LatLng> latLngs = new ArrayList<>();
    private ListView lv_places;
    static ArrayAdapter<String> arrAdapter;
    private SharedPreferences getNotes;
    private boolean isLongClick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getNotes = getSharedPreferences("edu.sjsu.xuy87.sweetplaces", MODE_PRIVATE);
        ArrayList<String> lats = new ArrayList<>();
        ArrayList<String> lons = new ArrayList<>();

        places.clear();
        lats.clear();
        lons.clear();
        latLngs.clear();
        try {
            places = (ArrayList<String>) ObjectSerializer.deserialize(getNotes.getString("places", ObjectSerializer.serialize(new ArrayList<String>())));
            lats = (ArrayList<String>) ObjectSerializer.deserialize(getNotes.getString("lats", ObjectSerializer.serialize(new ArrayList<String>())));
            lons = (ArrayList<String>) ObjectSerializer.deserialize(getNotes.getString("lons", ObjectSerializer.serialize(new ArrayList<String>())));

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (lats.size()>0 && lats.size() == places.size() && lons.size() == places.size()){
            for (int i = 0 ; i < lats.size() ; i++){
                latLngs.add(new LatLng(Double.parseDouble(lats.get(i)), Double.parseDouble(lons.get(i))));
            }
        } else{
            places.add("Add a new place...");
            latLngs.add(new LatLng(0,0));
        }
        lv_places = findViewById(R.id.lv_places);
        arrAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, places);

        lv_places.setAdapter(arrAdapter);
        lv_places.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
//                Toast.makeText(MainActivity.this, places.get(i), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("index", i);
                intent.putExtra("addrTitle",places.get(i));
                if(isLongClick== false) {
                    startActivity(intent);
                }
                isLongClick = false;
            }
        });
        lv_places.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int arg2, long arg3) {
                isLongClick = true;
                final int index = arg2;
                new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_DeviceDefault_Dialog_Alert)
                        .setTitle("Delete?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(places.size()>1) {
                            places.remove(index);
                            latLngs.remove(index);
                            arrAdapter.notifyDataSetChanged();
                            SharedPreferences shdPrf = getSharedPreferences("edu.sjsu.xuy87.sweetplaces", MODE_PRIVATE);
                            updateSharedPref(shdPrf, places, latLngs);
                        }
                    }
                }).setNegativeButton("No", null).show();

                return false;
            }
        });
    }

    static void updateSharedPref(SharedPreferences shdPrf, ArrayList<String> placeArrList, ArrayList<LatLng> latLngArrList){

        ArrayList<String> latitudeList = new ArrayList<>();
        ArrayList<String> longtitudeList = new ArrayList<>();
        for(LatLng latLng : latLngArrList){
            latitudeList.add(String.valueOf(latLng.latitude));
            longtitudeList.add(String.valueOf(latLng.longitude));
        }
        try{
            shdPrf.edit().putString("places", ObjectSerializer.serialize(placeArrList)).apply();
            shdPrf.edit().putString("lats", ObjectSerializer.serialize(latitudeList)).apply();
            shdPrf.edit().putString("lons", ObjectSerializer.serialize(longtitudeList)).apply();

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
