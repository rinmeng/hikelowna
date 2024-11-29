package com.example.hikelowna.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hikelowna.MainActivity;
import com.example.hikelowna.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private Button LogOut;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";

    // TODO: Rename and change types of parameters
    private String username;
    private String location;
    private String bio;
    private String level;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, fragment.username);
        args.putString(ARG_PARAM2, fragment.location);
        args.putString(ARG_PARAM3, fragment.bio);
        args.putString(ARG_PARAM4, fragment.level);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString("username");
            location = getArguments().getString("location");
            bio = getArguments().getString("bio");
            level = getArguments().getString("level");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View baseView = inflater.inflate(R.layout.fragment_profile, container, false);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView userView = baseView.findViewById(R.id.userView);
        userView.setText("Name: " + username);

        TextView userView2 = baseView.findViewById(R.id.locationView);
        userView2.setText("Location: " + location);

        TextView userView3 = baseView.findViewById(R.id.bioView);
        userView3.setText("Bio: " + bio);

        TextView userView4 = baseView.findViewById(R.id.levelView);
        userView4.setText("User Difficulty: " + level);

        Button logOut = baseView.findViewById(R.id.exit);
        logOut.setOnClickListener(v ->{
            logOut.setEnabled(false);
            signedOut();
        });
        return baseView;
    }

    private void signedOut(){
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK|intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        Toast.makeText(getActivity(),"Logged out successfully!",Toast.LENGTH_SHORT).show();
    }
}