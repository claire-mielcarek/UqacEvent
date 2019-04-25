package com.example.clair.uqacevent.Model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.clair.uqacevent.Controller.Profile.IResultConnectUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class User {
    private static User user;

    private String name;
    private String email;
    private String description;
    private String dateInscription;
    private boolean publicAccount;
    private String contact;
    private ArrayList<String> filteredOrganizersIds;
    // Firebase reference
    private transient DatabaseReference database;

    private String uid;
    private boolean first;

    private User() {
        this.email = "";
        this.name = "";
        this.description = "";
        this.dateInscription = "";
        this.publicAccount = false;
        this.contact = "";
        filteredOrganizersIds = new ArrayList<>();


        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser fireUser = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference();
        assert fireUser != null;
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
                interfaceForResult.OnFailed();
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

    public void setFilteredOrganizersIds(ArrayList<String> filteredOrganizersIds) {
        this.filteredOrganizersIds = filteredOrganizersIds;
    }

    public ArrayList<String> getFilteredOrganizersIds() {
        return filteredOrganizersIds;
    }

    public static void setUser(User user) {
        User.user = user;
    }
}
