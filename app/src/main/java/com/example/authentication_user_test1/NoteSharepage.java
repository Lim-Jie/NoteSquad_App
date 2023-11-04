package com.example.authentication_user_test1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import org.jetbrains.annotations.Nullable;

public final class NoteSharepage extends AppCompatActivity {
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_note_sharepage);



        //INSTANTIATE BUTTON AND OTHER OBJECTS
        Button backButton = (Button) this.findViewById(R.id.BackButton);





        //SETCLICKLISTENER TO OPEN HOMEPAGE WHEN BUTTON CLICKED
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomePage.class);
            startActivity(intent);
            finish();
        });




    }


}