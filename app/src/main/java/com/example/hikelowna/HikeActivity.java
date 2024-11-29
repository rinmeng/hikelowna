package com.example.hikelowna;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_hike);

        hikeTitle = findViewById(R.id.hikeTitle);
        hikeDetails = findViewById(R.id.hikeDetails);
        finishHikeButton = findViewById(R.id.finishHikeButton);
        pauseHikeButton = findViewById(R.id.pauseHikeButton);
        hikeTimer = findViewById(R.id.hikeTimer);
        hikeTimerInfo = findViewById(R.id.hikeTimerInfo);
        hikeLengthText = findViewById(R.id.hikeLengthText);
        hikeEstimatedTimeText = findViewById(R.id.hikeEstimatedTimeText);

        Intent it = getIntent();
        String trialName = it.getStringExtra("trailName");
        String trailDifficultyStars = it.getStringExtra("trailDifficultyStars");
        String trailRatingStars = it.getStringExtra("trailRatingStars");
        float trailLength = it.getFloatExtra("trailLength", 0);
        float trailEstimatedTime = it.getFloatExtra("trailEstimatedTime", 0);

        String trailDetails = "";
        if (trialName != null) {
            hikeTitle.setText(trialName);
            trailDetails = trailRatingStars + separator + trailDifficultyStars;
            hikeDetails.setText(trailDetails);
            hikeLengthText.setText(String.format("%.2f km", trailLength));
            hikeEstimatedTimeText.setText(String.format("%.2f hours", trailEstimatedTime));


        } else {
            Toast.makeText(this, "No trail was passed/found.", Toast.LENGTH_SHORT).show();
        }

        hikeDetails.setText(trailDetails);

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

        finishHikeButton.setOnClickListener(view -> {
            if (handler != null) {
                handler.removeCallbacks(runnable);
            }
            if (blinkHandler != null) {
                blinkHandler.removeCallbacks(blinkRunnable);
            }
            Toast.makeText(this, "Hike finished", Toast.LENGTH_SHORT).show();
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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
}