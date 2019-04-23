package com.example.clair.uqacevent.Calendar;

//https://github.com/avi-kr/SimpleCalendarTemplate

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.clair.uqacevent.MainActivity;
import com.example.clair.uqacevent.R;

/**
 * Created by abhishekkumar on 15/01/17.
 */

public class Calendar extends LinearLayout {

    private static final String CUSTOM_GREY = "#a0a0a0";
    public static final String[] FR_MONTH_NAMES = {"Janvier", "Février", "Mars", "Avril",
            "Mai", "Juin", "Juillet", "Aout", "Septembre", "Octobre", "Novembre", "Decembre"};
    private static final int WEEK_SIZE = 7;
    private static final int MONTH_NUMBER = 12;

    private Button selectedDayButton;
    private Button[] days;
    private int firstDayOfTheWeek;
    LinearLayout weekOneLayout;
    LinearLayout weekTwoLayout;
    LinearLayout weekThreeLayout;
    LinearLayout weekFourLayout;
    LinearLayout weekFiveLayout;
    LinearLayout weekSixLayout;
    private LinearLayout[] weeks;
    TextView currentDate;

    View calendarView;

    MainActivity activity;

    private int currentDateDay, chosenDateDay, currentDateMonth,
            chosenDateMonth, currentDateYear, chosenDateYear,
            pickedDateDay, pickedDateMonth, pickedDateYear;
    int userMonth, userYear;

    private java.util.Calendar calendar;
    LayoutParams defaultButtonParams;

    public Calendar(Context context) {
        super(context);
        init(context);
    }

    public Calendar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Calendar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        firstDayOfTheWeek = java.util.Calendar.MONDAY;
        activity = (MainActivity) context;

        calendarView = inflate(getContext(), R.layout.calendar, this);
        calendar = java.util.Calendar.getInstance();

        weekOneLayout = calendarView.findViewById(R.id.calendar_week_1);
        weekTwoLayout = calendarView.findViewById(R.id.calendar_week_2);
        weekThreeLayout = calendarView.findViewById(R.id.calendar_week_3);
        weekFourLayout = calendarView.findViewById(R.id.calendar_week_4);
        weekFiveLayout = calendarView.findViewById(R.id.calendar_week_5);
        weekSixLayout = calendarView.findViewById(R.id.calendar_week_6);
        currentDate = calendarView.findViewById(R.id.current_date);

        currentDateDay = chosenDateDay = calendar.get(java.util.Calendar.DAY_OF_MONTH);

        if (userMonth != 0 && userYear != 0) {
            currentDateMonth = chosenDateMonth = userMonth;
            currentDateYear = chosenDateYear = userYear;
        } else {
            currentDateMonth = chosenDateMonth = calendar.get(java.util.Calendar.MONTH);
            currentDateYear = chosenDateYear = calendar.get(java.util.Calendar.YEAR);
        }

        setCurrentDateText();

        initializeDaysWeeks();
        defaultButtonParams = getdaysLayoutParams();
        addDaysinCalendar(defaultButtonParams, context, metrics);

        initCalendarWithDate(chosenDateYear, chosenDateMonth, chosenDateDay);

