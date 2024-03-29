package com.chatr.random.stranger;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.net.Uri;
import android.util.Log;
import android.widget.ProgressBar;

import com.chatr.random.stranger.models.AllChatRecieverInfoModel;
import com.chatr.random.stranger.models.NewUserInDbModel;
import com.chatr.random.stranger.models.OneToOneMessageModel;
import com.chatr.random.stranger.models.SearchedUserModel;
import com.chatr.random.stranger.models.UserParticularDetailModel;
import com.chatr.random.stranger.network.Firebase;

import java.util.List;

public class Repository {

    Application application;
    Firebase firebase;

    public Repository(Application mApplication) {
        application = mApplication;
        firebase = new Firebase(application);
    }

    public MutableLiveData<NewUserInDbModel> getRandomUsers() {
        return firebase.getRandomUsers();
    }

    public void addNewGroup(String title, String lastMsg, String timeStamp) {
        firebase.createNewGroup(title,lastMsg,timeStamp);
    }

    public LiveData<List<SearchedUserModel>> returnMatchedUsers(String search_text) {
        return firebase.returnMatchedUsers(search_text);
    }

    public void writeNewMessageOTO(String messageText, String senderID, String recieverID,String senderName,String recieverName
            ,String senderProfilePic, String recieverProfilePic, String timeStamp) {
        firebase.writeNewMessageOTO(messageText,senderID,recieverID,senderName,recieverName, senderProfilePic , recieverProfilePic, timeStamp);
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

    public String getUserNameFromUID(String recieverUID) {
        return firebase.getUserNameFromUID(recieverUID);
    }

    public void setProfilePicture(Uri uri, String myUID, ProgressBar progressBar) {
        firebase.setProfilePicture(uri,myUID, progressBar);
    }

    public LiveData<NewUserInDbModel> getAllSettingsOfAUser(String myUID){
        return firebase.getAllSettingsOfAUser(myUID);
    }

    public LiveData<UserParticularDetailModel> getProfilePicAndUserNameFromUid(String UID) {
        return firebase.getProfilePicAndUserNameFromUid(UID);
    }

    public MutableLiveData<UserParticularDetailModel> getYourParticularDetails(String yourUID) {
        return firebase.getYourParticularDetails(yourUID);
    }

    public void setAllSettingsDetails(String uid, NewUserInDbModel userDetails) {
        firebase.setAllSettingsDetails(uid,userDetails);
    }
}
