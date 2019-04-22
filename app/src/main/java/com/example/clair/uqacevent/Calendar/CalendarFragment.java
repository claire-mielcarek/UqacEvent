package com.example.clair.uqacevent.Calendar;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.clair.uqacevent.R;

public class CalendarFragment extends Fragment {
    private String currentgroupId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.calendar_fragment, container, false);
    }

    public void setCurrentgroupId(String currentgroupId) {
        this.currentgroupId = currentgroupId;
    }
}
