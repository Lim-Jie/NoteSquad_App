package com.example.NoteSquad_TestApp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotesDisplayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotesDisplayFragment extends Fragment {


    public static NotesDisplayFragment newInstance(String imageUrl, String description) {
        // Create a new instance of the fragment with arguments
        NotesDisplayFragment fragment = new NotesDisplayFragment();
        Bundle args = new Bundle();
        args.putString("imageUrl", imageUrl);
        args.putString("description", description);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes_display, container, false);

        // Retrieve image URL and description from arguments
        String imageUrl = getArguments().getString("imageUrl", "");
        String description = getArguments().getString("description", "");

        // Load and display the image using an ImageView
        ImageView imageView = view.findViewById(R.id.imageView);
        Glide.with(this).load(imageUrl).into(imageView);


        // Set up the description overlay
        setUpDescriptionOverlay(view, description);

        // Existing code...
        return view;
    }

    private void setUpDescriptionOverlay(View view, String description) {
        RelativeLayout descriptionOverlay = view.findViewById(R.id.descriptionOverlay);
        TextView descriptionTextView = view.findViewById(R.id.descriptionTextView);

        // Set initial visibility
        descriptionOverlay.setVisibility(View.GONE);

        // Set description text
        descriptionTextView.setText(description);

        // Set click listener for the image to toggle description visibility
        ImageView imageView = view.findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle visibility of description overlay
                if (descriptionOverlay.getVisibility() == View.VISIBLE) {
                    descriptionOverlay.setVisibility(View.GONE);
                } else {
                    descriptionOverlay.setVisibility(View.VISIBLE);
                }
            }
        });
    }




}