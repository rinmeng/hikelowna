package com.example.hikelowna.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.hikelowna.R;
import com.example.hikelowna.core.Trail;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

// Retrofit imports
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

// Gson imports for JSON parsing
import com.google.gson.annotations.SerializedName;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap map;
    private Button zoomInButton, zoomOutButton;

    TextView mapMessage;
    ListView suggestionsListView;
    LatLng defaultLocation;
    float defaultZoom;
    float searchZoom;

    SearchView searcher;
    List<String> searchResults;

    private List<Trail> trails;
    private ArrayAdapter<Trail> adapter;

    // Add this method to initialize trails
    private void initializeTrails() {
        trails = new ArrayList<>();
        trails.add(new Trail("Knox Mountain", "Moderate", 3.8f));
        trails.add(new Trail("Mission Creek Greenway", "Easy", 16.5f));
        trails.add(new Trail("Mount Boucherie", "Difficult", 6.0f));
        trails.add(new Trail("Paul's Tomb", "Easy", 4.8f));
        trails.add(new Trail("Dilworth Mountain", "Moderate", 3.2f));
        trails.add(new Trail("Rose Valley Regional Park", "Moderate", 5.0f));
        trails.add(new Trail("Myra Canyon", "Easy", 12.0f));
        trails.add(new Trail("Crawford Falls", "Moderate", 1.5f));
        trails.add(new Trail("Apex Trail", "Difficult", 5.2f));
        trails.add(new Trail("Myra Canyon Trestles Loop", "Easy", 24.0f));
        trails.add(new Trail("Myra Canyon West Trail", "Moderate", 8.0f));
        trails.add(new Trail("Myra Canyon East Trail", "Moderate", 7.5f));
        trails.add(new Trail("Myra-Bellevue Angel Springs Trail", "Moderate", 6.4f));
        trails.add(new Trail("Myra Canyon KVR South", "Easy", 15.0f));
        trails.add(new Trail("Myra Canyon Trestle Valley Trail", "Moderate", 4.2f));

        Collections.sort(trails);
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, trails);
        suggestionsListView.setAdapter(adapter);
    }


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
        defaultLocation = new LatLng(49.8880, -119.4960);
        defaultZoom = 16.0f;
        searchZoom = 17.0f;

        // Default: Draw a marker for Kelowna
        moveCameraTo(defaultLocation, 12);

        setZoomControls();

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            // further on provide details about the hike
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();

                return true; // Return true to consume the event and prevent default behavior
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        zoomInButton = rootView.findViewById(R.id.zoomInButton);
        zoomOutButton = rootView.findViewById(R.id.zoomOutButton);
        searcher = rootView.findViewById(R.id.searcher);
        searchResults = new ArrayList<>();
        suggestionsListView = rootView.findViewById(R.id.suggestionsListView);
        mapMessage= rootView.findViewById(R.id.mapMessage);
        initializeTrails();


        searcher.setGravity(Gravity.CENTER);

        // Show list when search icon is clicked
        searcher.setOnSearchClickListener(v -> {
            // Show the suggestions list
            suggestionsListView.setVisibility(View.VISIBLE);
            // Trigger the filter to show all items
            adapter.getFilter().filter("");
            searcher.setBackgroundColor(requireContext().getResources().getColor(R.color.gainsboro, requireContext().getTheme()));
        });

        // When search bar is closed, close the list too
        searcher.setOnCloseListener(() -> {
            // Hide the suggestions list
            suggestionsListView.setVisibility(View.GONE);
            searcher.setBackgroundColor(requireContext().getResources().getColor(android.R.color.transparent, requireContext().getTheme()));
            return false;
        });


        searcher.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    adapter.getFilter().filter(""); // Show all trails
                } else {
                    adapter.getFilter().filter(newText); // Filter trails based on input
                }
                suggestionsListView.setVisibility(View.VISIBLE);
                return true;
            }
        });

        // Add ListView item click listener
        suggestionsListView.setOnItemClickListener((parent, view, position, id) -> {
            Trail selectedTrail = (Trail) parent.getItemAtPosition(position);
            searcher.setQuery(selectedTrail.getName(), false);
            searchAddressByName(selectedTrail, true);
            hideKeyboard();
            searcher.setBackgroundColor(requireContext().getResources().getColor(android.R.color.transparent, requireContext().getTheme()));
            suggestionsListView.setVisibility(View.GONE);
        });

        // Set up the map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        try {
            assert mapFragment != null;
            mapFragment.getMapAsync(this);
        } catch (Exception e) {
            Log.e("MapFragment", "Error initializing the map: " + e.getMessage(), e);
        }

        mapMessage.setVisibility(View.GONE);
        return rootView;
    }

    private LatLng getLatLngFromPlace(String trail) {
        // Use Geocoder to fetch coordinates for a city name
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(trail, 1);  // Get the first result
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                double latitude = address.getLatitude();
                double longitude = address.getLongitude();
                return new LatLng(latitude, longitude);
            } else {
                Log.e("MapFragment", "Trail not found");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("MapFragment", "Error in geocoding: " + e.getMessage());
        }
        return null;
    }

    private void pinAndMove(String title, String snippet, LatLng latLng, float zoomLevel) {
        if (map != null) {
            addMarker(title, snippet, latLng);
            // Zoom out to the default zoom level
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, defaultZoom), 1000, new GoogleMap.CancelableCallback() {
                @Override
                public void onFinish() {
                    // Move to the target location
                    map.animateCamera(CameraUpdateFactory.newLatLng(latLng), 1000, new GoogleMap.CancelableCallback() {
                        @Override
                        public void onFinish() {
                            // Step 3: Zoom in to the desired zoom level (search zoom)
                            map.animateCamera(CameraUpdateFactory.zoomTo(zoomLevel), 1000, null);
                        }

                        @Override
                        public void onCancel() {
                            // Handle if the animation is canceled
                        }
                    });
                }
                @Override
                public void onCancel() {
                    // Handle if the animation is canceled
                }
            });
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

    // Create an interface for the API
    public interface GeocodingService {
        @GET("maps/api/geocode/json")
        Call<GeocodingResponse> getLocationDetails(
                @Query("address") String address,
                @Query("key") String apiKey
        );
    }

    // Create a response model
    public static class GeocodingResponse {
        @SerializedName("results")
        public List<GeocodeResult> results;

        public  class GeocodeResult {
            @SerializedName("formatted_address")
            public String formattedAddress;

            @SerializedName("geometry")
            public Geometry geometry;
        }

        public  class Geometry {
            @SerializedName("location")
            public Location location;
        }

        public  class Location {
            @SerializedName("lat")
            public double latitude;

            @SerializedName("lng")
            public double longitude;
        }
    }

    // In your MapFragment
    private void searchAddressByName(Trail trail, boolean isTrail) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GeocodingService service = retrofit.create(GeocodingService.class);

        // API Key on a .java file, what could go wrong?
        // Move this to another file if possible
        Call<GeocodingResponse> call = service.getLocationDetails(
                trail.getName() + (isTrail ? "Trail Kelowna, British Columbia, Canada" : ""),
                "AIzaSyB5pETZIWcmksqfY20ZfTP27BKOL7bWFjk"
        );

        call.enqueue(new Callback<GeocodingResponse>() {
            @Override
            public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().results.isEmpty()) {
                    GeocodingResponse.GeocodeResult result = response.body().results.get(0);

                    // Get the formatted address to the actual name
                    // Wondering if this search also auto corrects?
                    String actualNameFound = result.formattedAddress.split(",")[0];

                    // Get coordinates
                    double latitude = result.geometry.location.latitude;
                    double longitude = result.geometry.location.longitude;

                    // Update map with trail location
                    LatLng trailLocation = new LatLng(latitude, longitude);
                    pinAndMove(actualNameFound, trail.toStringShort(), trailLocation, searchZoom);
                } else {
                    Toast.makeText(getContext(), "Trail not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GeocodingResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error searching trail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addMarker(String title, String snippet, LatLng position) {
        if (map != null) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(position)
                    .title(title)
                    .snippet(snippet);

            Marker marker = map.addMarker(markerOptions);

            // Show info window by default
            if (marker != null) {
                marker.showInfoWindow();
            }
        }

        // Usage example:
        // LatLng hikeLocation = new LatLng(49.8801, -119.4436);
        // Kelowna coordinates
        // addMarker("Hike Name", "Difficulty: Easy\nLength: 5km", hikeLocation);
    }

    private void hideKeyboard() {
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager)
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}