package com.chatr.random.stranger.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.chatr.random.stranger.Repository;
import com.chatr.random.stranger.models.NewUserInDbModel;
import com.chatr.random.stranger.models.SearchedUserModel;


public class RandomUserViewModel extends AndroidViewModel {

    public String randomUserName = null;
    public String randomUserProfilePic;
    public String randomUserStatus;
    Repository repository;
    MutableLiveData<NewUserInDbModel> randomGeneratedUser;
    public String randomUserUid = null;

    public RandomUserViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    public MutableLiveData<NewUserInDbModel> getRandomUsers() {
        randomGeneratedUser = repository.getRandomUsers();
        return randomGeneratedUser;
    }

    public void addNewGroup(String title, String lastMsg, String timeStamp) {
        repository.addNewGroup(title,lastMsg,timeStamp);
    }
}
