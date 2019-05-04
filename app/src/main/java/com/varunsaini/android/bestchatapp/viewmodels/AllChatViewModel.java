package com.varunsaini.android.bestchatapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.varunsaini.android.bestchatapp.Repository;
import com.varunsaini.android.bestchatapp.models.OneToOneMessageModel;

import java.util.List;

public class AllChatViewModel extends AndroidViewModel {

    Repository repository;
    LiveData<List<OneToOneMessageModel>> oneToOneListLive;

    public AllChatViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
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

}
