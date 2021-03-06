package com.example.clair.uqacevent.Controller.Profile;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clair.uqacevent.Controller.MainActivity;
import com.example.clair.uqacevent.Model.User;
import com.example.clair.uqacevent.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class ProfileFragment extends Fragment {
    private FirebaseAuth auth;
    private User user;
    DatabaseReference data;
    Activity activity;

    TextView nom;
    TextView email;
    boolean publicAccount;
    EditText contactEdit;
    EditText descriptionEdit;
    Button saveButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("[PROFILE]", "onCreateView");
        return inflater.inflate(R.layout.profile, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        activity = Objects.requireNonNull(getActivity());
        activity.setTitle(getTag());
        user = User.getCurrentUser();
        Log.d("[PROFILE]", "user : " + user);
    }

    @Override
    public void onStart() {
        super.onStart();
        auth = FirebaseAuth.getInstance();
        data = FirebaseDatabase.getInstance().getReference();

        nom = activity.findViewById(R.id.username);
        email = activity.findViewById(R.id.profile_email);
        descriptionEdit = activity.findViewById(R.id.profile_description);
        contactEdit = activity.findViewById(R.id.profile_contact);
        saveButton = activity.findViewById(R.id.profile_sauvegarder);
        fillDataFromUser();


        if(!publicAccount){
            Log.d("[PROFILE]", "Set unused field invisible because publicAccount is : " + publicAccount);
            LinearLayout descritpionContainer = activity.findViewById(R.id.profile_description_container);
            descritpionContainer.setVisibility(View.INVISIBLE);
            LinearLayout contactContainer = activity.findViewById(R.id.profile_contact_container);
            contactContainer.setVisibility(View.INVISIBLE);
            saveButton.setVisibility(View.INVISIBLE);
        }

        setListenerOnSaveButton();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.log_out, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_logout:
                auth.signOut();
                User.setUser(null);
                Fragment f = new ConnectionFragment();
                ((MainActivity) activity).openFragment(f, getString(R.string.connexion));
                ((MainActivity) activity).deleteCreationMenuItem();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void fillDataFromUser(){
        // set data on views

        Log.d("[PROFILE]", "user : " + user);
        nom.setText(user.getName());
        email.setText(user.getEmail());
        descriptionEdit.setText(user.getDescription());
        contactEdit.setText(user.getContact());
        publicAccount = user.isPublicAccount();

        Log.d("[PROFILE]", "profile chargé : " + nom.getText() + " mail : " + email.getText() + " descr : " + descriptionEdit.getText() + " contact : " + contactEdit.getText());
    }

    void setListenerOnSaveButton(){
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contact = contactEdit.getText().toString();
                String description = descriptionEdit.getText().toString();
                if (user != null) {
                    user.setDescription(description);
                    data.child("Users").child(user.getUid()).child("description").setValue(description);
                    user.setContact(contact);
                    data.child("Users").child(user.getUid()).child("contact").setValue(contact);
                }
                Toast.makeText(getActivity(), R.string.modif_saved, Toast.LENGTH_LONG).show();
                Log.d("[PROFILE]", "données à sauvegarder : " + contactEdit.getText() + " "+ descriptionEdit.getText() );
            }
        });
    }
}
