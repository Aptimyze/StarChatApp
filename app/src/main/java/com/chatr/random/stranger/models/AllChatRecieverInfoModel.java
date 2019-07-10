package com.chatr.random.stranger.models;

import android.support.annotation.NonNull;

public class AllChatRecieverInfoModel  implements Comparable< AllChatRecieverInfoModel >{

    public String recieverName, recieverUid, lastMessage,lastMsgSentBy, recieverProfilePic;
    public boolean isLastMsgRead = false;
    public String timestamp;

    public AllChatRecieverInfoModel(){}

    public AllChatRecieverInfoModel(String recieverName, String recieverUid, String lastMessage, String lastMsgSentBy, String timestamp, boolean isLastMsgRead) {
        this.recieverName = recieverName;
        this.recieverUid = recieverUid;
        this.lastMessage = lastMessage;
        this.lastMsgSentBy = lastMsgSentBy;
        this.timestamp = timestamp;
        this.isLastMsgRead = isLastMsgRead;
    }

    public AllChatRecieverInfoModel(String recieverName, String recieverUid, String lastMessage, String timestamp, String lastMsgSentBy, String recieverProfilePic, boolean isLastMsgRead) {
        this.recieverName = recieverName;
        this.recieverUid = recieverUid;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
        this.lastMsgSentBy = lastMsgSentBy;
        this.recieverProfilePic = recieverProfilePic;
        this.isLastMsgRead = isLastMsgRead;
    }

    @Override
    public int compareTo(@NonNull AllChatRecieverInfoModel o) {
        return this.timestamp.compareTo(o.timestamp);
    }
}
