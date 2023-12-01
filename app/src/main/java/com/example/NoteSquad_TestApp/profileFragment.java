package com.example.NoteSquad_TestApp;

import android.graphics.Paint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class profileFragment extends Fragment {
    FirebaseFirestore firestore;
    GoogleSignInAccount account;
    private ListenerRegistration listenerRegistration;
    TextView profileBiography;
    TextView profileUniversity;
    TextView profileUsername;
    TextView profileContributions;
    TextView profileEngagements;
    TextView profileConnections;
    TextView profileEmail;
    Button updateUserButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=  inflater.inflate(R.layout.fragment_profile, container, false);

        //INITIALIZE FIRESTORE
         firestore=FirebaseFirestore.getInstance();


         //INITIALIZE OBJECTS AND VIEW COMPONENTS
         updateUserButton= (Button)view.findViewById(R.id.updateUserDetailsButton);
         profileUsername=(TextView)view.findViewById(R.id.profileNameUser);
         profileUniversity=(TextView)view.findViewById(R.id.universityPP);
         profileBiography=(TextView)view.findViewById(R.id.biographyPP);
         profileContributions=(TextView)view.findViewById(R.id.ContributionsVal);
         profileEngagements=(TextView)view.findViewById(R.id.EngagementVal);
         profileConnections=(TextView)view.findViewById(R.id.ConnectionsVal);
         profileEmail=(TextView)view.findViewById(R.id.profileEmail);




        //LISTENER BUTTONS
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



         //SET FIRESTORE PROFILE VALUES INTO XML
         getFireStoreData("Users",getEmail(), "username",profileUsername);
         getFireStoreData("Users", getEmail(), "university", profileUniversity);
         getFireStoreData("Users", getEmail(), "biography", profileBiography);
         getFireStoreData("Users",getEmail(),"contributions",profileContributions);
         getFireStoreData("Users",getEmail(),"engagements",profileEngagements);
         getFireStoreData("Users",getEmail(),"connections",profileConnections);
         getFireStoreData("Users",getEmail(),"email",profileEmail);


         //UNDERLINE THE UNIVERSITY TEXTVIEW ENTIRELY
        profileUniversity.setPaintFlags(profileUniversity.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        return view;
    }



    public void getFireStoreData(String collection, String document, String FieldName, TextView textView){
        listenerRegistration = firestore.collection(collection)
                .document(document)
                .addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        return; //IF THERE IS ERRORS
                    }
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        Object value = documentSnapshot.get(FieldName);

                        if (value != null) {
                            if (value instanceof Number) {
                                textView.setText(String.valueOf((Number) value));
                            } else if (value instanceof String) {
                                // If the value is a string, you can directly set it
                                textView.setText((String) value);
                            }
                        }
                    }
                });
    }





    public String getEmail(){
        account = GoogleSignIn.getLastSignedInAccount(getContext());
        if(account!=null){
            return account.getEmail();
        }

        return null;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        // Remove the Firestore snapshot listener when the activity is destroyed
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
    }
}