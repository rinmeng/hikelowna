package com.example.hikelowna.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.hikelowna.R;
import com.example.hikelowna.core.Feed;
import com.example.hikelowna.core.Hike;
import com.example.hikelowna.core.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

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
        return rootView;
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

            if (feeds.size() == counter + 1) {
                layoutParams.setMargins(30, 20, 30, 200);
            } else {
                layoutParams.setMargins(30, 20, 30, 20);
                counter++;
            }
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

    private float getAverageRating(List<Feed> feeds) {
        float totalRating = 0;
        for (Feed feed : feeds) {
            totalRating += feed.getHike().getTrailRating();
        }
        return totalRating / feeds.size();
    }
}