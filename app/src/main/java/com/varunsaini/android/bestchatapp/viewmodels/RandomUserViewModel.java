package com.varunsaini.android.bestchatapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.varunsaini.android.bestchatapp.Repository;
import com.varunsaini.android.bestchatapp.models.SearchedUserModel;


public class RandomUserViewModel extends AndroidViewModel {

    public String randomUserName = null;
    public String randomUserProfilePic;
    public String randomUserStatus;
    Repository repository;
    MutableLiveData<SearchedUserModel> randomGeneratedUser;
    public String randomUserUid = null;

    public RandomUserViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    public MutableLiveData<SearchedUserModel> getRandomUsers() {
        randomGeneratedUser = repository.getRandomUsers();
        return randomGeneratedUser;
    }

    public void addNewGroup(String title, String lastMsg, String timeStamp) {
        repository.addNewGroup(title,lastMsg,timeStamp);
    }
}
