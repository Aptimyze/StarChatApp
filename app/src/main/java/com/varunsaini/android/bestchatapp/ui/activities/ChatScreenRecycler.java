package com.varunsaini.android.bestchatapp.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.varunsaini.android.bestchatapp.Constants;
import com.varunsaini.android.bestchatapp.R;
import com.varunsaini.android.bestchatapp.models.OneToOneMessageModel;
import com.varunsaini.android.bestchatapp.models.SearchedUserModel;

import java.util.List;



public class ChatScreenRecycler extends RecyclerView.Adapter<ChatScreenRecycler.ChatScreenViewHolder> {

    Context c;
    List<OneToOneMessageModel> oneToOneMessage;

    public ChatScreenRecycler(Context context, List<OneToOneMessageModel> mOneToOneMessage) {
        oneToOneMessage = mOneToOneMessage;
        c = context;
    }


    @NonNull
    @Override
    public ChatScreenViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.message_layout, viewGroup, false);
        return new ChatScreenViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatScreenViewHolder chatScreenViewHolder, final int i) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.weight = 1.0f;
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        chatScreenViewHolder.full_card.setLayoutParams(lp);

        chatScreenViewHolder.message_tv.setText(oneToOneMessage.get(i).messageText);
        chatScreenViewHolder.time_tv.setText(oneToOneMessage.get(i).timeStamp);
        chatScreenViewHolder.username_tv.setText(oneToOneMessage.get(i).senderName);
    }

    @Override
    public int getItemCount() {
        return oneToOneMessage.size();
    }

    public class ChatScreenViewHolder extends RecyclerView.ViewHolder {
        TextView message_tv, time_tv, username_tv;
        LinearLayout full_card;

        public ChatScreenViewHolder(View v) {
            super(v);
            message_tv = v.findViewById(R.id.message_tv);
            time_tv = v.findViewById(R.id.time_tv);
            username_tv = v.findViewById(R.id.username_tv);
            full_card = v.findViewById(R.id.full_card);
        }
    }

}
