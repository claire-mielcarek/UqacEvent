package com.example.clair.uqacevent.Dashboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.ArrayList;
import com.example.clair.uqacevent.R;

public class FilterAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> organizers;
    private ArrayList<Boolean> filters;

    FilterAdapter(Context context, ArrayList<String> organizers, ArrayList<Boolean> filters) {
        this.context = context;
        this.organizers = organizers;
        this.filters = filters;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.dashboard_filter_item, null);
            TextView t = convertView.findViewById(R.id.filter_list_text);
            t.setText(organizers.get(position));
            CheckBox box = convertView.findViewById(R.id.filter_list_checkbox);
            if (filters.get(position)){
                box.setChecked(false);
            }
            box.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    filters.set(position, !filters.get(position));
                    Log.d("[FILTER_ADAPTER]", organizers.get(position) + " " + filters.get(position));
                }
            });
        }
        return convertView;
    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Object getItem(int i) {
        return organizers.get(i);
    }

    @Override
    public int getCount() {
        return organizers.size();
    }

}
