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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.example.clair.uqacevent.Profile.ProfileFragment;
import com.example.clair.uqacevent.Profile.User;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
/*import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;*/
import com.example.clair.uqacevent.R;

public class Connexion extends Fragment {

    private static final int RC_SIGN_IN = 10;
    private static final String EMAIL = "email";
    private static final String PUBLIC_PROFILE = "public_profile";
    private boolean changeData;
    private boolean mail;
    private boolean first = true;
    private String newData;
    private SignInButton signButton;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    //private DatabaseReference database;
    private Context context;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_connexion, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        View v = getActivity().findViewById(R.id.inscription_button);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inscription(view);
            }
        });
        v = getActivity().findViewById(R.id.connexion_button);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connexionWithMail(view);
            }
        });
    }

    /*
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get the context
        changeData = getIntent().getBooleanExtra("ChangeData", false);
        mail = getIntent().getBooleanExtra("Mail", false);
        if (changeData)
            newData = getIntent().getStringExtra("Value");

        // permet de connaitre l'utilisateur connecte
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("Users");
        context = this;
        actionBar.setTitle(R.string.signin);

        FacebookSdk.sdkInitialize(context);
        setContentView(R.layout.connexion);
    }
    */

    public void connexionWithMail(View v) {
        // recupere les edit text contenant les donnees
        final EditText edit_mail = ((EditText) getView().findViewById(R.id.mail));
        final EditText edit_mdp = ((EditText) getView().findViewById(R.id.mdp));

        //recupere les donnees de l'utilisateur
        String mail = edit_mail.getText().toString();
        String mdp = edit_mdp.getText().toString();

        // connexion a la base de donnee avec les donnees de l'utilisateur
        /*
        auth.signInWithEmailAndPassword(mail, mdp).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    firebaseUser = auth.getCurrentUser();
                    Log.d("Connexion", "signInWithMail:success : " + firebaseUser.getUid());
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
        });*/
    }

    public void changeDataFunction(final String value) {
        if (mail) {
            firebaseUser.updateEmail(value).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        //database.child(firebaseUser.getUid()).child("mail").setValue(value);
                        Log.d("CHANGE DATA", "User email address updated.");
                    } else {
                        Log.d("CHANGE DATA", "User email address update failed.");
                    }
                }
            });
        } else {
            firebaseUser.updatePassword(value).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d("CHANGE DATA", "User password updated.");
                    } else {
                        Log.d("CHANGE DATA", "User password update failed.");
                    }
                }
            });
        }
    }

    public void inscription(View v) {
        // rediriger vers l'activité d'inscription
        //getActivity().getActionBar().setTitle(R.string.inscription);
        Fragment f = new Inscription();
        getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, f, "findThisFragment")
                .addToBackStack(null)
                .commit();
    }

    public void LoadDataAndStartActivity() {
        final User user;
        if (changeData)
            changeDataFunction(newData);
        user = new User();

        user.attachUserToFirebase(true, new IResultConnectUser() {
            @Override
            public void OnSuccess() {  // if operation is a success so show firebaseUser's informations
                // start Profile Activity
                //getActivity().getActionBar().setTitle(R.string.title_profile);
                Fragment f = new ProfileFragment();
                getActivity().getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, f, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
            @Override
            public void OnFailed() {
                Log.w("DatabaseChange", "Failed to read values");

                // show message for firebaseUser then reload connection
                //Utils.MyMessageButton("Read personal value has failed. Retry later please", context);
                startActivity(new Intent(context, Connexion.class));
            }
        });
    }
}
