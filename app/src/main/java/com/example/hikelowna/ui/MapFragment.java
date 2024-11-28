package com.example.hikelowna.ui;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.hikelowna.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap map;
    private Button zoomInButton, zoomOutButton;

    ListView suggestionsListView;
    LatLng defaultLocation;
    float defaultZoom;
    float searchZoom;

    SearchView searcher;
    List<String> searchResults;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        defaultLocation = getLatLngFromCity("Kelowna");
        defaultZoom = 12.0f;
        searchZoom = 15.0f;


        // Default: Draw a marker for Kelowna
        moveCameraTo(defaultLocation, defaultZoom);

        setZoomControls();
    }

    private LatLng getLatLngFromCity(String city) {
        // Use Geocoder to fetch coordinates for a city name
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(city, 1);  // Get the first result
            if (addresses != null && !addresses.isEmpty()) {
                searchResults = new ArrayList<>();
                for (Address ad : addresses){
                    String addressName = ad.getAddressLine(0);
                    searchResults.add(addressName);
                }
                Address address = addresses.get(0);
                Log.d("Addresses", searchResults.toString());
                Log.d("AddressFirst", searchResults.get(0));
                double latitude = address.getLatitude();
                double longitude = address.getLongitude();
                return new LatLng(latitude, longitude);
            } else {
                Log.e("MapFragment", "City not found.");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("MapFragment", "Error in geocoding: " + e.getMessage());
        }
        return null;
    }

    private void pinAndMove(String title, LatLng latLng, float zoomLevel) {
        if (map != null) {
            // Create and add the marker
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(title);
            Marker marker = map.addMarker(markerOptions);

            // Move camera to the location
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));

            // Show the info window by default
            if (marker != null) {
                marker.showInfoWindow();  // Show the info window immediately
            }
        } else {
            Log.e("MapFragment", "Map not initialized.");
        }
    }


    private void moveCameraTo(LatLng latLng, float zoomLevel){
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
    }


    private void setZoomControls() {
        zoomInButton.setOnClickListener(v -> {
            float currentZoom = map.getCameraPosition().zoom;
            if (currentZoom < map.getMaxZoomLevel()) {
                map.animateCamera(CameraUpdateFactory.zoomTo(currentZoom + 1));
            }
        });

        zoomOutButton.setOnClickListener(v -> {
            float currentZoom = map.getCameraPosition().zoom;
            if (currentZoom > map.getMinZoomLevel()) {
                map.animateCamera(CameraUpdateFactory.zoomTo(currentZoom - 1));
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        // Initialize buttons
        zoomInButton = rootView.findViewById(R.id.zoomInButton);
        zoomOutButton = rootView.findViewById(R.id.zoomOutButton);

        // Initialize Search
        searcher = rootView.findViewById(R.id.searcher);
        searchResults = new ArrayList<>();

        suggestionsListView = rootView.findViewById(R.id.suggestionsListView);

        // Set SearchView query listener
        searcher.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Call geocoding for the entered city name
                if (query.isEmpty()){
                    moveCameraTo(defaultLocation, defaultZoom);
                }else{
                    pinAndMove(query, getLatLngFromCity(query), searchZoom);
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Optional: You can use this method for suggestions as the text changes
                return false;
            }
        });

        // Set up the map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        try {
            assert mapFragment != null;
            mapFragment.getMapAsync(this);
        } catch (Exception e) {
            Log.e("MapFragment", "Error initializing the map: " + e.getMessage(), e);
        }

        return rootView;
    }
}
