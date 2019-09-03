package com.example.whack_a_mole2;

import android.database.Cursor;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class Maps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        ArrayList<Record> records = getRecordsFromDB();

        for (int i = 0; i < records.size(); i++) {

            LatLng latLng = new LatLng(records.get(i).getLatitude(),records.get(i).getLongitude());
            mMap.addMarker(new
                    MarkerOptions().position(latLng).title(records.get(i).getName()).snippet(records.get(i).getScore()+""));
        }

        if(records.size()>0) //zoom to the first place
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom( new LatLng(records.get(0).getLatitude(),records.get(0).getLongitude()), 10));
    }

    private ArrayList<Record> getRecordsFromDB() {
        DatabaseHelper db = new DatabaseHelper(this);
        Cursor data = db.getRecords();

        ArrayList<Record> dataList = new ArrayList<>();

        while (data.moveToNext()) {
            int seconds = Integer.parseInt(data.getString(5));
            int score = Integer.parseInt(data.getString(2));
            int miss = Integer.parseInt(data.getString(3));
            int bombs = Integer.parseInt(data.getString(4));
            String name = data.getString(1);
            double latitude = Double.parseDouble(data.getString(6));
            double longitude = Double.parseDouble(data.getString(7));

            Record record = new Record(name, seconds, score, miss, bombs, latitude, longitude);
            dataList.add(record);

        }
        return dataList;
    }


}
