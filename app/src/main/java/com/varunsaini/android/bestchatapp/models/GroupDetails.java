package com.varunsaini.android.bestchatapp.models;

public class GroupDetails {

    public String title;
    public String lastMsg;
    public String timeStamp;

    public GroupDetails() { }

    public GroupDetails(String title, String lastMsg, String timeStamp) {
        this.title = title;
        this.lastMsg = lastMsg;
        this.timeStamp = timeStamp;
    }

}
