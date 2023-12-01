package com.example.NoteSquad_TestApp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;


public class noteShareFragment extends Fragment {

    RecyclerView notesRecView;
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
        notePages.add(new NotePage("Physics",createNotesList()));
        notePages.add(new NotePage("Physics",createNotesList()));

       NotesPageRecycleViewAdapter adapter = new NotesPageRecycleViewAdapter(getContext());
        adapter.setNotePage(notePages);
        notesRecView.setAdapter(adapter);
        notesRecView.setLayoutManager(new LinearLayoutManager(getContext()));


        return view;
    }
    // Helper method to create a list of notes
    private ArrayList<Notes> createNotesList() {
        ArrayList<Notes> notes = new ArrayList<>();
        notes.add(new Notes("Haha1","haha1@gmail.com","https://i.ebayimg.com/images/g/u-UAAOSwrCVhGvP2/s-l1600.jpg","Physics"));
        notes.add(new Notes("Haha2","haha2@gmail.com","https://i.ebayimg.com/images/g/u-UAAOSwrCVhGvP2/s-l1600.jpg","Physics"));
        notes.add(new Notes("Haha3","haha3@gmail.com","https://i.ebayimg.com/images/g/u-UAAOSwrCVhGvP2/s-l1600.jpg","Physics"));
        return notes;
    }



}