package com.example.NoteSquad_TestApp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class homePageFragment extends Fragment {
    Button logoutButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_home_page, container, false);



        //BUTTON OBJECTS
         logoutButton = (Button) view.findViewById(R.id.logoutButton);


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