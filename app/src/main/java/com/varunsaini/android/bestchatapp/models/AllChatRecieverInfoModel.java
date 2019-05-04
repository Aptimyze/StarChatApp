package com.varunsaini.android.bestchatapp.models;

public class AllChatRecieverInfoModel {

    public String recieverName, recieverUid, lastMessage, timestamp,lastMsgSentBy;
    public boolean isLastMsgRead = false;

    public AllChatRecieverInfoModel(){}

    public AllChatRecieverInfoModel(String recieverName, String recieverUid, String lastMessage, String lastMsgSentBy, String timestamp, boolean isLastMsgRead) {
        this.recieverName = recieverName;
        this.recieverUid = recieverUid;
        this.lastMessage = lastMessage;
        this.lastMsgSentBy = lastMsgSentBy;
        this.timestamp = timestamp;
        this.isLastMsgRead = isLastMsgRead;
    }

}
