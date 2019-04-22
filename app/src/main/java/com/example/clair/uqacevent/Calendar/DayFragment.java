package com.example.clair.uqacevent.Calendar;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.clair.uqacevent.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DayFragment extends Fragment {
    int day;
    int month;
    int year;
    ListView list;
    DatabaseReference data;
    private ValueEventListener listListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.calendar_day, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle b = getArguments();
        day = b.getInt("day");
        month = b.getInt("month");
        year = b.getInt("year");


        data = FirebaseDatabase.getInstance().getReference();

        list = getActivity().findViewById(R.id.calendar_list_events);
        TextView title = getActivity().findViewById(R.id.calendar_day_title);
        title.setText(day + " " + Calendar.FR_MONTH_NAMES[month] + " " + year);
        addListEventListener();
    }

    private void addListEventListener() {
        listListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot event : dataSnapshot.child("Events").getChildren()){
                    String s = (String) event.child("date").getValue();
                    Log.d("[DAY]", "event date : " + s );
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }
}
