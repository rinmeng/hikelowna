package com.example.hikelowna;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hikelowna.core.User;
import com.example.hikelowna.core.UserManager;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    Button registerButton, backButton;
    EditText username, password, location, displayName;
    Spinner preferredDifficultyLevel;
    ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        Intent it = new Intent(this, MainActivity.class);

        registerButton = findViewById(R.id.registerButton);
        backButton = findViewById(R.id.backButton);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        location = findViewById(R.id.location);
        displayName = findViewById(R.id.displayName);
        preferredDifficultyLevel = findViewById(R.id.preferredDifficultyLevel);

        adapter = ArrayAdapter.createFromResource(this, R.array.difficulty, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        preferredDifficultyLevel.setAdapter(adapter);

        registerButton.setOnClickListener(v -> {
            String usernameText = username.getText().toString();
            String passwordText = password.getText().toString();
            String locationText = location.getText().toString();
            String displayNameText = displayName.getText().toString();

            if (isValidInput(usernameText, displayNameText, locationText)) {
                User user = new User(usernameText, passwordText, displayNameText, locationText,
                        preferredDifficultyLevel.getSelectedItem().toString());
                UserManager.getInstance().setCurrentUser(user);

                it.putExtra("user", user);

                FirebaseDatabase.getInstance().getReference("users").child(user.getUsername()).setValue(user);
                Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        backButton.setOnClickListener(v -> {
            finish();
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private boolean isValidInput(String username, String displayName, String location) {
        String usernamePattern = "^[a-zA-Z0-9]+$";
        String displayNamePattern = "^[a-zA-Z ]+$";
        String locationPattern = "^[a-zA-Z0-9 ]+$";

        if (username.isEmpty() || displayName.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!username.matches(usernamePattern)) {
            Toast.makeText(this, "Invalid username format", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!displayName.matches(displayNamePattern)) {
            Toast.makeText(this, "Invalid display name format", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!location.matches(locationPattern)) {
            Toast.makeText(this, "Invalid location format", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}