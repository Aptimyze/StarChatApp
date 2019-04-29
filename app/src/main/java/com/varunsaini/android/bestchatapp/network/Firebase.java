package com.varunsaini.android.bestchatapp.network;

import android.app.Application;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.varunsaini.android.bestchatapp.models.GroupDetails;

public class Firebase {

    Application application;
    private DatabaseReference mDatabase;

    public Firebase(Application mApplication) {
        application = mApplication;
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void createNewGroup(String title,String lastMsg,String timeStamp){
        GroupDetails groupDetails = new GroupDetails(title,lastMsg,timeStamp);
        mDatabase.child("GroupsBasicDetails").child(timeStamp).setValue(groupDetails);
    }

    public void getAllGroupsOfAUser() {
    }
}
