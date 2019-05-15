package com.varunsaini.android.bestchatapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;

import static com.varunsaini.android.bestchatapp.Constants.DISGUISED_STATE;
import static com.varunsaini.android.bestchatapp.Constants.NOTIFICATION_STATE;
import static com.varunsaini.android.bestchatapp.Constants.PROFILE_PIC_URI;
import static com.varunsaini.android.bestchatapp.Constants.TOKEN_ID;
import static com.varunsaini.android.bestchatapp.Constants.USER_ID;
import static com.varunsaini.android.bestchatapp.Constants.USER_NAME;

public class AppPreferences {


    Context context;
    SharedPreferences sharedPreferences;


    public AppPreferences(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);
    }

    public void writeUid(String userId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_ID, userId);
        editor.apply();
    }

    public String readUId() {
        return sharedPreferences.getString(USER_ID, null);
    }

    public void writeUName(String userName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_NAME, userName);
        editor.apply();
    }

    public String readUName() {
        return sharedPreferences.getString(USER_NAME, null);
    }

    public void writeProfilePicUri(String profilePic) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PROFILE_PIC_URI, profilePic);
        editor.apply();
    }

    public String readProfilePicUri() {
        return sharedPreferences.getString(PROFILE_PIC_URI, null);
    }

    public void writeToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN_ID, token);
        editor.apply();
    }

    public void writeNotificationState(boolean checked) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(NOTIFICATION_STATE, String.valueOf(checked));
        editor.apply();
    }

    public void writeDisguisedState(boolean checked) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DISGUISED_STATE, String.valueOf(checked));
        editor.apply();
    }

    public String readNotificationState() {
        return sharedPreferences.getString(NOTIFICATION_STATE, "true");
    }

    public String readDisguisedState() {
        return sharedPreferences.getString(DISGUISED_STATE, "true");
    }


}
