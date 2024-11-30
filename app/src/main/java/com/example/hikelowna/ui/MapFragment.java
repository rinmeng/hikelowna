package com.example.hikelowna.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.hikelowna.HikeActivity;
import com.example.hikelowna.R;
import com.example.hikelowna.core.Trail;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    GoogleMap map;
    Button zoomInButton, zoomOutButton, locatedTrailButton;

    Polyline currentPolyline;
    TextView mapMessage;
    ListView suggestionsListView;
    LatLng defaultLocation;
    float defaultZoom, searchZoom;
    Trail currentSelectedTrail;
    List<LatLng> trailPoints;

    Marker currentMarker;

    SearchView searcher;
    List<String> searchResults;

    List<Trail> trails;
    ArrayAdapter<Trail> adapter;

    public MapFragment() {
        // Required empty public constructor
    }

    private void addAndSaveTrail(String name, String difficulty, float length, int estimatedTime, float rating) {
        Trail trail = new Trail(name, difficulty, length, estimatedTime, rating);
        List<LatLng> points = Trail.points(trail);
        if (!points.isEmpty()) {
            trail.setLatLng(points.get(0));
        } else {
            Log.w("MapFragment", "No points found for trail: " + name);
        }
        saveTrailToLocal(trail);
        trails.add(trail);
    }

    // Initialize the list of trails
    private void initializeTrails() {
        trails = new ArrayList<>();
        logSavedTrails();

        addAndSaveTrail("Apex Trail - To Paul's Tomb", "Moderate", 1.21f, 23, 3.6f);
        addAndSaveTrail("Paul's Tomb Trail", "Moderate", 2.33f, 46, 0.0f);
        addAndSaveTrail("Gordon Trail/Camelot Trail", "Easy", 1.13f, 22, 0.0f);
        addAndSaveTrail("Pavilion Trail", "Easy", 0.71f, 13, 0.0f);
        addAndSaveTrail("Apex Trail - East", "Hard", 2.70f, 54, 0.0f);
        addAndSaveTrail("Pine Trail - To Country Club Dr", "Easy", 0.89f, 17, 0.0f);

        // Sort trails alphabetically for better usability
        Collections.sort(trails);

        // Bind the trails to a ListView using an ArrayAdapter
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, trails);
        suggestionsListView.setAdapter(adapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap; // Initialize the Google Map
        defaultLocation = new LatLng(49.8880, -119.4960); // Set the default location (Kelowna)
        defaultZoom = 16.0f; // Set the default zoom level
        searchZoom = 17.0f; // Set the zoom level for search results

        // Center the map on the default location with a lower zoom level
        moveCameraTo(defaultLocation, 12);

        setZoomControls(); // Set up the zoom controls for the map

        // Handle marker click events
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // Move camera to the clicked marker with a zoom level for search results
                moveCameraTo(marker.getPosition(), searchZoom);
                showMarkerPopup(marker, currentSelectedTrail); // Show a popup dialog with marker details
                marker.showInfoWindow(); // Show the marker's info window
                // Show the trail points

                return true; // Consume the event to prevent default behavior
            }
        });
    }

    private void displayTrailPolyline(List<LatLng> points) {
        if (map != null && points != null && !points.isEmpty()) {
            // Remove the existing polyline if it exists
            if (currentPolyline != null) {
                currentPolyline.remove();
            }

            PolylineOptions polylineOptions = new PolylineOptions()
                    .addAll(points)
                    .color(Color.BLUE) // Set the color of the polyline
                    .width(5); // Set the width of the polyline

            // Add the polyline to the map and store the reference
            currentPolyline = map.addPolyline(polylineOptions);
        }
    }

    // Show a popup with details about the selected marker
    private void showMarkerPopup(Marker marker, Trail trail) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View popupView = inflater.inflate(R.layout.marker_popup, null);

        // Get references to the UI elements in the popup
        TextView titleView = popupView.findViewById(R.id.popupTitle);
        TextView descriptionView = popupView.findViewById(R.id.popupDescription);
        Button viewHikeRouteButton = popupView.findViewById(R.id.viewHikeRouteButton);
        Button startHikeButton = popupView.findViewById(R.id.startHikeButton);

        // Set marker information in the popup
        titleView.setText(marker.getTitle());
        descriptionView.setText(marker.getSnippet());

        // Attach the custom view to the dialog
        builder.setView(popupView);
        AlertDialog dialog = builder.create();

        // View hike route button
        viewHikeRouteButton.setOnClickListener(v -> {
            dialog.dismiss();
            try {
                moveCameraTo(Trail.points(currentSelectedTrail).get(Trail.points(currentSelectedTrail).size() / 2), searchZoom - 2);
            } catch (Exception e) {
                Toast.makeText(getContext(), "No trail selected to re-center", Toast.LENGTH_SHORT).show();
            }

        });

        // Hike button action to start the hike
        startHikeButton.setOnClickListener(view -> {
            dialog.dismiss();
            Toast.makeText(getContext(), marker.getTitle() + " hike started!", Toast.LENGTH_SHORT).show();
            moveCameraTo(marker.getPosition(), searchZoom + 1);

            Intent it = new Intent(getContext(), HikeActivity.class);
            it.putExtra("trailName", trail.getName());
            it.putExtra("trailDifficultyStars", trail.getDifficultyStars());
            it.putExtra("trailRatingStars", trail.getRatingStars());
            it.putExtra("trailLength", trail.getLength());
            it.putExtra("trailEstimatedTime", trail.getEstimatedTime());

            startActivity(it);
        });

        // Show the dialog
        dialog.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        // Bind UI components to variables
        zoomInButton = rootView.findViewById(R.id.zoomInButton);
        zoomOutButton = rootView.findViewById(R.id.zoomOutButton);
        searcher = rootView.findViewById(R.id.searcher);
        searchResults = new ArrayList<>();
        suggestionsListView = rootView.findViewById(R.id.suggestionsListView);
        mapMessage = rootView.findViewById(R.id.mapMessage);
        locatedTrailButton = rootView.findViewById(R.id.locatedTrailButton);

        initializeTrails(); // Set up the trail list and adapter

        suggestionsListView.setVisibility(View.GONE); // Hide suggestions list by default

        searcher.setGravity(Gravity.CENTER); // Center-align the text in the search bar

        // Set up event handlers for the SearchView
        searcher.setOnSearchClickListener(v -> {
            // Show the suggestions list when the search bar is expanded
            suggestionsListView.setVisibility(View.VISIBLE);
            adapter.getFilter().filter(""); // Reset the filter to show all trails
            searcher.setBackgroundColor(requireContext().getResources().getColor(R.color.gainsboro, requireContext().getTheme()));
        });

        searcher.setOnCloseListener(() -> {
            // Hide the suggestions list when the search bar is closed
            suggestionsListView.setVisibility(View.GONE);
            searcher.setBackgroundColor(requireContext().getResources().getColor(android.R.color.transparent, requireContext().getTheme()));
            return false; // Let the system handle additional close behavior
        });

        // Handle changes in the SearchView text
        searcher.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true; // Search is already handled by text change
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    // Show all trails if no search query is entered
                    adapter.getFilter().filter("");
                    suggestionsListView.setVisibility(View.VISIBLE);
                } else {
                    // Filter trails based on the query
                    adapter.getFilter().filter(newText, count -> {
                        // Show/hide suggestions based on the number of matches
                        suggestionsListView.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
                    });
                }
                return true;
            }
        });

        suggestionsListView.setOnItemClickListener((parent, view, position, id) -> {
            Trail selectedTrail = (Trail) parent.getItemAtPosition(position);
            searcher.setQuery(selectedTrail.getName(), false);
            searchAddressByName(selectedTrail, true);
            suggestionsListView.post(() -> suggestionsListView.setVisibility(View.GONE));
            searcher.clearFocus();

            // Retrieve and display trail points
            trailPoints = Trail.points(selectedTrail);
            if (!trailPoints.isEmpty()) {
                displayTrailPolyline(trailPoints);
            } else {
                Log.d("MapFragment", "No trail points found for " + selectedTrail.getName());
            }
            hideKeyboard();
            LatLng trailLatLng = selectedTrail.getLatLng();
            if (trailLatLng != null) {
                Marker marker = map.addMarker(new MarkerOptions()
                        .position(trailLatLng)
                        .title(selectedTrail.getName())
                        .snippet(selectedTrail.toStringShort()));
                if (marker != null) {
                    showMarkerPopup(marker, selectedTrail);
                }
            }
        });

        // Set up the map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        try {
            assert mapFragment != null; // Ensure the map fragment is not null
            mapFragment.getMapAsync(this); // Initialize the map asynchronously
        } catch (Exception e) {
            Log.e("MapFragment", "Error initializing map: " + e.getMessage());
        }

        mapMessage.setVisibility(View.GONE);
        return rootView; // Return the created view
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Ensure the suggestionsListView is hidden whenever the view is created
        suggestionsListView.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Ensure the suggestionsListView is hidden whenever the fragment resumes
        if (suggestionsListView != null) {
            suggestionsListView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        // Clear the SearchView query
        if (searcher != null) {
            searcher.setQuery("", false);  // Empty query, don't submit
            searcher.clearFocus();  // Remove focus from the search view
            searcher.onActionViewCollapsed(); // Collapse the search bar
        }

        // Hide the suggestions list
        if (suggestionsListView != null) {
            suggestionsListView.setVisibility(View.GONE);
        }

        // Reset the adapter filter
        if (adapter != null) {
            adapter.getFilter().filter("");
        }
    }


    // Hide the keyboard programmatically
    private void hideKeyboard() {
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // Set up the zoom in and zoom out button functionalities
    private void setZoomControls() {
        zoomInButton.setOnClickListener(v -> map.animateCamera(CameraUpdateFactory.zoomIn()));
        zoomOutButton.setOnClickListener(v -> map.animateCamera(CameraUpdateFactory.zoomOut()));
        locatedTrailButton.setOnClickListener(view -> {
            try {
                moveCameraTo(currentSelectedTrail.getLatLng(), searchZoom + 1);
                if (currentMarker != null) {
                    currentMarker.showInfoWindow();
                }
            } catch (Exception e) {
                Toast.makeText(getContext(), "No trail selected to re-center", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Move the camera to a specific location with the given zoom level
    private void moveCameraTo(LatLng location, float zoomLevel) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel));
    }

    // Perform a geocoding search for a given address or trail
    private void searchAddressByName(Trail trail, boolean addMarker) {
        // First, check if the trail exists locally
        Log.d("APISaver", "Checking to see if we can save API calls...");
        Trail savedTrail = findTrailByName(trail.getName());

        if (savedTrail != null && savedTrail.getLatLng() != null) {

            Log.d("APISaver", "Trail found. We saved an API call!");
            // Trail found locally, use its latLng
            LatLng latLng = savedTrail.getLatLng();

            if (addMarker) {
                addMarkerToMap(savedTrail, latLng);
            }

            // Move the camera to the saved location
            moveCameraTo(latLng, searchZoom);

            Log.d("UserSave", "Loaded trail from local storage: " + savedTrail.getName());
        }
//        } else {
//            // Trail not found locally, perform geocoding
//            Log.d("APISaver", "Trail not found locally, performing api call...");
//            Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
//            try {
//                // Perform the geocoding query to get matching addresses
//                List<Address> addresses = geocoder.getFromLocationName(trail.getName() + " Kelowna, BC", 1);
//                if (!addresses.isEmpty()) {
//                    // Get the location of the first address
//                    Address address = addresses.get(0);
//                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
//
//                    // Add a marker if required
//                    if (addMarker) {
//                        addMarkerToMap(trail, latLng);
//                    }
//
//                    // Set the trail's latLng and save it locally
//                    trail.setLatLng(latLng);
//                    saveTrailToLocal(trail);
//
//                    // Move the camera to the found location
//                    moveCameraTo(latLng, searchZoom);
//                } else {
//                    // No matching addresses found, show an error toast
//                    Toast.makeText(requireContext(), "Location not found for: " + trail.getName(), Toast.LENGTH_SHORT).show();
//                }
//            } catch (IOException e) {
//                Log.e("MapFragment", "Geocoding error: " + e.getMessage());
//            }
//        }
        currentSelectedTrail = trail;
        Log.d("currentSelectedTrail", "Selected trail: " + trail.getName());
    }

    /**
     * Adds a marker to the map for the given trail at the specified location.
     *
     * @param trail  The Trail object.
     * @param latLng The location where the marker should be placed.
     */
    private void addMarkerToMap(Trail trail, LatLng latLng) {
        requireActivity().runOnUiThread(() -> {
            currentMarker = map.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(trail.getName())
                    .snippet(trail.toStringShort()));
            if (currentMarker != null) {
                currentMarker.showInfoWindow();
            }
        });
    }

    /**
     * Saves a Trail object locally using SharedPreferences.
     * If a trail with the same name exists, it replaces the existing trail.
     *
     * @param trail The Trail object to save.
     */
    private void saveTrailToLocal(Trail trail) {
        try {
            Gson gson = new Gson();

            SharedPreferences sharedPreferences = requireContext().getSharedPreferences("local_trails", Context.MODE_PRIVATE);
            String json = sharedPreferences.getString("trails", null);

            Type type = new TypeToken<List<Trail>>() {
            }.getType();
            List<Trail> trailList;

            if (json != null) {
                trailList = gson.fromJson(json, type);
                Log.d("SaveTrail", "Loaded existing trails: " + trailList.size());
            } else {
                trailList = new ArrayList<>();
                Log.d("SaveTrail", "Initialized new trail list.");
            }

            boolean trailExists = false;

            for (int i = 0; i < trailList.size(); i++) {
                if (trailList.get(i).getName().equalsIgnoreCase(trail.getName())) {
                    trailList.set(i, trail);
                    trailExists = true;
                    Log.d("SaveTrail", "Replaced existing trail: " + trail.getName());
                    break;
                }
            }

            if (!trailExists) {
                trailList.add(trail);
                Log.d("SaveTrail", "Added new trail: " + trail.getName());
            }

            String updatedJson = gson.toJson(trailList);
            boolean isSaved = sharedPreferences.edit().putString("trails", updatedJson).commit(); // Using commit()
            Log.d("SaveTrail", "SharedPreferences commit successful: " + isSaved);


        } catch (Exception e) {
            Log.e("SaveTrail", "Error saving Trail", e);
        }
    }

    private List<Trail> getSavedTrails() {
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("local_trails", Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("trails", null);
        Type type = new TypeToken<List<Trail>>() {
        }.getType();
        return json != null ? gson.fromJson(json, type) : new ArrayList<>();
    }

    private Trail findTrailByName(String name) {
        List<Trail> savedTrails = getSavedTrails();
        for (Trail t : savedTrails) {
            if (t.getName().equalsIgnoreCase(name)) {
                return t;
            }
        }
        return null;
    }

    private void logSavedTrails() {
        List<Trail> savedTrails = getSavedTrails();
        for (Trail trail : savedTrails) {
            Log.d("SavedTrails", "Trail: " + trail.getName() + ", Location: " + trail.getLatLng());
        }
    }

}