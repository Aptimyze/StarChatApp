package com.varunsaini.android.bestchatapp.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.varunsaini.android.bestchatapp.Constants;
import com.varunsaini.android.bestchatapp.R;
import com.varunsaini.android.bestchatapp.models.AllChatRecieverInfoModel;
import com.varunsaini.android.bestchatapp.models.SearchedUserModel;
import com.varunsaini.android.bestchatapp.ui.activities.ChatScreenActivity;

import java.util.List;

public class AllFragmentsRecycler extends RecyclerView.Adapter<AllFragmentsRecycler.AllFragmentViewHolder> {

    List<AllChatRecieverInfoModel> allChatRecieverInfo;
    Context c;


    public AllFragmentsRecycler(Context context,List<AllChatRecieverInfoModel> mAllChatRecieverInfo) {
        c = context;
        allChatRecieverInfo = mAllChatRecieverInfo;
    }


    @NonNull
    @Override
    public AllFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.all_chats_card_layout, viewGroup, false);
        return new AllFragmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AllFragmentViewHolder allFragmentViewHolder, final int i) {
        AllChatRecieverInfoModel model = allChatRecieverInfo.get(i);
        allFragmentViewHolder.name.setText(model.recieverName);
        allFragmentViewHolder.last_message.setText(model.lastMsgSentBy + "-" + model.lastMessage );
        allFragmentViewHolder.time.setText(model.timestamp);

        if(!model.isLastMsgRead){
            allFragmentViewHolder.name.setTypeface(null, Typeface.BOLD);
            allFragmentViewHolder.last_message.setTypeface(null, Typeface.BOLD);
            allFragmentViewHolder.time.setTypeface(null, Typeface.BOLD);
        }


        allFragmentViewHolder.full_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(c, ChatScreenActivity.class);
                ii.putExtra(Constants.RECIEVER_USERNAME,allChatRecieverInfo.get(i).recieverName);
                ii.putExtra(Constants.RECIEVER_UID,allChatRecieverInfo.get(i).recieverUid);
                c.startActivity(ii);
            }
        });

    }

    @Override
    public int getItemCount() {
        return allChatRecieverInfo.size();
    }

    public class AllFragmentViewHolder extends RecyclerView.ViewHolder {
        ImageView profile_pic;
        LinearLayout full_card;
        TextView name, last_message, is_delivered, time;

        public AllFragmentViewHolder(View v) {
            super(v);
            profile_pic = v.findViewById(R.id.profile_pic);
            name = v.findViewById(R.id.name);
            last_message = v.findViewById(R.id.last_message);
            is_delivered = v.findViewById(R.id.is_delivered);
            time = v.findViewById(R.id.time);
            full_card = v.findViewById(R.id.full_card);
        }

    }

}
