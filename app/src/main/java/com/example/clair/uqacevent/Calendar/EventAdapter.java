package com.example.clair.uqacevent.Calendar;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import com.example.clair.uqacevent.R;

public class EventAdapter extends BaseAdapter{
    private ArrayList<Event> data;
    private Context context;

    public EventAdapter(Context context, ArrayList<Event> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Event e = (Event) getItem(i);
        Log.d("[ EVENT_ADAPTER ]", e.toString());
        String date = e.getDate();
        String description = e.getDescription();
        String organizer = e.getOrganizer();
        String place = e.getPlace();
        String title = e.getTitle();
        if (view ==null){
            view = LayoutInflater.from(context).inflate(R.layout.event, viewGroup, false);
        }

        TextView titleView = view.findViewById(R.id.news_title);
        TextView descrView = view.findViewById(R.id.news_description);
        TextView dateView = view.findViewById(R.id.news_date);
        TextView placeView = view.findViewById(R.id.news_place);
        TextView organizerView = view.findViewById(R.id.news_organizer);
        titleView.setText(title);
        descrView.setText(description);
        dateView.setText(date);
        placeView.setText(place);
        organizerView.setText(organizer);

        return view;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        Log.d("[EVENT_ADAPTER]", "notifyDataSetChanged : " + data.toString());
    }
}