        addNavigationListener();


    }

    private void setCurrentDateText() {
        Log.d("[CALENDAR]", "month title set");
        String day = FR_MONTH_NAMES[chosenDateMonth] + " " + chosenDateYear;
        currentDate.setText(day);
    }

    private void addNavigationListener() {
        ImageButton leftBtn = calendarView.findViewById(R.id.chevron_left);
        ImageButton rightBtn = calendarView.findViewById(R.id.chevron_right);

        addListener(-1, leftBtn);
        addListener(1, rightBtn);
    }

    private void addListener(final int i, ImageButton btn) {
        if (btn != null) {
            btn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(((chosenDateMonth == java.util.Calendar.DECEMBER) && (i==1)) ||
                            ((chosenDateMonth == java.util.Calendar.JANUARY) && (i== -1))){
                        chosenDateYear = chosenDateYear + i;
                    }
                    chosenDateMonth = modulo(chosenDateMonth + i, MONTH_NUMBER);
                    initCalendarWithDate(chosenDateYear, chosenDateMonth, chosenDateDay);
                    setCurrentDateText();
                    Log.d("[CALENDAR]", "New chosen month : " + chosenDateMonth);
                }
            });
        }
        else
            Log.d("[CALENDAR]", "onClickListener can't be set, because button is null");
    }

    private void initializeDaysWeeks() {
        weeks = new LinearLayout[6];
        days = new Button[6 * 7];

        weeks[0] = weekOneLayout;
        weeks[1] = weekTwoLayout;
        weeks[2] = weekThreeLayout;
        weeks[3] = weekFourLayout;
        weeks[4] = weekFiveLayout;
        weeks[5] = weekSixLayout;
    }

    private void initCalendarWithDate(int year, int month, int day) {
        if (calendar == null)
            calendar = java.util.Calendar.getInstance();
        calendar.set(year, month, day);
        calendar.setFirstDayOfWeek(this.firstDayOfTheWeek);

        int daysInCurrentMonth = calendar.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
        Log.d("[CALENDAR]", "nombre de jours dans le mois : " + daysInCurrentMonth);

        chosenDateYear = year;
        chosenDateMonth = month;
        chosenDateDay = day;

        calendar.set(year, month, 1);
        int firstDayOfCurrentMonth = calendar.get(java.util.Calendar.DAY_OF_WEEK);
        Log.d("[CALENDAR]", "premier jour du mois : " + firstDayOfCurrentMonth);
        Log.d("[CALENDAR]", "premier jour de la semaine : " + calendar.getFirstDayOfWeek());

        calendar.set(year, month, calendar.getActualMaximum(java.util.Calendar.DAY_OF_MONTH));

        int dayNumber = 1;
        int daysLeftInFirstWeek;
        int indexOfDayAfterLastDayOfMonth;
        int indexOfFirstDayOfMonth = modulo((firstDayOfCurrentMonth - this.firstDayOfTheWeek), WEEK_SIZE);
        Log.d("[CALENDAR]", "-1 modulo 7 " + modulo(-1, 7));
        if (firstDayOfCurrentMonth != this.firstDayOfTheWeek) {
            daysLeftInFirstWeek = indexOfFirstDayOfMonth;
            indexOfDayAfterLastDayOfMonth = daysLeftInFirstWeek + daysInCurrentMonth;
            Log.d("[CALENDAR]", "First day of the week isn't monday");
            Log.d("[CALENDAR]", "indices of days : from " + indexOfFirstDayOfMonth +
                    " to " + (indexOfFirstDayOfMonth + daysInCurrentMonth - 1));
            for (int i = indexOfFirstDayOfMonth; i < indexOfFirstDayOfMonth + daysInCurrentMonth; ++i) {
                if (currentDateMonth == chosenDateMonth
                        && currentDateYear == chosenDateYear
                        && dayNumber == currentDateDay) {
                    days[i].setBackgroundColor(getResources().getColor(R.color.pink));
                    days[i].setTextColor(Color.WHITE);
                } else {
                    days[i].setTextColor(Color.BLACK);
                    days[i].setBackgroundColor(Color.TRANSPARENT);
                }

                int[] dateArr = new int[3];
                dateArr[0] = dayNumber;
                dateArr[1] = chosenDateMonth;
                dateArr[2] = chosenDateYear;
                days[i].setTag(dateArr);
                days[i].setText(String.valueOf(dayNumber));

                days[i].setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onDayClick(v);
                    }
                });
                ++dayNumber;
            }
        } else {
            daysLeftInFirstWeek = WEEK_SIZE;
            indexOfDayAfterLastDayOfMonth = daysLeftInFirstWeek + daysInCurrentMonth;
            for (int i = WEEK_SIZE; i < WEEK_SIZE + daysInCurrentMonth; ++i) {
                if (currentDateMonth == chosenDateMonth
                        && currentDateYear == chosenDateYear
                        && dayNumber == currentDateDay) {
                    days[i].setBackgroundColor(getResources().getColor(R.color.pink));
                    days[i].setTextColor(Color.WHITE);
                } else {
                    days[i].setTextColor(Color.BLACK);
                    days[i].setBackgroundColor(Color.TRANSPARENT);
                }

                int[] dateArr = new int[3];
                dateArr[0] = dayNumber;
                dateArr[1] = chosenDateMonth;
                dateArr[2] = chosenDateYear;
                days[i].setTag(dateArr);
                days[i].setText(String.valueOf(dayNumber));

                days[i].setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onDayClick(v);
                    }
                });
                ++dayNumber;
            }
        }

        if (month > 0)
            calendar.set(year, month - 1, 1);
        else
            calendar.set(year - 1, 11, 1);
        int daysInPreviousMonth = calendar.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);

        //Formatter les jours avant le début du mois
        for (int i = daysLeftInFirstWeek - 1; i >= 0; --i) {
            int[] dateArr = new int[3];

            if (chosenDateMonth > 0) {
                if (currentDateMonth != chosenDateMonth - 1
                        || currentDateYear != chosenDateYear
                        || daysInPreviousMonth != currentDateDay) {
                            days[i].setBackgroundColor(Color.TRANSPARENT);
                        }

                dateArr[0] = daysInPreviousMonth;
                dateArr[1] = chosenDateMonth - 1;
                dateArr[2] = chosenDateYear;
            } else {
                if (currentDateMonth != 11
                        || currentDateYear != chosenDateYear - 1
                        || daysInPreviousMonth != currentDateDay) {
                            days[i].setBackgroundColor(Color.TRANSPARENT);
                        }

                dateArr[0] = daysInPreviousMonth;
                dateArr[1] = 11;
                dateArr[2] = chosenDateYear - 1;
            }

            days[i].setTag(dateArr);
            days[i].setTextColor(Color.parseColor(CUSTOM_GREY));
            days[i].setText(String.valueOf(daysInPreviousMonth--));
            days[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDayClick(v);
                }
            });
        }

        //Formatter les jours après la fin du mois
        int nextMonthDaysCounter = 1;
        for (int i = indexOfDayAfterLastDayOfMonth; i < days.length; ++i) {
            int[] dateArr = new int[3];

            if (chosenDateMonth < 11) {
                if (currentDateMonth == chosenDateMonth + 1
                        && currentDateYear == chosenDateYear
                        && nextMonthDaysCounter == currentDateDay) {
                    days[i].setBackgroundColor(getResources().getColor(R.color.pink));
                } else {
                    days[i].setBackgroundColor(Color.TRANSPARENT);
                }

                dateArr[0] = nextMonthDaysCounter;
                dateArr[1] = chosenDateMonth + 1;
                dateArr[2] = chosenDateYear;
            } else {
                if (currentDateMonth == 0
                        && currentDateYear == chosenDateYear + 1
                        && nextMonthDaysCounter == currentDateDay) {
                    days[i].setBackgroundColor(getResources().getColor(R.color.pink));
                } else {
                    days[i].setBackgroundColor(Color.TRANSPARENT);
                }

                dateArr[0] = nextMonthDaysCounter;
                dateArr[1] = 0;
                dateArr[2] = chosenDateYear + 1;
            }

            days[i].setTag(dateArr);
            days[i].setTextColor(Color.parseColor(CUSTOM_GREY));
            days[i].setText(String.valueOf(nextMonthDaysCounter++));
            days[i].setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDayClick(v);
                }
            });
        }

        calendar.set(chosenDateYear, chosenDateMonth, chosenDateDay);
    }

    /**
     * Deal with negative numbers, so that the return will always be between 0 and b-1
     * (b-1) included
     * @param a
     * @param b
     * @return a%b
     */
    private int modulo(int a, int b) {
        int ret = a % b;
        if (ret < 0 ){
            ret = ret + b;
        }
        return ret;
    }

    public void onDayClick(View view) {
        if (view != null) {

            if (selectedDayButton != null) {
                if (chosenDateYear == currentDateYear
                        && chosenDateMonth == currentDateMonth
                        && pickedDateDay == currentDateDay) {
                    selectedDayButton.setBackgroundColor(getResources().getColor(R.color.pink));
                    selectedDayButton.setTextColor(Color.WHITE);
                } else {
                    selectedDayButton.setBackgroundColor(Color.TRANSPARENT);
                    if (selectedDayButton.getCurrentTextColor() != Color.RED) {
                        selectedDayButton.setTextColor(getResources()
                                .getColor(R.color.calendar_number));
                    }
                }
            }
            selectedDayButton = (Button) view;
            if (selectedDayButton.getTag() != null) {
                int[] dateArray = (int[]) selectedDayButton.getTag();
                pickedDateDay = dateArray[0];
                pickedDateMonth = dateArray[1];
                pickedDateYear = dateArray[2];
                Log.d("[CALENDAR]", "Picked Date : " + pickedDateDay + pickedDateMonth + pickedDateYear);
            }

            if (pickedDateYear == currentDateYear
                    && pickedDateMonth == currentDateMonth
                    && pickedDateDay == currentDateDay) {
                selectedDayButton.setBackgroundColor(getResources().getColor(R.color.pink));
                selectedDayButton.setTextColor(Color.WHITE);
            } else {
                selectedDayButton.setBackgroundColor(getResources().getColor(R.color.grey));
                if (selectedDayButton.getCurrentTextColor() != Color.RED) {
                    selectedDayButton.setTextColor(Color.WHITE);
                }
            }

            //Start the fragment of the day, which presents all the events of this day
            Fragment f = new DayFragment();
            Bundle args = new Bundle();
            args.putInt("day", pickedDateDay);
            args.putInt("month", pickedDateMonth);
            args.putInt("year", pickedDateYear);
            f.setArguments(args);
            activity.openFragment(f, pickedDateDay + " " + Calendar.FR_MONTH_NAMES[pickedDateMonth] + " " + pickedDateYear);
        }
    }

    private void addDaysinCalendar(LayoutParams buttonParams, Context context,
                                   DisplayMetrics metrics) {
        int dayCounter = 0;

        for (int weekNumber = 0; weekNumber < 6; ++weekNumber) {
            for (int dayInWeek = 0; dayInWeek < 7; ++dayInWeek) {
                final Button day = new Button(context);
                day.setTextColor(Color.parseColor(CUSTOM_GREY));
                day.setBackgroundColor(Color.TRANSPARENT);
                day.setLayoutParams(buttonParams);
                day.setTextSize((int) metrics.density * 8);
                day.setSingleLine();

                days[dayCounter] = day;
                weeks[weekNumber].addView(day);

                ++dayCounter;
            }
        }
    }

    private LayoutParams getdaysLayoutParams() {
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        buttonParams.weight = 1;
        return buttonParams;
    }
/*
    private void setDayOnTouchListener(Button day) {
        OnTouchListener listener = new OnTouchListener() {

            private GestureDetector gestureDetector = new GestureDetector(Test.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    Log.d("TEST", "onDoubleTap");
                    return super.onDoubleTap(e);
                }


            });

            @Override
            public boolean onTouch(View calendarView, MotionEvent motionEvent) {
                return false;
            }
        };
        day.setOnTouchListener(listener);
    }*/



}