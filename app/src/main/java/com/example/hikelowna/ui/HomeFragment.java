package com.example.hikelowna.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.hikelowna.R;
import com.example.hikelowna.core.Feed;
import com.example.hikelowna.core.Hike;
import com.example.hikelowna.core.HikeData;
import com.example.hikelowna.core.TrailDifficulty;
import com.example.hikelowna.core.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    Button importSharedHike;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        fetchFeedsFromDatabase(rootView);

        importSharedHike = rootView.findViewById(R.id.importSharedHike);


        importSharedHike.setOnClickListener(v -> {
            // make a dialog to import shared hike
            // the layout of the dialog is named import_hike_window

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View dialogView = getLayoutInflater().inflate(R.layout.import_hike_window, null);
            builder.setView(dialogView);
            AlertDialog dialog = builder.create();

            EditText codeInput = dialogView.findViewById(R.id.codeInput);
            Button cancelButton = dialogView.findViewById(R.id.cancelButton);
            Button importButton = dialogView.findViewById(R.id.importButton);

            cancelButton.setOnClickListener(v1 -> dialog.dismiss());

            importButton.setOnClickListener(v2 -> {
                // get the hike name and hike description from the dialog
                String code = codeInput.getText().toString();
                // check if the code is valid
                if (code.isEmpty()) {
                    Toast.makeText(getContext(), "Please enter a shared hike code!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    showHikeDetailsDialog(code);
                }

                // close the dialog
                dialog.dismiss();
            });
            dialog.show();
        });
        return rootView;
    }

    private void showHikeDetailsDialog(String code) {
        try {
            HikeData hd = new HikeData();
            Hike sharedHike = hd.translateHikeCodeToHike(code);
            showHikeDialog(sharedHike, null, null);
        } catch (Exception e) {
            showHikeDialog(null, "Invalid Hike Code!", "Please enter a valid hike code.");
        }
    }

    private void showHikeDialog(Hike sharedHike, String errorMessage, String errorDescription) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        View sharedHikeView = getLayoutInflater().inflate(R.layout.feed_item, null);
        builder1.setView(sharedHikeView);
        AlertDialog dialog1 = builder1.create();

        TextView usernameTextHike = sharedHikeView.findViewById(R.id.username);
        TextView userDetailsText = sharedHikeView.findViewById(R.id.userDetailsText);
        TextView trailName = sharedHikeView.findViewById(R.id.trailName);
        TextView userTrailInfoText = sharedHikeView.findViewById(R.id.userTrailInfoText);
        TextView trailDetails = sharedHikeView.findViewById(R.id.trailDetails);
        TextView titleText = sharedHikeView.findViewById(R.id.titleText);
        TextView descriptionText = sharedHikeView.findViewById(R.id.descriptionText);
        Button shareHikeButton = sharedHikeView.findViewById(R.id.shareHikeButton);

        if (sharedHike != null) {
            usernameTextHike.setVisibility(View.GONE);
            userDetailsText.setVisibility(View.GONE);
            String userTrailInfo = "★ " + sharedHike.getTrailRating() + " | " + "Time Elapsed: " + sharedHike.getElapsedTime();
            userTrailInfoText.setText(userTrailInfo);
            titleText.setText(sharedHike.getHikeName());
            descriptionText.setText(sharedHike.getHikeDescription());
            trailName.setText(sharedHike.getTrailName());
            getAverageRatingForTrail(sharedHike.getTrailName(), averageRating -> {
                String trailDetailsText = String.format(Locale.getDefault(), "★ %.2f | %s | %.1f km | %s mins",
                        averageRating,
                        TrailDifficulty.toStars(sharedHike.getTrailDifficultyStars()),
                        sharedHike.getTrailLength(),
                        sharedHike.getTrailEstimatedTime());
                trailDetails.setText(trailDetailsText);
            });
        } else {
            usernameTextHike.setText(errorMessage);
            userDetailsText.setText(errorDescription);
            trailName.setVisibility(View.GONE);
            userTrailInfoText.setVisibility(View.GONE);
            trailDetails.setVisibility(View.GONE);
            titleText.setVisibility(View.GONE);
            descriptionText.setVisibility(View.GONE);
        }

        shareHikeButton.setText("Back");
        shareHikeButton.setTextSize(15);
        shareHikeButton.setBackgroundResource(0);
        shareHikeButton.setOnClickListener(v3 -> dialog1.dismiss());

        dialog1.show();
    }

    private void fetchFeedsFromDatabase(View rootView) {
        List<Feed> feeds = new ArrayList<>();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference feedsRef = db.getReference("feeds");
        feedsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                float totalRating = 0;
                int ratingCount = 0;
                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                    Feed feed = snapshot.getValue(Feed.class);
                    if (feed != null) {
                        feeds.add(feed);
                        totalRating += feed.getHike().getTrailRating();
                        ratingCount++;
                        Log.d("Feed", feed.toString());
                    }
                }


                populateFeeds(rootView, feeds);
            }
        });
    }

    private void populateFeeds(View rootView, List<Feed> feeds) {
        LinearLayout feedContainer = rootView.findViewById(R.id.feedContainer);
        int counter = 0;
        if (feeds.size() != 0) {
            TextView feedMessage = rootView.findViewById(R.id.feedMessage);
            feedMessage.setVisibility(View.GONE);
        }

        for (Feed feed : feeds) {
            View feedView = getLayoutInflater().inflate(R.layout.feed_item, null);
            // Set layout parameters with margin
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            Button feedShareHikeButton = feedView.findViewById(R.id.shareHikeButton);

            if (feeds.size() == counter + 1) {
                layoutParams.setMargins(30, 20, 30, 200);
            } else {
                layoutParams.setMargins(30, 20, 30, 20);
                counter++;
            }
            feedView.setLayoutParams(layoutParams);

            TextView usernameText = feedView.findViewById(R.id.username);
            TextView userDetailsText = feedView.findViewById(R.id.userDetailsText);
            TextView trailName = feedView.findViewById(R.id.trailName);
            TextView userTrailInfoText = feedView.findViewById(R.id.userTrailInfoText);
            TextView trailDetails = feedView.findViewById(R.id.trailDetails);
            TextView titleText = feedView.findViewById(R.id.titleText);
            TextView descriptionText = feedView.findViewById(R.id.descriptionText);

            Hike feedHike = feed.getHike();
            User feedPoster = feed.getPoster();
            String username = feedPoster.getDisplayName();
            usernameText.setText(username);
            String userDetail = feedPoster.getLocation() + " | " + feedPoster.getHikingHistory().size() + " hikes";
            userDetailsText.setText(userDetail);
            String userTrailInfo = "★ " + feedHike.getTrailRating() + " | " + "Time Elapsed: " + feedHike.getElapsedTime();
            userTrailInfoText.setText(userTrailInfo);
            titleText.setText(feedHike.getHikeName());
            descriptionText.setText(feedHike.getHikeDescription());
            trailName.setText(feedHike.getTrailName());
            getAverageRatingForTrail(feedHike.getTrailName(), averageRating -> {
                String trailDetailsText = String.format(Locale.getDefault(), "★ %.2f | %s | %.1f km | %s mins",
                        averageRating,
                        TrailDifficulty.toStars(feedHike.getTrailDifficultyStars()),
                        feedHike.getTrailLength(),
                        feedHike.getTrailEstimatedTime());
                trailDetails.setText(trailDetailsText);
            });

            feedShareHikeButton.setOnClickListener(v -> {
                HikeData hd = new HikeData();
                String sharedHikeCode = hd.createHikeCode(feed.getHike());
                HikeData.copyShareCodeToClipboard(getContext(), sharedHikeCode);
                Toast.makeText(getContext(), "Hike code copied to clipboard!", Toast.LENGTH_SHORT).show();
            });


            feedContainer.addView(feedView);
        }
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

    interface RatingCallback {
        void onRatingCalculated(float averageRating);
    }
}