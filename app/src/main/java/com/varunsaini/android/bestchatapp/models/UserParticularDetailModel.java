package com.varunsaini.android.bestchatapp.models;

public class UserParticularDetailModel {
    public String displayName;
    public String profilePicUrl;
    public boolean isOnline;
    public String lastSeen;

    public UserParticularDetailModel(String displayName, String profilePicUrl, boolean isOnline, String lastSeen) {
        this.displayName = displayName;
        this.profilePicUrl = profilePicUrl;
        this.isOnline = isOnline;
        this.lastSeen = lastSeen;
    }

    public UserParticularDetailModel(String displayName, String profilePicUrl) {
        this.displayName = displayName;
        this.profilePicUrl = profilePicUrl;
    }

    public UserParticularDetailModel() {
    }
}
