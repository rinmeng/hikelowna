package com.example.hikelowna.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.hikelowna.MainActivity;
import com.example.hikelowna.R;
import com.example.hikelowna.core.User;
import com.example.hikelowna.core.UserManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    TextView username;
    EditText displayName, location, preferredDifficulty;
    Button editButton, logoutButton;

    boolean isEditing = true;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        username = rootView.findViewById(R.id.usernameText);
        displayName = rootView.findViewById(R.id.userDisplayNameText);
        location = rootView.findViewById(R.id.locationText);
        preferredDifficulty = rootView.findViewById(R.id.preferredDifficultyText);
        editButton = rootView.findViewById(R.id.editButton);
        logoutButton = rootView.findViewById(R.id.logoutButton);

        UserManager userManager = UserManager.getInstance();
        User user = userManager.getCurrentUser();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = db.getReference("users");

        // Set the text fields to the user's information
        String usernameText = "@" + user.getUsername();
        username.setText(usernameText);
        displayName.setText(user.getDisplayName());
        location.setText(user.getLocation());
        preferredDifficulty.setText(user.getPreferredDifficultyLevel());

        editButton.setOnClickListener(view -> {
            if (isEditing) {
                // Enable the text fields
                String editButtonText = "Save";
                editButton.setText(editButtonText);
                displayName.setEnabled(true);
                location.setEnabled(true);
                preferredDifficulty.setEnabled(true);
                isEditing = false;
            } else {
                // Save the changes
                String editButtonText = "Edit";
                editButton.setText(editButtonText);

                // Disable the text fields
                displayName.setEnabled(false);
                location.setEnabled(false);
                preferredDifficulty.setEnabled(false);

                // Update the user's information
                user.setDisplayName(displayName.getText().toString());
                user.setLocation(location.getText().toString());
                user.setPreferredDifficultyLevel(preferredDifficulty.getText().toString());
                userManager.updateUser(user);
                try {
                    usersRef.child(user.getUsername()).setValue(user);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Failed to update profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getContext(), "Profile updated", Toast.LENGTH_SHORT).show();
                isEditing = true;
            }
        });

        logoutButton.setOnClickListener(view -> {
            userManager.logout();
            Toast.makeText(getContext(), "Logged out", Toast.LENGTH_SHORT).show();

            // Create an intent to start MainActivity
            Intent it = new Intent(getActivity(), MainActivity.class);
            // Clear the back stack to prevent returning to previous screens
            it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(it);

            // If the current activity should close
            if (getActivity() != null) {
                getActivity().finish();
            }
        });


        // Inflate the layout for this fragment
        return rootView;
    }
}