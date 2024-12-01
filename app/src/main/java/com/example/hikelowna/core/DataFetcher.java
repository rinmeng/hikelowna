package com.example.hikelowna.core;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DataFetcher {


    public void refetchAllData(User user) {
        try {
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference feedsRef = db.getReference("feeds");
            DatabaseReference userRef = db.getReference("users");

            // Retrieve the user from the database
            userRef.orderByChild("username").equalTo(user.getUsername()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot userSnapshot) {
                    if (userSnapshot.exists()) {
                        for (DataSnapshot snapshot : userSnapshot.getChildren()) {
                            User userFromDB = snapshot.getValue(User.class);
                            if (userFromDB != null) {
                                // Now retrieve the feeds and update the poster if usernames match
                                feedsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot feedSnapshot) {
                                        for (DataSnapshot feedChild : feedSnapshot.getChildren()) {
                                            Feed feed = feedChild.getValue(Feed.class);
                                            if (feed != null && feed.getPoster().getUsername().equals(userFromDB.getUsername())) {
                                                feed.setPoster(userFromDB);
                                                try {
                                                    feedsRef.child(feedChild.getKey()).setValue(feed);
                                                } catch (Exception e) {
                                                    Log.e("Error", "Error updating feed: " + e.getMessage());
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.e("Error", "Error fetching feeds: " + databaseError.getMessage());
                                    }
                                });
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("Error", "Error fetching user: " + databaseError.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("Error", "Error in refetchAllData: " + e.getMessage());
        }
    }
}
