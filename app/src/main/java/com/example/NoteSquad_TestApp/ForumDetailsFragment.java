package com.example.NoteSquad_TestApp;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ForumDetailsFragment extends Fragment {
    private Forum forum;
    private TextView textViewTitle;
    private TextView textViewDescription;
    private TextView textViewNoComments;
    private RecyclerView commentsRecyclerView;

    public void setForum(Forum forum) {
        this.forum = forum;
    }
    public static ForumDetailsFragment newInstance(Forum forum) {
        ForumDetailsFragment fragment = new ForumDetailsFragment();
        fragment.setForum(forum); // Set the forum object on the fragment
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum_details, container, false);
        textViewTitle = view.findViewById(R.id.forumDetailsTitle);
        textViewDescription = view.findViewById(R.id.forumDetailsDescription);
        commentsRecyclerView = view.findViewById(R.id.commentsRecyclerView);
        textViewNoComments = view.findViewById(R.id.textViewNoComments);

        // Retrieve forum data from arguments
        if (forum != null) {
            textViewTitle.setText(forum.getTitle());
            textViewDescription.setText(forum.getDescription());

            textViewNoComments.setVisibility(View.VISIBLE);
            textViewNoComments.setText("No comments");
        }

        return view;
    }
}
