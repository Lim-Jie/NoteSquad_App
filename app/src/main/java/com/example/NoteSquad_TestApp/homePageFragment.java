package com.example.NoteSquad_TestApp;
import static android.content.ContentValues.TAG;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.checkerframework.checker.units.qual.K;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class homePageFragment extends Fragment {
    Button logoutButton;
    private SearchBar searchBar;
    SearchView searchView;
    private FirebaseFirestore Firestore;
    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_home_page, container, false);


        //INSTANTIATE FIRESTORE
        Firestore=FirebaseFirestore.getInstance();

        //BUTTON OBJECTS
         logoutButton = (Button) view.findViewById(R.id.logoutButton);
         listView = view.findViewById(R.id.listView);


        //OPEN ACTIVITY
        logoutButton.setOnClickListener(v -> {
            HomePage activity= (HomePage) getActivity();
            if(activity!=null){
                activity.SignOut();
            }
        });




        return view;
    }
















}




