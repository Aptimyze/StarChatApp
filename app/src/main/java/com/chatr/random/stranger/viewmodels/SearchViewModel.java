package com.chatr.random.stranger.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.chatr.random.stranger.Repository;
import com.chatr.random.stranger.models.SearchedUserModel;

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
