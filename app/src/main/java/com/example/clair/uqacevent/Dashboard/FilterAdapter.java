package com.example.clair.uqacevent.Dashboard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.clair.uqacevent.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class FilterAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> organizers;
    ArrayList<Boolean> filters;

    public FilterAdapter(Context context, ArrayList<String> organizers, ArrayList<Boolean> filters) {
        this.context = context;
        this.organizers = organizers;
        this.filters = filters;
    }

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
