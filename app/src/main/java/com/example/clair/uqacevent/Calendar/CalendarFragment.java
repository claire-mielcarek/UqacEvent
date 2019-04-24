package com.example.clair.uqacevent.Calendar;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.clair.uqacevent.R;

import java.util.Objects;

public class CalendarFragment extends Fragment {
    ViewGroup calendar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(getActivity()).setTitle(getTag());
        return inflater.inflate(R.layout.calendar_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        ViewGroup calendar_container = Objects.requireNonNull(getActivity()).findViewById(R.id.calendar_container);
        calendar = new Calendar(getContext());
        calendar_container.addView(calendar);

    }
    }
