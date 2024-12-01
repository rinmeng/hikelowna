package com.example.hikelowna;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hikelowna.core.Feed;
import com.example.hikelowna.core.Hike;
import com.example.hikelowna.core.TrailDifficulty;
import com.example.hikelowna.core.UserManager;
import com.google.firebase.database.FirebaseDatabase;

public class ReviewActivity extends AppCompatActivity {
    TextView trailNameText, trailDecscriptionText;
    EditText titleInput, descriptionInput;
    Button postButton, backButton;
    RadioGroup saveRadioGroup, ratingRadioGroup;
    RadioButton saveToHistoryButton, saveToHistoryAndPostButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_review);

        trailNameText = findViewById(R.id.trailNameText);
        trailDecscriptionText = findViewById(R.id.trailDescriptionText);
        titleInput = findViewById(R.id.titleInput);
        descriptionInput = findViewById(R.id.descriptionInput);
        postButton = findViewById(R.id.postButton);
        backButton = findViewById(R.id.backButton);
        saveRadioGroup = findViewById(R.id.saveRadioGroup);
        ratingRadioGroup = findViewById(R.id.ratingRadioGroup);
        saveToHistoryButton = findViewById(R.id.saveToHistoryButton);
        saveToHistoryAndPostButton = findViewById(R.id.saveToHistoryAndPostButton);

        Intent it = getIntent();
        String trialName = it.getStringExtra("trailName");
        int trailDifficulty = it.getIntExtra("trailDifficulty", 0);
        String trailRatingStars = it.getStringExtra("trailRatingStars");
        float trailLength = it.getFloatExtra("trailLength", 0);
        int trailEstimatedTime = it.getIntExtra("trailEstimatedTime", 0);
        String elapsedTime = it.getStringExtra("elapsedTime");

        trailNameText.setText(trialName);
        String trailDetails = TrailDifficulty.toStars(trailDifficulty)
                + " | " + trailLength + " km | \n" + trailEstimatedTime
                + " minutes" + " | " + elapsedTime;
        trailDecscriptionText.setText(trailDetails);

        // get the inputs


        backButton.setOnClickListener(v -> finish());

        postButton.setOnClickListener(v -> {
            // check if the user wants to save to history
            boolean saveToHistoryAndPost = saveToHistoryAndPostButton.isChecked();
            // get checkbox values
            int rating = Integer.parseInt(((RadioButton) findViewById(ratingRadioGroup.getCheckedRadioButtonId())).getText().toString());

            String title = titleInput.getText().toString();
            String description = descriptionInput.getText().toString();
            Hike hike = new Hike(title, description);
            hike.setElapsedTime(elapsedTime);
            hike.setTrailName(trialName);
            hike.setTrailDifficultyStars(trailDifficulty);
            hike.setTrailRating(rating);
            hike.setTrailLength(trailLength);
            hike.setTrailEstimatedTime(trailEstimatedTime);


            UserManager userManager = UserManager.getInstance();
            userManager.getCurrentUser().addHikeToHistory(hike);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            Feed feed = new Feed(hike, userManager.getCurrentUser());

            if (saveToHistoryAndPost) {
                database.getReference("feeds").push().setValue(feed);
            }
            // Save to user's history either way
            database.getReference("users")
                    .child(userManager.getCurrentUser().getUsername())
                    .setValue(userManager.getCurrentUser());
            Toast.makeText(this, "Hike recorded!", Toast.LENGTH_SHORT).show();
            finish();
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}