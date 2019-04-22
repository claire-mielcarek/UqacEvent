package com.example.clair.uqacevent.Profile;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.clair.uqacevent.Login.IResultConnectUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class User {
    private static User user;

    String name;
    String email;
    String description;
    String dateInscription;
    boolean publicAccount;
    String contact;
    // Firebase reference
    private transient FirebaseAuth auth;
    private transient FirebaseUser fireUser;
    private transient DatabaseReference database;

    private String uid;
    private boolean first;

    public User() {
        this.email = "";
        this.name = "";
        this.description = "";
        this.dateInscription = "";
        this.publicAccount = false;
        this.contact = "";

        auth = FirebaseAuth.getInstance();
        fireUser = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference();
        this.uid = fireUser.getUid();
        first = true;
    }

    public static User getCurrentUser(){
        return user;
    }

    // handle the instance of user
    public static User InstantiateUser(){
        user = new User();
        Log.d("[USER]", "User instantiate : " + user);
        return user;
    }

    // attach User to Firebase which fills data on runtime
    public void attachUserToFirebase(final boolean one_time, final IResultConnectUser interfaceForResult) {
        // get data from firebase
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot dataUser = dataSnapshot.child("Users").child(uid);

                if (!one_time||first) {
                    // get private user data
                    fillDataFromFirebase(dataUser);
                    // this no more the first time
                    first = false;
                }
                interfaceForResult.OnSuccess();
                Log.d("[USER]", "onDataChange done");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("[USER]", "Failed to read values from DataBase", databaseError.toException());
            }
        });
    }

    // get private data from firebase
    private void fillDataFromFirebase(/*StorageReference storage, */DataSnapshot dataUser){
        String valUserName = (String) dataUser.child("nom").getValue();
        String valEMail = (String) dataUser.child("mail").getValue();
        String valInscription = (String) dataUser.child("dateMembre").getValue();
        String valDescription = (String) dataUser.child("description").getValue();
        String valPublicAccout = (String) dataUser.child("accountIsPublic").getValue();
        String valContact = (String) dataUser.child("contact").getValue();

        // set data on views
        if (valUserName != null)
            name = valUserName;
        if (valEMail != null)
            email = valEMail;
        if (valInscription != null)
            dateInscription = valInscription;
        if (valDescription != null)
            description = valDescription;
        if (valPublicAccout != null && valPublicAccout.equals("true"))
            publicAccount = true;
        if (valContact != null)
            contact = valContact;

        Log.d("[USER]", "profile : "+ name + " " + email + " " +  dateInscription + " " +  description + " " +  publicAccount);
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getDescription() {
        return description;
    }

    public String getDateInscription() {
        return dateInscription;
    }

    public boolean isPublicAccount() {
        return publicAccount;
    }

    public String getContact() {
        return contact;
    }

    public String getUid() {
        return uid;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", description='" + description + '\'' +
                ", dateInscription='" + dateInscription + '\'' +
                ", publicAccount=" + publicAccount +
                ", contact='" + contact + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}
