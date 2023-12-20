package com.example.NoteSquad_TestApp;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.NoteSquad_TestApp.databinding.ActivityHomePageBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
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

        //INSTANTIATE THE FIRESTORE DATABASE
        firestore=FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();




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
            else if( R.id.item_2==no){ //ProfileFragment Button 4
                replaceFragment(new exploreFragment());
                return true;
            }
            else if( R.id.item_3==no){ //NoteShareFragment Button 2
                replaceFragment(new noteShareFragment());
                return true;
            }
            else if( R.id.item_4==no){ //ForumFragment Button 3
                replaceFragment(new forumFragment());
                return true;
            }
            else if( R.id.item_5==no){ //ProfileFragment Button 4
                replaceFragment(new profileFragment());
                return true;
            }
            else{ return false;}
        });

        setContentView(binding.getRoot());




        }

    @Override
    protected void onStart() {
        super.onStart();
        MainActivity.onHomepageStart();
    }



    public void replaceFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.constraintLayoutFragment,fragment);
        ft.commit();

    }

    public void GoogleSignOut() {
        gsc.signOut().addOnCompleteListener(v -> {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        });
    }






}




//__________________________________TIPS AREA ____________________________________________________________________________________



/*
If your code is correct and error still shows, rebuild project OR "File" > "Invalidate Caches / Restart..." and select "Invalidate and Restart."


________________________________________________________________________
     //HOW TO START AN ACTIVITY
    public final void OpenNoteShare() {
        Intent intent = new Intent(this, NoteSharepage.class);
        startActivity(intent);
    }
________________________________________________________________________
    //CREATE A FRAGMENT REPLACER METHOD
   private void replaceFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.constraintLayoutFragment,fragment);
        ft.commit();

    }
____________________________________________________________________
     //OPEN METHOD FROM ACTIVITY TO FRAGMENT(OVERCOME STATIC AND NON-STATIC ERROR)


       dog.setOnClickListener(v->{
          HomePage activity = (HomePage) getActivity();
            activity.replaceFragment(new catFragment());
        });





      if (getActivity() instanceof HomePage) {
                ((HomePage) getActivity()).replaceFragment(new visitProfileFragment());
            }
}

____________________________________________________________________
    //USED TO END THE ACTIVITY
    activity.finish();





_____________________________________________________________________
    //USE ANOTHER METHOD FROM ANOTHER ACTIVITY (without causing conflicts)
       ***Other Activity

       private static MainActivity instance;

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

=====================================

    ***Your Activity
     @Override
    protected void onStart() {
        super.onStart();

        MainActivity.onHomepageStart();
    }


________________________________________________________________________



//getActivity() is used for fragments, but instances are used for activity to activity
GetActivity()                                                                                   Activity-> fragment
instance                                                                                        Activity-> Activity
Fragment fragment = (Fragment1) getFragmentManager().findFragmentByTag("fragment1_tag");        fragment-> fragment
________________________________________________________________________


HomeFragment homefragment = (HomeFragment) getFragmentManager().findFragmentByTag("homefragment_tag");        fragment-> fragment

if (homefragment != null) {
    homefragment.use();
}



IMPORTANT FOR EXAMS SO FAR, START ACTIVITY(INTENT), FRAGMENT, BUTTON, EVENT LISTENERS, ACTIVITY BINDING
*/

