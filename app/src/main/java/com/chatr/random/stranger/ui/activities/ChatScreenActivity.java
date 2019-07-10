package com.chatr.random.stranger.ui.activities;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chatr.random.stranger.AppPreferences;
import com.chatr.random.stranger.Constants;
import com.chatr.random.stranger.R;
import com.chatr.random.stranger.models.OneToOneMessageModel;
import com.chatr.random.stranger.models.UserParticularDetailModel;
import com.chatr.random.stranger.ui.viewholders.ChatScreenRecycler;
import com.chatr.random.stranger.viewmodels.ChatScreenViewModel;
import com.vistrav.ask.Ask;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import id.zelory.compressor.Compressor;

public class ChatScreenActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_IMAGE = 1;
    RecyclerView recyclerView;
    EditText message_et;
    ImageButton send_ib;
    boolean isChatEmpty = true;
    android.support.v7.widget.AppCompatTextView title_tv;
    ChatScreenViewModel chatScreenViewModel;
    String recieverUID, senderUID, recieverUserName, senderName;
    final String[] senderProfilePic = new String[1];
    final String[] recieverProfilePic = new String[1];
    String reciUserName;
    AppPreferences appPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);
        initViews();
        chatScreenViewModel = ViewModelProviders.of(this).get(ChatScreenViewModel.class);

        getAllIntentExtras();
        setRecyclerView();

        getWindow().setBackgroundDrawableResource(R.drawable.chat_background);
        (getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_abs_chat_layout);
        ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.tvTitle)).setText(recieverUserName);
        chatScreenViewModel.getYourParticularDetails(recieverUID).observe(this, new Observer<UserParticularDetailModel>() {
            @Override
            public void onChanged(@Nullable UserParticularDetailModel userParticularDetailModel) {
                if (userParticularDetailModel.isOnline) {
                    ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.is_online)).setText("Online");
                } else {
                    ((TextView) getSupportActionBar().getCustomView().findViewById(R.id.is_online)).setText("Last seen on " + userParticularDetailModel.lastSeen);
                }
                if (userParticularDetailModel.profilePicUrl != null && !userParticularDetailModel.profilePicUrl.isEmpty()) {
                    Glide.with(ChatScreenActivity.this).load(userParticularDetailModel.profilePicUrl).into((ImageView) findViewById(R.id.profile_pic));
                } else {
                    Glide.with(ChatScreenActivity.this).load(R.drawable.placeholder_person).into((ImageView) findViewById(R.id.profile_pic));
                }
            }
        });

        findViewById(R.id.profile_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), FullScreenImageActivity.class);
                i.putExtra(Constants.IMAGE_URL_FOR_FULL_SCREEN, recieverProfilePic[0]);
                startActivity(i);
            }
        });

        appPreferences = new AppPreferences(this);
        senderUID = appPreferences.readUId();
        senderName = appPreferences.readUName();

        chatScreenViewModel.getProfilePicAndUserNameFromUid(recieverUID, senderUID);
        chatScreenViewModel.usernameAndProfilePicForSender.observe(this, new Observer<UserParticularDetailModel>() {
            @Override
            public void onChanged(@Nullable UserParticularDetailModel userParticularDetailModel) {
                assert userParticularDetailModel != null;
                senderProfilePic[0] = userParticularDetailModel.profilePicUrl;
            }
        });
        chatScreenViewModel.usernameAndProfilePicForReciever.observe(this, new Observer<UserParticularDetailModel>() {
            @Override
            public void onChanged(@Nullable UserParticularDetailModel userParticularDetailModel) {
                assert userParticularDetailModel != null;
                recieverProfilePic[0] = userParticularDetailModel.profilePicUrl;
                reciUserName = userParticularDetailModel.displayName;
            }
        });

        send_ib.setOnClickListener(this);

        chatScreenViewModel.getAllOneToOneMessages(senderUID, recieverUID).observe(this, new Observer<List<OneToOneMessageModel>>() {
            @Override
            public void onChanged(@Nullable List<OneToOneMessageModel> oneToOneMessageModels) {
                if (oneToOneMessageModels.size() > 0) {
                    isChatEmpty = false;
                }
                recyclerView.setAdapter(new ChatScreenRecycler(getApplicationContext(), oneToOneMessageModels));
            }
        });


    }

    private void setRecyclerView() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(0,0);
    }

    private void getAllIntentExtras() {
        recieverUID = getIntent().getStringExtra(Constants.RECIEVER_UID);
        recieverUserName = getIntent().getStringExtra(Constants.RECIEVER_USERNAME);
        chatScreenViewModel.isOnline = getIntent().getBooleanExtra(Constants.RECIEVER_IS_ONLINE, true);
        chatScreenViewModel.recieverProfilePic = getIntent().getStringExtra(Constants.RECIEVER_PROFILE_PICTURE);
        chatScreenViewModel.recieverLastSeen = getIntent().getStringExtra(Constants.RECIEVER_LAST_SEEN);

//        chatScreenViewModel.getUserNameFromUID(recieverUID);

    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        message_et = findViewById(R.id.message_et);
        send_ib = findViewById(R.id.send_ib);
//        title_tv = getSupportActionBar().getCustomView().findViewById(R.id.tvTitle);
    }

    @Override
    public void onClick(View v) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM hh:mm a");
        String format = simpleDateFormat.format(new Date());
        Log.d("sasa", "onClick: " + senderProfilePic[0] + "::::" + recieverProfilePic[0]);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        chatScreenViewModel.writeNewMessageOTO(message_et.getText().toString(), senderUID, recieverUID, senderName, reciUserName, senderProfilePic[0], recieverProfilePic[0], String.valueOf(timestamp.getTime()));
        chatScreenViewModel.getAllOneToOneMessages(senderUID, recieverUID);
