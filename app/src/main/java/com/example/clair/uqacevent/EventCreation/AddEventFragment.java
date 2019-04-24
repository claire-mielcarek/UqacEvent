package com.example.clair.uqacevent.EventCreation;

import android.app.DatePickerDialog;
import android.content.Context;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddEventFragment extends Fragment {
    private FirebaseAuth auth;
    private DatabaseReference database;
    private DatabaseReference newEventRef;

    private EditText ETTitle;
    private EditText ETDescription;
    private EditText ETDate;
    private EditText ETPlace;
    private Spinner sTypeEvent;
    private Button bAddEvent;

    private Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_event, container, false);
    }

    public void onStart(){
        super.onStart();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        ETTitle = getActivity().findViewById(R.id.editTextTitle);
        ETDescription = getActivity().findViewById(R.id.editTextDescription);
        ETDate = getActivity().findViewById(R.id.editTextDate);
        ETDate.setOnClickListener(datePickerListener);
        ETPlace = getActivity().findViewById(R.id.editTextPlace);
        sTypeEvent = getActivity().findViewById(R.id.spinner_type_event);
        bAddEvent = getActivity().findViewById(R.id.button_add_event);
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
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        ETDate.setText(sdf.format(myCalendar.getTime()));
    }

    //open a dialog to pick a date
    private View.OnClickListener datePickerListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new DatePickerDialog(getActivity(), date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    };


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
                String organizerId = auth.getInstance().getCurrentUser().getUid();

                Event newEvent = new Event(date, description, place, title, User.getCurrentUser().getName(), typeEvent, organizerId);

                DatabaseReference eventsRef = database.child("Events");
                newEventRef = eventsRef.push();
                newEventRef.setValue(newEvent,new DatabaseReference.CompletionListener(){

                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            Toast.makeText(getActivity(), "Evénement ajouté avec succès", Toast.LENGTH_LONG).show();
                            ETTitle.setText("");
                            ETDescription.setText("");
                            ETDate.setText("");
                            ETPlace.setText("");
                        } else {
                            Toast.makeText(getActivity(), "Problème avec l'enregistrement, veuillez réessayer", Toast.LENGTH_LONG).show();
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
            return "Vous devez ajouter un titre";
        }

        if (ETDate.getText().toString().equals("")) {
            return "Vous devez ajouter une date";
        }
        return "";
    }
}
