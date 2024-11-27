package com.example.hikelowna;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

@SuppressLint("MissingInflatedId")

    public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

        private GoogleMap myMap;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(MapActivity.this);

        }


        @Override
        public void onMapReady(@NonNull  GoogleMap  googleMap) {
            myMap = googleMap;
            LatLng Kelowna = new LatLng(55, -125);
            myMap.addMarker(new MarkerOptions().position(Kelowna).title("Kelowna"));
            myMap.moveCamera(CameraUpdateFactory.newLatLng(Kelowna));
            myMap.animateCamera(CameraUpdateFactory.zoomIn());
            myMap.animateCamera(CameraUpdateFactory.zoomOut());
        }

}