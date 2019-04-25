package com.example.clair.uqacevent.Controller.Profile;




public interface IResultConnectUser {

    // call this method if user's data has been recovered from firebase
    void OnSuccess();

    // call this method if there was a problem (e.g. message to the current user)
    void OnFailed();


}
