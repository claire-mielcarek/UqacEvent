package com.example.clair.uqacevent.Login;

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

import com.example.clair.uqacevent.MainActivity;
import com.example.clair.uqacevent.Profile.ProfileFragment;
import com.example.clair.uqacevent.Profile.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.example.clair.uqacevent.R;

import java.util.Objects;

public class Connexion extends Fragment {
    private FirebaseAuth auth;
    Activity activity;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = Objects.requireNonNull(getActivity());
        activity.setTitle(getTag());
        return inflater.inflate(R.layout.login_connexion, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        auth = FirebaseAuth.getInstance();
        View v = activity.findViewById(R.id.inscription_button);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inscription();
            }
        });
        v = activity.findViewById(R.id.connexion_button);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connexionWithMail();
            }
        });
    }

    public void connexionWithMail() {
        // recupere les edit text contenant les donnees
        View v = Objects.requireNonNull(getView());
        final EditText edit_mail = (v .findViewById(R.id.mail));
        final EditText edit_mdp = (v.findViewById(R.id.mdp));

        //recupere les donnees de l'utilisateur
        String mail = edit_mail.getText().toString();
        String mdp = edit_mdp.getText().toString();

        // connexion a la base de donnee avec les donnees de l'utilisateur

        auth.signInWithEmailAndPassword(mail, mdp).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    LoadDataAndStartActivity();
                } else {
                    // debug
                    Log.d("Connexion", "signInWithMail:failed : " + task.getException());
                    // changer la couleur des editText en rouge
                    edit_mail.setBackgroundResource(R.drawable.error_edit_text_bg);
                    edit_mdp.setBackgroundResource(R.drawable.error_edit_text_bg);

                    edit_mail.getBackground().setAlpha(50);
                    edit_mdp.getBackground().setAlpha(50);

                    Log.d("SIGNIN", "Connexion a échoué");
                }
            }
        });
    }


    public void inscription() {
        // rediriger vers l'activité d'inscription
        //getActivity().getActionBar().setTitle(R.string.inscription);
        Fragment f = new Inscription();
        ((MainActivity) activity).openFragment(f, getString(R.string.inscription));
    }

    public void LoadDataAndStartActivity() {
        final User user;
        user = User.InstantiateUser();
        Log.d("[CONNEXION]", "User retourné : " + user);

        user.attachUserToFirebase(true, new IResultConnectUser() {
            @Override
            public void OnSuccess() {  // if operation is a success so show firebaseUser's informations
                // start Profile Activity
                //getActivity().getActionBar().setTitle(R.string.title_profile);
                Fragment f = new ProfileFragment();
                ((MainActivity) activity).openFragment(f, getString(R.string.title_profile));
                if (user.isPublicAccount())
                    ((MainActivity) activity).addCreationMenuItem();
                Log.d("[CONNEXION]", "fragment must have changed");
            }
            @Override
            public void OnFailed() {
                Log.d("[CONNEXION]", "Failed to read values");

                // show message for firebaseUser then reload connection
                //Utils.MyMessageButton("Read personal value has failed. Retry later please", context);
            }
        });
    }
}
