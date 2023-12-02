package com.example.NoteSquad_TestApp;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

public class visitProfileFragment extends Fragment {
    private Button connectButton;
    private TextView profileBiography;
    private TextView profileUniversity;
    private TextView profileUsername;
    private TextView profileContributions;
    private TextView profileEngagements;
    private TextView profileConnections;
    private TextView profileEmail;
    FirebaseFirestore Firestore;
    private ListenerRegistration listenerRegistration;
    private String Username;
    private String EmailString;



    public visitProfileFragment(String email){
        EmailString=email;
        Log.d("EmailVisitProfile", "Email in VisitProfile constructor"+EmailString);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_visit_profile, container, false);

        Firestore=FirebaseFirestore.getInstance();

        //INITIALIZE OBJECTS AND VIEW COMPONENTS
        connectButton= (Button)view.findViewById(R.id.connectButton);
        profileUsername=(TextView)view.findViewById(R.id.profileNameUserVP);
        profileUniversity=(TextView)view.findViewById(R.id.universityPPVP);
        profileBiography=(TextView)view.findViewById(R.id.biographyPPVP);
        profileContributions=(TextView)view.findViewById(R.id.ContributionsValVP);
        profileEngagements=(TextView)view.findViewById(R.id.EngagementValVP);
        profileConnections=(TextView)view.findViewById(R.id.ConnectionsValVP);
        profileEmail=(TextView)view.findViewById(R.id.profileEmailVP);





        getFireStoreData("Users", EmailString, "username", profileUsername);
        getFireStoreData("Users", EmailString, "university", profileUniversity);
        getFireStoreData("Users", EmailString, "biography", profileBiography);
        getFireStoreData("Users", EmailString, "contributions", profileContributions);
        getFireStoreData("Users", EmailString, "engagements", profileEngagements);
        getFireStoreData("Users", EmailString, "connections", profileConnections);
        getFireStoreData("Users", EmailString, "email", profileEmail);



        connectButton.setOnClickListener(v->{


        });






        return view;
    }




    private void getFireStoreData(String collection, String document, String FieldName, TextView textView){
        listenerRegistration = Firestore.collection(collection)
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

    public void RequestToConnectToUser(){

    }











}