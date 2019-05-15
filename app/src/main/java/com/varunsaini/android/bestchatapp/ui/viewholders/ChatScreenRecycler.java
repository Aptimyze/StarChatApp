package com.varunsaini.android.bestchatapp.ui.viewholders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.varunsaini.android.bestchatapp.AppPreferences;
import com.varunsaini.android.bestchatapp.Constants;
import com.varunsaini.android.bestchatapp.R;
import com.varunsaini.android.bestchatapp.models.OneToOneMessageModel;
import com.varunsaini.android.bestchatapp.ui.activities.FullScreenImageActivity;

import java.text.SimpleDateFormat;
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ChatScreenViewHolder chatScreenViewHolder, final int i) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.weight = 1.0f;
        lp.gravity = Gravity.END;
//        chatScreenViewHolder.full_card.setLayoutParams(lp);
        final OneToOneMessageModel model = oneToOneMessage.get(i);
        AppPreferences appPreferences = new AppPreferences(c);
        if (model.senderID.equals(appPreferences.readUId())) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) chatScreenViewHolder.full_card.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            chatScreenViewHolder.full_card.setLayoutParams(params);
            chatScreenViewHolder.full_card.setBackgroundResource(R.drawable.my_message);
            chatScreenViewHolder.time_tv.setGravity(Gravity.END);
            chatScreenViewHolder.message_tv.setGravity(Gravity.END);
        } else {
            chatScreenViewHolder.message_tv.setTextColor(c.getColor(R.color.their_message_color));
            chatScreenViewHolder.time_tv.setTextColor(c.getColor(R.color.their_message_color));
            chatScreenViewHolder.full_card.setBackgroundResource(R.drawable.their_message);
        }
        long timeStamp = Long.parseLong(model.timeStamp);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM hh:mm a");
        chatScreenViewHolder.time_tv.setText(simpleDateFormat.format(timeStamp));
        if (!model.messageText.contains(Constants.FIREBASE_STORAGE_PART_OF_DOWNLOAD_LINK)) {
            chatScreenViewHolder.message_tv.setText(model.messageText);
            chatScreenViewHolder.image_iv.setVisibility(View.GONE);
        } else {
            Glide.with(c).load(model.messageText).into(chatScreenViewHolder.image_iv);
            chatScreenViewHolder.message_tv.setVisibility(View.GONE);
        }

        chatScreenViewHolder.full_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model.messageText.contains(Constants.FIREBASE_STORAGE_PART_OF_DOWNLOAD_LINK)) {
                    Intent i = new Intent(c, FullScreenImageActivity.class);
                    i.putExtra(Constants.IMAGE_URL_FOR_FULL_SCREEN, model.messageText);
                    c.startActivity(i);
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return oneToOneMessage.size();
    }

    public class ChatScreenViewHolder extends RecyclerView.ViewHolder {
        TextView message_tv, time_tv, username_tv;
        LinearLayout full_card;
        ImageView image_iv;

        public ChatScreenViewHolder(View v) {
            super(v);
            message_tv = v.findViewById(R.id.message_tv);
            time_tv = v.findViewById(R.id.time_tv);
            username_tv = v.findViewById(R.id.username_tv);
            image_iv = v.findViewById(R.id.image_iv);
            full_card = v.findViewById(R.id.full_card);
        }
    }

}
