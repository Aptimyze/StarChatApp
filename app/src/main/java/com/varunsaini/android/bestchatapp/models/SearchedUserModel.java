package com.varunsaini.android.bestchatapp.models;

public class SearchedUserModel {
    public String displayName,email,uid,profilePicUrl,status,lastSeen;
    public boolean isOnline;
    public SearchedUserModel(String displayName, String email) {
        this.displayName = displayName;
        this.email = email;
    }

    public SearchedUserModel(String displayName, String email, String uid) {
        this.displayName = displayName;
        this.email = email;
        this.uid = uid;
    }

    public SearchedUserModel(String uid, String email, String displayName, String profilePicUrl, String status, boolean isOnline, String lastSeen) {
        this.uid = uid;
        this.email = email;
        this.displayName = displayName;
        this.profilePicUrl = profilePicUrl;
        this.status = status;
        this.isOnline = isOnline;
        this.lastSeen = lastSeen;
    }

    public SearchedUserModel(){}
}
