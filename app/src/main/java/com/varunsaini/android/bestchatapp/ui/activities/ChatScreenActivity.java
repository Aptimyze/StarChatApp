package com.varunsaini.android.bestchatapp.ui.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.media.Image;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.varunsaini.android.bestchatapp.AppPreferences;
import com.varunsaini.android.bestchatapp.Constants;
import com.varunsaini.android.bestchatapp.R;
import com.varunsaini.android.bestchatapp.models.OneToOneMessageModel;
import com.varunsaini.android.bestchatapp.viewmodels.ChatScreenViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ChatScreenActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    EditText message_et;
    ImageButton send_ib;
    ChatScreenViewModel chatScreenViewModel;
    String recieverUID,senderUID,recieverUserName,senderName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);
        initViews();
        getAllIntentExtras();
        setRecyclerView();

        AppPreferences appPreferences = new AppPreferences(this);
         senderUID= appPreferences.readUId();
         senderName = appPreferences.readUName();

        chatScreenViewModel = ViewModelProviders.of(this).get(ChatScreenViewModel.class);
        send_ib.setOnClickListener(this);

        chatScreenViewModel.getAllOneToOneMessages(senderUID,recieverUID).observe(this, new Observer<List<OneToOneMessageModel>>() {
            @Override
            public void onChanged(@Nullable List<OneToOneMessageModel> oneToOneMessageModels) {
                recyclerView.setAdapter(new ChatScreenRecycler(getApplicationContext(),oneToOneMessageModels));
            }
        });

    }

    private void setRecyclerView() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
    }

    private void getAllIntentExtras() {
        recieverUID = getIntent().getStringExtra(Constants.RECIEVER_UID);
        recieverUserName = getIntent().getStringExtra(Constants.RECIEVER_USERNAME);
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        message_et = findViewById(R.id.message_et);
        send_ib = findViewById(R.id.send_ib);
    }

    @Override
    public void onClick(View v) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        String format = simpleDateFormat.format(new Date());
        chatScreenViewModel.writeNewMessageOTO(message_et.getText().toString(),senderUID,recieverUID,senderName,recieverUserName,format);
        chatScreenViewModel.getAllOneToOneMessages(senderUID,recieverUID);
        message_et.setText("");
        Log.d("sas", "onClick: "+"in activity");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        chatScreenViewModel.turnBooleanMsgReadTrue(senderUID,recieverUID);
    }
}
