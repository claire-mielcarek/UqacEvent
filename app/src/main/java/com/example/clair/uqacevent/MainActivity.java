package com.example.clair.uqacevent;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView navigation;
    ActionBar actionBar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment f;
            switch (item.getItemId()) {
                case R.id.navigation_profile:
                    actionBar.setTitle(R.string.title_profile);
                    f = new ProfileFragment();
                    openFragment(f);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        actionBar = getSupportActionBar();
    }

    private void openFragment(Fragment f){
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container,f);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
