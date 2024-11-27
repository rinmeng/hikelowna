//package com.example.cosc341project;
//
//import android.health.connect.datatypes.ExerciseRoute;
//import android.os.Bundle;
//
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//public class MapsMarkerActivity extends AppCompatActivity implements OnMapReadyCallback {
//    // onCreate method is called when the activity is first created
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        // Retrieve the content view that renders the map.
//        setContentView(R.layout.activity_main);
//
//        // Get the SupportMapFragment and request notification
//        // when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment)
//                getSupportFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//    }
//
//    // This method is called when the map is ready to be used.
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        // Add a marker in Sydney, Australia,
//        // and move the map's camera to the same location.
//        ExerciseRoute.Location Location = null;
//        LatLng myPos = new LatLng(Location.getLatitude(), Location.getLongitude());
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(myPos));
//    }
//}