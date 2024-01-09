package com.example.NoteSquad_TestApp;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.core.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    private String CurrentUserEmail;
    private String EmailString;
    static boolean ExistInNetworkBool;
    static boolean InvitationExistsBool;
    RecyclerView recyclerView;
    NotesRecycleViewAdapter notesAdapter;

    public visitProfileFragment(String email, String ownUserEmail) {
        CurrentUserEmail = ownUserEmail;
        EmailString = email;
        Log.d("EmailVisitProfile", "Email of VisitProfile:  " + EmailString);
        Log.d("OwnUserEmail", "Own user Email:  " + CurrentUserEmail);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_visit_profile, container, false);

        Firestore = FirebaseFirestore.getInstance();

        //INITIALIZE OBJECTS AND VIEW COMPONENTS
        connectButton = (Button) view.findViewById(R.id.connectButton);
        profileUsername = (TextView) view.findViewById(R.id.VPprofileNameUser);
        profileUniversity = (TextView) view.findViewById(R.id.VPuniversityPP);
        profileBiography = (TextView) view.findViewById(R.id.VPbiographyPP);
        profileContributions = (TextView) view.findViewById(R.id.VPContributionsVal);
        profileEngagements = (TextView) view.findViewById(R.id.VPEngagementVal);
        profileConnections = (TextView) view.findViewById(R.id.VPConnectionsVal);
        profileEmail = (TextView) view.findViewById(R.id.VPprofileEmail);


        getFireStoreData("Users", EmailString, "username", profileUsername);
        getFireStoreData("Users", EmailString, "university", profileUniversity);
        getFireStoreData("Users", EmailString, "biography", profileBiography);
        getFireStoreData("Users", EmailString, "contributions", profileContributions);
        getFireStoreData("Users", EmailString, "engagements", profileEngagements);
        getFireStoreData("Users", EmailString, "connections", profileConnections);
        getFireStoreData("Users", EmailString, "email", profileEmail);





        //REQUEST FOR CONNECTION ONCE BUTTON IS CLICKED/ ACCEPT INVITATION ONCE BUTTON IS CLICKED
        connectButton.setOnClickListener(v -> {
            buttonTextListener(new ButtonTextListenerCallback() {
                @Override
                public void OnButtonTextListener(boolean buttonTextListener) {
                    if(buttonTextListener==false){
                        RequestToConnectToUser();
                    }
                }
            });

            checkConnectionExist(new CheckConnectionExistCallback() {
                @Override
                public void OnCheckConnectionExist(boolean checkConnectionExistBool) {
                    if(checkConnectionExistBool){
                        RemoveConnection(CurrentUserEmail,EmailString);
                        RemoveConnection(EmailString,CurrentUserEmail);

                        ColorStateList colorStateList=ColorStateList.valueOf(Color.parseColor("#00BFFF"));
                        connectButton.setBackgroundTintList(colorStateList);
                        connectButton.setText("Connect");


                    }
                }
            });



            checkRequestExist(new CheckRequestExistCallback() {
                @Override
                public void onCheckRequestExistCallback(boolean checkRequestExistBool) {
                    if(checkRequestExistBool){

                        RemoveRequest("Requested",CurrentUserEmail,EmailString);
                        RemoveRequest("Invitation",EmailString,CurrentUserEmail);

                        ColorStateList colorStateList=ColorStateList.valueOf(Color.parseColor("#00BFFF"));
                        connectButton.setBackgroundTintList(colorStateList);
                        colorStateList= colorStateList.valueOf(Color.parseColor("#FFFFFF"));
                        connectButton.setTextColor(colorStateList);
                        connectButton.setText("Connect");

                        // TDODO:Check why is REMOVEREQUEST() method is crashing
                    }
                }
            });
        });




        //IF REQUEST EXISTS, BUTTON WILL DISPLAY "REQUESTED" AND TURN WHITE-GREY IN COLOUR
        checkRequestExist(new CheckRequestExistCallback() {
            @Override
            public void onCheckRequestExistCallback(boolean checkRequestExistBool) {
                if(checkRequestExistBool){
                    ColorStateList colorStateList = ColorStateList.valueOf(Color.parseColor("#F1F1F1"));
                    connectButton.setBackgroundTintList(colorStateList);

                    colorStateList = ColorStateList.valueOf(Color.parseColor("#767676"));
                    connectButton.setTextColor(colorStateList);
                    connectButton.setText("Requested");
                }
            }
        });


        //IF INVITATION EXISTS, BUTTON WILL DISPLAY "ACCEPT INVITATION" AND TURN WHITE-GREY IN COLOUR
        InvitationExists(new InvitationExistCallback() {
            @Override
            public void OnInvitationExistCallback(boolean invitationExistsBool) {
                if(invitationExistsBool){
                    ColorStateList colorStateList = ColorStateList.valueOf(Color.parseColor("#F1F1F1"));
                    connectButton.setBackgroundTintList(colorStateList);

                    colorStateList = ColorStateList.valueOf(Color.parseColor("#767676"));
                    connectButton.setTextColor(colorStateList);
                    connectButton.setText("Accept Invitation");
                }
            }
        });


        //IF CONNECTION EXISTS, BUTTON WILL DISPLAY "CONNECTED" AND TURN GREEN IN COLOUR
        checkConnectionExist(new CheckConnectionExistCallback() {
            @Override
            public void OnCheckConnectionExist(boolean checkConnectionExistBool) {
                if(checkConnectionExistBool){
                    ColorStateList colorstateList = ColorStateList.valueOf(Color.parseColor("#008000"));
                    connectButton.setBackgroundTintList(colorstateList);
                    connectButton.setText("Connected");
                    colorstateList = ColorStateList.valueOf(Color.parseColor("#FFFFFF"));
                    connectButton.setTextColor(colorstateList);
                }
            }
        });



        recyclerView = view.findViewById(R.id.VPrecyclerView);
        notesAdapter = new NotesRecycleViewAdapter(getContext());
        recyclerView.setAdapter(notesAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2)); // Adjust the layout manager as needed

        // Load and display user-specific notes
        loadUserSpecificNotes();





        return view;
    }


    public void AddOrMinusVariablesUserCollection(char AddOrMinusCharacters, String KeyNameToAdjust, String UserEmailValue) {
        Firestore.collection("Users")
                .document(UserEmailValue)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {

                        AddOrMinus(AddOrMinusCharacters, documentSnapshot, new AddOrMinusCallback() {
                            @Override
                            public void OnAddOrMinus(int currentConnections) {
                                updateConnections(currentConnections, KeyNameToAdjust, UserEmailValue);
                            }
                        });

                    } else {
                        // Handle the case when the document does not exist
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failures in retrieving the document
                });
    }

    private void updateConnections(int currentConnections, String KeyNameToAdjust, String UserEmailValue) {
        Firestore.collection("Users")
                .document(UserEmailValue)
                .update(KeyNameToAdjust, currentConnections)
                .addOnSuccessListener(v -> {
                    Log.d("ConnectionValue", "Successfully added/removed a connection");
                })
                .addOnFailureListener(e -> {
                    Log.e("ConnectionValue", "Failed to add/remove a connection", e);
                });
        Log.d("Users", "Successfully added or removed " + KeyNameToAdjust);
    }

    public interface AddOrMinusCallback{ void OnAddOrMinus(int currentConnections );}
    public void AddOrMinus(char AddOrMinusCharacter, DocumentSnapshot documentSnapshot, AddOrMinusCallback callback){

        Integer currentConnections = documentSnapshot.getLong("connections").intValue();
        if (AddOrMinusCharacter == '+') {
             currentConnections++;
        } else if (AddOrMinusCharacter == '-') {
             currentConnections--;
        }
        callback.OnAddOrMinus(currentConnections);
    }











    public void RemoveConnection(String UserEmail, String EmailToRemove){

        //READ THE USER'S CONNECTION IN FIRESTORE
        Firestore.collection("Connections")
                .document(UserEmail)
                .collection("Network")
                .document("Connection")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    Log.d("Network","Successfully read DocumentSnapshot for RemoveConnection()");

                    Map<String,Object> map= documentSnapshot.getData();
                    map.remove("Username",EmailToRemove);

                    //SET THE FIRESTORE CONNECTION TO REMOVE THE "EmailToRemove" CONNECTION
                    Firestore.collection("Connections")
                            .document(UserEmail)
                            .collection("Network")
                            .document("Connection")
                            .set(map)
                            .addOnSuccessListener(v->{
                               Log.d("Network","Successfully Removed"+UserEmail+"'s "+EmailToRemove+"Connection");
                               AddOrMinusVariablesUserCollection('-',"connections",UserEmail);
                            })
                            .addOnFailureListener(e->{
                                Log.e("Network","Unable to remove"+UserEmail+"'s "+EmailToRemove+"Connection");
                            });

                }).addOnFailureListener(e->{
                   Log.e("Network","Unable to read DocumentSnapshot for RemoveConnection()",e);
                });

    }


    public void RemoveRequest(String documentName,String CurrentUser, String UserToRemove){

        //READ THE USER'S CONNECTION IN FIRESTORE
        Firestore.collection("Connections")
                .document(CurrentUser)
                .collection("Network")
                .document(documentName)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    Log.d("Network","Successfully read DocumentSnapshot for RemoveRequest()");

                    Map<String,Object> map= documentSnapshot.getData();
                    map.remove("Username",UserToRemove);

                    //SET THE FIRESTORE CONNECTION TO REMOVE THE "EmailToRemove" CONNECTION
                    Firestore.collection("Connections")
                            .document(CurrentUser)
                            .collection("Network")
                            .document(documentName)
                            .set(map)
                            .addOnSuccessListener(v->{
                                Log.d("Network","Successfully Removed"+CurrentUser+"'s "+UserToRemove+"Request");
                            })
                            .addOnFailureListener(e->{
                                Log.e("Network","Unable to remove"+CurrentUser+"'s "+UserToRemove+"Request");
                            });

                }).addOnFailureListener(e->{
                    Log.e("Network","Unable to read DocumentSnapshot for RemoveRequest()",e);
                });

    }
    public interface ButtonTextListenerCallback{ void OnButtonTextListener(boolean buttonTextListener);}
    public void buttonTextListener(ButtonTextListenerCallback callback){
        callback.OnButtonTextListener(connectButton.getText().equals("Connected"));
    }




    private void getFireStoreData(String collection, String document, String FieldName, TextView textView) {
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




    public void RequestToConnectToUser() {
        existInNetwork(new ExistInNetworkCallback() {
            @Override
            public void onExistenceChecked(boolean existsInNetwork) {
                Log.d("Network", "ExistInNetwork method returns " + existsInNetwork);
                if(!existsInNetwork){
                    Request(CurrentUserEmail,EmailString);
                    ColorStateList colorStateList = ColorStateList.valueOf(Color.parseColor("#F1F1F1"));
                    connectButton.setBackgroundTintList(colorStateList);
                     colorStateList = ColorStateList.valueOf(Color.parseColor("#767676"));
                    connectButton.setTextColor(colorStateList);
                    connectButton.setText("Requested");
                }
            }
        });

        InvitationExists(new InvitationExistCallback() {
            @Override
            public void OnInvitationExistCallback(boolean invitationExistsBool) {
                if(invitationExistsBool){
                    AddAsConnection();
                    OtherUserAddAsConnection();


                    ColorStateList colorstateList = ColorStateList.valueOf(Color.parseColor("#008000"));
                    connectButton.setBackgroundTintList(colorstateList);
                    connectButton.setText("Connected");
                    colorstateList = ColorStateList.valueOf(Color.parseColor("#FFFFFF"));
                    connectButton.setTextColor(colorstateList);
                }
            }
        });


    }

    public void Request(String Sender, String Receiver) {
        Map<String, Object> map = new HashMap<>();
        map.put("Username", Receiver);

        Map<String,Object> map2 = new HashMap<>();
        map2.put("Username", Sender);

        //REQUEST USER TO CONNECT
        Firestore.collection("Connections")
                .document(CurrentUserEmail)
                .collection("Network")
                .document("Requested")
                .update(map) // Use set with merge to add a new field
                .addOnSuccessListener(v -> {
                    Log.d("Network", "Requested successfully to " + EmailString);

                    Firestore.collection("Connections")
                            .document(EmailString)
                            .collection("Network")
                            .document("Invitation")
                            .update(map2)
                            .addOnSuccessListener(aVoid -> {
                                Log.d("Network", "Invitation successfully from " + CurrentUserEmail);
                            }).addOnFailureListener(e ->{
                                Log.d("Network", "Error in invitation from " + CurrentUserEmail);
                            });
                })
                .addOnFailureListener(e -> {
                    Log.e("Network", "Error in requesting to connect " + EmailString, e);
                });

        //CREATE INVITATION FOR OTHER USER

    }

    //IF INVITATION EXIST FOR ME, REMOVE INVITATION, ACCEPT AS CONNECTION
    public void AddAsConnection() {

        Firestore.collection("Connections")
                .document(CurrentUserEmail)
                .collection("Network")
                .document("Invitation")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Map<String, Object> data = documentSnapshot.getData();
                        if (data.containsKey("Username") && data.get("Username").equals(EmailString)) {
                            data.remove("Username",EmailString);

                            //REMOVE INVITATION
                            Firestore.collection("Connections")
                                    .document(CurrentUserEmail)
                                    .collection("Network")
                                    .document("Invitation")
                                    .set(data)
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d("Network", "Removed 'username' field from 'Invitation' document");
                                        Map<String, Object> connectionMap    = new HashMap<>();
                                        connectionMap.put("Username",EmailString);

                                        //ADD CONNECTION FOR ME
                                            Firestore.collection("Connections")
                                                    .document(CurrentUserEmail)
                                                    .collection("Network")
                                                    .document("Connection")
                                                    .update(connectionMap)
                                                    .addOnSuccessListener(v ->{
                                                        Log.d("Network","Successfully removed invitation and added as Connection");

                                                    })
                                                    .addOnFailureListener(e ->{
                                                        Log.e("Network","Can remove invitatioon but not add Connection", e);
                                                    });
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("Network", "Failed to remove 'username' field: " + e);
                                    });
                        }
                    }

                })
                .addOnFailureListener(e -> {
                    Log.e("Network", "Failed to retrieve 'Invitation' document: for RemoveInvitation() " + e);
                });


    }
    //FOR OTHER USER, REMOVE HIS REQUEST, ACCEPT HIS CONNECTION
    public void OtherUserAddAsConnection(){

        Firestore.collection("Connections")
                .document(EmailString)
                .collection("Network")
                .document("Requested")
                .get()
                .addOnSuccessListener(DocumentSnapshot-> {
                    Map <String,Object> map = DocumentSnapshot.getData();
                    if(map.containsKey("Username") && map.containsValue(CurrentUserEmail)){
                        map.remove("Username",CurrentUserEmail);

                        //REMOVE REQUEST TO ME
                        Firestore.collection("Connections")
                                .document(EmailString)
                                .collection("Network")
                                .document("Requested")
                                .set(map)
                                .addOnSuccessListener(v-> {

                                    Map<String,Object> map2= new HashMap<>();
                                    map2.put("Username", CurrentUserEmail);

                                    Firestore.collection("Connections")
                                            .document(EmailString)
                                            .collection("Network")
                                            .document("Connection")
                                            .update(map2)
                                            .addOnSuccessListener(aVoid->{

                                                //ADD CONNECTION +1 WHEN PROCESS COMPLETE
                                                AddOrMinusVariablesUserCollection('+',"connections",CurrentUserEmail);
                                                AddOrMinusVariablesUserCollection('+',"connections",EmailString);


                                                Log.d("Network","Successfully removed request and added as Connection");
                                            }).addOnFailureListener(e-> {
                                                Log.e("Network","Error removing request and adding as Connection",e);
                                            });
                                }).addOnFailureListener(e->{
                                    Log.e("Network", "Failed to remove request from User2",e);
                                });
                    }
                }).addOnFailureListener(e->{
                    Log.e("Network","Error in OtherUserAddAsConnection method");
                });

    }

    //checkRequestExist
    public interface CheckRequestExistCallback{void onCheckRequestExistCallback(boolean checkRequestExistBool);}
    public void checkRequestExist(CheckRequestExistCallback callback){
        Firestore.collection("Connections")
                .document(CurrentUserEmail)
                .collection("Network")
                .document("Requested")
                .get()
                .addOnSuccessListener(documentSnapshot->{
                   Map<String,Object> map= documentSnapshot.getData();
                   if(map.containsKey("Username") && map.containsValue(EmailString)){
                       callback.onCheckRequestExistCallback(true);
                       Log.d("Network", EmailString+" request has been found in CheckRequestExist method");
                   }else{
                       callback.onCheckRequestExistCallback(false);
                       Log.d("Network", EmailString+" request has not been found in CheckRequestExist method");
                   }
                })
                .addOnFailureListener(e->{
                    callback.onCheckRequestExistCallback(false);
                    Log.e("Network", EmailString+" request ERROR in CheckRequestExist method",e);
                });
    }



    public interface ExistInNetworkCallback { void onExistenceChecked(boolean existsInNetwork);
    }

    public void existInNetwork(ExistInNetworkCallback callback) {

        Firestore.collection("Connections")
                .document(CurrentUserEmail)
                .collection("Network")
                .whereEqualTo("Username", EmailString)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if(!queryDocumentSnapshots.isEmpty()){
                        callback.onExistenceChecked(true);
                        Log.d("Network",EmailString+"does exist in Network");
                    }else{
                        callback.onExistenceChecked(false);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Network", "Failed to check 'Network' sub-collection " + e);
                    callback.onExistenceChecked(false);
                });
    }


    public interface InvitationExistCallback{ void OnInvitationExistCallback(boolean invitationExistsBool);}


    public void InvitationExists(InvitationExistCallback callback) {

        Firestore.collection("Connections")
                .document(CurrentUserEmail)
                .collection("Network")
                .document("Invitation")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Check if the username exists in the "Invitation" document
                        String username = documentSnapshot.getString("Username");
                        if (username != null && username.equals(EmailString)) {
                            Log.d("Network", "Username '" + EmailString + "' exists in the 'Invitation' document");
                            callback.OnInvitationExistCallback(true);
                        } else {
                            Log.e("Network", "Username '" + EmailString + "' doesn't exist in the 'Invitation' document");
                            callback.OnInvitationExistCallback(false);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Network", "Failed to check 'Invitation' document: " + e);
                        callback.OnInvitationExistCallback(false);
                });
    }

    //checkConnectionExist

    public interface CheckConnectionExistCallback{ void OnCheckConnectionExist(boolean checkConnectionExistBool);}
    public void checkConnectionExist(CheckConnectionExistCallback callback){
        Firestore.collection("Connections")
                .document(CurrentUserEmail)
                .collection("Network")
                .document("Connection")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    Map<String,Object> map= documentSnapshot.getData();
                    if(map.containsKey("Username") && map.containsValue(EmailString)){
                        callback.OnCheckConnectionExist(true);
                    }
                });
    }

    private void loadUserSpecificNotes() {
        // Fetch user-specific notes from Firestore based on user's email
        ArrayList<Notes> userNotesList = new ArrayList<>();

        // Replace the collection path and query based on your Firestore structure
        Firestore.collection("notes")
                .whereEqualTo("userEmail", EmailString)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Notes note = document.toObject(Notes.class);
                            userNotesList.add(note);
                        }
                        // Update the RecyclerView with user-specific notes
                        notesAdapter.setNotes(userNotesList);
                    } else {
                        Log.e("profileFragment", "Error getting user-specific notes", task.getException());
                    }
                });
    }






}



//Updated UI 9th Jan 24







