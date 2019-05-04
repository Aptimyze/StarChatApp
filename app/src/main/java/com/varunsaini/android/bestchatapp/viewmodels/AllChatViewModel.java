package com.varunsaini.android.bestchatapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.varunsaini.android.bestchatapp.Repository;
import com.varunsaini.android.bestchatapp.models.AllChatRecieverInfoModel;
import com.varunsaini.android.bestchatapp.models.OneToOneMessageModel;

import java.util.List;

public class AllChatViewModel extends AndroidViewModel {

    Repository repository;
    LiveData<List<AllChatRecieverInfoModel>> allChatListOfAUser;

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
