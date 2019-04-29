package com.varunsaini.android.bestchatapp;

import android.app.Application;

import com.varunsaini.android.bestchatapp.network.Firebase;

public class Repository {

    Application application;
    Firebase firebase;

    public Repository(Application mApplication) {
        application = mApplication;
        firebase = new Firebase(application);
    }

    public void getAllGroupsOfAUser() {
        firebase.getAllGroupsOfAUser();
    }

    public void addNewGroup(String title, String lastMsg, String timeStamp) {
        firebase.createNewGroup(title,lastMsg,timeStamp);
    }
}
