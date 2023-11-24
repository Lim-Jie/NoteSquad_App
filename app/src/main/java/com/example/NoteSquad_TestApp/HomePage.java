package com.example.NoteSquad_TestApp;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.NoteSquad_TestApp.databinding.ActivityHomePageBinding;
import com.example.NoteSquad_TestApp.databinding.ActivityMainBinding;
import com.example.NoteSquad_TestApp.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.navigation.NavigationBarView;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.NoteSquad_TestApp.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Nullable;


public final class HomePage extends AppCompatActivity {
    //change build.gradle to include buildFeatures{ viewBinding=true }
    private ActivityHomePageBinding binding;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


        firestore=FirebaseFirestore.getInstance();


        //SETS HOMEPAGE FRAGMENT AS DEFAULT FRAGMENT WHEN STARTING ACTIVITY (SAVEDINSTANCESTATE==NULL)
        if (savedInstanceState == null) {
            homePageFragment defaultFragment = new homePageFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.constraintLayoutFragment, defaultFragment)
                    .commit();
        }


        //GOOGLE FIREBASE SETTINGS
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this, gso);


        //SWITCH FRAGMENT ON BOTTOM NAVIGATION BAR
        binding= ActivityHomePageBinding.inflate(getLayoutInflater());
        binding.bottom.setOnItemSelectedListener(item -> {
            int no=item.getItemId();

            if(R.id.item_1==no){ //HomePageFragment Button 1 (All logics are moved to Fragment instead of activity)
                replaceFragment(new homePageFragment());
                return true;
            }
            else if( R.id.item_2==no){ //NoteShareFragment Button 2
                replaceFragment(new noteShareFragment());
                return true;
            }
            else if( R.id.item_3==no){ //ForumFragment Button 3
                return true;
            }
            else if( R.id.item_4==no){ //ProfileFragment Button 4
                replaceFragment(new profileFragment());
                return true;
            }
            else{ return false;}
        });

        setContentView(binding.getRoot());



        }



















    public void replaceFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.constraintLayoutFragment,fragment);
        ft.commit();

    }




    public void SignOut() {
        gsc.signOut().addOnCompleteListener(v -> {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        });
    }






}




//__________________________________TIPS AREA ____________________________________________________________________________________



/*
If your code is correct and error still shows, rebuild project OR "File" > "Invalidate Caches / Restart..." and select "Invalidate and Restart."



     //HOW TO START AN ACTIVITY
    public final void OpenNoteShare() {
        Intent intent = new Intent(this, NoteSharepage.class);
        startActivity(intent);
    }

    //CREATE A FRAGMENT REPLACER METHOD
   private void replaceFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.constraintLayoutFragment,fragment);
        ft.commit();

    }

     //OPEN METHOD FROM ANOTHER ACTIVITY (OVERCOME STATIC AND NON-STATIC ERROR)


       dog.setOnClickListener(v->{
          HomePage activity = (HomePage) getActivity();
            activity.replaceFragment(new catFragment());
        });


    //USED TO END THE ACTIVITY
    activity.finish();



IMPORTANT FOR EXAMS SO FAR, START ACTIVITY(INTENT), FRAGMENT, BUTTON, EVENT LISTENERS, ACTIVITY BINDING
*/

