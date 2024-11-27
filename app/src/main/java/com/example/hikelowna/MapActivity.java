package com.example.hikelowna;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

@SuppressLint("MissingInflatedId")

    public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

        private GoogleMap myMap;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(MainActivity.this);

        }


        @Override
        public void onMapReady(@NonNull  GoogleMap  googleMap) {

            myMap = googleMap;

            LatLng sydney = new LatLng(-34, 151);
            myMap.addMarker(new MarkerOptions().position(sydney).title("Sydney"));
            myMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        }

}