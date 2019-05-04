package com.varunsaini.android.bestchatapp.models;

public class NewUserInDbModel {
    public String uid , email, displayName;

    public NewUserInDbModel() {
    }

    public NewUserInDbModel(String uid, String email, String displayName) {
        this.uid = uid;
        this.email = email;
        this.displayName = displayName;
    }
}
