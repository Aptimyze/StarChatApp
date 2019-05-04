package com.varunsaini.android.bestchatapp.ui.activities;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.varunsaini.android.bestchatapp.Constants;
import com.varunsaini.android.bestchatapp.R;
import com.varunsaini.android.bestchatapp.models.SearchedUserModel;

import java.util.List;

public class SearchRecycler extends RecyclerView.Adapter<SearchRecycler.SearchViewHolder> {

    Context c;
    List<SearchedUserModel> searchedUser;

    public SearchRecycler(Context context, List<SearchedUserModel> searchedUserModels) {
        searchedUser = searchedUserModels;
        c = context;
    }


    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.all_chats_card_layout, viewGroup, false);

        return new SearchViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchViewHolder searchViewHolder, final int i) {
        searchViewHolder.name.setText(searchedUser.get(i).displayName);
        searchViewHolder.last_message.setText(searchedUser.get(i).email);
        searchViewHolder.time.setText(searchedUser.get(i).uid);

        searchViewHolder.full_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(c,ChatScreenActivity.class);
                ii.putExtra(Constants.RECIEVER_USERNAME,searchedUser.get(i).displayName);
                ii.putExtra(Constants.RECIEVER_UID,searchedUser.get(i).uid);
                c.startActivity(ii);
            }
        });


    }

    @Override
    public int getItemCount() {
        return searchedUser.size();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        ImageView profile_pic;
        LinearLayout full_card;
        TextView name, last_message, is_delivered,time;

        public SearchViewHolder(View v) {
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
