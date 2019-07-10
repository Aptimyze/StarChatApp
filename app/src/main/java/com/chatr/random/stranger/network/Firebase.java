package com.chatr.random.stranger.network;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.chatr.random.stranger.AppPreferences;
import com.chatr.random.stranger.Constants;
import com.chatr.random.stranger.models.AllChatRecieverInfoModel;
import com.chatr.random.stranger.models.GroupDetails;
import com.chatr.random.stranger.models.NewUserInDbModel;
import com.chatr.random.stranger.models.OneToOneMessageModel;
import com.chatr.random.stranger.models.SearchedUserModel;
import com.chatr.random.stranger.models.UserParticularDetailModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Firebase {

    Application application;
    private DatabaseReference mDatabase;


    public Firebase(Application mApplication) {
        application = mApplication;
        FirebaseDatabase fb = FirebaseDatabase.getInstance();
//        fb.setPersistenceEnabled(true);
        mDatabase = fb.getReference();
        mDatabase.keepSynced(true);
    }

    public void createNewGroup(String title, String lastMsg, String timeStamp) {
        GroupDetails groupDetails = new GroupDetails(title, lastMsg, timeStamp);
        mDatabase.child("GroupsBasicDetails").child(timeStamp).setValue(groupDetails);
    }

    public MutableLiveData<NewUserInDbModel> getRandomUsers() {
        final int[] noOfOnlineUsers = {0};
        final int[] randomIndex = {1};
        final MutableLiveData<NewUserInDbModel> mutableLiveData = new MutableLiveData<>();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("UsersBasicDetails").orderByChild("isOnline").equalTo(true).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    noOfOnlineUsers[0]++;
                }
                randomIndex[0] = (int) Math.floor(Math.random() * noOfOnlineUsers[0])+1;
                Log.d("random", "onDataChange: " + randomIndex[0]);
                DatabaseReference df1 = FirebaseDatabase.getInstance().getReference();
                df1.child("UsersBasicDetails").orderByChild("isOnline").equalTo(true).limitToFirst(randomIndex[0]).
                        addValueEventListener(new ValueEventListener() {
                            int i =0 ;
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                int count=0;
                                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                    count++;
                                    if(count == randomIndex[0]) {
                                        NewUserInDbModel user = snapshot.getValue(NewUserInDbModel.class);
                                        mutableLiveData.setValue(user);
                                        break;
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });




//        DatabaseReference df1 = FirebaseDatabase.getInstance().getReference();
//        final MutableLiveData<List<SearchedUserModel>> mutableLiveData = new MutableLiveData<>();
//        final List<SearchedUserModel> searchedUserList = new ArrayList<>();
//        df1.child("UsersBasicDetails").orderByChild("isOnline").equalTo(true).
//                addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        searchedUserList.clear();
//                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                            SearchedUserModel user = snapshot.getValue(SearchedUserModel.class);
//                            System.out.println(user.email);
//                            System.out.println(user.displayName);
//                            System.out.println("----------");
//                            searchedUserList.add(user);
//                        }
//                        mutableLiveData.setValue(searchedUserList);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
        return mutableLiveData;
    }

    public void registerNewUserInDatabase(String uid, String email, String displayName) {
        NewUserInDbModel newUser = new NewUserInDbModel(uid, email, displayName, "", "No Status Set", true);
        mDatabase.child("UsersBasicDetails").child(uid).setValue(newUser);
    }

    public MutableLiveData<List<SearchedUserModel>> returnMatchedUsers(String search_text) {
        Log.d("Search_Text", "returnMatchedUsers: " + search_text);
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.keepSynced(true);
        final MutableLiveData<List<SearchedUserModel>> mutableLiveData = new MutableLiveData<>();
        final List<SearchedUserModel> searchedUserList = new ArrayList<>();
        database.child("UsersBasicDetails").orderByChild("displayName").startAt(search_text).endAt(search_text + "\uf8ff").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                searchedUserList.clear();
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

    public void writeNewMessageOTO(final String messageText, final String senderID, final String recieverID, final String senderName, final String recieverName,
                                   final String senderProfilePic, final String recieverProfilePic, final String timeStamp) {
        final String combinedId;
        if (senderID.compareTo(recieverID) > 0) {
            combinedId = senderID + recieverID;
        } else {
            combinedId = recieverID + senderID;
        }
        if (!messageText.contains(Constants.APP_PACKAGE_NAME)) {
            OneToOneMessageModel oneToOneMessage = new OneToOneMessageModel(messageText, senderID, recieverID, senderName, timeStamp);
            mDatabase.child("OneToOneChats").child(combinedId).push().setValue(oneToOneMessage);
            AllChatRecieverInfoModel allChatRecieverInfoForUser1 = new AllChatRecieverInfoModel(recieverName, recieverID, messageText, timeStamp, senderName, recieverProfilePic, true);
            AllChatRecieverInfoModel allChatRecieverInfoForUser2 = new AllChatRecieverInfoModel(senderName, senderID, messageText, timeStamp, senderName, senderProfilePic, false);
            mDatabase.child("AllRecieversForASender").child(senderID).child(recieverID).setValue(allChatRecieverInfoForUser1);
            mDatabase.child("AllRecieversForASender").child(recieverID).child(senderID).setValue(allChatRecieverInfoForUser2);
        } else {
            Uri uri = Uri.parse(messageText);
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            final StorageReference profilePics = storageRef.child("Photos/" + uri.getLastPathSegment());
            final UploadTask uploadTask = profilePics.putFile(uri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return profilePics.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                        OneToOneMessageModel oneToOneMessage = new OneToOneMessageModel(downloadUri.toString(), senderID, recieverID, senderName, timeStamp);
                        AllChatRecieverInfoModel allChatRecieverInfoForUser1 = new AllChatRecieverInfoModel(recieverName, recieverID, "\uD83D\uDCF7 Image", timeStamp, senderName, recieverProfilePic, true);
                        AllChatRecieverInfoModel allChatRecieverInfoForUser2 = new AllChatRecieverInfoModel(senderName, senderID, "\uD83D\uDCF7 Image", timeStamp, senderName, senderProfilePic, false);
                        mDatabase.child("OneToOneChats").child(combinedId).push().setValue(oneToOneMessage);
                        database.child("AllRecieversForASender").child(senderID).child(recieverID).setValue(allChatRecieverInfoForUser1);
                        database.child("AllRecieversForASender").child(recieverID).child(senderID).setValue(allChatRecieverInfoForUser2);
                    } else {
                    }
                }
            });
        }
    }


    public MutableLiveData<List<OneToOneMessageModel>> getAllOneToOneMessages(String combinedId) {
        Log.d("sas", "onClick: " + "in fire");
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.keepSynced(true);
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
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return mutableLiveData;
    }

    public LiveData<List<AllChatRecieverInfoModel>> getAllChatListOfAParticularUser(String userID) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.keepSynced(true);
        final MutableLiveData<List<AllChatRecieverInfoModel>> mutableLiveData = new MutableLiveData<>();
        final List<AllChatRecieverInfoModel> allChatRecieverInfoList = new ArrayList<>();
        database.child("AllRecieversForASender").child(userID).orderByKey().addValueEventListener(new ValueEventListener() {
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
                Collections.sort(allChatRecieverInfoList,Collections.<AllChatRecieverInfoModel>reverseOrder());
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

    public String getUserNameFromUID(String UserID) {
        final String[] userName = new String[1];
        mDatabase.child("UsersBasicDetails").child(UserID).child("displayName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userName[0] = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return userName[0];
    }

    public void setProfilePicture(Uri uri, final String myUID, final ProgressBar progressBar) {
        AppPreferences appPreferences = new AppPreferences(application);
        appPreferences.writeProfilePicUri(uri.toString());
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        final StorageReference profilePics = storageRef.child("ProfilePictures/" + uri.getLastPathSegment());
        final UploadTask uploadTask = profilePics.putFile(uri);
        final Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return profilePics.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                    database.child("UsersBasicDetails").child(myUID).child("profilePicUrl").setValue(downloadUri.toString());
                    Log.d("sasa", "onComplete: " + downloadUri.toString());
//                    progressBar.setVisibility(View.GONE);
                } else {
                }
            }
        });
    }


    public LiveData<NewUserInDbModel> getAllSettingsOfAUser(String myUID) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.keepSynced(true);
        final MutableLiveData<NewUserInDbModel> mutableLiveData = new MutableLiveData<>();
        database.child("UsersBasicDetails").child(myUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                NewUserInDbModel model = dataSnapshot.getValue(NewUserInDbModel.class);
                mutableLiveData.setValue(model);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return mutableLiveData;
    }

    public LiveData<UserParticularDetailModel> getProfilePicAndUserNameFromUid(String uid) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.keepSynced(true);
        final MutableLiveData<UserParticularDetailModel> mutableLiveData = new MutableLiveData<>();
        database.child("UsersBasicDetails").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserParticularDetailModel model = dataSnapshot.getValue(UserParticularDetailModel.class);
                mutableLiveData.setValue(model);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return mutableLiveData;
    }


    public void putUserAsOnline(String myUID) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.keepSynced(true);
        database.child("UsersBasicDetails").child(myUID).child("isOnline").setValue(true);
    }

    public void putUserAsOffline(String myUid) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM hh:mm a");
        String format = simpleDateFormat.format(new Date());
        database.keepSynced(true);
        database.child("UsersBasicDetails").child(myUid).child("isOnline").setValue(false);
        database.child("UsersBasicDetails").child(myUid).child("lastSeen").setValue(format);
    }

    public MutableLiveData<UserParticularDetailModel> getYourParticularDetails(String yourUID) {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        final MutableLiveData<UserParticularDetailModel> mutableLiveData = new MutableLiveData<UserParticularDetailModel>();
        database.child("UsersBasicDetails").child(yourUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserParticularDetailModel model = dataSnapshot.getValue(UserParticularDetailModel.class);
                mutableLiveData.setValue(model);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return mutableLiveData;
    }

    public void setAllSettingsDetails(String uid, NewUserInDbModel userDetails) {
        mDatabase.child("UsersBasicDetails").child(uid).child("age").setValue(userDetails.age);
        mDatabase.child("UsersBasicDetails").child(uid).child("nationality").setValue(userDetails.nationality);
        mDatabase.child("UsersBasicDetails").child(uid).child("status").setValue(userDetails.status);
        mDatabase.child("UsersBasicDetails").child(uid).child("gender").setValue(userDetails.gender);
    }
}
