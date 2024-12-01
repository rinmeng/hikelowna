package com.example.hikelowna;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hikelowna.core.DataFetcher;
import com.example.hikelowna.core.User;
import com.example.hikelowna.core.UserManager;
import com.example.hikelowna.ui.LandingPage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    // To retreieve user
    private static final String PREFS_NAME = "hikelownaUserPrefs";
    private static final String USER_KEY = "saved_user";

    Button loginButton, registerButton;
    Intent it;
    CheckBox rememberMeInput;
    EditText usernameInput, passwordInput;
    TextView welcomeText;

    User testUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("users");


        // Process
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        rememberMeInput = findViewById(R.id.rememberMe);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        welcomeText = findViewById(R.id.welcomeText);

        welcomeText.setText(getWelcomeText());

        // Working on Register Button soon
        registerButton.setEnabled(false);
        registerButton.setTextColor(Color.GRAY);

        // Determine if user was remembered last time, and compare it to the user on the userRef, if they mismatch then
        // Alert user that their password was changed

        User savedUser = getUserFromLocal();
        if (savedUser != null) {
            // Set the username and password to the one found locally
            usernameInput.setText(savedUser.getUsername());
            passwordInput.setText(savedUser.getPasswordHash());
            rememberMeInput.setChecked(true);
            if (!UserManager.isLoggedInOnce()) {
                try {
                    validateUser(userRef, savedUser.getUsername(), savedUser.getPasswordHash(), rememberMeInput.isChecked());
                } catch (Exception e) {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }

        loginButton.setOnClickListener(view -> {
            // Convert to String
            String username = getInputAsString(usernameInput);
            String password = getInputAsString(passwordInput);
            boolean rememberMe = Boolean.parseBoolean(getInputAsString(rememberMeInput));
            Log.d("UserInputs", "username: " + username + " password: " + password + " rememberMe: " + rememberMe);
            validateUser(userRef, username, password, rememberMe);
        });

        registerButton.setOnClickListener(view -> {
            it = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(it);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    private void validateUser(DatabaseReference userRef, String usr, String psw, boolean rememberMe) {

        // Add null checks for input parameters
        if (usr == null || psw == null) {
            Toast.makeText(MainActivity.this, "Username or password cannot be null", Toast.LENGTH_SHORT).show();
            return;
        }

        userRef.orderByChild("username").equalTo(usr).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User foundUser = snapshot.getValue(User.class);

                        if (foundUser != null) {

                            // Add null check for password
                            String storedPassword = foundUser.getPasswordHash();

                            if (storedPassword == null) {
                                Toast.makeText(MainActivity.this, "User password is not set", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Log.d("UserFound", "User data: " + foundUser);
                            Toast.makeText(MainActivity.this, "Logging in...", Toast.LENGTH_SHORT).show();

                            // Null-safe password comparison
                            if (storedPassword.equals(psw)) {
                                // Then advance them to the next screen
                                Toast.makeText(MainActivity.this, "Welcome back " + foundUser.getUsername() + "!", Toast.LENGTH_SHORT).show();
                                // Move Intent creation and start inside the successful login block
                                Intent it = new Intent(MainActivity.this, LandingPage.class);
                                UserManager.getInstance().setCurrentUser(foundUser);
                                it.putExtra("userFoundedFromSearch", foundUser);
                                it.putExtra("openFragment", "MapFragment");
                                UserManager.isLoggedInOnce(true);
                                startActivity(it);

                                DataFetcher df = new DataFetcher();
                                df.refetchAllData(foundUser);

                                if (rememberMe) {
                                    saveUserToLocal(foundUser);
                                } else {
                                    clearSavedUser();
                                }
                            } else {
                                // They input the wrong password
                                Toast.makeText(MainActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Error: User data is corrupted", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Failed to fetch user: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserToLocal(User user) {
        try {
            // Get SharedPreferences instance
            SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String userJson = gson.toJson(user);

            // Save the JSON string
            editor.putString(USER_KEY, userJson);

            // Apply the changes
            editor.apply();

            // Confirm that the user has been saved
            Log.d("UserSave", "User saved locally: " + user.getUsername());
        } catch (Exception e) {
            Log.e("UserSave", "Error saving user", e);
        }
    }

    // Method to retrieve saved user
    private User getUserFromLocal() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            // Retrieve the JSON string
            String userJson = sharedPreferences.getString(USER_KEY, null);

            if (userJson != null) {
                // Convert JSON string back to User object
                Gson gson = new Gson();
                User savedUser = gson.fromJson(userJson, User.class);

                Log.d("UserLoad", "User loaded from local storage: " +
                        (savedUser != null ? savedUser.getUsername() : "null"));

                return savedUser;
            }
        } catch (Exception e) {
            Log.e("UserLoad", "Error loading user", e);
        }

        return null;
    }

    // Method to clear saved user (useful for logout)
    private void clearSavedUser() {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            // Remove the saved user
            editor.remove(USER_KEY);
            editor.apply();

            Log.d("UserClear", "Saved user cleared");
            Toast.makeText(this, "User data cleared", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("UserClear", "Error clearing user", e);
        }
    }

    private String getInputAsString(Object item) {
        if (item instanceof EditText) {
            return ((EditText) item).getText().toString().trim();
        } else if (item instanceof CheckBox) {
            return Boolean.toString(((CheckBox) item).isChecked());
        }
        return "";
    }

    private String getWelcomeText() {
        String[] welcomeTexts = {
                "Your next adventure awaits.",
                "Discover trails, find your path.",
                "Nature's playground, your escape.",
                "Explore. Wander. Repeat.",
                "Your journey starts here.",
                "Uncover hidden gems.",
                "Let the trail be your guide.",
                "Embrace the wild within.",
                "Lose yourself, find yourself.",
                "Your adventure, your way.",
                "Ignite your spirit, one step at a time.",
                "Connect with nature, one breath at a time.",
                "Challenge yourself, reward your soul.",
                "Let the mountains be your muse.",
                "Find your peace, one trail at a time.",
                "Ready to hit the trail? Let's go!",
                "Your passport to outdoor adventure.",
                "Your hiking buddy, always by your side.",
                "Let's get lost together.",
                "Your next adventure begins now."
        };

        // Randomly select a welcome text
        int randomIndex = (int) (Math.random() * welcomeTexts.length);
        return welcomeTexts[randomIndex] + " Welcome back.";
    }


}


