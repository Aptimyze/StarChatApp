package com.chatr.random.stranger.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.ProgressBar;

import com.chatr.random.stranger.Repository;
import com.chatr.random.stranger.models.NewUserInDbModel;

public class SettingsViewModel extends AndroidViewModel {

    public LiveData<NewUserInDbModel> newUserInDb;
    public Repository repository;
    public int selectedValueFromPicker =0;

    public SettingsViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    public void setProfilePicture(Uri uri,String myUID, ProgressBar progressBar){
        repository.setProfilePicture(uri,myUID, progressBar);
    }

    public LiveData<NewUserInDbModel> getAllSettingsOfAUser(String myUID){
        newUserInDb = repository.getAllSettingsOfAUser(myUID);
        return newUserInDb;
    }

    public void setAllSettingsDetails(String UID,NewUserInDbModel userDetails){
        repository.setAllSettingsDetails(UID,userDetails);
    }
}
