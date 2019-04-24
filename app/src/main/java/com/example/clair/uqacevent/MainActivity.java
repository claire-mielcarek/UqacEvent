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
import android.view.Menu;
import android.view.MenuItem;

import com.example.clair.uqacevent.Calendar.CalendarFragment;
import com.example.clair.uqacevent.Dashboard.DashboardFragment;
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

        if (!this.isAlreadyInstantiated) {
            Log.d("[HOME_ACTIVITY]", "screen wasn't instantiated");
            this.isAlreadyInstantiated = true;

            // load user data if connected
            if (firebaseUser != null) {
                // get user data
                user = User.InstantiateUser();
                user.attachUserToFirebase(true, new IResultConnectUser() {
                    @Override
                    public void OnSuccess() {
                    }

                    @Override
                    public void OnFailed() {
                        Log.w("DatabaseChange", "Failed to read values.");
                    }
                });
            }
        }

        Fragment f = new DashboardFragment();
        openFragment(f, getString(R.string.title_dashboard));
        createMenu();
    }

    /**
     * Ajoute les items du menu de navigation
     * Si b est vrai, on ajoute aussi l'item de création d'events
     */
    private void createMenu() {
        navigation.getMenu().removeItem(R.id.navigation_dashboard);
        navigation.getMenu().removeItem(R.id.navigation_calendar);
        navigation.getMenu().removeItem(R.id.navigation_creation);
        navigation.getMenu().removeItem(R.id.navigation_profile);
        navigation.getMenu().add(Menu.NONE, R.id.navigation_dashboard, 1, R.string.title_dashboard).setIcon(R.drawable.ic_dashboard_black_24dp);
        navigation.getMenu().add(Menu.NONE, R.id.navigation_calendar, 2, R.string.title_calendar).setIcon(R.drawable.ic_date_range_black_24dp);
        if ((firebaseUser != null) && User.getCurrentUser().isPublicAccount()) {
            navigation.getMenu().add(Menu.NONE, R.id.navigation_creation, 3, R.string.title_creation).setIcon(R.drawable.ic_add_black_24dp);
        }
        navigation.getMenu().add(Menu.NONE, R.id.navigation_profile, 4, R.string.title_profile).setIcon(R.drawable.ic_person_black_24dp);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment f;
            switch (item.getItemId()) {
                case R.id.navigation_profile:
                    if (mAuth.getCurrentUser() != null) {
                        f = new ProfileFragment();
                        openFragment(f, getString(R.string.title_profile));
                    } else {
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
        invalidateOptionsMenu();
    }

    public void addCreationMenuItem() {
        MenuItem m = navigation.getMenu().findItem(R.id.navigation_creation);
        if (m == null) {
            navigation.getMenu().add(Menu.NONE, R.id.navigation_creation, 3, R.string.title_creation).setIcon(R.drawable.ic_add_black_24dp);
        }
    }

    public void deleteCreationMenuItem() {
        MenuItem m = navigation.getMenu().findItem(R.id.navigation_creation);
        if (m != null) {
            navigation.getMenu().removeItem(R.id.navigation_creation);
        }
    }
}
