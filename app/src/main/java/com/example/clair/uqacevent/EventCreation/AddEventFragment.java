package com.example.clair.uqacevent.EventCreation;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.clair.uqacevent.Calendar.Event;

import com.example.clair.uqacevent.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        ETPlace = getActivity().findViewById(R.id.editTextPlace);
        sTypeEvent = getActivity().findViewById(R.id.spinner_type_event);
        bAddEvent = getActivity().findViewById(R.id.button_add_event);
        bAddEvent.setOnClickListener(addEventListener);

        /*if(checkFields().equals("")){
            bAddEvent.setEnabled(true);
        }*/ //ajouter listener sur les champs?
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
                String organizer = auth.getInstance().getCurrentUser().getDisplayName();
                String organizerId = auth.getInstance().getCurrentUser().getUid();

                Event newEvent = new Event(date, description, place, title, organizer, typeEvent, organizerId);

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