//        if (appPreferences.readNotificationState().equals("true")) {
            sendNotification();
//        }

        message_et.setText("");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!isChatEmpty) {
            chatScreenViewModel.turnBooleanMsgReadTrue(senderUID, recieverUID);
        }
    }

    public void selectAtachments(View view) {
        selectImageAttachment();
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle(R.string.select_attachment_type)
//                .setItems(R.array.colors_array, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        switch (which) {
//                            case 0:
//                                selectImageAttachment();
//                        }
//                    }
//                });
//        builder.create().show();
    }

    private void selectImageAttachment() {

        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("outputX", 256);
        intent.putExtra("outputY", 256);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PICK_IMAGE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
//                Toast.makeText(this, data.getData().toString(), Toast.LENGTH_SHORT).show();
                Uri selectedImageURI = data.getData();
                File compressedImageFile = null;
                File imageFile = new File(getPath(selectedImageURI));
                try {
                    compressedImageFile = new Compressor(this).compressToFile(imageFile);
                    Log.d("dsds", "onActivityResult: " + Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES).getAbsolutePath());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM hh:mm a");
                    String format = simpleDateFormat.format(new Date());
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    chatScreenViewModel.writeNewMessageOTO(String.valueOf(Uri.fromFile(compressedImageFile)), senderUID, recieverUID, senderName, reciUserName, senderProfilePic[0], recieverProfilePic[0], String.valueOf(timestamp.getTime()));

                } catch (IOException e) {
                    e.printStackTrace();
                    Ask.on(this)
                            .id(1) // in case you are invoking multiple time Ask from same activity or fragment
                            .forPermissions(Manifest.permission.READ_EXTERNAL_STORAGE
                                    , Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .withRationales("In order to Share and save Media Files you will need to grant storage permission") //optional
                            .go();
                }
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM hh:mm a");
//                String format = simpleDateFormat.format(new Date());
//                chatScreenViewModel.writeNewMessageOTO(String.valueOf(Uri.fromFile(compressedImageFile)),senderUID,recieverUID,senderName,reciUserName,senderProfilePic[0],recieverProfilePic[0],format);

            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void sendNotification() {

//        Toast.makeText(this, "Current Recipients is : user1@gmail.com ( Just For Demo )", Toast.LENGTH_SHORT).show();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    String send_email;

                    //This is a Simple Logic to Send Notification different Device Programmatically....
//                    if (MainActivity.LoggedIn_User_Email.equals("user1@gmail.com")) {
//                        send_email = "user2@gmail.com";
//                    } else {
//                        send_email = "user1@gmail.com";
//                    }

                    try {
                        String jsonResponse;

                        URL url = new URL("https://onesignal.com/api/v1/notifications");
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setUseCaches(false);
                        con.setDoOutput(true);
                        con.setDoInput(true);

                        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        con.setRequestProperty("Authorization", "Basic NGViZDFmMWEtNTNhMi00YjczLWJhOTEtMGNmNWNmYzg5YmVi");
                        con.setRequestMethod("POST");
                        String x = recieverUID;

                        String strJsonBody = "{"
                                + "\"app_id\": \"59b073cf-fbe6-45aa-848b-ef513a515e4d\","

                                + "\"filters\": [{\"field\": \"tag\", \"key\": \"user_name\", \"relation\": \"=\", \"value\": \"" + x + "\"}],"

                                + "\"data\": {\"foo\": \"bar\"},"
                                + "\"contents\": {\"en\": \"You have a new message\"}"
                                + "}";

//                        if(appPreferences.readDisguisedState().equals("true")){
//                            strJsonBody = "{"
//                                    + "\"app_id\": \"59b073cf-fbe6-45aa-848b-ef513a515e4d\","
//
//                                    + "\"filters\": [{\"field\": \"tag\", \"key\": \"user_name\", \"relation\": \"=\", \"value\": \"" + x + "\"}],"
//
//                                    + "\"data\": {\"foo\": \"bar\"},"
//                                    + "\"contents\": {\"en\": \"Bug Captured\"}"
//                                    + "}";
//                        }


                        System.out.println("strJsonBody:\n" + strJsonBody);

                        byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                        con.setFixedLengthStreamingMode(sendBytes.length);

                        OutputStream outputStream = con.getOutputStream();
                        outputStream.write(sendBytes);

                        int httpResponse = con.getResponseCode();
                        System.out.println("httpResponse: " + httpResponse);

                        if (httpResponse >= HttpURLConnection.HTTP_OK
                                && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                            Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        } else {
                            Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                            jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                            scanner.close();
                        }
                        System.out.println("jsonResponse:\n" + jsonResponse);

                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        });
    }
}
