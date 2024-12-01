package com.example.hikelowna.core;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.Base64;
import java.util.Arrays;

public class HikeData {

    private static final String AES_ALGORITHM = "AES";
    // Static key used for encryption and decryption
    private static final String SECRET_KEY = "your16bytekey123"; // 16 bytes for AES-128

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

    // Encrypts the combined string of Hike data using AES
    public String createHikeCode(Hike hike) {
        String combinedData = translateHikeCode(hike);
        return encrypt(combinedData);
    }

    // Encrypts the combined string of Feed data using AES
    public String createHikeCode(Feed feed) {
        String combinedData = translateHikeCode(feed);
        return encrypt(combinedData);
    }

    // Encrypt a string using AES with the static secret key
    private String encrypt(String data) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), AES_ALGORITHM);
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.encodeToString(encryptedBytes, Base64.DEFAULT); // Use android.util.Base64
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Decrypt a string using AES with the static secret key
    private String decrypt(String encryptedData) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), AES_ALGORITHM);
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

            byte[] decryptedBytes = cipher.doFinal(Base64.decode(encryptedData, Base64.DEFAULT)); // Use android.util.Base64
            return new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Translates an encrypted hike code back into a Hike object.
     *
     * @param encryptedHikeCode the encrypted hike string
     * @return the reconstructed Hike object
     */
    public Hike translateHikeCodeToHike(String encryptedHikeCode) {
        String decryptedData = decrypt(encryptedHikeCode);
        String[] fields = decryptedData.split("\\|");

        if (fields.length != 8) {
            throw new IllegalArgumentException("Invalid hike code. Expected 8 fields but got " + fields.length);
        }

        Hike hike = new Hike();
        hike.setHikeDescription(fields[0]);
        hike.setHikeName(fields[1]);
        hike.setElapsedTime(fields[2]);
        hike.setTrailName(fields[3]);
        hike.setTrailDifficultyStars(Integer.parseInt(fields[4]));
        hike.setTrailRating(Integer.parseInt(fields[5]));
        hike.setTrailLength(Float.parseFloat(fields[6]));
        hike.setTrailEstimatedTime(Float.parseFloat(fields[7]));

        return hike;
    }

    /**
     * Translates an encrypted feed code back into a Feed object.
     *
     * @param encryptedFeedCode the encrypted feed string
     * @return the reconstructed Feed object
     */
    public Feed translateHikeCodeToFeed(String encryptedFeedCode) {
        String decryptedData = decrypt(encryptedFeedCode);
        assert decryptedData != null;
        String[] fields = decryptedData.split("\\|");

        if (fields.length != 13) { // 8 fields for Hike + 5 fields for User
            throw new IllegalArgumentException("Invalid feed code. Expected 13 fields but got " + fields.length);
        }

        // Reconstruct Hike
        Hike hike = translateHikeCodeToHike(String.join("|", Arrays.copyOfRange(fields, 0, 8)));

        // Reconstruct User
        User poster = new User();
        poster.setUsername(fields[8]);
        poster.setDisplayName(fields[9]);
        poster.setLocation(fields[10]);
        poster.setPreferredDifficultyLevel(fields[11]);
        // Assuming passwordHash is not included for security reasons

        // Reconstruct Feed
        Feed feed = new Feed();
        feed.setHike(hike);
        feed.setPoster(poster);

        return feed;
    }
}
