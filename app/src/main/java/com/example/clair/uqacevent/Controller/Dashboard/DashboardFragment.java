package com.example.clair.uqacevent.Controller.Dashboard;

import android.app.Activity;
import android.os.Bundle;
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
import android.widget.ListView;

import com.example.clair.uqacevent.Model.Event;
import com.example.clair.uqacevent.Controller.EventAdapter;
import com.example.clair.uqacevent.Controller.MainActivity;
import com.example.clair.uqacevent.Model.User;
import com.example.clair.uqacevent.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class DashboardFragment extends Fragment {

    protected FirebaseAuth auth;
    DatabaseReference data;
    private DatabaseReference eventsRef;
    EventAdapter eventAdapter;
    ListView list;
    ArrayList<String> filteredOrganizersIds;
    Activity activity;


    ArrayList<Event> events;
    ArrayList<String> eventsKey;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = Objects.requireNonNull(getActivity());
        activity.setTitle(getTag());
        return inflater.inflate(R.layout.dashboard, container, false);
    }


    public void displayListEvents() {
        eventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot event : dataSnapshot.getChildren()) {
                        String newsTitle = (String) event.child("title").getValue();
                        String newsDescription = (String) event.child("description").getValue();
                        String newsDate = (String) event.child("date").getValue();
                        String newsPlace = (String) event.child("place").getValue();
                        String newsOrganizer = (String) event.child("organizer").getValue();
                        String newsType = (String) event.child("type").getValue();
                        String newsOrganizerId = (String) event.child("organizerId").getValue();
                        String postingTime = (String) event.child("postingTime").getValue();

                        String key = event.getKey();
                        String organizerkey = (String) event.child("organizerId").getValue();
                        if (!eventsKey.contains(key) && !filteredOrganizersIds.contains(organizerkey)) {
                            eventsKey.add(key);
                            events.add(new Event(newsDate, newsDescription, newsPlace, newsTitle, newsOrganizer, newsType, newsOrganizerId, postingTime));
                        }
                        if (eventsKey.contains(key) && filteredOrganizersIds.contains(organizerkey)) {
                            int id = eventsKey.indexOf(key);
                            eventsKey.remove(id);
                            events.remove(id);
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onStart() {
        super.onStart();

        setHasOptionsMenu(true);
        auth = FirebaseAuth.getInstance();
        data = FirebaseDatabase.getInstance().getReference();
        eventsRef = data.child("Events").getRef();
        events = new ArrayList<>();
        eventsKey = new ArrayList<>();
        filteredOrganizersIds = new ArrayList<>();
        if (User.getCurrentUser() != null) {
            filteredOrganizersIds = User.getCurrentUser().getFilteredOrganizersIds();
        }
        Log.d("[DASHBOARD]", filteredOrganizersIds.toString());
        eventAdapter = new EventAdapter(getActivity(), events);
        list = activity.findViewById(R.id.list_events_dashboard);
        displayListEvents();

        list.setAdapter(eventAdapter);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        User user = User.getCurrentUser();
        if (user != null) {
            inflater.inflate(R.menu.filters, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_filter:
                Fragment f = new FilterFragment();
                ((MainActivity) activity).openFragment(f, getString(R.string.filters));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
