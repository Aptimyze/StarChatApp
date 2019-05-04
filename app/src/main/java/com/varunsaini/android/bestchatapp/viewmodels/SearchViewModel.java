package com.varunsaini.android.bestchatapp.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.varunsaini.android.bestchatapp.Repository;
import com.varunsaini.android.bestchatapp.models.SearchedUserModel;

import java.util.List;

public class SearchViewModel extends AndroidViewModel {

    Repository repository;
    LiveData<List<SearchedUserModel>> searchedUsers;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    public LiveData<List<SearchedUserModel>> returnMatchedUsers(String search_text){
        searchedUsers = repository.returnMatchedUsers(search_text);
        return searchedUsers;
    }

}
