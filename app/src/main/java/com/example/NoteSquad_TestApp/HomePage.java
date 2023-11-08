package com.example.NoteSquad_TestApp;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import org.jetbrains.annotations.Nullable;


public final class HomePage extends AppCompatActivity {

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_home_page);

        //BUTTON OBJECT CREATION
        Button openNoteShareButton = (Button)this.findViewById(R.id.NoteShareButton);
        Button logoutButton= (Button)findViewById(R.id.logoutButton);



        openNoteShareButton.setOnClickListener(v->{
            HomePage.this.OpenNoteShare();
        });
        logoutButton.setOnClickListener(v->{
            SignOut();
        });



        gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        gsc= GoogleSignIn.getClient(this,gso);



    }

    public final void OpenNoteShare() {
        Intent intent = new Intent(this, NoteSharepage.class);
        startActivity(intent);
    }


    private void SignOut(){
        gsc.signOut().addOnCompleteListener(v->{
            finish();
            startActivity(new Intent(this, MainActivity.class));
        });
    }



}
