package com.varunsaini.android.bestchatapp;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.varunsaini.android.bestchatapp.models.AllChatRecieverInfoModel;
import com.varunsaini.android.bestchatapp.models.OneToOneMessageModel;
import com.varunsaini.android.bestchatapp.models.SearchedUserModel;
import com.varunsaini.android.bestchatapp.network.Firebase;

import java.util.List;

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

    public LiveData<List<SearchedUserModel>> returnMatchedUsers(String search_text) {
        return firebase.returnMatchedUsers(search_text);
    }

    public void writeNewMessageOTO(String messageText, String senderID, String recieverID,String senderName,String recieverName, String timeStamp) {
        firebase.writeNewMessageOTO(messageText,senderID,recieverID,senderName,recieverName, timeStamp);
    }

    public LiveData<List<OneToOneMessageModel>> getAllOneToOneMessages(String combinedId) {
        Log.d("sas", "onClick: "+"in repo");
        return firebase.getAllOneToOneMessages(combinedId);
    }

    public LiveData<List<AllChatRecieverInfoModel>> getAllChatListOfAParticularUser(String userID) {
        return firebase.getAllChatListOfAParticularUser(userID);
    }

    public void turnBooleanMsgReadTrue(String senderUID, String recieverUID) {
        firebase.turnBooleanMsgReadTrue(senderUID,recieverUID);
    }
}
