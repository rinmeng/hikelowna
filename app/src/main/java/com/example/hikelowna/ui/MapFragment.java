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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.hikelowna.HikeActivity;
import com.example.hikelowna.R;
import com.example.hikelowna.core.Feed;
import com.example.hikelowna.core.Hike;
import com.example.hikelowna.core.Trail;
import com.example.hikelowna.core.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    Button zoomInButton, zoomOutButton, locatedTrailButton, hikeInfoButton;


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
    List<Integer> ratings;

    List<Trail> trails;
    ArrayAdapter<Trail> adapter;
    float currentTrailRating = 0.0f;

    public MapFragment() {
        // Required empty public constructor
    }

    private void fetchTrailRating(String trailName, TrailRatingCallback callback) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference feedsRef = db.getReference("feeds");
        ratings = new ArrayList<>();

        feedsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    Feed feed = snapshot.getValue(Feed.class);
                    if (feed != null && feed.getHike().getTrailName().equals(trailName)) {
                        ratings.add(feed.getHike().getTrailRating());
                    }
                }
                callback.onRatingFetched(ratings);
            }
        });
    }

    private float calculateAverageRating(List<Integer> rtngs) {
        if (rtngs.isEmpty()) {
            return 0.0f; // Default rating if no ratings exist
        }
        float sum = 0;
        for (Integer rating : rtngs) {
            sum += rating;
        }
        return sum / rtngs.size();
    }

    private void addAndSaveTrail(String name, String difficulty, float length, int estimatedTime, float rating) {

        Trail trail = new Trail(name, difficulty, length, estimatedTime, calculateAverageRating(ratings));
        List<LatLng> points = Trail.points(trail);
        if (!points.isEmpty()) {
            trail.setLatLng(points.get(0));
        } else {
            Log.w("MapFragment", "No points found for trail: " + name);
        }
//        saveTrailToLocal(trail);
        trails.add(trail);
    }

    private void initializeTrails() {
        trails = new ArrayList<>();
        logSavedTrails();

        initializeTrail("Apex Trail - To Paul's Tomb", "Moderate", 1.21f, 23);
        initializeTrail("Paul's Tomb Trail", "Moderate", 2.33f, 46);
        initializeTrail("Gordon Trail/Camelot Trail", "Easy", 1.13f, 22);
        initializeTrail("Pavilion Trail", "Easy", 0.71f, 13);
        initializeTrail("Apex Trail - East", "Hard", 2.70f, 54);
        initializeTrail("Pine Trail - To Country Club Dr", "Easy", 0.89f, 17);

        // Sort trails alphabetically for better usability
        Collections.sort(trails);

        // Bind the trails to a ListView using an ArrayAdapter
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, trails);
        suggestionsListView.setAdapter(adapter);
    }

    private void initializeTrail(String trailName, String difficulty, float length, int estimatedTime) {
        fetchTrailRating(trailName, ratings -> {
            addAndSaveTrail(trailName, difficulty, length, estimatedTime, calculateAverageRating(ratings));
            Log.d("Ratings", "Ratings for " + trailName + ": " + ratings);
        });
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
        Button cancelButton = popupView.findViewById(R.id.cancelButton);
        Button reviewsButton = popupView.findViewById(R.id.reviewsButton);

        // Set marker information in the popup
        titleView.setText(marker.getTitle());
        descriptionView.setText(marker.getSnippet());

        // Attach the custom view to the dialog
        builder.setView(popupView);
        AlertDialog dialog = builder.create();

        // Set up event handlers for the hike info button
        reviewsButton.setOnClickListener(v -> {
            // Inflate the reviews container layout
            View reviewsContainer = inflater.inflate(R.layout.reviews_container, null);
            Button btnBack = reviewsContainer.findViewById(R.id.btnBack);
            TextView reviewMessage = reviewsContainer.findViewById(R.id.reviewMessage);

            // Fetch and populate the reviews
            fetchFeedsFromDatabase(reviewsContainer);


            // Create and show the AlertDialog
            AlertDialog.Builder reviewsBuilder = new AlertDialog.Builder(requireContext());
            AlertDialog reviewsDialog = reviewsBuilder.setView(reviewsContainer).create();
            reviewsDialog.show();

            // Set the back button click listener
            btnBack.setOnClickListener(view -> reviewsDialog.dismiss());
        });

        // View hike route button
        viewHikeRouteButton.setOnClickListener(v -> {
            dialog.dismiss();
            try {
                moveCameraTo(Trail.points(currentSelectedTrail).get(Trail.points(currentSelectedTrail).size() / 2), searchZoom - 2);
            } catch (Exception e) {
                Toast.makeText(getContext(), "No trail selected to re-center", Toast.LENGTH_SHORT).show();
            }

        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());

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
        hikeInfoButton = rootView.findViewById(R.id.hikeInfoButton);


        initializeTrails();
        suggestionsListView.setVisibility(View.GONE);
        searcher.setGravity(Gravity.CENTER);
        hikeInfoButton.setVisibility(View.GONE);


        // Set up event handlers for the SearchView
        searcher.setOnSearchClickListener(v -> {
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


        hikeInfoButton.setOnClickListener(view -> {
            // open the hike popup
            try {
                showMarkerPopup(currentMarker, currentSelectedTrail);
            } catch (Exception e) {
                Toast.makeText(getContext(), "No trail selected to view info", Toast.LENGTH_SHORT).show();
            }

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
                    moveCameraTo(trailLatLng, searchZoom);
                    marker.showInfoWindow();
                    hikeInfoButton.setVisibility(View.VISIBLE);
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

    private void fetchFeedsFromDatabase(View rootView) {
        List<Feed> feeds = new ArrayList<>();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference feedsRef = db.getReference("feeds");
        feedsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    Feed feed = snapshot.getValue(Feed.class);
                    if (feed != null) {
                        feeds.add(feed);
                        Log.d("Feed", feed.toString());
                    }
                }
                populateFeeds(rootView, feeds);
            }
        });
    }

    private void populateFeeds(View rootView, List<Feed> feeds) {
        LinearLayout feedContainer = rootView.findViewById(R.id.reviewContainer);

        boolean isFound = false;

        for (Feed feed : feeds) {
            if (feed.getHike().getTrailName().equals(currentSelectedTrail.getName())) {
                isFound = true;
                View feedView = getLayoutInflater().inflate(R.layout.feed_item, null);
                // Set layout parameters with margin
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );


                layoutParams.setMargins(30, 20, 30, 20);

                feedView.setLayoutParams(layoutParams);

                TextView usernameText = feedView.findViewById(R.id.usernameText);
                TextView userDetailsText = feedView.findViewById(R.id.userDetailsText);
                TextView trailName = feedView.findViewById(R.id.trailName);
                TextView userTrailInfoText = feedView.findViewById(R.id.userTrailInfoText);
                TextView trailDetails = feedView.findViewById(R.id.trailDetails);
                TextView titleText = feedView.findViewById(R.id.titleText);
                TextView descriptionText = feedView.findViewById(R.id.descriptionText);

                Hike feedHike = feed.getHike();
                User feedPoster = feed.getPoster();
                String username = "@" + feedPoster.getUsername();
                usernameText.setText(username);
                String userDetail = feedPoster.getLocation() + " | " + feedPoster.getHikingHistory().size() + " hikes";
                userDetailsText.setText(userDetail);
                String userTrailInfo = "★ " + feedHike.getTrailRating() + " | " + "Time Elapsed: " + feedHike.getElapsedTime();
                userTrailInfoText.setText(userTrailInfo);
                titleText.setText(feedHike.getHikeName());
                descriptionText.setText(feedHike.getHikeDescription());
                trailName.setText(feedHike.getTrailName());
                String trailDetailsText = "★ " + getAverageRating(feeds) + " | " + feedHike.getTrailDifficultyStars()
                        + " | " + feedHike.getTrailLength() + "km" + " | " + feedHike.getTrailEstimatedTime() + "mins";
                trailDetails.setText(trailDetailsText);

                feedContainer.addView(feedView);
            }
        }

        if (isFound) {
            TextView reviewMessage = rootView.findViewById(R.id.reviewMessage);
            reviewMessage.setVisibility(View.GONE);
        }
    }

    private float getAverageRating(List<Feed> feeds) {
        float totalRating = 0;
        for (Feed feed : feeds) {
            totalRating += feed.getHike().getTrailRating();
        }
        return totalRating / feeds.size();
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


    //    private void saveTrailToLocal(Trail trail) {
//        try {
//            Gson gson = new Gson();
//
//            SharedPreferences sharedPreferences = requireContext().getSharedPreferences("local_trails", Context.MODE_PRIVATE);
//            String json = sharedPreferences.getString("trails", null);
//
//            Type type = new TypeToken<List<Trail>>() {
//            }.getType();
//            List<Trail> trailList;
//
//            if (json != null) {
//                trailList = gson.fromJson(json, type);
//                Log.d("SaveTrail", "Loaded existing trails: " + trailList.size());
//            } else {
//                trailList = new ArrayList<>();
//                Log.d("SaveTrail", "Initialized new trail list.");
//            }
//
//            boolean trailExists = false;
//
//            for (int i = 0; i < trailList.size(); i++) {
//                if (trailList.get(i).getName().equalsIgnoreCase(trail.getName())) {
//                    trailList.set(i, trail);
//                    trailExists = true;
//                    Log.d("SaveTrail", "Replaced existing trail: " + trail.getName());
//                    break;
//                }
//            }
//
//            if (!trailExists) {
//                trailList.add(trail);
//                Log.d("SaveTrail", "Added new trail: " + trail.getName());
//            }
//
//            String updatedJson = gson.toJson(trailList);
//            boolean isSaved = sharedPreferences.edit().putString("trails", updatedJson).commit(); // Using commit()
//            Log.d("SaveTrail", "SharedPreferences commit successful: " + isSaved);
//
//
//        } catch (Exception e) {
//            Log.e("SaveTrail", "Error saving Trail", e);
//        }
//    }
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

    public interface TrailRatingCallback {
        void onRatingFetched(List<Integer> ratings);
    }

}