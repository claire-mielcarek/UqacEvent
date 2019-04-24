package com.example.clair.uqacevent.Controller.Login;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.clair.uqacevent.Controller.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;

import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.clair.uqacevent.Controller.Profile.ProfileFragment;
import com.example.clair.uqacevent.Model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.example.clair.uqacevent.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class InscriptionFragment extends Fragment {
    private FirebaseAuth auth;
    private DatabaseReference database;
    Activity activity;
    /*
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
        activity = Objects.requireNonNull(getActivity());
        activity.setTitle(getTag());
        return inflater.inflate(R.layout.login_inscription, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        auth = FirebaseAuth.getInstance();// recupere l'objet firebase pour l'authantification et une reference vers la database
        database = FirebaseDatabase.getInstance().getReference();
        View v = activity.findViewById(R.id.inscription);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try_inscription();
            }
        });
    }

    public void try_inscription() {
        int code = -1;
        first = true;
        probleme = false;

        mail = activity.findViewById(R.id.mail);
        mdp = activity.findViewById(R.id.mdp);
        mdp_confirm = activity.findViewById(R.id.mdp_confirm);
        nom = activity.findViewById(R.id.nom);

        final String typeAccount = ((RadioButton) activity.findViewById(((RadioGroup) activity.findViewById(R.id.type_account)).getCheckedRadioButtonId())).getText().toString();
        final String accountIsPublic = Boolean.toString(typeAccount.equals(getString(R.string.public_account)));
        final String mail_value = mail.getText().toString();
        final String mdp_value = mdp.getText().toString();
        String mdp_confirm_value = mdp_confirm.getText().toString();
        final String nom_value = nom.getText().toString();
        final String date_membre = android.text.format.DateFormat.format("dd/MM/yyyy", new java.util.Date()).toString();


        if (!mail_value.equals("") && !mdp_value.equals("") && !mdp_confirm_value.equals("") && !nom_value.equals("") &&
                mdp_value.equals(mdp_confirm_value)) {

            // check if first name and last name already exist

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
                            completeSignIn(mail_value, mdp_value, date_membre, nom_value, accountIsPublic);

                        first = false;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    ErrorDetected(-1);

                }
            });


        } else {
            // definie le code d'erreur
            if (nom_value.equals(""))
                code = 5;
            else if (mail_value.equals(""))
                code = 1;
            else if (mdp_value.equals(""))
                code = 2;
            else if (mdp_confirm_value.equals(""))
                code = 3;
            else if (!mdp_value.equals(mdp_confirm_value))
                code = 7;

            ErrorDetected(code);
        }

    }


    private void completeSignIn(String mail, String mdp, final String date_membre, final String nom, final String accountIsPublic) {

        auth.createUserWithEmailAndPassword(mail, mdp).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                FirebaseUser firebaseUser;

                if (task.isSuccessful()) {
                    // enregistrer les informations dans la base de donnees
                    firebaseUser = auth.getCurrentUser();
                    Log.d("[INSCRIPTION]", "current user : " + firebaseUser);

                    if (firebaseUser != null) {
                        database.child("Users").child(firebaseUser.getUid()).child("dateMembre").setValue(date_membre);
                        database.child("Users").child(firebaseUser.getUid()).child("mail").setValue(firebaseUser.getEmail());
                        database.child("Users").child(firebaseUser.getUid()).child("nom").setValue(nom);
                        database.child("Users").child(firebaseUser.getUid()).child("accountIsPublic").setValue(accountIsPublic);

                        //setContentView(R.layout.waiting);

                        // get data of user from firebase

                        final User user = User.InstantiateUser();
                        user.attachUserToFirebase(true, new IResultConnectUser() {
                            @Override
                            public void OnSuccess() {
                                Fragment f = new ProfileFragment();
                                ((MainActivity) activity).openFragment(f, getString(R.string.title_profile));
                                Log.d("[INSCRIPTION]", "" + user);
                                if (accountIsPublic.equals("true"))
                                    ((MainActivity) activity).addCreationMenuItem();
                            }

                            @Override
                            public void OnFailed() {
                                //Utils.MyMessageButton("Problems to read data", context);
                            }
                        });
                    }
                } else {
                    Log.d("InscriptionMail", String.valueOf(task.getException()));    // probleme lors de la creation du compte
                    ErrorDetected(0);
                }
            }
        });
    }

    public void ErrorDetected(int code) {
        String message;

        // definie un message suivant le code recu
        if (code == 0) {
            message = getResources().getString(R.string.sign_up_fail);
            mail.setBackgroundResource(R.drawable.error_edit_text_bg);
            mdp.setBackgroundResource(R.drawable.error_edit_text_bg);
            mdp_confirm.setBackgroundResource(R.drawable.error_edit_text_bg);
            mail.getBackground().setAlpha(50);
            mdp.getBackground().setAlpha(50);
            mdp_confirm.getBackground().setAlpha(50);
        } else if (code == 1) {
            message = getResources().getString(R.string.sign_up_mail);
            mail.setBackgroundResource(R.drawable.error_edit_text_bg);
            mail.getBackground().setAlpha(50);
        } else if (code == 2) {
            message = getResources().getString(R.string.sign_up_password);
            mdp.setBackgroundResource(R.drawable.error_edit_text_bg);
            mdp.getBackground().setAlpha(50);
        } else if (code == 3) {
            message = getResources().getString(R.string.confirm_password);
            mdp_confirm.setBackgroundResource(R.drawable.error_edit_text_bg);
            mdp_confirm.getBackground().setAlpha(50);

        } else if (code == 5) {
            message = getResources().getString(R.string.sign_up_name);
            nom.setBackgroundResource(R.drawable.error_edit_text_bg);
            nom.getBackground().setAlpha(50);
        } else if (code == 7) {
            message = getResources().getString(R.string.sign_up_password_mismatch);
            mdp.setBackgroundResource(R.drawable.error_edit_text_bg);
            mdp_confirm.setBackgroundResource(R.drawable.error_edit_text_bg);
            mdp.getBackground().setAlpha(50);
            mdp_confirm.getBackground().setAlpha(50);
        } else if (code == 8) {
            message = getResources().getString(R.string.sign_up_name_already_exists);
            nom.setBackgroundResource(R.drawable.error_edit_text_bg);
            nom.getBackground().setAlpha(50);
        } else {
            message = getResources().getString(R.string.sign_up_error);
        }

        Log.d("[INSCRIPTION]", message);
    }

}
