package com.varunsaini.android.bestchatapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.varunsaini.android.bestchatapp.Repository;
import com.varunsaini.android.bestchatapp.models.OneToOneMessageModel;
import com.varunsaini.android.bestchatapp.models.UserParticularDetailModel;

import java.util.List;

public class ChatScreenViewModel extends AndroidViewModel {

    Repository repository;
    LiveData<List<OneToOneMessageModel>>oneToOneListLive;
    public LiveData<UserParticularDetailModel> usernameAndProfilePicForReciever,usernameAndProfilePicForSender;
    public String recieverProfilePic, recieverLastSeen;
    public boolean isOnline;

    public ChatScreenViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    public void writeNewMessageOTO(String messageText, String senderID, String recieverID, String senderName, String recieverName
                                   , String senderProfilePic, String recieverProfilePic, String timeStamp) {
        repository.writeNewMessageOTO(messageText,senderID,recieverID,senderName,recieverName , senderProfilePic, recieverProfilePic, timeStamp);
    }

    public LiveData<List<OneToOneMessageModel>> getAllOneToOneMessages(String senderID,String recieverID){
        String combinedId;
        if(senderID.compareTo(recieverID)>0){
            combinedId = senderID+recieverID;
        }else{
            combinedId = recieverID+senderID;
        }
        Log.d("sas", "onClick: "+"in vm");
        oneToOneListLive = repository.getAllOneToOneMessages(combinedId);
        return oneToOneListLive;
    }

    public void turnBooleanMsgReadTrue(String senderUID, String recieverUID) {
        repository.turnBooleanMsgReadTrue(senderUID,recieverUID);
    }

    public void getProfilePicAndUserNameFromUid(String recieverUid,String senderUId) {
        usernameAndProfilePicForReciever = repository.getProfilePicAndUserNameFromUid(recieverUid);
        usernameAndProfilePicForSender = repository.getProfilePicAndUserNameFromUid(senderUId);
    }

    public MutableLiveData<UserParticularDetailModel> getYourParticularDetails(String yourUID){
        return repository.getYourParticularDetails(yourUID);
    }


}
