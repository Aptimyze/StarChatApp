package com.varunsaini.android.bestchatapp.models;

public class OneToOneMessageModel {

    public String messageText, senderID, recieverID, senderName;
    public String timeStamp;

    public OneToOneMessageModel() {
    }

    public OneToOneMessageModel(String messageText, String senderID, String recieverID, String senderName,String timeStamp) {
        this.messageText = messageText;
        this.senderID = senderID;
        this.recieverID = recieverID;
        this.timeStamp = timeStamp;
        this.senderName = senderName;
    }
}
