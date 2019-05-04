package com.varunsaini.android.bestchatapp.network;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.varunsaini.android.bestchatapp.models.AllChatRecieverInfoModel;
import com.varunsaini.android.bestchatapp.models.GroupDetails;
import com.varunsaini.android.bestchatapp.models.NewUserInDbModel;
import com.varunsaini.android.bestchatapp.models.OneToOneMessageModel;
import com.varunsaini.android.bestchatapp.models.SearchedUserModel;
import com.varunsaini.android.bestchatapp.ui.activities.SearchActivity;
import com.varunsaini.android.bestchatapp.viewmodels.SearchViewModel;

import java.util.ArrayList;
import java.util.List;

public class Firebase {

    Application application;
    private DatabaseReference mDatabase;

    public Firebase(Application mApplication) {
        application = mApplication;
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void createNewGroup(String title, String lastMsg, String timeStamp) {
        GroupDetails groupDetails = new GroupDetails(title, lastMsg, timeStamp);
        mDatabase.child("GroupsBasicDetails").child(timeStamp).setValue(groupDetails);
    }

    public void getAllGroupsOfAUser() {
    }

    public void registerNewUserInDatabase(String uid, String email, String displayName) {
        NewUserInDbModel newUser = new NewUserInDbModel(uid, email, displayName);
        mDatabase.child("UsersBasicDetails").child(uid).setValue(newUser);
    }

    public MutableLiveData<List<SearchedUserModel>> returnMatchedUsers(String search_text) {
        Log.d("Search_Text", "returnMatchedUsers: " + search_text);
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        final MutableLiveData<List<SearchedUserModel>> mutableLiveData = new MutableLiveData<>();
        final List<SearchedUserModel> searchedUserList = new ArrayList<>();
        database.child("UsersBasicDetails").orderByChild("displayName").equalTo(search_text).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SearchedUserModel user = snapshot.getValue(SearchedUserModel.class);
                    System.out.println(user.email);
                    System.out.println(user.displayName);
                    System.out.println("----------");
                    searchedUserList.add(user);
                }
                mutableLiveData.setValue(searchedUserList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });
        return mutableLiveData;
    }

    public void writeNewMessageOTO(String messageText, String senderID, String recieverID, String senderName,String recieverName, String timeStamp) {
        OneToOneMessageModel oneToOneMessage = new OneToOneMessageModel(messageText, senderID, recieverID,senderName, timeStamp);
        String combinedId;
        if (senderID.compareTo(recieverID) > 0) {
            combinedId = senderID + recieverID;
        } else {
            combinedId = recieverID + senderID;
        }
        mDatabase.child("OneToOneChats").child(combinedId).push().setValue(oneToOneMessage);

        AllChatRecieverInfoModel allChatRecieverInfoForUser1 = new AllChatRecieverInfoModel(recieverName,recieverID,messageText,senderName,timeStamp,true);
        AllChatRecieverInfoModel allChatRecieverInfoForUser2 = new AllChatRecieverInfoModel(senderName,senderID,messageText,senderName,timeStamp,false);
        mDatabase.child("AllRecieversForASender").child(senderID).child(recieverID).setValue(allChatRecieverInfoForUser1);
        mDatabase.child("AllRecieversForASender").child(recieverID).child(senderID).setValue(allChatRecieverInfoForUser2);
    }


    public MutableLiveData<List<OneToOneMessageModel>> getAllOneToOneMessages(String combinedId) {
        Log.d("sas", "onClick: " + "in fire");
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        final MutableLiveData<List<OneToOneMessageModel>> mutableLiveData = new MutableLiveData<>();
        final List<OneToOneMessageModel> oneToOneMessageList = new ArrayList<>();
        System.out.print(combinedId);
        database.child("OneToOneChats").orderByKey().equalTo(combinedId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    OneToOneMessageModel message = snapshot.getValue(OneToOneMessageModel.class);
                    System.out.println(message.messageText);
                    System.out.println(message.senderID);
                    System.out.println("----------");
                    oneToOneMessageList.add(message);
                }
                mutableLiveData.setValue(oneToOneMessageList);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                oneToOneMessageList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    OneToOneMessageModel message = snapshot.getValue(OneToOneMessageModel.class);
                    System.out.println(message.messageText);
                    System.out.println(message.senderID);
                    System.out.println("----------");
                    oneToOneMessageList.add(message);
                }
                mutableLiveData.setValue(oneToOneMessageList);
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        return mutableLiveData;
    }

    public LiveData<List<AllChatRecieverInfoModel>> getAllChatListOfAParticularUser(String userID) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        final MutableLiveData<List<AllChatRecieverInfoModel>> mutableLiveData = new MutableLiveData<>();
        final List<AllChatRecieverInfoModel> allChatRecieverInfoList = new ArrayList<>();
        database.child("AllRecieversForASender").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allChatRecieverInfoList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    AllChatRecieverInfoModel model = snapshot.getValue(AllChatRecieverInfoModel.class);
                    System.out.println(model.recieverName);
                    System.out.println(model.lastMessage);
                    System.out.println("----------");
                    allChatRecieverInfoList.add(model);
                }
                mutableLiveData.setValue(allChatRecieverInfoList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return mutableLiveData;
    }

    public void turnBooleanMsgReadTrue(String senderUID, String recieverUID) {
        mDatabase.child("AllRecieversForASender").child(senderUID).child(recieverUID).child("isLastMsgRead").setValue(true);
    }
}
