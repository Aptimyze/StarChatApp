package com.varunsaini.android.bestchatapp.models;

public class SearchedUserModel {
    public String displayName,email,uid;

    public SearchedUserModel(String displayName, String email) {
        this.displayName = displayName;
        this.email = email;
    }

    public SearchedUserModel(String displayName, String email, String uid) {
        this.displayName = displayName;
        this.email = email;
        this.uid = uid;
    }

    public SearchedUserModel(){}
}
