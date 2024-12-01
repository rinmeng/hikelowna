package com.example.hikelowna.core;

// in Hike.java
//  private String hikeDescription;
//    private String hikeName;
//    private String elapsedTime;
//    private String trailName;
//    private String trailDifficultyStars;
//    private int trailRating;
//    private float trailLength;
//    private float trailEstimatedTime;

// in Feed.java
//  private Hike hike;
//  private User poster;

// in User.java
//      private final List<Hike> hikingHistory;
//    private String username;
//    private String displayName;
//    private String passwordHash;
//    private String location;
//    private String preferredDifficultyLevel;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import java.security.MessageDigest;

public class HikeData {
    

    public static void copyShareCodeToClipboard(Context context, String shareCode) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);

        if (clipboard != null) {
            ClipData clip = ClipData.newPlainText("Hike Share Code", shareCode);
            clipboard.setPrimaryClip(clip);
        }
    }

    // Combines all string fields of a Hike object into a single string
    public String translateHikeCode(Hike hike) {
        return hike.getHikeDescription() + "|"
                + hike.getHikeName() + "|"
                + hike.getElapsedTime() + "|"
                + hike.getTrailName() + "|"
                + hike.getTrailDifficultyStars() + "|"
                + hike.getTrailRating() + "|"
                + hike.getTrailLength() + "|"
                + hike.getTrailEstimatedTime();
    }

    // Combines all string fields of a Feed object (Hike + User) into a single string
    public String translateHikeCode(Feed feed) {
        Hike hike = feed.getHike();
        User poster = feed.getPoster();

        return translateHikeCode(hike) + "|"
                + poster.getUsername() + "|"
                + poster.getDisplayName() + "|"
                + poster.getLocation() + "|"
                + poster.getPreferredDifficultyLevel();
    }

    // Generates a unique code by combining fields and applying a hash for a Hike object
    public String createHikeCode(Hike hike) {
        String combinedData = translateHikeCode(hike);
        return generateHash(combinedData);
    }

    // Generates a unique code by combining fields and applying a hash for a Feed object
    public String createHikeCode(Feed feed) {
        String combinedData = translateHikeCode(feed);
        return generateHash(combinedData);
    }

    // Helper method to generate a hash of a string
    private String generateHash(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(data.getBytes());
            StringBuilder hashString = new StringBuilder();

            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hashString.append('0');
                hashString.append(hex);
            }

            return hashString.toString().substring(0, 8); // Truncated to 8 characters
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

