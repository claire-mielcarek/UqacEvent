package com.example.clair.uqacevent;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.example.clair.uqacevent.Calendar.CalendarFragment;
import com.example.clair.uqacevent.EventCreation.AddEventFragment;
import com.example.clair.uqacevent.Login.Connexion;
import com.example.clair.uqacevent.Login.IResultConnectUser;
import com.example.clair.uqacevent.Profile.ProfileFragment;
import com.example.clair.uqacevent.Profile.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView navigation;
    ActionBar actionBar;
    private FirebaseAuth mAuth;
    boolean isAlreadyInstantiated;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        actionBar = getSupportActionBar();
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        isAlreadyInstantiated = false;
    }

    @Override
    public void onStart() {
        super.onStart();
        User user;
        Log.d("[HOME_ACTIVITY]", "onStart");

        if(!this.isAlreadyInstantiated) {
            Log.d("[HOME_ACTIVITY]", "screen wasn't instantiated");
            this.isAlreadyInstantiated = true;



            // load user data if connected
            if (firebaseUser != null) {

                // get user data
                user = User.InstantiateUser();
                user.attachUserToFirebase(true, new IResultConnectUser() {
                    @Override
                    public void OnSuccess() {  // if operation is a success so show user's informations
                        // redefine good layout
                        //setContentView(R.layout.activity_main);
                        /*list = findViewById(R.id.home_publications);
                        adapter = new PostAdapter(context, listItems);
                        list.setAdapter(adapter);
                        addPostListener();
                        ((PostAdapter) list.getAdapter()).notifyDataSetChanged();*/
                    }

                    @Override
                    public void OnFailed() {
                        Log.w("DatabaseChange", "Failed to read values.");

                        // show message for user then reload connection
                        //Utils.MyMessageButton("Read personal value has failed.", context);
                    }
                });
            }
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment f;
            switch (item.getItemId()) {
                case R.id.navigation_profile:
                    if(mAuth.getCurrentUser() != null) {
                        actionBar.setTitle(R.string.title_profile);
                        f = new ProfileFragment();
                        openFragment(f);
                    }
                    else{
                        actionBar.setTitle(R.string.connexion);
                        f = new Connexion();
                        openFragment(f);
                    }
                    return true;
                case R.id.navigation_dashboard:
                    actionBar.setTitle(R.string.title_dashboard);
                    f = new DashboardFragment();
                    openFragment(f);
                    return true;
                case R.id.navigation_calendar:
                    actionBar.setTitle(R.string.title_calendar);
                    f = new CalendarFragment();
                    openFragment(f);
                    return true;
                case R.id.navigation_creation:
                    actionBar.setTitle(R.string.title_creation);
                    f = new AddEventFragment();
                    openFragment(f);
                    return true;
            }
            return false;
        }
    };


    private void openFragment(Fragment f){
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container,f);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
