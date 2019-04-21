package com.example.clair.uqacevent.Profile;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.clair.uqacevent.Login.IResultConnectUser;

public class User {
    String name;

    public User() {
        this.name = "";
    }

    // attach User to Firebase which fills data on runtime
    public void attachUserToFirebase(/*DataSnapshot dataSnapshot, StorageReference storage, */final boolean one_time, final IResultConnectUser interfaceForResult) {
        // get data from firebase
        /*database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot dataUser = dataSnapshot.child("Users").child(uid);

                if (!one_time||first) {
                    // get private user data
                    fillPrivateDataFromFirebase(dataUser);
                    // get public user data
                    fillPublicDataFromFirebase(dataUser);
                    // get instruments list
                    instruments = fillDataListFromFirebase(dataUser, 1);
                    Log.d("GET DATA USER", "List intruments OK");
                    // get played style list
                    listened_styles = fillDataListFromFirebase(dataUser, 2);
                    Log.d("GET DATA USER", "List listened style OK");
                    // get listened style list
                    played_styles = fillDataListFromFirebase(dataUser, 3);
                    Log.d("GET DATA USER", "List played style OK");
                    // get user's portfolio
                    fillPortfolioImagesFromFirebase(dataUser, interfaceForResult);

                    // this no more the first time
                    first = false;
                }

                // get key images
                fillKeyImagesFromFireBase(dataUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("DatabaseChange", "Failed to read values.", databaseError.toException());
            }
        });*/
    }
}
