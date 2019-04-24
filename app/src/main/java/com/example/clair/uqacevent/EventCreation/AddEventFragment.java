package com.example.clair.uqacevent.EventCreation;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.clair.uqacevent.Calendar.Event;

import com.example.clair.uqacevent.Profile.User;
import com.example.clair.uqacevent.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import java.util.Objects;

public class AddEventFragment extends Fragment {
    private DatabaseReference database;

    private EditText ETTitle;
    private EditText ETDescription;
    private EditText ETDate;
    private EditText ETPlace;
    private Spinner sTypeEvent;
    Activity activity;

    private Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_event, container, false);
    }

    public void onStart(){
        super.onStart();
        database = FirebaseDatabase.getInstance().getReference();

        activity = Objects.requireNonNull(getActivity());

        ETTitle = activity.findViewById(R.id.editTextTitle);
        ETDescription = activity.findViewById(R.id.editTextDescription);
        ETDate = activity.findViewById(R.id.editTextDate);
        ETDate.setOnClickListener(datePickerListener);
        ETPlace = activity.findViewById(R.id.editTextPlace);
        sTypeEvent = activity.findViewById(R.id.spinner_type_event);
        Button bAddEvent = activity.findViewById(R.id.button_add_event);
        bAddEvent.setOnClickListener(addEventListener);

        myCalendar = Calendar.getInstance();

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateText();
            }
        };
        updateText();
    }


    private void updateText() {
        String myFormat = getResources().getString(R.string.format_date_eu);
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        ETDate.setText(sdf.format(myCalendar.getTime()));
    }

    //open a dialog to pick a date
    private View.OnClickListener datePickerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new DatePickerDialog(activity, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    };


    public String getCurrentTime() {
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(getResources().getString(R.string.format_date_fr_posting_time), Locale.CANADA);

        return sdf.format(currentDate);
    }

    private View.OnClickListener addEventListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            String errorText;
            errorText = checkFields();
            if (errorText.equals("")){
                String title = ETTitle.getText().toString();
                String description = ETDescription.getText().toString();
                String date = ETDate.getText().toString();
                String place = ETPlace.getText().toString();
                String typeEvent = sTypeEvent.getSelectedItem().toString();
                String organizerId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                String postingTime = getCurrentTime();

                Event newEvent = new Event(date, description, place, title, User.getCurrentUser().getName(), typeEvent, organizerId, postingTime);

                DatabaseReference eventsRef = database.child("Events");
                DatabaseReference newEventRef = eventsRef.push();
                newEventRef.setValue(newEvent,new DatabaseReference.CompletionListener(){

                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.add_event_success), Toast.LENGTH_LONG).show();
                            ETTitle.setText("");
                            ETDescription.setText("");
                            ETDate.setText("");
                            ETPlace.setText("");
                        } else {
                            Toast.makeText(getActivity(), getResources().getString(R.string.add_event_fail), Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }else {
                Toast.makeText(getActivity(), errorText, Toast.LENGTH_LONG).show();
            }

        }
    };

    private String checkFields() {
        if (ETTitle.getText().toString().equals("")) {
            return getResources().getString(R.string.check_fields_title);
        }

        if (ETDate.getText().toString().equals("")) {
            return getResources().getString(R.string.check_fields_date);
        }
        return "";
    }
}
