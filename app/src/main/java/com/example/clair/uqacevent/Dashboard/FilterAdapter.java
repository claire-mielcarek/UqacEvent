package com.example.clair.uqacevent.Dashboard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.util.List;

public class FilterAdapter extends ArrayAdapter<String> {
    public FilterAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
    }
}
