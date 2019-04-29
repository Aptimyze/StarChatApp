package com.varunsaini.android.bestchatapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.varunsaini.android.bestchatapp.Repository;


public class AllGroupViewModel extends AndroidViewModel {

    Repository repository;

    public AllGroupViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    public void getAllGroupsOfAUser() {
        repository.getAllGroupsOfAUser();
    }

    public void addNewGroup(String title, String lastMsg, String timeStamp) {
        repository.addNewGroup(title,lastMsg,timeStamp);
    }
}
