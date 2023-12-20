package com.example.NoteSquad_TestApp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;


public final class RegistrationPage extends AppCompatActivity {


    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    FirebaseFirestore Firestore;
    private String TypedEmailString;
    private Button registerButton;
    private TextView username;
    private TextView password;
    private TextView confirmPassword;

    private Button goBacktoLoginPage;


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_registration_page);

        Firestore= FirebaseFirestore.getInstance();



        //INSTANTIATE REGISTER BUTTON OBJECT
       registerButton =(Button)findViewById(R.id.registerButton);
       username=findViewById(R.id.editTextTextEmailAddress);
       password=findViewById(R.id.PasswordRegistration);
       confirmPassword=findViewById(R.id.confirmPassword);
       goBacktoLoginPage= (Button) findViewById(R.id.GoBackToLoginPageButton);
        //TextView username = (TextView)findViewById(R.id.EmailAddress); Whats the difference between this??


        goBacktoLoginPage.setOnClickListener(v->{
            openLoginPage();
        });

        registerButton.setOnClickListener(v -> {
            TypedEmailString = username.getText().toString().trim(); // Trim to remove leading/trailing spaces

            if (!TypedEmailString.isEmpty()) {
                CheckIfUsernameExist(TypedEmailString, new CheckIfUsernameExistCallback() {
                    @Override
                    public void onCheckIfUsernameExist(boolean finishedOperation) {
                        if(finishedOperation){

                           //TODO: Open The HomePage



                        }
                    }
                });

            } else {
                // Handle case where email is empty
                Toast.makeText(RegistrationPage.this, "Please enter an email", Toast.LENGTH_SHORT).show();
            }
        });

    }




 public interface CheckIfUsernameExistCallback{void onCheckIfUsernameExist(boolean finishedOperation);}

    public void CheckIfUsernameExist(String email, CheckIfUsernameExistCallback callback) {
        callback.onCheckIfUsernameExist(false);

        DocumentReference documentReference = Firestore.collection("Users").document(email);

        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();

                if (document.exists()) {
                    Toast.makeText(RegistrationPage.this, "Email Username has already been used, account currently exists", Toast.LENGTH_SHORT).show();
                    Log.d("Registration", "Document already exists!");
                } else {
                    initializeUserData(documentReference);
                    Toast.makeText(RegistrationPage.this, "Successfully Registered account"+TypedEmailString, Toast.LENGTH_SHORT).show();
                    callback.onCheckIfUsernameExist(true);
                }
            } else {
                // An error occurred
                Log.e("Firestore", "Error getting document: ", task.getException());
            }
        });

    }






    public void openHomePage() {
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
        finish();
    }




    public void openLoginPage(){
        Intent intent= new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }


    public interface CheckIfPasswordsAreMatchingCallback{void onCheckIfPasswordsAreMatching(boolean matching);}
    public void CheckIfPasswordsAreMatching(CheckIfPasswordsAreMatchingCallback callback){

        if(password.getText().toString().equals(confirmPassword.getText().toString())){
            callback.onCheckIfPasswordsAreMatching(true);
        }else{
            callback.onCheckIfPasswordsAreMatching(false);
        }
    }



    //INITIALIZE DATABASE PROCESS FOR FIRST TIME LOGIN
    private void initializeUserData(DocumentReference userDocRef) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("username", TypedEmailString);
        userData.put("password", password.getText().toString());
        userData.put("university", null);
        userData.put("biography", null);
        userData.put("contributions",0);
        userData.put("engagements",0);
        userData.put("connections",0);
        userData.put("email",TypedEmailString);

        userDocRef.set(userData)
                .addOnSuccessListener(aVoid -> Log.d("MainActivity", "User data initialized successfully"))
                .addOnFailureListener(e -> Log.e("MainActivity", "Error initializing user data: ", e));
    }

}


// setFirestoreUsername();



