package com.example.clair.uqacevent.Calendar;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

import com.example.clair.uqacevent.Calendar.DayFragment;
import com.example.clair.uqacevent.R;

public class dayOnTouchListener implements OnTouchListener {
    int day;
    int month;
    int year;
    Activity activity;
    Button b;
    int currentDay;
    int currentMonth;
    int currentYear;

    public dayOnTouchListener(Activity activity, Button b, int currentDay, int currentMonth, int currentYear) {
        this.activity = activity;
        this.b = b;
        this.currentDay = currentDay;
        this.currentMonth = currentMonth;
        this.currentYear = currentYear;
        if ((b != null) && (b.getTag() != null)){
            int[] pickedDate = (int[]) b.getTag();
            day = pickedDate[0];
            month = pickedDate[1];
            year = pickedDate[2];
        }
    }

    private GestureDetector gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.d("[CALENDAR]", "Picked Date : " + day + month + year);

            Fragment f = new DayFragment();
            Bundle args = new Bundle();
            args.putInt("day", day);
            args.putInt("month", month);
            args.putInt("year", year);
            f.setArguments(args);
            activity.getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, f, "findThisFragment")
                    .addToBackStack(null)
                    .commit();
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (b != null) {
                if (year == currentYear
                        && month == currentMonth
                        && day == currentDay) {
                    b.setBackgroundColor(activity.getResources().getColor(R.color.pink));
                    b.setTextColor(Color.WHITE);
                } else {
                    b.setBackgroundColor(activity.getResources().getColor(R.color.grey));
                    if (b.getCurrentTextColor() != Color.RED) {
                        b.setTextColor(Color.WHITE);
                    }
                }
                return false;
            }
            else {
                return true;
            }
        }
    });

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        gestureDetector.onTouchEvent(motionEvent);
        return true;
    }
}
