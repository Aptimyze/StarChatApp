package com.chatr.random.stranger.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.chatr.random.stranger.Repository;
import com.chatr.random.stranger.models.AllChatRecieverInfoModel;

import java.util.List;

public class AllChatViewModel extends AndroidViewModel {

    Repository repository;
    LiveData<List<AllChatRecieverInfoModel>> allChatListOfAUser;
    public Context mContext;

    public AllChatViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    public LiveData<List<AllChatRecieverInfoModel>> getAllChatListOfAParticularUser(String userID){
        Log.d("sas", "onClick: "+"in vm");
        allChatListOfAUser = repository.getAllChatListOfAParticularUser(userID);
        return allChatListOfAUser;
    }

}
