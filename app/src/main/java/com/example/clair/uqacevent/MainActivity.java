package com.example.clair.uqacevent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.example.clair.uqacevent.Calendar.CalendarFragment;
import com.example.clair.uqacevent.Login.Connexion;
import com.example.clair.uqacevent.Login.IResultConnectUser;
import com.example.clair.uqacevent.Profile.ProfileFragment;
import com.example.clair.uqacevent.Profile.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity{
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
                        f = new ProfileFragment();
                        openFragment(f, getString(R.string.title_profile));
                    }
                    else{
                        f = new Connexion();
                        openFragment(f, getString(R.string.connexion));
                    }
                    return true;
                case R.id.navigation_dashboard:
                    f = new DashboardFragment();
                    openFragment(f, getString(R.string.title_dashboard));
                    return true;
                case R.id.navigation_calendar:
                    f = new CalendarFragment();
                    openFragment(f, getString(R.string.title_calendar));
                    return true;
                case R.id.navigation_creation:
                    f = new AddEventFragment();
                    openFragment(f, getString(R.string.title_creation));
                    return true;
            }
            return false;
        }
    };


    public void openFragment(Fragment f, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, f, tag);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
