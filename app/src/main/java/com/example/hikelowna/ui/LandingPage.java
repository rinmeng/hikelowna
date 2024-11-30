package com.example.hikelowna.ui;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hikelowna.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LandingPage extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    MapFragment mapFragment = new MapFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this);

        // Get the fragment to open from the Intent
        String openFragment = getIntent().getStringExtra("openFragment");

        //user info
        String username = getIntent().getStringExtra("username");
        String location = getIntent().getStringExtra("location");
        String bio = getIntent().getStringExtra("bio");
        String level = getIntent().getStringExtra("level");

        Bundle b = new Bundle();
        b.putString("username",username);
        b.putString("location",location);
        b.putString("bio",bio);
        b.putString("level",level);
        profileFragment.setArguments(b);

        if (openFragment != null) {
            switch (openFragment) {
                case "MapFragment":
                    bottomNavigationView.setSelectedItemId(R.id.map); // Set the map as selected
                    break;
                case "ProfileFragment":
                    bottomNavigationView.setSelectedItemId(R.id.profile); // Set the profile as selected
                    break;
                default:
                    bottomNavigationView.setSelectedItemId(R.id.home); // Default to home
                    break;
            }
        } else {
            // Default selection if no intent extra is provided
            bottomNavigationView.setSelectedItemId(R.id.home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.home) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flFragment, homeFragment)
                    .commit();
        } else if (itemId == R.id.map) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flFragment, mapFragment)
                    .commit();
        } else if (itemId == R.id.profile) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flFragment, profileFragment)
                    .commit();
        }
        return true;
    }
}
