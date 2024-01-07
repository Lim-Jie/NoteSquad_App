package com.example.NoteSquad_TestApp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;


public class noteShareFragment extends Fragment {

    RecyclerView notesRecView;
    FloatingActionButton addingButton;

    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_note_share, container, false);
        notesRecView=view.findViewById(R.id.notesPageRecView);
        firestore = FirebaseFirestore.getInstance();


        fetchNotesFromFirestore();

       addingButton = view.findViewById(R.id.addingButton);
        addingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Replace the current fragment with submit_notes fragment
                loadSubmitNotesFragment();
            }
        });


        return view;
    }

    private void loadSubmitNotesFragment() {
        // Create an instance of the submit_notes fragment
        Submit_Notes submitNotesFragment = new Submit_Notes();

        // Use a FragmentTransaction to replace the current fragment with submit_notes
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.constraintLayoutFragment, submitNotesFragment)
                .addToBackStack(null)  // Optional: Adds the transaction to the back stack
                .commit();
    }
    private void fetchNotesFromFirestore() {
        // Fetch notes data from Firebase Firestore based on subject titles

        ArrayList<NotePage> notePages = new ArrayList<>();

        // Define the subjects you want to retrieve
        String[] subjects = {"Physics", "Mathematics", "Biology", "General", "Computer Science", "Economics", "English"};

        for (String subject : subjects) {
            ArrayList<Notes> notesList = new ArrayList<>();

            // Replace the collection path and query based on your Firestore structure
            firestore.collection("notes")
                    .whereEqualTo("subjectTitle", subject)
                    .whereEqualTo("isPublic", true) // Filter notes where isPublic is true
                    .addSnapshotListener((value, error) -> {
                        if (error != null) {
                            Log.e("LOL", "Listen failed.", error);
                            return;
                        }

                        notesList.clear(); // Clear the existing list

                        for (QueryDocumentSnapshot document : value) {
                            Notes note = document.toObject(Notes.class);
                            notesList.add(note);
                        }

                        NotePage notePage = new NotePage(subject, notesList);
                        notePages.add(notePage);
                        updateRecyclerViewAdapter(notePages);
                    });
        }
    }

    private void updateRecyclerViewAdapter(ArrayList<NotePage> notePages) {
        NotesPageRecycleViewAdapter adapter = new NotesPageRecycleViewAdapter(getContext());
        adapter.setNotePage(notePages);

        notesRecView.setAdapter(adapter);
        notesRecView.setLayoutManager(new LinearLayoutManager(getContext()));
    }




}



