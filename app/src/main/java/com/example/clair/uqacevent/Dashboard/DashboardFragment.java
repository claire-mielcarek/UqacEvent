package com.example.clair.uqacevent.Dashboard;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.clair.uqacevent.Calendar.Event;
import com.example.clair.uqacevent.Calendar.EventAdapter;
import com.example.clair.uqacevent.Login.Connexion;
import com.example.clair.uqacevent.MainActivity;
import com.example.clair.uqacevent.Profile.User;
import com.example.clair.uqacevent.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class DashboardFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private User user;
    DatabaseReference data;
    private DatabaseReference eventsRef;
    EventAdapter eventAdapter;
    ListView list;


    ArrayList<Event> events;
    ArrayList<String> eventsKey;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle(getTag());
        return inflater.inflate(R.layout.dashboard, container, false);
    }


    public void displayListEvents(){
        eventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for(DataSnapshot event: dataSnapshot.getChildren()) {
                        String newsTitle = (String) event.child("title").getValue();
                        String newsDescription = (String) event.child("description").getValue();
                        String newsDate = (String) event.child("date").getValue();
                        String newsPlace = (String) event.child("place").getValue();
                        String newsOrganizer = (String) event.child("organizer").getValue();
                        String newsType = (String) event.child("type").getValue();
                        String newsOrganizerId = (String) event.child("organizerId").getValue();
                        String postingTime = (String) event.child("postingTime").getValue();

                        String key = event.getKey();
                        if (!eventsKey.contains(key)) {
                            eventsKey.add(key);
                            events.add(new Event(newsDate, newsDescription, newsPlace, newsTitle, newsOrganizer, newsType, newsOrganizerId, postingTime));
                        }
                    }
                }
                eventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Read Events failure: ", databaseError.getMessage());
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
    }

    @Override
    public void onStart(){
        super.onStart();

        setHasOptionsMenu(true);
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        data = FirebaseDatabase.getInstance().getReference();
        eventsRef = data.child("Events").getRef();
        events = new ArrayList<>();
        eventsKey = new ArrayList<>();
        eventAdapter = new EventAdapter(getActivity(), events);
        list = getActivity().findViewById(R.id.list_events_dashboard);
        displayListEvents();

        list.setAdapter(eventAdapter);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.filters, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_filter:
                Fragment f = new FilterFragment();
                ((MainActivity) getActivity()).openFragment(f, getString(R.string.filters));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
