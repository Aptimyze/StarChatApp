package com.chatr.random.stranger.ui.viewholders;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chatr.random.stranger.Constants;
import com.chatr.random.stranger.R;
import com.chatr.random.stranger.models.SearchedUserModel;
import com.chatr.random.stranger.ui.activities.ChatScreenActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

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
        SearchedUserModel model = searchedUser.get(i);
        searchViewHolder.name.setText(model.displayName);
        searchViewHolder.email_id.setText(model.status);
        searchViewHolder.email_id.setMaxLines(1);
        searchViewHolder.time.setVisibility(View.GONE);
        searchViewHolder.status.setText(model.status);
        if(model.profilePicUrl != null && !(model.profilePicUrl).isEmpty()) {
            Glide.with(c).load(model.profilePicUrl).into(searchViewHolder.profile_pic);
        }else{
            Glide.with(c).load(R.drawable.placeholder_person).into(searchViewHolder.profile_pic);
        }


        searchViewHolder.full_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(c, ChatScreenActivity.class);
                ii.putExtra(Constants.RECIEVER_USERNAME,searchedUser.get(i).displayName);
                ii.putExtra(Constants.RECIEVER_PROFILE_PICTURE,searchedUser.get(i).profilePicUrl);
                ii.putExtra(Constants.RECIEVER_IS_ONLINE,searchedUser.get(i).isOnline);
                ii.putExtra(Constants.RECIEVER_LAST_SEEN,searchedUser.get(i).lastSeen);
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
        CircleImageView profile_pic;
        LinearLayout full_card;
        TextView name, email_id, status,time;

        public SearchViewHolder(View v) {
            super(v);
            profile_pic = v.findViewById(R.id.profile_image);
            name = v.findViewById(R.id.name);
            email_id = v.findViewById(R.id.last_message);
            status = v.findViewById(R.id.is_delivered);
            time = v.findViewById(R.id.time);
            full_card = v.findViewById(R.id.full_card);
        }
    }

}
