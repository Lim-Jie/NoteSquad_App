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
        Button googleimage=(Button)findViewById(R.id.GoogleRegisterButton);
        Button logoutButton= (Button)findViewById(R.id.logoutButton);

        //TextView username = (TextView)findViewById(R.id.EmailAddress); Whats the difference between this??



        //GOOGLE SIGN IN USING GMAIL

        gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        gsc= GoogleSignIn.getClient(this,gso);


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



    public void openHomePage() {
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
        finish();
    }
}







/*
//REGISTER USERNAME AND PASSWORD IN HASHMAP

registerButton.setOnClickListener(v-> {


            FirebaseAuth auth= FirebaseAuth.getInstance();

            String usernameString= username.getText().toString();
            String passwordString= password.getText().toString();
            String confirmPasswordString= confirmPassword.getText().toString();

            if(usernameString.isEmpty() || passwordString.isEmpty() || confirmPasswordString.isEmpty()){
                Toast.makeText(this, "Please fill in all fields",Toast.LENGTH_SHORT).show();
            }else if(!passwordString.equals(confirmPasswordString)){
                Toast.makeText(this,"Passwords do not match",Toast.LENGTH_SHORT).show();
            }else{
              auth.createUserWithEmailAndPassword(usernameString, passwordString).addOnCompleteListener(this, task->{
                          if(task.isSuccessful()){
                              Toast.makeText(this,"Registration successful",Toast.LENGTH_SHORT).show();
                              FirebaseUser firebaseUser = auth.getCurrentUser();

                              firebaseUser.sendEmailVerification();
                              Intent intent= new Intent(this,HomePage.class);

                          }else{
                              Toast.makeText(this,"Registration failed", Toast.LENGTH_SHORT).show();
                          }
                  }
              );
            }
        });

*/

