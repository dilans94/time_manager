package com.timemanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.function.DoubleUnaryOperator;



public class MyLocationActivity extends Activity implements OnMapReadyCallback {
    private MapFragment frag_map;
    double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_loc_activity);
        Intent i=getIntent();
        latitude=Double.parseDouble(i.getStringExtra("latitude"));
        longitude= Double.parseDouble(i.getStringExtra("longitude"));
        frag_map = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.frag_map);
        frag_map.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

    }
}