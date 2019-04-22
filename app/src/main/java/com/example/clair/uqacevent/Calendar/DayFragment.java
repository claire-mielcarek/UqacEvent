package com.example.clair.uqacevent.Calendar;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.clair.uqacevent.R;

public class DayFragment extends Fragment {
    int day;
    int month;
    int year;

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

        TextView title = getActivity().findViewById(R.id.calendar_day_title);
        title.setText(day + " " + Calendar.FR_MONTH_NAMES[month] + " " + year);
    }
}
