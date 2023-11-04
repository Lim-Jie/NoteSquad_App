package com.example.NoteSquad_TestApp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // LOGIN PAGE FOR USERNAME, PASSWORD, AND BUTTON
        TextView username = findViewById(R.id.EmailAddress);
        TextView password = findViewById(R.id.Password);
        Button loginButton = findViewById(R.id.LoginButton);
        Button signUpButton = findViewById(R.id.SignUpButton);

        // SET THE FIREBASE VERIFICATION HERE
        loginButton.setOnClickListener(v-> {
            if (username.getText().toString().equals("Li Jie") && password.getText().toString().equals("123")) {
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                openHomePage();
            } else {
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
            }
        });

        // SIGN UP BUTTON FOR REGISTRATION PAGE
        signUpButton.setOnClickListener(v->{
            openHomePage();
        });
    }

    // LITERALLY OPEN THE HOME PAGE
    public void openHomePage() {
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
        finish();
    }

    // LITERALLY OPEN THE REGISTRATION PAGE
    public void openRegistrationPage() {
        Intent intent = new Intent(this, RegistrationPage.class);
        startActivity(intent);
        finish();
    }
}