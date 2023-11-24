package com.example.NoteSquad_TestApp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

public class profileFragment extends Fragment {
    FirebaseFirestore firestore;
    private ListenerRegistration listenerRegistration;
    TextView profileBiography;
    TextView profileUniversity;
    TextView profileUsername;
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

         getFireStoreData("userDetails","UserProfilePageDetails", "username",profileUsername);
         getFireStoreData("userDetails", "UserProfilePageDetails", "university", profileUniversity);
         getFireStoreData("userDetails", "UserProfilePageDetails", "biography", profileBiography);







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

                        String value = documentSnapshot.getString(FieldName);
                        if (value != null) {
                            textView.setText(value);
                        }
                    }
                });
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