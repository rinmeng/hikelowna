package com.example.hikelowna;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HikeActivity extends AppCompatActivity {
    TextView hikeTitle, hikeDetails, hikeTimer, hikeTimerInfo, hikeLengthText, hikeEstimatedTimeText;
    Button finishHikeButton, pauseHikeButton;
    Handler handler, blinkHandler;
    Runnable runnable, blinkRunnable;
    long startTime, elapsedTime;
    boolean isPaused = false;
    String separator = " | ";
    String trialName, trailRatingStars;
    int trailDifficulty;
    float trailLength;
    int trailEstimatedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_hike);

        // Initialize views
        hikeTitle = findViewById(R.id.hikeTitle);
        hikeDetails = findViewById(R.id.hikeDetails);
        finishHikeButton = findViewById(R.id.finishHikeButton);
        pauseHikeButton = findViewById(R.id.pauseHikeButton);
        hikeTimer = findViewById(R.id.hikeTimer);
        hikeTimerInfo = findViewById(R.id.hikeTimerInfo);
        hikeLengthText = findViewById(R.id.hikeLengthText);
        hikeEstimatedTimeText = findViewById(R.id.hikeEstimatedTimeText);

        // Retrieve intent data
        Intent it = getIntent();
        trialName = it.getStringExtra("trailName");
        trailDifficulty = it.getIntExtra("trailDifficulty", 0);
        trailRatingStars = it.getStringExtra("trailRatingStars");
        trailLength = it.getFloatExtra("trailLength", 0);
        trailEstimatedTime = it.getIntExtra("trailEstimatedTime", 0);

        // Set trail information
        if (trialName != null) {
            hikeTitle.setText(trialName);
            String trailDetails = trailRatingStars + separator + trailDifficulty;
            hikeDetails.setText(trailDetails);
            hikeLengthText.setText(String.format("%.2f km", trailLength));
            hikeEstimatedTimeText.setText(trailEstimatedTime + " minutes");
        } else {
            Toast.makeText(this, "No trail was passed/found.", Toast.LENGTH_SHORT).show();
        }

        // Pause Hike Button
        pauseHikeButton.setOnClickListener(view -> {
            if (isPaused) {
                startTimer();
                startBlinking();
                pauseHikeButton.setText(R.string.pauseHike);
                Toast.makeText(this, "Hike resumed", Toast.LENGTH_SHORT).show();
            } else {
                pauseTimer();
                stopBlinking();
                pauseHikeButton.setText(R.string.resumeHike);
                Toast.makeText(this, "Hike paused", Toast.LENGTH_SHORT).show();
            }
        });

        // Finish Hike Button
        finishHikeButton.setOnClickListener(view -> showFinishHikeDialog());

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Start timer automatically when activity is created
        startTimer();
        startBlinking();
    }


    private void startTimer() {
        handler = new Handler();
        if (isPaused) {
            startTime = System.currentTimeMillis() - elapsedTime;
        } else {
            startTime = System.currentTimeMillis();
        }
        isPaused = false;

        runnable = new Runnable() {
            @Override
            public void run() {
                elapsedTime = System.currentTimeMillis() - startTime;
                long hours = (elapsedTime / 1000) / 3600;
                long minutes = ((elapsedTime / 1000) % 3600) / 60;
                long seconds = (elapsedTime / 1000) % 60;
                String time = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                hikeTimer.setText(time);
                handler.postDelayed(this, 1000);
            }
        };

        handler.post(runnable);
    }

    private void pauseTimer() {
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
        isPaused = true;
    }

    private void startBlinking() {
        blinkHandler = new Handler();
        blinkRunnable = new Runnable() {
            @Override
            public void run() {
                if (hikeTimerInfo.getText().equals(getString(R.string.redCircle))) {
                    hikeTimerInfo.setText(R.string.blackCircle);
                } else {
                    hikeTimerInfo.setText(R.string.redCircle);
                }
                blinkHandler.postDelayed(this, 500);
            }
        };
        blinkHandler.post(blinkRunnable);
    }

    private void stopBlinking() {
        if (blinkHandler != null) {
            blinkHandler.removeCallbacks(blinkRunnable);
        }
        hikeTimerInfo.setText(R.string.redCircle);
    }

    private void showFinishHikeDialog() {
        // Inflate the new layout
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View customView = getLayoutInflater().inflate(R.layout.custom_dialog, null);

        // Bind the views
        Button backButton = customView.findViewById(R.id.backButton);
        Button positiveButton = customView.findViewById(R.id.positiveButton);
        Button negativeButton = customView.findViewById(R.id.negativeButton);


        // Set the custom view in the dialog
        builder.setView(customView);
        builder.setCancelable(true);

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();


        // Handle "Back" button click
        backButton.setOnClickListener(view -> {
            Toast.makeText(this, "Returning back to hike", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        // Handle "Yes" button click
        positiveButton.setOnClickListener(view -> {

            Intent it = new Intent(this, ReviewActivity.class);
            it.putExtra("trailName", trialName);
            it.putExtra("trailDifficulty", trailDifficulty);
            it.putExtra("trailRatingStars", trailRatingStars);
            it.putExtra("trailLength", trailLength);
            it.putExtra("trailEstimatedTime", trailEstimatedTime);
            it.putExtra("elapsedTime", hikeTimer.getText().toString());
            startActivity(it);
            finish();
        });

        // Handle "No" button click
        negativeButton.setOnClickListener(view -> {
            pauseTimer();
            stopBlinking();
            Toast.makeText(this, "Hike not saved.", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}