package com.example.NoteSquad_TestApp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;
public class updateuserdetails extends Fragment {
    FirebaseFirestore firestore;
    GoogleSignInAccount account;
    TextView username;
    TextView university;
    TextView biography;
    TextView usernameExistErrorTextView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=  inflater.inflate(R.layout.fragment_updateuserdetails, container, false);

        //OBJECT AND VIEW INITIALIZATION
        Button updateUserDetailsButton = (Button)view.findViewById(R.id.updateButtonProfilePage);
        username =(TextView) view.findViewById(R.id.Username);
        university =(TextView) view.findViewById(R.id.University);
        biography =(TextView) view.findViewById(R.id.Biography);
        usernameExistErrorTextView = (TextView)view.findViewById(R.id.UsernameExistErrorTextView);
        String currentTypedUsername = username.getText().toString();

        //FIRESTORE INITIALIZATION
        firestore=FirebaseFirestore.getInstance();


        //INITIALIZE GOOGLE EMAIL AS USERNAME
        updateUserDetailsButton.setOnClickListener(v->{
            collectText();
        });


        //CHECK LIVE WHETHER UPDATED USER STRING VALUE EXISTS IN DATABASE CURRENTLY, IF NOT, ALLOW TO ADD
        UsernameLiveChecker(currentTypedUsername);

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                String currentTypedUsername = editable.toString();
                UsernameLiveChecker(currentTypedUsername);
            }
        });




    return view;
    }



    //POST USER DETAILS DATA AS HASHMAP INTO FIRESTORE
    private void collectText(){
        Map<String,Object> userDetailsMap= new HashMap<>();
        String usernameString = username.getText().toString();
        String universityString= university.getText().toString();
        String biographyString= biography.getText().toString();

        //IF STRINGS ARE ALL EMPTY, HASHMAP WILL BE EMPTY
        if(!(usernameString.equals("") && biographyString.equals("")&& universityString.equals(""))){
            userDetailsMap.put("username",usernameString);
            userDetailsMap.put("university",universityString);
            userDetailsMap.put("biography",biographyString);
        }

        DocumentReference documentRef = firestore.collection("Users").document(getEmailObject());



        if (!userDetailsMap.isEmpty() && !usernameExistErrorTextView.getText().equals("Username already exists")) {
                documentRef.update(filledHashMapValues(userDetailsMap))
                        .addOnSuccessListener(v -> {
                            Toast.makeText(getContext(), "Update Successful", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            // Handle failure, if needed
                            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });

        } else {
            Toast.makeText(getContext(), "Please fill in fields with valid values", Toast.LENGTH_SHORT).show();
            }




        }




    public String getEmailObject(){
        account = GoogleSignIn.getLastSignedInAccount(getContext());
        if(account!=null){
            return account.getEmail();}
        return null;
    }

    public void UsernameLiveChecker(String currentTypedUsername){
        firestore.collection("Users")
                .whereEqualTo("username", currentTypedUsername)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        usernameExistErrorTextView.setText("Username already exists");
                    }else {
                        // Username does not exist
                        usernameExistErrorTextView.setText("");
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle the failure, e.g., log the error
                    Log.e("Firestore", "Error checking username existence", e);
                });

    }



        public boolean checkEmptyHashmap(Map<String, Object> map) {
            //  RETURNS TRUE IF THERE ARE EMPTY VALUES TO UPDATE INSTEAD OF SET FIRESTORE DATABASE
            // RETURNS FALSE IF ALL FIELD VALUES ARE FILLED IN HASHMAP

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (key != null && value == "") {
                    return true;
                }
            }
            return false;
        }


        public Map<String,Object> filledHashMapValues(Map<String,Object>map){
            Map<String,Object> newMap= new HashMap<>();

            for(Map.Entry<String,Object> entry: map.entrySet()){
                String key= entry.getKey();
                Object value=entry.getValue();

                if (!value.toString().equals("")) {
                    newMap.put(key, value);
                }
            }
        return newMap;
        }
    }


