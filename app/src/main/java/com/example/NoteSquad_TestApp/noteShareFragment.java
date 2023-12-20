package com.example.NoteSquad_TestApp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class noteShareFragment extends Fragment {

    RecyclerView notesRecView;
    FloatingActionButton addingButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_note_share, container, false);
        notesRecView=view.findViewById(R.id.notesPageRecView);
        ArrayList<NotePage> notePages=new ArrayList<>();

        notePages.add(new NotePage("Physics",createNotesList()));
        notePages.add(new NotePage("Mathematics",createNotesList()));
        notePages.add(new NotePage("Biology",createNotesList()));

       NotesPageRecycleViewAdapter adapter = new NotesPageRecycleViewAdapter(getContext());
        adapter.setNotePage(notePages);

        notesRecView.setAdapter(adapter);
        notesRecView.setLayoutManager(new LinearLayoutManager(getContext()));
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
    // Helper method to create a list of notes
    private ArrayList<Notes> createNotesList() {
        ArrayList<Notes> notes = new ArrayList<>();
        notes.add(new Notes("Note For Tutorial 7","In this set of Chinese notes, we delve into the evolutionary journey and future trends of Artificial Intelligence (AI). From the early days of expert systems to the current realms of deep learning and natural language processing, AI technology has been continually advancing. We provide an in-depth exploration of fundamental concepts in machine learning, including supervised learning, unsupervised learning, and reinforcement learning, discussing their real-life applications. ","https://i.ebayimg.com/images/g/Yp0AAOSw6pRlFAOj/s-l1200.jpg","Physics"));
        notes.add(new Notes("Note For Tutorial 10","haha2@gmail.com","https://i.ebayimg.com/images/g/pQUAAOSw7JNidtGV/s-l1200.jpg","Physics"));
        notes.add(new Notes("Note For Tutorial 20","haha3@gmail.com","https://i.pinimg.com/750x/94/43/28/9443284564d71afb67de298e8426fcd1.jpg","Physics"));
        return notes;
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



}



