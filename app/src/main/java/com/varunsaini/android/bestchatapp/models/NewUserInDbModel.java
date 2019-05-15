package com.varunsaini.android.bestchatapp.models;

public class NewUserInDbModel {
    public String uid , email, displayName;
    public String profilePicUrl, status;
    public boolean isOnline;
    public String age,nationality,gender;

    public NewUserInDbModel() {
    }

    public NewUserInDbModel(String uid, String email, String displayName) {
        this.uid = uid;
        this.email = email;
        this.displayName = displayName;
    }

    public NewUserInDbModel(String uid, String email, String displayName, String profilePicUrl, String status, boolean isOnline) {
        this.uid = uid;
        this.email = email;
        this.displayName = displayName;
        this.profilePicUrl = profilePicUrl;
        this.status = status;
        this.isOnline = isOnline;
    }

    public NewUserInDbModel(String uid, String email, String displayName, String profilePicUrl, String status, boolean isOnline, String age, String nationality, String gender) {
        this.uid = uid;
        this.email = email;
        this.displayName = displayName;
        this.profilePicUrl = profilePicUrl;
        this.status = status;
        this.isOnline = isOnline;
        this.age = age;
        this.nationality = nationality;
        this.gender = gender;
    }

    public NewUserInDbModel(String status, String age, String nationality, String gender) {
        this.status = status;
        this.age = age;
        this.nationality = nationality;
        this.gender = gender;
    }
}
