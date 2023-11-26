package com.example.NoteSquad_TestApp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.auth.User;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingFormatArgumentException;
import java.util.NoSuchElementException;
import java.util.Random;


public final class MainActivity extends AppCompatActivity {
    Object Email;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    GoogleSignInAccount account;
    private static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            FirebaseApp.initializeApp(this);

            firebaseAuth = FirebaseAuth.getInstance();
            firestore = FirebaseFirestore.getInstance();

            // LOGIN PAGE FOR USERNAME, PASSWORD, AND BUTTON
            TextView username = findViewById(R.id.EmailAddress);
            TextView password = findViewById(R.id.Password);
            Button loginButton = findViewById(R.id.LoginButton);
            Button signUpButton = findViewById(R.id.SignUpButton);
            Button googleimage=(Button)findViewById(R.id.googleSigninButton);


            // MANUAL VERIFICATION
            loginButton.setOnClickListener(v-> {
                if (username.getText().toString().equals("Li Jie") && password.getText().toString().equals("123")) {
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                    openHomePage();
                } else {
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            });


            // // REDIRECT TO REGISTRATION PAGE
            signUpButton.setOnClickListener(v->{
                openRegistrationPage();
            });


            //GOOGLE RESUME SIGNED IN ACCOUNT
            gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            gsc= GoogleSignIn.getClient(this,gso);
            account = GoogleSignIn.getLastSignedInAccount(this);

            if(account!=null){
                checkAndInitializeUserData();
                openHomePage();

            }

            //GOOGLE SIGN IN BUTTON
            googleimage.setOnClickListener(v->{
                SignIn();
            });












    }


    @Override
    protected void onStart() {
        super.onStart();
        //TO LET HOMEPAGE REFER A METHOD IN MAINACTIVITY
        instance = this;
    }



    public static void onHomepageStart() {
        if (instance != null) {
            // Call the check() method or any other logic you want
            instance.checkAndInitializeUserData();
        }
    }




    //CHECK IF THIS IS NEW USER, IF DATA.B EMPTY, INITIALIZE DATABASE TO ALL NULL EXCEPT USERNAME
    public void checkAndInitializeUserData(){
        DocumentReference userDocRef = firestore.collection("Users").document(getEmailObject());

        userDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (!document.exists()) {
                    // Document doesn't exist, initialize the user data
                    initializeUserData(userDocRef);
                    setFirestoreUsername(getEmailObject().substring(0, getEmailObject().indexOf("@")));
                }
            } else {
                Log.e("MainActivity", "Error checking user data: ", task.getException());
            }
        });
    }

    //INITIALIZE DATABASE PROCESS FOR FIRST TIME LOGIN
    private void initializeUserData(DocumentReference userDocRef) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("username", null);
        userData.put("password", null);
        userData.put("university", null);
        userData.put("biography", null);
        userData.put("contributions",0);
        userData.put("engagements",0);
        userData.put("connections",0);
        userData.put("email",getEmailObject());

        userDocRef.set(userData)
                .addOnSuccessListener(aVoid -> Log.d("MainActivity", "User data initialized successfully"))
                .addOnFailureListener(e -> Log.e("MainActivity", "Error initializing user data: ", e));
    }


    public String getEmailObject(){
        account = GoogleSignIn.getLastSignedInAccount(this);
        if(account!=null){
            return account.getEmail();}
        return null;
    }





    private void SignIn(){
        Intent intent= gsc.getSignInIntent();
        startActivityForResult(intent,100);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==100){
            Task<GoogleSignInAccount> task= GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                task.getResult(ApiException.class);
                openHomePage();
            }catch(ApiException e){
                e.printStackTrace();
                Toast.makeText(this,"Login Successful",Toast.LENGTH_SHORT).show();
            }
        }
    }



    public void setFirestoreUsername(String username) {
        checkIfUsernameExists(username);
    }

    private void checkIfUsernameExists(String username) {
        firestore.collection("Users")
                .whereEqualTo("username", username)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (querySnapshot.isEmpty()) {
                        // NO SAME USERNAME EXISTS
                        addUsernameField(username);
                    } else {
                        // Document with the username exists, call the replaceWord method
                        setFirestoreUsername(RandomUsernameGenerator(username));
                    }
                })
                .addOnFailureListener(e -> {

                });
    }

    private void addUsernameField(String username) {
        Map<String, Object> data = new HashMap<>();
        data.put("username", username);

        firestore.collection("Users")
                .document(getEmailObject())
                .set(data, SetOptions.merge()) // Use set with merge to add a new field
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "Document added successfully for username: " + username);
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error adding document for username: " + username, e);
                });
    }






    public String RandomUsernameGenerator(String username){

        StringBuilder sb=new StringBuilder();
        Random random=new Random();
        sb.append(username);
        char randomChar = (char) ('A' + random.nextInt(26));
        sb.append(randomChar);


        return sb.toString();
    }














    // OPEN ACTIVITY HOMEPAGE
    public void openHomePage() {
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
        finish();
    }

    // OPEN ACTIVITY REGISTRATIONPAGE
    public void openRegistrationPage() {
        startActivity(new Intent(this, RegistrationPage.class));
        finish();
    }
}

