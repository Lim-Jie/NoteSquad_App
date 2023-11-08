package com.example.NoteSquad_TestApp;
import android.content.Intent;
import android.os.Bundle;
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

import org.jetbrains.annotations.Nullable;


public final class RegistrationPage extends AppCompatActivity {


    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_registration_page);




        //INSTANTIATE REGISTER BUTTON OBJECT
        Button registerButton=(Button)findViewById(R.id.registerButton);
        TextView username=findViewById(R.id.EmailAddress);
        TextView password=findViewById(R.id.Password);
        TextView confirmPassword=findViewById(R.id.confirmPassword);
        Button logoutButton= (Button)findViewById(R.id.logoutButton);
        Button goBacktoLoginPage= (Button) findViewById(R.id.GoBackToLoginPageButton);
        //TextView username = (TextView)findViewById(R.id.EmailAddress); Whats the difference between this??


        goBacktoLoginPage.setOnClickListener(v->{
            openLoginPage();
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
}






