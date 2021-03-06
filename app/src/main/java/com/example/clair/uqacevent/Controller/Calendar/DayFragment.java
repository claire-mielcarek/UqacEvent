package com.example.clair.uqacevent.Controller.Calendar;


import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.clair.uqacevent.Controller.EventAdapter;
import com.example.clair.uqacevent.Model.Event;
import com.example.clair.uqacevent.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class DayFragment extends Fragment {
    int day;
    int month;
    int year;
    ListView list;
    DatabaseReference data;
    ArrayList<Event> events;
    ArrayList<String> eventsKey;
    EventAdapter listAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Objects.requireNonNull(getActivity()).setTitle(getTag());
        return inflater.inflate(R.layout.calendar_day, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle b = getArguments();
        assert b != null;
        day = b.getInt("day");
        month = b.getInt("month");
        year = b.getInt("year");

        events = new ArrayList<>();
        eventsKey = new ArrayList<>();
        data = FirebaseDatabase.getInstance().getReference();

        listAdapter = new EventAdapter(getActivity(), events);
        list = Objects.requireNonNull(getActivity()).findViewById(R.id.calendar_list_events);
        getActivity().setTitle(day + " " + Calendar.FR_MONTH_NAMES[month] + " " + year);
        addListEventListener();
        if (list != null) {
            list.setAdapter(listAdapter);
        }
        else{
            Log.d("[DAY]", "L'objet list à modifier est null");
        }
    }

    private void addListEventListener() {
        ValueEventListener listListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot event : dataSnapshot.child("Events").getChildren()) {
                    String s = (String) event.child("date").getValue();
                    String d = (String) event.child("description").getValue();
                    String p = (String) event.child("place").getValue();
                    String t = (String) event.child("title").getValue();
                    String o = (String) event.child("organizer").getValue();
                    String type = (String) event.child("type").getValue();
                    String id = (String) event.child("organizerId").getValue();
                    String pt = (String) event.child("postingTime").getValue();
                    String key = event.getKey();
                    if (isCurrentDate(s) && !eventsKey.contains(key)) {
                        eventsKey.add(event.getKey());
                        events.add(new Event(s, d, p, t, o, type, id, pt));
                    }
                    Log.d("[DAY]", "event date : " + s);
                }
                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        data.addValueEventListener(listListener);
    }

    boolean isCurrentDate(String dataBaseDate){
        boolean ret;
        String[] date = dataBaseDate.split("/");
        ret = (Integer.parseInt(date[0]) == day) && (Integer.parseInt(date[1]) == month + 1) && (Integer.parseInt(date[2])==year);
        return ret;
    }
}
