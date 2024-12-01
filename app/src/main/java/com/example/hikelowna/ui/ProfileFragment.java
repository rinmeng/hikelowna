package com.example.hikelowna.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.hikelowna.MainActivity;
import com.example.hikelowna.R;
import com.example.hikelowna.core.DataFetcher;
import com.example.hikelowna.core.Feed;
import com.example.hikelowna.core.Hike;
import com.example.hikelowna.core.HikeData;
import com.example.hikelowna.core.TrailDifficulty;
import com.example.hikelowna.core.User;
import com.example.hikelowna.core.UserManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    TextView username;
    EditText displayName, location;
    Button editButton, logoutButton;
    Spinner preferredDifficulty;

    LinearLayout hikingHistoryLayout;

    boolean isEditing = true;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        username = rootView.findViewById(R.id.usernameText);
        displayName = rootView.findViewById(R.id.userDisplayNameText);
        location = rootView.findViewById(R.id.locationText);

        editButton = rootView.findViewById(R.id.editButton);
        logoutButton = rootView.findViewById(R.id.logoutButton);
        hikingHistoryLayout = rootView.findViewById(R.id.hikingHistoryLayout);

        preferredDifficulty = rootView.findViewById(R.id.preferredDifficultyText);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.difficulty, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        preferredDifficulty.setAdapter(adapter);

        UserManager userManager = UserManager.getInstance();
        User user = userManager.getCurrentUser();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = db.getReference("users");

        // Set the text fields to the user's information
        String usernameText = "@" + user.getUsername();
        username.setText(usernameText);
        displayName.setText(user.getDisplayName());
        location.setText(user.getLocation());

        int position = adapter.getPosition(user.getPreferredDifficultyLevel());
        preferredDifficulty.setSelection(position);

        preferredDifficulty.setEnabled(false);


        // set the user's hiking history
        List<Hike> userHikingHistory = getUserHikingHistory(user);

        // populate the user's hiking history
        for (Hike hike : userHikingHistory) {
            View hikeView = inflater.inflate(R.layout.feed_item, hikingHistoryLayout, false);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(30, 20, 30, 20);
            hikeView.setLayoutParams(layoutParams);


            TextView usernameTextHike = hikeView.findViewById(R.id.usernameText);
            TextView userDetailsText = hikeView.findViewById(R.id.userDetailsText);
            TextView trailName = hikeView.findViewById(R.id.trailName);
            TextView userTrailInfoText = hikeView.findViewById(R.id.userTrailInfoText);
            TextView trailDetails = hikeView.findViewById(R.id.trailDetails);
            TextView titleText = hikeView.findViewById(R.id.titleText);
            TextView descriptionText = hikeView.findViewById(R.id.descriptionText);

            Button shareHikeButton = hikeView.findViewById(R.id.shareHikeButton);


            usernameTextHike.setVisibility(View.GONE);
            userDetailsText.setVisibility(View.GONE);
            String userTrailInfo = "★ " + hike.getTrailRating() + " | " + "Time Elapsed: " + hike.getElapsedTime();
            userTrailInfoText.setText(userTrailInfo);
            titleText.setText(hike.getHikeName());
            descriptionText.setText(hike.getHikeDescription());
            trailName.setText(hike.getTrailName());
            getAverageRatingForTrail(hike.getTrailName(), averageRating -> {
                String trailDetailsText = String.format(Locale.getDefault(), "★ %.2f | %s | %.1f km | %s mins",
                        averageRating,
                        TrailDifficulty.toStars(hike.getTrailDifficultyStars()),
                        hike.getTrailLength(),
                        hike.getTrailEstimatedTime());
                trailDetails.setText(trailDetailsText);
            });

            shareHikeButton.setOnClickListener(view -> {
                HikeData hd = new HikeData();
                String hikeCode = hd.createHikeCode(hike);
                if (hikeCode != null) {
                    HikeData.copyShareCodeToClipboard(getContext(), hikeCode);
                    Toast.makeText(getContext(), "Share code copied!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Failed to generate share code", Toast.LENGTH_SHORT).show();
                }
            });

            hikingHistoryLayout.addView(hikeView);
        }


        editButton.setOnClickListener(view -> {
            if (isEditing) {
                // Enable the text fields
                String editButtonText = "Save";
                editButton.setText(editButtonText);
                displayName.setEnabled(true);
                location.setEnabled(true);
                preferredDifficulty.setEnabled(true);
                isEditing = false;
            } else {
                // Save the changes
                String editButtonText = "Edit";
                editButton.setText(editButtonText);

                // Disable the text fields
                displayName.setEnabled(false);
                location.setEnabled(false);
                preferredDifficulty.setEnabled(false);

                // Update the user's information
                user.setDisplayName(displayName.getText().toString());
                user.setLocation(location.getText().toString());
                user.setPreferredDifficultyLevel(preferredDifficulty.getSelectedItem().toString());
                userManager.updateUser(user);
                try {
                    usersRef.child(user.getUsername()).setValue(user);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Failed to update profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getContext(), "Profile updated", Toast.LENGTH_SHORT).show();
                isEditing = true;

                DataFetcher df = new DataFetcher();
                df.refetchAllData(user);
            }
        });

        logoutButton.setOnClickListener(view -> {
            userManager.logout();
            Toast.makeText(getContext(), "Logged out", Toast.LENGTH_SHORT).show();

            // Create an intent to start MainActivity
            Intent it = new Intent(getActivity(), MainActivity.class);
            // Clear the back stack to prevent returning to previous screens
            it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(it);

            // If the current activity should close
            if (getActivity() != null) {
                getActivity().finish();
            }
        });


        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        DataFetcher df = new DataFetcher();
        UserManager userManager = UserManager.getInstance();
        User user = userManager.getCurrentUser();
        df.refetchAllData(user);
    }


    private void getAverageRatingForTrail(String trailName, RatingCallback callback) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference feedsRef = db.getReference("feeds");

        feedsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                float totalRating = 0;
                int ratingCount = 0;
                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    Feed feed = snapshot.getValue(Feed.class);
                    if (feed != null && feed.getHike().getTrailName().equals(trailName)) {
                        totalRating += feed.getHike().getTrailRating();
                        ratingCount++;
                    }
                }
                float averageRating = ratingCount > 0 ? totalRating / ratingCount : 0;
                callback.onRatingCalculated(averageRating);
            } else {
                Log.e("HomeFragment", "Error fetching feeds", task.getException());
            }
        });
    }

    private List<Hike> getUserHikingHistory(User user) {
        // Get the user's hiking history
        List<Hike> hikingHistory = user.getHikingHistory();
        // Create a new list to store the user's hiking history
        List<Hike> userHikingHistory = new ArrayList<>();
        // Loop through the user's hiking history
        for (Hike hike : hikingHistory) {
            // Check if the hike is not null
            if (hike != null) {
                // Add the hike to the user's hiking history
                userHikingHistory.add(hike);
            }
        }
        // Return the user's hiking history
        return userHikingHistory;
    }

    interface RatingCallback {
        void onRatingCalculated(float averageRating);
    }

}