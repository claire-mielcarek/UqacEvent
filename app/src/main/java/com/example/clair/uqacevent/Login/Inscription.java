package com.example.clair.uqacevent.Login;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.google.android.gms.tasks.OnCompleteListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.clair.uqacevent.Profile.ProfileFragment;
import com.example.clair.uqacevent.Profile.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
/*
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
*/

import com.example.clair.uqacevent.R;

public class Inscription extends Fragment {
    private FirebaseAuth auth;
    /*
    private DatabaseReference database;
    private Context context;
*/
    private EditText mail;
    private EditText mdp;
    private EditText mdp_confirm;
    private EditText nom;
    private boolean probleme;
    private boolean first;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_inscription, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        auth = FirebaseAuth.getInstance();
        View v = getActivity().findViewById(R.id.inscription);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try_inscription(view);
            }
        });
    }
/*
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.inscription);

        // recupere l'objet firebase pour l'authantification et une reference vers la database
        database = FirebaseDatabase.getInstance().getReference();
        actionBar.setTitle(R.string.signup);
    }
    */

    public void try_inscription(View v){
        int code = -1;
        first = true;
        //probleme = false;

        mail = getActivity().findViewById(R.id.mail);
        mdp = getActivity().findViewById(R.id.mdp);
        mdp_confirm = getActivity().findViewById(R.id.mdp_confirm);
        nom = getActivity().findViewById(R.id.nom);

        final String mail_value = mail.getText().toString();
        final String mdp_value = mdp.getText().toString();
        String mdp_confirm_value = mdp_confirm.getText().toString();
        final String nom_value = nom.getText().toString();
        final String date_membre = android.text.format.DateFormat.format("dd/MM/yyyy", new java.util.Date()).toString();


        if (!mail_value.equals("") && !mdp_value.equals("") && !mdp_confirm_value.equals("") && !nom_value.equals("") &&
                mdp_value.equals(mdp_confirm_value)){

            // check if first name and last name already exist
            /*
            database.child("Users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (first) {
                        for (DataSnapshot firebaseUser : dataSnapshot.getChildren()) {
                            if (nom_value.equals(firebaseUser.child("name").getValue())) {
                                ErrorDetected(8);
                                probleme = true;
                                break;
                            }
                        }

                        if (!probleme)
                            completeSignIn(mail_value, mdp_value, date_membre, nom_value);

                        first = false;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    ErrorDetected(-1);
                }
            });
            */

        }else{
            // definie le code d'erreur
            if (mail_value.equals(""))
                code = 1;
            else if (mdp_value.equals(""))
                code  = 2;
            else if (mdp_confirm_value.equals(""))
                code = 3;
            else if (nom_value.equals(""))
                code = 5;
            else if (!mdp_value.equals(mdp_confirm_value))
                code = 7;

            ErrorDetected(code);
        }

    }


    private void completeSignIn(String mail, String mdp, final String date_membre, final String nom){

        auth.createUserWithEmailAndPassword(mail, mdp).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                FirebaseUser firebaseUser;
                /*
                if (task.isSuccessful()){
                    // enregistrer les informations dans la base de donnees
                    firebaseUser = auth.getCurrentUser();

                    if (firebaseUser != null) {
                        database.child("Users").child(firebaseUser.getUid()).child("dateMembre").setValue(date_membre);
                        database.child("Users").child(firebaseUser.getUid()).child("mail").setValue(firebaseUser.getEmail());
                        database.child("Users").child(firebaseUser.getUid()).child("nom").setValue(nom);

                        //setContentView(R.layout.waiting);

                        // get data of user from firebase
                        User user = new User();
                        user.attachUserToFirebase(true, new IResultConnectUser() {
                            @Override
                            public void OnSuccess() {
                                // send to profile
                                Fragment f = new ProfileFragment();
                                getActivity().getFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, f, "findThisFragment")
                                        .addToBackStack(null)
                                        .commit();
                            }

                            @Override
                            public void OnFailed() {
                                //Utils.MyMessageButton("Problems to read data", context);
                            }
                        });
                    }
                }else{
                    Log.d("InscriptionMail", String.valueOf(task.getException()));    // probleme lors de la creation du compte
                    ErrorDetected(0);
                }*/
            }
        });
    }

    public void ErrorDetected(int code){
        String message;

        // definie un message suivant le code recu
        if (code == 0){
            message = "Votre inscription a echoué. L'adresse mail est peut-être déjà utilisé.";
            mail.setBackgroundResource(R.drawable.error_edit_text_bg);
            mdp.setBackgroundResource(R.drawable.error_edit_text_bg);
            mdp_confirm.setBackgroundResource(R.drawable.error_edit_text_bg);
            mail.getBackground().setAlpha(50);
            mdp.getBackground().setAlpha(50);
            mdp_confirm.getBackground().setAlpha(50);
        }else if (code == 1) {
            message = "L'adresse mail est obligatoire pour s'inscrire.";
            mail.setBackgroundResource(R.drawable.error_edit_text_bg);
            mail.getBackground().setAlpha(50);
        }else if (code == 2) {
            message = "Le mot de passe est obligatoire pour s'inscrire.";
            mdp.setBackgroundResource(R.drawable.error_edit_text_bg);
            mdp.getBackground().setAlpha(50);
        }else if (code == 3) {
            message = "La confirmation du mot de passe est obligatoire pour s'inscrire.";
            mdp_confirm.setBackgroundResource(R.drawable.error_edit_text_bg);
            mdp_confirm.getBackground().setAlpha(50);

        }else if (code == 5) {
            message = "Le nom est obligatoire pour s'inscrire.";
            nom.setBackgroundResource(R.drawable.error_edit_text_bg);
            nom.getBackground().setAlpha(50);
        }else if (code == 7) {
            message = "Les mots de passes ne sont pas identiques. Veuillez réessayer.";
            mdp.setBackgroundResource(R.drawable.error_edit_text_bg);
            mdp_confirm.setBackgroundResource(R.drawable.error_edit_text_bg);
            mdp.getBackground().setAlpha(50);
            mdp_confirm.getBackground().setAlpha(50);
        }else if (code == 8){
            message = "Le nom et prenom existe déjà.";
            nom.setBackgroundResource(R.drawable.error_edit_text_bg);
            nom.getBackground().setAlpha(50);
        }else{
            message = "Une erreur inconnue a été rencontré. Veuillez réessayer.";
        }

        //Utils.MyMessageButton(message, context);
    }

}