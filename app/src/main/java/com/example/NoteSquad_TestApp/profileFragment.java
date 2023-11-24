package com.example.NoteSquad_TestApp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class profileFragment extends Fragment {
    Button updateUserButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=  inflater.inflate(R.layout.fragment_profile, container, false);

         updateUserButton= (Button)view.findViewById(R.id.updateUserDetailsButton);

         updateUserButton.setOnClickListener(v->{
             FragmentManager fm = getActivity().getSupportFragmentManager();
             FragmentTransaction ft = fm.beginTransaction();
             ft.replace(R.id.constraintLayoutFragment, new updateuserdetails());
             ft.commit();

             /* CAN ALSO DO THIS METHOD

             HomePage activity  = (HomePage) getActivity();
             activity.replaceFragment(new updateuserdetails());
              */
         });










        return view;
    }
}