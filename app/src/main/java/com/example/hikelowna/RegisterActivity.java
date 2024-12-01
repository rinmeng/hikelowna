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
            // Register the user
            // If the user is registered successfully, go to the main activity

            User user = new User(username.getText().toString(), password.getText().toString(),
                    displayName.getText().toString(), location.getText().toString(),
                    preferredDifficultyLevel.getSelectedItem().toString());
            UserManager.getInstance().setCurrentUser(user);

            it.putExtra("user", user);

            FirebaseDatabase.getInstance().getReference("users").child(user.getUsername()).setValue(user);
            Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show();
            finish();
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
}