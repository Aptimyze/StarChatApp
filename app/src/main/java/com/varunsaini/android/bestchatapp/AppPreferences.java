package com.varunsaini.android.bestchatapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;

import static com.varunsaini.android.bestchatapp.Constants.USER_ID;
import static com.varunsaini.android.bestchatapp.Constants.USER_NAME;

public class AppPreferences {

    Context context;
    SharedPreferences sharedPreferences;


    public AppPreferences(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(Constants.APP_NAME,Context.MODE_PRIVATE);
    }

    public void writeUid(String userId){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_ID,userId);
        editor.apply();
    }

    public String readUId() {
        return sharedPreferences.getString(USER_ID,null);
    }

    public void writeUName(String userName){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_NAME,userName);
        editor.apply();
    }

    public String readUName() {
        return sharedPreferences.getString(USER_NAME,null);
    }
}
