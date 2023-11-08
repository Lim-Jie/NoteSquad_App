package com.example.NoteSquad_TestApp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    FirebaseFirestore firestore;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            FirebaseApp.initializeApp(this);


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

            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            if(account!=null){
                String name= account.getDisplayName();
                String Mail= account.getEmail();
                openHomePage();
            }

            //GOOGLE SIGN IN BUTTON
            googleimage.setOnClickListener(v->{
                SignIn();
            });






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
                Toast.makeText(this,"Google Sign Up Error",Toast.LENGTH_SHORT).show();
            }
        }
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